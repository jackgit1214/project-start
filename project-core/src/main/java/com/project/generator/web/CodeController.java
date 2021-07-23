package com.project.generator.web;

import java.io.File;
import java.util.List;

//import com.project.generator.model.MysqlTables;
//import com.project.generator.service.MysqlTablesService;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.web.controller.BaseController;
import com.project.generator.model.GeneratorActionProperties;
import com.project.generator.model.GeneratorDaoProperties;
import com.project.generator.model.GeneratorPageProperties;
import com.project.generator.model.GeneratorProperties;
import com.project.generator.model.GeneratorServiceProperties;
import com.project.generator.model.MysqlTables;
import com.project.generator.service.ConcreteGeneratorImpl;
import com.project.generator.service.MysqlTablesService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成工具控制类
 *
 * @author lilj
 * @date 2021/03/22
 **/
@Controller
@RequestMapping("/code")
@Slf4j
public class CodeController extends BaseController {

    private final MysqlTablesService mysqlTablesServiceImpl;
    private final GeneratorServiceProperties generatorServiceProperties;
    private final GeneratorDaoProperties generatorDaoProperties;
    private final GeneratorActionProperties generatorActionProperties;
    private final GeneratorPageProperties generatorPageProperties;
    private final GeneratorProperties generatorProperties;
    private final ConcreteGeneratorImpl concreteGenerator;

    public CodeController(MysqlTablesService mysqlTablesServiceImpl, GeneratorServiceProperties generatorServiceProperties, GeneratorDaoProperties generatorDaoProperties, GeneratorActionProperties generatorActionProperties, GeneratorPageProperties generatorPageProperties, GeneratorProperties generatorProperties, ConcreteGeneratorImpl concreteGenerator) {
        this.mysqlTablesServiceImpl = mysqlTablesServiceImpl;
        this.generatorServiceProperties = generatorServiceProperties;
        this.generatorDaoProperties = generatorDaoProperties;
        this.generatorActionProperties = generatorActionProperties;
        this.generatorPageProperties = generatorPageProperties;
        this.generatorProperties = generatorProperties;
        this.concreteGenerator = concreteGenerator;
    }


    @RequestMapping("/index")
    public String index(ModelMap map) {
        log.info(System.getProperty("user.dir"));
        List<MysqlTables> pageData = null;
        try {
            pageData = this.mysqlTablesServiceImpl.getDatabaseTables("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        generatorDaoProperties.setProjectName(projectConfig.getConfigure().getProjectShortName());
        generatorProperties.setProjectDirector(projectConfig.getConfigure().getModuleName());
        map.put("data", pageData);
        map.put("modelP", generatorProperties);
        map.put("daoP", generatorDaoProperties);
        map.put("actionP", generatorActionProperties);
        map.put("serviceP", generatorServiceProperties);
        map.put("pageP", generatorPageProperties);
        return "code/index";
    }

    @RequestMapping("/generating")
    @ResponseBody
    public Object CodeGenerating(@RequestBody GenParam genParam) throws Exception {
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), "");
        try {
            concreteGenerator.generateCode(genParam.getModelP(), genParam.getDaoP(), genParam.getServiceP(),
                    genParam.getActionP(), genParam.getPageP());
        } catch (Exception e) {
            rtnMsg.setCode(ReturnCode.FAILED.getCode());
            rtnMsg.setMessage("代码生成错误，请检查生成结果！");
            rtnMsg.setData(e.getMessage());
            e.printStackTrace();
        }
        return rtnMsg;
    }

    @Data
    public static class GenParam {
        private GeneratorProperties modelP;
        private GeneratorDaoProperties daoP;
        private GeneratorServiceProperties serviceP;
        private GeneratorActionProperties actionP;
        private GeneratorPageProperties pageP;

    }

}
