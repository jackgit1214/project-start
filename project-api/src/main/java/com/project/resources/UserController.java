package com.project.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.UserApi;
import com.project.core.common.SysConstant;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.model.SystemRole;
import com.project.system.service.SysRoleFuncService;
import com.project.system.service.SysRoleUserService;
import com.project.system.service.SysUserService;
import com.project.system.service.impl.SysModuleServiceImpl;
import com.project.system.service.impl.SysRoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@SystemLog(moduleId = "用户管理")
public class UserController extends AbstractController implements UserApi {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;
    private final SysUserService sysUserServiceImpl;
    private final SysRoleServiceImpl sysRoleServiceImpl;
    private final SysModuleServiceImpl sysModuleServiceImpl;
    private final SysRoleFuncService sysRoleFuncServiceImpl;
    private final SysRoleUserService sysRoleUserServiceImpl;

    public static final String UUID_TOKEN = "user_token";

    public UserController(SysUserService sysUserServiceImpl, SysRoleServiceImpl sysRoleServiceImpl,
                             SysModuleServiceImpl sysModuleServiceImpl, SysRoleFuncService sysRoleFuncServiceImpl,
                             SysRoleUserService sysRoleUserServiceImpl,
                             ObjectMapper objectMapper,HttpServletRequest request) {
        this.sysUserServiceImpl = sysUserServiceImpl;
        this.sysRoleServiceImpl = sysRoleServiceImpl;
        this.sysModuleServiceImpl = sysModuleServiceImpl;
        this.sysRoleFuncServiceImpl = sysRoleFuncServiceImpl;
        this.sysRoleUserServiceImpl = sysRoleUserServiceImpl;
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    @SystemLog(description = "查询当前登录用户",opeType= SystemLog.OpeType.DISPLAY)
    public ResponseEntity<SysUser> currentUser() {
        SysUser userInfo =(SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userInfo);
    }

    @Override
    @QueryModelParam
    @SystemLog(description = "查询用户数据",opeType= SystemLog.OpeType.DISPLAY)
    public ResponseEntity<BaseResult> findUsers(QueryModel queryModel, int page, int limit,String departmentId) {
        PageResult pageResult = new PageResult<SysUser>(page, limit);
        try {
            List<SysUser> users = this.sysUserServiceImpl.findUserByCondition(queryModel, departmentId, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @SystemLog(description = "查询用户数据",opeType= SystemLog.OpeType.DISPLAY)
    public ResponseEntity<SysUser> findUser(String userId) {
        SysUser sysUser = this.sysUserServiceImpl.findObjectById(userId);
        return ResponseEntity.ok(sysUser);
    }

    @Override
    @SystemLog(description = "删除用户数据",opeType= SystemLog.OpeType.DEL)
    public ResponseEntity<BaseResult> deleteUsers(String[] userIds) {
        int rows = this.sysUserServiceImpl.delete(userIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @SystemLog(description = "修改用户",opeType= SystemLog.OpeType.EDIT)
    public ResponseEntity<BaseResult> updateUser(String userId, SysUser record, String[] userRoles, String[] departmentIds, MultipartFile file) {
        return this.createOrUpdateUser( userId,  record, userRoles,  departmentIds, file);
    }


    @Override
    @SystemLog(description = "创建新用户",opeType= SystemLog.OpeType.EDIT)
    public ResponseEntity<BaseResult> createUser(SysUser record, String[] userRoles, String[] departmentIds, MultipartFile file) {
        return this.createOrUpdateUser( null,  record, userRoles,  departmentIds, file);
    }

    @Override
    @SystemLog(description = "激活或禁用用户数据，修改密码等，",opeType= SystemLog.OpeType.EDIT)
    public ResponseEntity<SysUser> updateUserStatus(String userId, UserRequestParam param) {
        SysUser user = param.getUser();
        ModelMap modelMap = new ModelMap();
        if ("3".equals(param.getType())) { //3时为重置密码
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(SysConstant.SYSDEFAULTPASSWORD));
        }
        int rows = this.sysUserServiceImpl.save(user);
        //BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Void> createUsersWithListInput(List<SysUser> users) {
        return null;
    }

    private ResponseEntity<BaseResult> createOrUpdateUser(String userId, SysUser record, String[] userRoles, String[] departmentIds, MultipartFile file){
        int rows = 0;
        BaseResult rtnMsg = null;
        try {
            byte[] test = file.getBytes();
            if (file!=null && file.getOriginalFilename() != null && !"".equals(file.getOriginalFilename()) && file.getBytes().length>0) {
                record.setAvatar( file.getBytes());
            }else
                record.setAvatar(null);
            rows = this.sysUserServiceImpl.saveWithBlob(record, userRoles, departmentIds);
            rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), record);
        } catch (IOException e) {
            e.printStackTrace();
            rtnMsg = new BaseResult(ReturnCode.FAILED.getCode(), e.getMessage(), rows);
        }
        return ResponseEntity.ok(rtnMsg);
    }

}