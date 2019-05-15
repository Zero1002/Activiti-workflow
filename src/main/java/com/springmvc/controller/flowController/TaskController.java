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
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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
            // 返回角色列表
            Map<String, Object> params = new HashMap<String, Object>();
            List<Role> rolelists = roleService.list(params);
            modelAndView.addObject("rolelists", rolelists);
            // 返回用户列表
            List<User> userlists = userService.list(params);
            modelAndView.addObject("userlists", userlists);
            return modelAndView;
        } catch (Exception e) {
            result.put("errorMsg", "待办查询失败：" + e.getMessage());
            result.put("success", false);
        }
        return modelAndView;
    }

    /**
     * 已办列表
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
    @RequestMapping("/finishedList")
    public ModelAndView finishedList(String s_taskName, String roleId, String userId,
                                     @RequestParam(required = false) String page,
                                     @RequestParam(required = false) String rows) throws Exception {
        if (s_taskName == null) {
            s_taskName = "";
        }
        PageBean pageBean = new PageBean();
        if (page != null) {
            pageBean.setPage(Integer.parseInt(page));
        }
        if (rows != null) {
            pageBean.setPageSize(Integer.parseInt(rows));
        }
        JSONObject result = new JSONObject();
        List<String> roleIdList = Arrays.asList(StringUtils.split(roleId, ","));
        ModelAndView modelAndView = new ModelAndView();
        try {
            List<HistoricTaskInstance> taskList = new ArrayList<HistoricTaskInstance>();
            // 1、根据角色查询roleName
            List<HistoricTaskInstance> taskListGroup = historyService.createHistoricTaskInstanceQuery().
                    taskCandidateGroupIn(roleIdList).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            taskList.addAll(taskListGroup);
            // 2、根据用户查询userId
            List<HistoricTaskInstance> taskListUser = historyService.createHistoricTaskInstanceQuery().
                    taskCandidateUser(userId).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            taskList.addAll(taskListUser);
            // 根据taskId去重，避免并行网关的问题
            List<HistoricTaskInstance> list = new ArrayList<HistoricTaskInstance>();
            List<String> pid = new ArrayList<String>();
            for (int i = 0; i < taskList.size(); i++) {
                if (!pid.contains(taskList.get(i).getId())) {
                    pid.add(taskList.get(i).getId());
                    list.add(taskList.get(i));
                }
            }
            taskList = list;
            // 取数据
            List<MyTask> myTasks = new ArrayList<MyTask>(30);
            for (HistoricTaskInstance hit : taskList) {
                // 如果该任务对应的流程实例在运行时任务表里查询到，说明就是这个流程实例未走完  并且用用户id以及任务id在运行时候任务表里查询不到结果  才算是已办任务
                // 因为可能是任务刚分配给了别人
                if ((taskService.createTaskQuery().processInstanceId(hit.getProcessInstanceId()) != null)
                        && (taskService.createTaskQuery().taskCandidateUser(userId).taskId(hit.getId()).list().size() == 0)) {
                    MyTask myTask = new MyTask();
                    // 通过comment表查询上一节点审批人/元素反转
                    List<Comment> comments = taskService.getProcessInstanceComments(hit.getProcessInstanceId());
                    Collections.reverse(comments);
                    String processInstanceId = hit.getProcessInstanceId();
                    myTask.setProcessInstanceId(processInstanceId);
                    // 获取单号(获取历史流程变量)
                    List<HistoricVariableInstance> hisVarList = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
                    for (HistoricVariableInstance variable : hisVarList) {
                        // 任务id
                        if (variable.getVariableName().equals("id")) {
                            myTask.setId(String.valueOf(variable.getValue()));
                        }
                        // 流程发起人
                        if (variable.getVariableName().equals("flowStarter")) {
                            User starter = userService.selectByPrimaryKey(Integer.valueOf(String.valueOf(variable.getValue())));
                            myTask.setFlowStarter(starter.getLoginName());
                        }
                    }


                    // 查询当前节点任务办理候选组
//                    List<HistoricIdentityLink> identityLinksForTask = historyService.getHistoricIdentityLinksForTask(hit.getId());
                    // 节点处理人
                    for (int i = 0; i < comments.size(); i++) {
                        if (hit.getId().equals(comments.get(i).getTaskId())) {
                            User user = userService.selectByPrimaryKey(Integer.valueOf(comments.get(i).getUserId()));
                            myTask.setCurrentHandleName(user == null ? comments.get(i).getUserId() : user.getLoginName());
                            myTask.setOperation(comments.get(i).getFullMessage().split(",")[0]);
                            myTask.setDescription(comments.get(i).getFullMessage().split(",").length <= 1 ? null : comments.get(i).getFullMessage().split(",")[1]);
                        }
                    }

                    myTask.setTaskId(hit.getId());
                    myTask.setTaskName(hit.getName());
                    myTask.setCreateTime(hit.getCreateTime());
                    myTask.setEndTime(hit.getEndTime());
                    try {
                        // 查询定时任务获取到期时间
                        Job job = managementService.createJobQuery().processInstanceId(processInstanceId).singleResult();
                        myTask.setExpectTime(job == null ? null : job.getDuedate());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    myTasks.add(myTask);
                }

            }
            // 按集合createTime降序排列
            Collections.sort(myTasks, new Comparator() {
                public int compare(Object o1, Object o2) {
                    MyTask task1 = (MyTask) o1;
                    MyTask task2 = (MyTask) o2;
                    int flag = task2.getCreateTime().compareTo(task1.getCreateTime());
                    return flag;
                }
            });
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
            JSONArray jsonArray = JSONArray.fromObject(myTasks, jsonConfig);
            result.put("success", true);
            result.put("rows", jsonArray);
            result.put("total", jsonArray.size());
            // 返回jsp
            modelAndView.setViewName("views/finishedList");
            modelAndView.addObject("taskList", myTasks);
        } catch (Exception e) {
            result.put("errorMsg", "查询失败：" + e.getMessage());
            result.put("success", false);
        }
        return modelAndView;
    }


    /**
     * 查询任务详情
     *
     * @param id
     * @param taskId
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping("/taskDetail")
    public ModelAndView view(Integer id, String taskId, HttpServletRequest req) {
        WorkItem record = workItemService.selectByPrimaryKey(id);
        record.getExtraInfo();
        ModelAndView mv = new ModelAndView();
        try {
            // 读取blob格式并转化成json对象
            String extraInfo = new String(record.getExtraInfo(), "GB2312");
            JSONObject extraInfoJson = JSONObject.fromObject(extraInfo);
            mv.addObject("extraInfoJson", extraInfoJson);
            mv.addObject("workItem", record);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        mv.addObject("task", task);
        mv.setViewName("views/task_handle");
        return mv;
    }

    /**
     * 根据任务id及角色返回相应操作名称
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/listRoleWithOperations")
    public ResponseObject<Map<String, Object>>
    listRoleWithOperations(String taskId, String flowName) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        String taskName = "";
        ArrayList<String> list = new ArrayList<String>();
        try {
            String[] splitOP = new String[0];
            Map<String, Object> map = new HashMap<String, Object>();
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            map.put("taskName", task.getName());
            taskName = task.getName();
            map.put("flowName", flowName);
            RoleOperation roleOperation = roleOperationService.find(map).get(0);
            splitOP = roleOperation.getOperation().split(",");
            result.put("taskName", taskName);
            result.put("operations", splitOP);
            result.put("success", true);
            return new ResponseObject<Map<String, Object>>(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("errorMsg", e.getMessage());
            return new ResponseObject<Map<String, Object>>(result);
        }
    }

    /**
     * 委托办理功能
     *
     * @param taskId
     * @param assignUserId
     * @param assignRole
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/entrustHandle")
    public ResponseObject<Map<String, Object>> entrustHandle(String taskId, String assignUserId, String assignRole) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        if (assignUserId == null && assignRole == null) {
            result.put("errorMsg", "请选定委托办理人/组");
            result.put("success", false);
            return new ResponseObject<Map<String, Object>>(result);
        }
        try {
            if (assignUserId != null && assignUserId != "") {
                taskService.addCandidateUser(taskId, assignUserId);
            }
            if (assignRole != null && assignRole != "") {
                taskService.addCandidateGroup(taskId, assignRole);
            }
            result.put("success", true);
            return new ResponseObject<Map<String, Object>>(result);
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
            result.put("success", false);
            return new ResponseObject<Map<String, Object>>(result);
        }
    }
}
