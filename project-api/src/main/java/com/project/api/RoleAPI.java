package com.project.api;

import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysModule;
import com.project.system.model.SystemRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(tags = "角色管理")
@RequestMapping("/role")
public interface RoleAPI {

    @QueryModelParam
    @GetMapping("/roles")
    @SystemLog(description = "查阅角色数据，显示首页", opeType = SystemLog.OpeType.DISPLAY)
    @Operation(summary = "角色信息", description = "取得所有角色信息")
    ResponseEntity<BaseResult> getRoles(
            QueryModel queryModel, int page, int limit);

    @QueryModelParam
    @GetMapping("/roles/all")
    @SystemLog(description = "查阅所有角色数据", opeType = SystemLog.OpeType.DISPLAY)
    @Operation(summary = "角色信息", description = "取得所有角色信息")
    ResponseEntity<BaseResult> getAllRoles(QueryModel queryModel);

    @DeleteMapping("/{id}")
    @SystemLog(description = "删除角色数据", opeType = SystemLog.OpeType.DEL)
    @Operation(summary = "删除角色", description = "根据ids删除角色")
    ResponseEntity<BaseResult> deleteRole(@PathVariable("id") String[] roleIds);

    @PostMapping("/role")
    @SystemLog(description = "保存角色数据", opeType = SystemLog.OpeType.EDIT)
    @Operation(summary = "保存角色", description = "保存角色数据")
    ResponseEntity<BaseResult> addOrUpdate(
            @Parameter(in = ParameterIn.DEFAULT, description = "角色", required = false) @RequestBody  RoleRequestParam record);

    @GetMapping("/{id}")
    @Operation(summary = "查询角色", description = "根据id返回角色")
    @SystemLog(description = "查阅指定角色数据", opeType = SystemLog.OpeType.DISPLAY)
    ResponseEntity<BaseResult> getRoleId(String id);

    @GetMapping("/roles/modules")
    @SystemLog(description = "查阅所有角色数据", opeType = SystemLog.OpeType.DISPLAY)
    @Operation(summary = "查询角色", description = "查询所有角色信息")
    ResponseEntity<BaseResult> getAllRoleModules();

    @Data
    @ApiModel(value="RoleRequestParam",description = "请求模块操作，post数据，主要用于requestBody")
    public static class RoleRequestParam {
        private SystemRole role;
        private String[] roleFun;

    }
}
