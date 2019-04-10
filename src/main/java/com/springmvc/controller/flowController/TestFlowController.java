package com.springmvc.controller.flowController;

import com.springmvc.pojo.MyTask;
import com.springmvc.pojo.User;
import com.springmvc.pojo.WorkItem;
import com.springmvc.service.RoleOperationService;
import com.springmvc.service.UserService;
import com.springmvc.service.WorkItemService;
import com.springmvc.utils.ResponseObject;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
    public ResponseObject<Map<String, Object>> start(Integer id, String userId, String operation, String comment,
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
            ProcessInstance pi = runtimeService.startProcessInstanceByKey("TestFlow", variables);
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
            taskService.addComment(taskId, processInstacnId, operation + "," + comment);
            taskService.complete(taskId, variables);
            // 获取单号
            Integer id = (Integer) taskService.getVariable(taskId, "id");
            Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            if (nextTask != null) {
                if (nextTask.getName().equals("已处理")) {
                    taskService.complete(nextTask.getId());
                }
                // 修改状态
                WorkItem workItem = new WorkItem();
                workItem.setId(id);
                workItem.setState(nextTask.getName());
                workItemService.updateByPrimaryKeySelective(workItem);
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
     * 流程图
     *
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/showProcess")
    public ResponseObject<Map<String, Object>> showProcess(String processInstanceId) throws Exception {
        if (processInstanceId == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();
            // 流程逆转，按创建时间降序排列
            Collections.reverse(list);
            List<MyTask> tasks = new ArrayList<MyTask>(30);
            // 查询历史批注表
            List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            // 获取单号
            Integer id = (Integer) taskService.getVariable(task.getId(), "id");
            for (HistoricTaskInstance ht : list) {
                MyTask t = new MyTask();
                t.setProcessInstanceId(processInstanceId);
                t.setId(id.toString());
                t.setFlowName("TestFlow");
                // 节点处理人
                for (int i = 0; i < comments.size(); i++) {
                    if (ht.getId().equals(comments.get(i).getTaskId())) {
                        User user = userService.selectByPrimaryKey(Integer.valueOf(comments.get(i).getUserId()));
                        t.setCurrentHandleName(user == null ? comments.get(i).getUserId() : user.getLoginName());
                        t.setOperation(comments.get(i).getFullMessage().split(",")[0]);
                        t.setDescription(comments.get(i).getFullMessage().split(",").length <= 1 ? null : comments.get(i).getFullMessage().split(",")[1]);
                    }
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
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
            JSONArray jsonArray = JSONArray.fromObject(tasks, jsonConfig);
            result.put("rows", jsonArray);
            result.put("success", true);
            return new ResponseObject<Map<String, Object>>(result);
        } catch (Exception e) {
            result.put("errorMsg", "查询出错：" + e.getMessage());
            result.put("success", false);
            return new ResponseObject<Map<String, Object>>(result);
        }

    }
}
