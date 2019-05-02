package com.springmvc.controller.flowController;

import com.springmvc.pojo.PageBean;
import com.springmvc.utils.ResponseObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("/deploy")
public class DeployController {

    private static final Logger logger = LoggerFactory.getLogger(DeployController.class);

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 上传流程部署文件，zip格式包含bpmn/png
     *
     * @param deployFile
     * @return
     * @throws Exception
     */
    @RequestMapping("/deploy")
    public ResponseObject<Map<String, Object>> deploy(@RequestParam("deployFile") MultipartFile deployFile) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            repositoryService.createDeployment()
                    .name(deployFile.getOriginalFilename())
                    .addZipInputStream(new ZipInputStream(deployFile.getInputStream()))
                    .deploy();
            result.put("success", true);
            return new ResponseObject<Map<String, Object>>(result);
        } catch (Exception e) {
            logger.error("部署流程失败" + e.getMessage());
            result.put("success", false);
            return new ResponseObject<Map<String, Object>>(result);
        }
    }

    /**
     * 流程部署查询
     *
     * @param page
     * @param rows
     * @param s_name
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, String s_name, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView();
        try {
            if (s_name == null) {
                s_name = "";
            }
            PageBean pageBean = new PageBean();
            if (page != null) {
                pageBean.setPage(Integer.parseInt(page));
            }

            if (rows != null) {
                pageBean.setPageSize(Integer.parseInt(rows));
            }

            List<Deployment> deploymentList = repositoryService.createDeploymentQuery()  //创建流程部署查询
                    .orderByDeploymentName().desc()         //根据部署时间降序排列
                    .deploymentNameLike("%" + s_name + "%")     //根据流程部署名称模糊查询
                    .list();

            long total = repositoryService.createDeploymentQuery().deploymentNameLike("%" + s_name + "%").count();  //查询名字像这个总数
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(new String[]{"resources"});
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
            JSONObject result = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(deploymentList, jsonConfig);
            mv.addObject("deployLists", jsonArray);
            mv.setViewName("views/deployManagement");
            return mv;
        } catch (Exception e) {
            logger.error("查询流程失败" + e.getMessage());
        }
        return null;
    }

    /**
     * 删除流程部署
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public ResponseObject<Map<String, Object>> delete(@RequestParam(value = "ids") String ids) throws Exception {
        String[] strids = ids.split(",");
        for (String str : strids) {
            if (str != null) {
                repositoryService.deleteDeployment(str, true);   //是否级联删除
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return new ResponseObject<Map<String, Object>>(result);
    }
}
