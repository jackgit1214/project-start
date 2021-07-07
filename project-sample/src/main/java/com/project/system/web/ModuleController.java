package com.project.system.web;

import java.util.List;

import com.project.core.common.SysConstant;
import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.model.SystemRole;
import com.project.system.service.impl.SysModuleServiceImpl;
import lombok.Data;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    public Object deleteModule(String[] moduleIds) {
        ModelMap map = new ModelMap();
        int rows = this.sysModuleServiceImpl.delete(moduleIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/save")
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
