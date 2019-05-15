package com.springmvc.controller.flowController;

import com.springmvc.pojo.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("def")
public class processDefController {
    private static final Logger logger = LoggerFactory.getLogger(processDefController.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 流程定义查询
     *
     * @param page
     * @param rows
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(required = false) String page,
                             @RequestParam(required = false) String rows, String s_name,
                             @RequestParam(required = false) Boolean isAll, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();
        try {
            if (s_name == null) {
                s_name = "";
            }
            if (isAll == null) {
                isAll = true;
            }

            PageBean pageBean = new PageBean();
            if (page != null) {
                pageBean.setPage(Integer.parseInt(page));
            }

            if (rows != null) {
                pageBean.setPageSize(Integer.parseInt(rows));
            }
            //  PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                    .orderByProcessDefinitionKey().desc()
                    .processDefinitionNameLike("%" + s_name + "%")
                    .list();
            long total = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike("%" + s_name + "%").count(); //查询名字像这个总数
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(new String[]{"identityLinks", "processDefinition"});
            JSONObject result = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(processDefinitionList, jsonConfig);
            if (!isAll) {
                // 去重 项目提交时的下拉框
                List<ProcessDefinition> tmpList = new ArrayList<ProcessDefinition>();
                List<String> keyName = new ArrayList<String>();
                for (int i = 0; i < processDefinitionList.size(); i++) {
                    if (!keyName.contains(processDefinitionList.get(i).getKey())) {
                        tmpList.add(processDefinitionList.get(i));
                    }
                }
                jsonArray = JSONArray.fromObject(tmpList, jsonConfig);
            }
            mv.addObject("defLists", jsonArray);
            mv.setViewName("views/defManagement");
            return mv;
        } catch (Exception e) {
            logger.error("查询流程失败" + e.getMessage());
        }
        return null;
    }

    /**
     * 部署定义流程图查看
     *
     * @param deploymentId
     * @param diagramResourceName
     * @return
     */
    @RequestMapping("/showCurrentView")
    public ModelAndView showCurrentView(String deploymentId, String diagramResourceName) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("deploymentId", deploymentId);
        mv.addObject("diagramResourceName", diagramResourceName);
        mv.setViewName("views/flowView");
        return mv;
    }
}
