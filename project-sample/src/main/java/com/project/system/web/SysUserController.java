package com.project.system.web;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.project.core.common.SysConstant;
import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.model.SystemRole;
import com.project.system.service.SysRoleFuncService;
import com.project.system.service.SysRoleUserService;
import com.project.system.service.SysUserService;
import com.project.system.service.impl.SysModuleServiceImpl;
import com.project.system.service.impl.SysRoleServiceImpl;
import lombok.Data;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController {

    private final SysUserService sysUserServiceImpl;
    private final SysRoleServiceImpl sysRoleServiceImpl;
    private final SysModuleServiceImpl sysModuleServiceImpl;
    private final SysRoleFuncService sysRoleFuncServiceImpl;
    private final SysRoleUserService sysRoleUserServiceImpl;

    public static final String UUID_TOKEN = "user_token";

    public SysUserController(SysUserService sysUserServiceImpl, SysRoleServiceImpl sysRoleServiceImpl, SysModuleServiceImpl sysModuleServiceImpl, SysRoleFuncService sysRoleFuncServiceImpl, SysRoleUserService sysRoleUserServiceImpl) {
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.sysRoleServiceImpl = sysRoleServiceImpl;
        this.sysModuleServiceImpl = sysModuleServiceImpl;
        this.sysRoleFuncServiceImpl = sysRoleFuncServiceImpl;
        this.sysRoleUserServiceImpl = sysRoleUserServiceImpl;
    }

    @RequestMapping("/index")
    public String index(ModelMap map) {
        String path = "system/user/index";
        return path;
    }

    @ResponseBody
    @RequestMapping("/users")
    @QueryModelParam
    public Object getUsers(
            QueryModel queryModel, int page, int limit, String depId) {
        ModelMap map = new ModelMap();
        PageResult pageResult = new PageResult<SysUser>(page, limit);
        try {
            List<SysUser> users = this.sysUserServiceImpl.findUserByCondition(queryModel, depId, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        map.addAttribute("status", rtnMsg);
        return map;
    }


    @RequestMapping("/edit")
    @DuplicateSubmission(needSaveToken = true, tokenName = UUID_TOKEN)
    public String edit(String id, ModelMap map) {
        SysUser sysUser = this.sysUserServiceImpl.findObjectById(id);
        List<SystemRole> roles = sysRoleServiceImpl.findAllObjects();
        List<SysModule> modules = this.sysModuleServiceImpl.findAllObjects();

        if (sysUser == null) {
            sysUser = new SysUser();

        }

        String path = "system/user/edit";
        map.put("user", sysUser);
        map.put("roles", roles);
        map.put("modules", modules);
        return path;
    }


    @ResponseBody
    @RequestMapping("/delete")
    public Object deleteUser(String[] userIds) {
        ModelMap map = new ModelMap();
        int rows = this.sysUserServiceImpl.delete(userIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/save")
    @DuplicateSubmission(needRemoveToken = true, tokenName = UUID_TOKEN)
    public ModelMap addOrUpdate(SysUser record, String[] userRoles, String[] departmentIds, @RequestParam("avatarFile") MultipartFile file) {
        ModelMap modelMap = new ModelMap();
        int rows = 0;
        BaseResult rtnMsg = null;
        try {
            if (file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename())) {
                record.setAvatar(file.getBytes());
            }
            rows = this.sysUserServiceImpl.saveWithBlob(record, userRoles, departmentIds);
            rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        } catch (IOException e) {
            e.printStackTrace();
            rtnMsg = new BaseResult(ReturnCode.FAILED.getCode(), e.getMessage(), rows);
        }
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/status")
    public ModelMap handleSimpleOperation(@RequestBody UserRequestParam param) {

        SysUser user = param.getUser();
        ModelMap modelMap = new ModelMap();
        if ("3".equals(param.getType())) { //3时为重置密码
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(SysConstant.SYSDEFAULTPASSWORD));
        }
        int rows = this.sysUserServiceImpl.save(user);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @Data
    public static class UserRequestParam {
        private String type;
        private SysUser user;
    }
}