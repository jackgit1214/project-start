package com.project.system.web;

import com.nimbusds.openid.connect.sdk.federation.api.OperationType;
import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.service.impl.SysModuleServiceImpl;
import lombok.Data;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.project.core.common.anaotation.SystemLog.*;

/**
 * 模块管理控制 类
 *
 * @author lilj
 * @date 2021/04/26
 **/
@Controller
@RequestMapping("/module")
public class ModuleController {

    private final SysModuleServiceImpl sysModuleServiceImpl;
    public static final String UUID_TOKEN = "module_token";
    public ModuleController(SysModuleServiceImpl sysModuleServiceImpl) {
        this.sysModuleServiceImpl = sysModuleServiceImpl;
    }


    @RequestMapping("/index")
    public String index(ModelMap map) {
        String path = "system/module/index";
        return path;
    }


    @ResponseBody
    @RequestMapping("/module")
    @QueryModelParam
    @SystemLog(moduleId = "模块管理",description = "查看模块数据",opeType=OpeType.DISPLAY)
    public Object getModules(
            QueryModel queryModel, int page, int limit) {


        ModelMap map = new ModelMap();
        PageResult pageResult = new PageResult<SysUser>(page, limit);
        try {
            queryModel.setOrderByClause("funOrder");
            this.sysModuleServiceImpl.findObjectsByPage(queryModel, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        map.addAttribute("status", rtnMsg);
        return map;
    }


    @RequestMapping("/edit")
    @SystemLog(moduleId = "模块管理",description = "编辑模块数据，打开编辑页面",opeType=OpeType.DISPLAY)
    @DuplicateSubmission(needSaveToken = true, tokenName = UUID_TOKEN)
    public String edit(String id, String superId, ModelMap map) {
        SysModule sysModule = this.sysModuleServiceImpl.findObjectById(id);
        if (sysModule == null) {
            sysModule = new SysModule();
            sysModule.setSuperModId(superId);
            sysModule.setFunOrder(0);
            sysModule.setIsUse(1);
        }
        String path = "system/module/edit";
        map.put("module", sysModule);
        return path;
    }

    @ResponseBody
    @RequestMapping("/delete")
    @SystemLog(moduleId = "模块管理",description = "删除模块数据",opeType=OpeType.DEL)
    public Object deleteModule(String[] moduleIds) {
        ModelMap map = new ModelMap();
        int rows = this.sysModuleServiceImpl.delete(moduleIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/save")
    @SystemLog(moduleId = "模块管理",description = "保存模块数据",opeType=OpeType.EDIT)
    @DuplicateSubmission(needRemoveToken = true, tokenName = UUID_TOKEN)
    public ModelMap addOrUpdate(SysModule record) {
        ModelMap modelMap = new ModelMap();
        int rows = this.sysModuleServiceImpl.save(record);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);

        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/status")
    @SystemLog(moduleId = "模块管理",description = "修改模块数据状态",opeType=OpeType.EDIT)
    public ModelMap handleSimpleOperation(@RequestBody ModuleRequestParam param) {

        SysModule module = param.getModule();
        ModelMap modelMap = new ModelMap();
        int rows = this.sysModuleServiceImpl.save(module);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @Data
    public static class ModuleRequestParam {
        private String type;
        private SysModule module;
    }
}
