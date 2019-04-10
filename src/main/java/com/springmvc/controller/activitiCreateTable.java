package com.springmvc.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;

public class activitiCreateTable {
    // 数据库中插入25张表
    private static ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-createTable.xml").buildProcessEngine();


    /**
     * 部署流程
     */
    @Test
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        //bpmn文件的名称
        builder.addClasspathResource("flow/TestFlow.bpmn");  // 测试流程


        builder.deploy();
    }

}
