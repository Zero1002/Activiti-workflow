package com.springmvc.pojo;


import java.io.Serializable;
import java.util.Date;

public class MyTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;                                  // 表单号
    private String taskId;                              // 任务taskId
    private String taskName;                            // 任务名称
    private String processInstanceId;                   // 流程实例Id
    private String currentHandleName;                   // 当前节点候补人/组
    private String preHandleName;                       // 前一节点办理人
    private Date createTime;                            // 创建时间
    private Date endTime;                               // 结束时间,实际处理时间
    private Date expectTime;                            // 预计处理时间
    private String description;                         // 描述
    private String flowName;                            // 工作流名称
    private String operation;                           // 操作
    private String flowStarter;                         // 流程发起人

    public MyTask() {
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getCurrentHandleName() {
        return currentHandleName;
    }

    public void setCurrentHandleName(String currentHandleName) {
        this.currentHandleName = currentHandleName;
    }

    public String getPreHandleName() {
        return preHandleName;
    }

    public void setPreHandleName(String preHandleName) {
        this.preHandleName = preHandleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlowStarter() {
        return flowStarter;
    }

    public void setFlowStarter(String flowStarter) {
        this.flowStarter = flowStarter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
