package com.springmvc.controller.flowController;

import com.springmvc.pojo.*;
import com.springmvc.service.RoleOperationService;
import com.springmvc.service.RoleService;
import com.springmvc.service.UserService;
import com.springmvc.service.WorkItemService;
import com.springmvc.utils.ResponseObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.*;
import org.activiti.engine.runtime.Job;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RoleOperationService roleOperationService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WorkItemService workItemService;

    /**
     * 根据用户id分页查询任务
     *
     * @param s_taskName
     * @param roleId
     * @param userId
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/toDoList")
    public ModelAndView toDoList(String s_taskName, String roleId, String userId,
                                 @RequestParam(required = false) String page,
                                 @RequestParam(required = false) String rows) throws Exception {
        if (s_taskName == null) {
            s_taskName = "";
        }
        ModelAndView modelAndView = new ModelAndView();
        PageBean pageBean = new PageBean();
        if (page != null) {
            pageBean.setPage(Integer.parseInt(page));
        }
        if (rows != null) {
            pageBean.setPageSize(Integer.parseInt(rows));
        }
        JSONObject result = new JSONObject();
        List<String> roleIdList = Arrays.asList(StringUtils.split(roleId, ","));
        try {
            List<Task> taskList = new ArrayList<Task>();
            // 1、根据角色查询
            List<Task> taskListGroup = taskService.createTaskQuery().
                    taskCandidateGroupIn(roleIdList).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            taskList.addAll(taskListGroup);
            // 2、根据用户查询
            List<Task> taskListUser = taskService.createTaskQuery().
                    taskCandidateUser(userId).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            taskList.addAll(taskListUser);
            // 根据taskId去重，避免并行网关的问题
            List<Task> list = new ArrayList<Task>();
            List<String> pid = new ArrayList<String>();
            for (int i = 0; i < taskList.size(); i++) {
                if (!pid.contains(taskList.get(i).getId())) {
                    pid.add(taskList.get(i).getId());
                    list.add(taskList.get(i));
                }
            }
            taskList = list;
            // 取数据
            List<MyTask> tasks = new ArrayList<MyTask>(30);
            for (Task task : taskList) {
                MyTask t = new MyTask();
                // 通过comment表查询上一节点审批人/元素反转 时间降序排列
                List<Comment> comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
                // 按集合createTime降序排列
                Collections.sort(comments, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Comment c1 = (Comment) o1;
                        Comment c2 = (Comment) o2;
                        int flag = c2.getTime().compareTo(c1.getTime());
                        return flag;
                    }
                });

                String processInstanceId = task.getProcessInstanceId();
                t.setProcessInstanceId(processInstanceId);
                // 获取单号
                Integer id = (Integer) taskService.getVariable(task.getId(), "id");
                t.setId(String.valueOf(id));
                WorkItem workItem = workItemService.selectByPrimaryKey(id);

                // 查询当前节点任务办理 候选组/候选人
                List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(task.getId());
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
                // 查询前一个节点的处理人是谁
                User user = userService.selectByPrimaryKey(Integer.valueOf(comments.get(0).getUserId()));
                t.setPreHandleName(user.getLoginName());
                t.setTaskId(task.getId());
                t.setTaskName(task.getName());
                t.setCreateTime(task.getCreateTime());
                t.setOperation(comments.get(0).getFullMessage().split(",")[0]);
                t.setDescription(comments.get(0).getFullMessage().split(",").length <= 1 ? null : comments.get(0).getFullMessage().split(",")[1]);
                try {
                    // 查询定时任务获取到期时间
                    Job job = managementService.createJobQuery().processInstanceId(processInstanceId).singleResult();
                    t.setExpectTime(job == null ? null : job.getDuedate());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                // 获取流程发起人
                String starterId = (String) taskService.getVariable(task.getId(), "flowStarter");
                User starter = userService.selectByPrimaryKey(Integer.valueOf(starterId));
                t.setFlowStarter(starter.getLoginName());

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
            result.put("success", true);
            result.put("rows", jsonArray);
            result.put("total", jsonArray.size());
            // 返回jsp
            modelAndView.setViewName("views/toDoList");
            modelAndView.addObject("taskList", tasks);
            return modelAndView;
        } catch (Exception e) {
            result.put("errorMsg", "待办查询失败：" + e.getMessage());
            result.put("success", false);
        }
        return modelAndView;
    }

    /**
     * 根据任务id及角色返回相应操作名称
     *
     * @param taskId
     * @param roleName 多角色","拼接
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/listRoleWithOperations")
    public ResponseObject<Map<String, Object>>
    listRoleWithOperations(String taskId, String roleName, String flowName) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if (roleName == null || roleName.equals("")) {
            roleName = " ";
        }
        String[] roleNameList = StringUtils.split(roleName, ",");
        String taskName = "";
        ArrayList<String> list = new ArrayList<String>();
        try {
            for (String role_name : roleNameList) {
                Map<String, Object> map = new HashMap<String, Object>();
                Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
                map.put("taskName", task.getName());
                taskName = task.getName();
                map.put("flowName", flowName);
                map.put("roleName", role_name);
                List<RoleOperation> roleOperations = roleOperationService.find(map);
                for (RoleOperation ro : roleOperations) {
                    list.add(ro.getOperation());
                }
            }
            result.put("taskName", taskName);
            result.put("operations", list);
            result.put("success", true);
            return new ResponseObject<Map<String, Object>>(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("errorMsg", e.getMessage());
            return new ResponseObject<Map<String, Object>>(result);
        }
    }
}
