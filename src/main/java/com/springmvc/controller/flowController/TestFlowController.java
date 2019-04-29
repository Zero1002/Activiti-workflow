package com.springmvc.controller.flowController;

import com.springmvc.pojo.MyTask;
import com.springmvc.pojo.Role;
import com.springmvc.pojo.User;
import com.springmvc.pojo.WorkItem;
import com.springmvc.service.RoleOperationService;
import com.springmvc.service.RoleService;
import com.springmvc.service.UserService;
import com.springmvc.service.WorkItemService;
import com.springmvc.utils.ResponseObject;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/testFlow")
public class TestFlowController {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private WorkItemService workItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RoleService roleService;

    /**
     * 流程启动
     *
     * @param id
     * @param userId
     * @param operation
     * @param comment
     * @param assignUser
     * @param assignRole
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/start")
    public ResponseObject<Map<String, Object>> start(Integer id, String userId, String operation, String comment, String flowKey,
                                                     @RequestParam(required = false) String assignUser,
                                                     @RequestParam(required = false) String assignRole) throws Exception {
        // 结果返回
        Map<String, Object> result = new HashMap<String, Object>();
        // 流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("flowStarter", userId);                               // 当前申请人
        variables.put("id", id);                                            // 单号
        variables.put("operation", operation);                              // 操作
        variables.put("assignUser", assignUser == null ? "" : assignUser);  // 指定所属人
        variables.put("assignRole", assignRole == null ? "" : assignRole);  // 指定所属组
        try {
            ProcessInstance pi = runtimeService.startProcessInstanceByKey(flowKey, variables);
            Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
            String processInstacnId = task.getProcessInstanceId();
            Authentication.setAuthenticatedUserId(userId);
            if (comment == null || comment == "") {
                comment = operation;
            }
            taskService.addComment(task.getId(), processInstacnId, operation + "," + comment);
            taskService.complete(task.getId(), variables);
            Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            // 修改状态,更新流程id
            WorkItem workItem = new WorkItem();
            workItem.setId(id);
            workItem.setState(nextTask.getName());
            workItem.setProcessInstanceId(pi.getProcessInstanceId());
            workItemService.updateByPrimaryKeySelective(workItem);
        } catch (Exception e) {
            result.put("success", false);
            result.put("errorMsg", "流程启动失败: " + e.getMessage());
            return new ResponseObject<Map<String, Object>>(result);
        }
        result.put("success", true);
        return new ResponseObject<Map<String, Object>>(result);
    }

    /**
     * 审批
     *
     * @param taskId
     * @param userId
     * @param operation
     * @param comment
     * @param assignUser
     * @param assignRole
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/handle")
    public ResponseObject<Map<String, Object>> approvalHandle(String taskId, String userId, String operation, String comment,
                                                              @RequestParam(required = false) String assignUser,
                                                              @RequestParam(required = false) String assignRole) throws Exception {
        // 结果返回
        Map<String, Object> result = new HashMap<String, Object>();
        // 流程变量
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("operation", operation);
        variables.put("assignUser", assignUser == null ? "" : assignUser);  // 指定所属人
        variables.put("assignRole", assignRole == null ? "" : assignRole);  // 指定所属组
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String processInstacnId = task.getProcessInstanceId();
            Authentication.setAuthenticatedUserId(userId);
            if (comment == null || comment == "") {
                comment = operation;
            }
            // 获取单号
            Integer id = (Integer) taskService.getVariable(taskId, "id");
            taskService.addComment(taskId, processInstacnId, operation + "," + comment);
            taskService.complete(taskId, variables);
            // 获取下个节点状态,并行网关查出来两个
            List<Task> nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
            List<String> taskNames = new ArrayList<String>();
            for (Task nt : nextTask) {
                taskNames.add(nt.getName());
                if (nt != null) {
                    if (nt.getName().equals("产品上线")) {
                        taskService.complete(nt.getId());
                    }
                    // 修改状态
                    WorkItem workItem = new WorkItem();
                    workItem.setId(id);
                    workItem.setState(StringUtils.join(taskNames, ","));
                    workItemService.updateByPrimaryKeySelective(workItem);
                }
            }
        } catch (Exception e) {
            result.put("errorMsg", "流程审批流程失败：" + e.getMessage());
            result.put("success", false);
            return new ResponseObject<Map<String, Object>>(result);
        }
        result.put("success", true);
        return new ResponseObject<Map<String, Object>>(result);
    }

    /**
     * 查询当前流程图
     *
     * @param processInstanceId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showCurrentView")
    public ModelAndView showCurrentView(String processInstanceId, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        // 通过任务id查询流程定义
//        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : taskList) {
            String processDefinitionId = task.getProcessDefinitionId(); //获取流程定义Id
            // 创建流程定义查询
            // 根据流程定义id查询
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            mav.addObject("deploymentId", processDefinition.getDeploymentId()); // 部署id
            mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // 图片资源文件名称
            // 查看当前活动坐标
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
            // 根据流程实例id查询活动实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().
                    processInstanceId(processInstanceId).singleResult();
            ActivityImpl activityImpl = processDefinitionEntity.findActivity(processInstance.getActivityId());
            mav.addObject("x", activityImpl.getX());
            mav.addObject("y", activityImpl.getY());
            mav.addObject("width", activityImpl.getWidth());
            mav.addObject("height", activityImpl.getHeight());
            try {
                List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .list();
                // 流程逆转，按创建时间降序排列
                Collections.reverse(list);
                List<MyTask> tasks = new ArrayList<MyTask>(30);
                // 查询历史批注表
                List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
                // 获取单号
                Integer id = (Integer) taskService.getVariable(task.getId(), "id");
                for (HistoricTaskInstance ht : list) {
                    MyTask t = new MyTask();
                    t.setProcessInstanceId(processInstanceId);
                    t.setId(id.toString());
                    t.setFlowName("TestFlow");
                    // 已处理节点处理人
                    for (int i = 0; i < comments.size(); i++) {
                        if (ht.getId().equals(comments.get(i).getTaskId())) {
                            User user = userService.selectByPrimaryKey(Integer.valueOf(comments.get(i).getUserId()));
                            t.setCurrentHandleName(user == null ? comments.get(i).getUserId() : user.getLoginName());
                            t.setOperation(comments.get(i).getFullMessage().split(",")[0]);
                            t.setDescription(comments.get(i).getFullMessage().split(",").length <= 1 ? null : comments.get(i).getFullMessage().split(",")[1]);
                        }
                    }
                    if (t.getCurrentHandleName() == null || t.getCurrentHandleName() == "") {
                        // 查询当前节点任务办理 候选组/候选人
                        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(ht.getId());
                        StringBuilder str = new StringBuilder();
                        for (int i = 0; i < identityLinksForTask.size(); i++) {
                            String tmpCandidate = "";
                            // 显示userId
                            if (identityLinksForTask.get(i).getUserId() != null) {
                                tmpCandidate = identityLinksForTask.get(i).getUserId();
                                User user = userService.selectByPrimaryKey(Integer.valueOf(tmpCandidate));
                                tmpCandidate = user == null ? tmpCandidate : user.getLoginName();
                            }
                            if (identityLinksForTask.get(i).getGroupId() != null) {
                                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                                Matcher m = p.matcher(identityLinksForTask.get(i).getGroupId());
                                if (m.find()) {
                                    tmpCandidate = identityLinksForTask.get(i).getGroupId();
                                } else {
                                    Role role = roleService.selectByPrimaryKey(Integer.valueOf(identityLinksForTask.get(i).getGroupId()));
                                    tmpCandidate = role.getRoleName();
                                }
                            }
                            if (tmpCandidate != null && tmpCandidate != "") {
                                str.append(" " + tmpCandidate);
                            }
                        }
                        t.setCurrentHandleName(str.toString().trim());
                    }
                    t.setTaskId(ht.getId());
                    t.setTaskName(ht.getName());
                    t.setCreateTime(ht.getCreateTime());
                    t.setEndTime(ht.getEndTime());
                    // 查询定时任务获取到期时间
                    Job job = managementService.createJobQuery().processInstanceId(processInstanceId).singleResult();
                    t.setExpectTime(job == null ? null : job.getDuedate());
                    tasks.add(t);
                }
                // 按集合createTime降序排列
                Collections.sort(tasks, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        MyTask task1 = (MyTask) o1;
                        MyTask task2 = (MyTask) o2;
                        int flag = task2.getCreateTime().compareTo(task1.getCreateTime());
                        return flag;
                    }
                });
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
                JSONArray jsonArray = JSONArray.fromObject(tasks, jsonConfig);
                mav.addObject("taskList", jsonArray);
            } catch (Exception e) {
                System.out.println("查询出错：" + e.getMessage());
            }
        }
        mav.setViewName("views/flowView");
        return mav;
    }

    /**
     * 查看流程图
     *
     * @param deploymentId
     * @param diagramResourceName
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showView")
    public String showView(String deploymentId, String diagramResourceName, HttpServletResponse response) throws Exception {
        try {
            InputStream in = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
            OutputStream out = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println("查询流程图失败" + e.getMessage());
        }
        return null;
    }
}
