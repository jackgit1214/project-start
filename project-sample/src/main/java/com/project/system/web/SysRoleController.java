package com.project.system.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.project.core.common.SysConstant;
import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.security.model.SysRole;
import com.project.core.web.controller.BaseController;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.model.SystemRole;
import com.project.system.service.SysRoleFuncService;
import com.project.system.service.SysRoleService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/role")
@SystemLog(moduleId = "角色管理")
public class SysRoleController extends BaseController {
    private final SysRoleService sysRoleServiceImpl;
    public static final String UUID_TOKEN = "role_token";

    public SysRoleController(SysRoleFuncService sysRoleFuncServiceImpl, SysRoleService sysRoleServiceImpl) {

        this.sysRoleServiceImpl = sysRoleServiceImpl;
    }

    @RequestMapping("/index")
    public String index(ModelMap map) {
        String path = "system/role/index";
        return path;
    }

    @ResponseBody
    @QueryModelParam
    @RequestMapping("/roles")
    @SystemLog(description = "查阅角色数据，显示首页",opeType= SystemLog.OpeType.DISPLAY)
    public Object getRoles(
            QueryModel queryModel, int page, int limit) {
        ModelMap map = new ModelMap();
        PageResult pageResult = new PageResult<SysRole>(page, limit);
        try {
            this.sysRoleServiceImpl.findObjectsByPage(queryModel, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        map.addAttribute("status", rtnMsg);
        return map;
    }


    @RequestMapping("/edit")
    @SystemLog(description = "查阅角色数据，打开角色编辑页面",opeType= SystemLog.OpeType.DISPLAY)
    @DuplicateSubmission(needSaveToken = true, tokenName = UUID_TOKEN)
    public String edit(String id, ModelMap map) {
        SystemRole role = sysRoleServiceImpl.getRolePlusById(id);

        if (role == null)
            role = new SystemRole();
        String path = "system/role/edit";
        map.put("role", role);
        return path;
    }


    @ResponseBody
    @RequestMapping("/delete")
    @SystemLog(description = "删除角色数据",opeType= SystemLog.OpeType.DEL)
    public Object deleteRole(String[] roleIds) {
        ModelMap map = new ModelMap();
        int rows = this.sysRoleServiceImpl.delete(roleIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/save")
    @SystemLog(description = "保存角色数据",opeType= SystemLog.OpeType.EDIT)
    @DuplicateSubmission(needRemoveToken = true, tokenName = UUID_TOKEN)
    public ModelMap addOrUpdate(SystemRole record, String[] rolesFun) {
        ModelMap modelMap = new ModelMap();
        int rows = this.sysRoleServiceImpl.save(record, rolesFun);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        modelMap.addAttribute("data", record);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/{id}")
    public ModelMap getRoleId(String id) {
        ModelMap modelMap = new ModelMap();
        SystemRole role = this.sysRoleServiceImpl.getRolePlusById(id);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), role);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/roleModules")
    public ModelMap getAllRoleModules() {
        ModelMap modelMap = new ModelMap();
        Map<String, List<SysModule>> sysModules = this.sysRoleServiceImpl.getRoleModules();
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), sysModules);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

}