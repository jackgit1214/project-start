package com.project.resources;

import com.project.api.RoleAPI;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysRole;
import com.project.core.mybatis.util.PageResult;
import com.project.system.model.SysModule;
import com.project.system.model.SystemRole;
import com.project.system.service.SysRoleFuncService;
import com.project.system.service.SysRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@SystemLog(moduleId = "角色管理")
public class SysRoleController extends AbstractController implements RoleAPI {

    public static final String UUID_TOKEN = "role_token";
    private final SysRoleService sysRoleServiceImpl;

    public SysRoleController(SysRoleFuncService sysRoleFuncServiceImpl, SysRoleService sysRoleServiceImpl) {
        this.sysRoleServiceImpl = sysRoleServiceImpl;
    }

    @Override
    @QueryModelParam
    @SystemLog(description = "查阅角色数据，显示首页", opeType = SystemLog.OpeType.DISPLAY)
    public ResponseEntity<BaseResult> getRoles(
            QueryModel queryModel, int page, int limit) {
        PageResult pageResult = new PageResult<SysRole>(page, limit);
        try {
            this.sysRoleServiceImpl.findObjectsByPage(queryModel, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @QueryModelParam
    @SystemLog(description = "查阅所有角色数据", opeType = SystemLog.OpeType.DISPLAY)
    public ResponseEntity<BaseResult> getAllRoles(QueryModel queryModel) {
        List<SystemRole> roles = this.sysRoleServiceImpl.findObjects(queryModel);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), roles);
        return ResponseEntity.ok(rtnMsg);
    }


    @Override
    @SystemLog(description = "删除角色数据", opeType = SystemLog.OpeType.DEL)
    public ResponseEntity<BaseResult> deleteRole(String[] roleIds) {
        int rows = this.sysRoleServiceImpl.delete(roleIds);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @SystemLog(description = "保存角色数据", opeType = SystemLog.OpeType.EDIT)
    public ResponseEntity<BaseResult> addOrUpdate(RoleRequestParam record) {
        int rows = this.sysRoleServiceImpl.save(record.getRole(), record.getRoleFun());
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), record.getRole());
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @SystemLog(description = "查阅指定角色数据", opeType = SystemLog.OpeType.DISPLAY)
    public ResponseEntity<BaseResult> getRoleId(String id) {
        SystemRole role = this.sysRoleServiceImpl.getRolePlusById(id);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), role);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    @SystemLog(description = "查阅所有角色数据", opeType = SystemLog.OpeType.DISPLAY)
    public ResponseEntity<BaseResult> getAllRoleModules() {
        Map<String, List<SysModule>> sysModules = this.sysRoleServiceImpl.getRoleModules();
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), sysModules);
        return ResponseEntity.ok(rtnMsg);
    }

}