package com.springmvc.listeners;

import com.springmvc.pojo.User;
import com.springmvc.service.UserService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.*;

/**
 * 创建监听器，实现ActivitiEventListener接口
 */
public class MyEventListener implements ActivitiEventListener {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private UserService userService;

    public void onEvent(ActivitiEvent event) {
        // 任务创建，当流程走到一个任务节点时，会进入这个条件,即下一节点审批人
        if (event.getType() == ActivitiEventType.TASK_CREATED) {
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
            TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();
            // taskEntity.getAssignee()，它是ID
            Set<IdentityLink> candidates = taskEntity.getCandidates();//这个是获取候选的
            ArrayList<String> groupIds = new ArrayList<String>();
            ArrayList<String> userIds = new ArrayList<String>();
            ArrayList<String> userMails = new ArrayList<String>();
            for (IdentityLink identityLink : candidates) {
                if (identityLink.getGroupId() != null && identityLink.getGroupId() != "") {
                    groupIds.add(identityLink.getGroupId());
                }
                if (identityLink.getUserId() != null && identityLink.getUserId() != "") {
                    userIds.add(identityLink.getUserId());
                }
            }
            // 通过roleId获取所有用户
            for (String groupId : groupIds) {
                List<User> users = userService.listByRoleId(Integer.valueOf(groupId));
                for (User user : users) {
                    if (!userIds.contains(String.valueOf(user.getId()))) {
                        userIds.add(String.valueOf(user.getId()));
                    }
                }
            }
            // 获取用户
            for (String userId : userIds) {
                User user = userService.selectByPrimaryKey(Integer.valueOf(userId));
                if (user != null) {
                    userMails.add(user.getEmail());
                }
            }
            String usersMailAddress = StringUtils.join(userMails, ",");
            this.sendTaskMail(taskEntity.getId(), taskEntity.getName(), usersMailAddress);
        }
        if (event.getType() == ActivitiEventType.TASK_ASSIGNED) {// 任务认领，按照谁先登录谁先认领的原则，认领后会给该认领的用户发邮件
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
            TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();//认领后我们主要用taskEntity.getAssignee()来查询当前用户信息，并发邮件
            // this.sendTaskMail(taskEntity.getOwner(), taskEntity.getAssignee(), taskEntity.getName(), event.getType());
        }
        // 审批完成，首先会进入下一节点，并给下一节点的用户发邮件，之后会通过历史流程去找到流程发起者，并将审批信息通过邮件发送给发起者。
        // 这里要注意，该类不能注入historyService，因为它先于historyService存在，必须通过entityEvent.getEngineServices();去获取。
        if (event.getType() == ActivitiEventType.TASK_COMPLETED) {
            ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
            TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();
            // this.sendTaskMail(taskEntity.getId(), taskEntity.getAssignee(), taskEntity.getName(), event.getType());//不要太在意这个方法，随便写的
        }
    }

    public boolean isFailOnException() {
        // true表示打印错误信息，并抛异常，否则任务会往下执行
        return false;
    }

    /**
     * 发送邮件
     *
     * @param taskId
     * @param taskName
     * @param userMails
     */
    public void sendTaskMail(String taskId, String taskName, String userMails) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setSubject("流程审批通知");
        msg.setTo(userMails);
        msg.setText("任务id:【" + taskId + "】，任务节点：【" + taskName + "】，需要您处理，请尽快处理！");
        msg.setFrom("649314761@qq.com");
        try {
            mailSender.send(msg);
        } catch (MailException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
