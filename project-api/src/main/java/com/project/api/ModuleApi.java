package com.project.api;

import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysModule;
import com.project.resources.ModuleController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "模块管理")
@RequestMapping("/module")
public interface ModuleApi {


    String UUID_TOKEN = "module_token";

    @Operation(summary = "模块数据", description = "取得所有模块数据，")
    @GetMapping(value = "/modules",produces = {"application/json", "application/xml"})
    @QueryModelParam
    ResponseEntity<BaseResult> findModules(
            @Parameter(in = ParameterIn.DEFAULT, description = "map类型参数", required = false) QueryModel queryModel);

    @Operation(summary = "用户模块数据", description = "根据当前登录用户，取得用户有权限的模块数据，")
    @GetMapping(value = "/modules/{userId}",
            produces = {"application/json", "application/xml"})
    ResponseEntity<BaseResult> findModulesByUser(
            @Parameter(in = ParameterIn.PATH, description = "用户的ID", required = true) @PathVariable("userId") String userId);


    @DeleteMapping("/modules")
    @SystemLog(moduleId = "模块管理", description = "删除模块数据", opeType = SystemLog.OpeType.DEL)
    @Operation(summary = "删除用户模块数据", description = "模块维护管理，删除指定的模块数据")
    ResponseEntity<BaseResult> deleteModule(
            @Parameter(in = ParameterIn.DEFAULT, description = "删除的模块id数组", required = true )String[] moduleIds);

    @PostMapping("/modules")
    @SystemLog(moduleId = "模块管理", description = "保存模块数据", opeType = SystemLog.OpeType.EDIT)
    @DuplicateSubmission(needRemoveToken = true, tokenName = UUID_TOKEN)
    @Operation(summary = "保存或更新用户模块数据", description = "模块维护管理，保存或更新用户模块数据")
    ResponseEntity<BaseResult> addOrUpdate(
            @Parameter(in = ParameterIn.DEFAULT, description = "要更改的模块", required = true ) SysModule record);

    @PostMapping("/status")
    @SystemLog(moduleId = "模块管理", description = "修改模块数据状态", opeType = SystemLog.OpeType.EDIT)
    @Operation(summary = "更新用户模块状态", description = "模块维护管理，更新用户模块状态")
    ResponseEntity<BaseResult> handleSimpleOperation(
            @Parameter(in = ParameterIn.DEFAULT, description = "要更改的模块", required = true) @RequestBody ModuleController.ModuleRequestParam param);

    @Data
    @ApiModel(value="ModuleRequestParam",description = "请求模块操作，post数据，主要用于requestBody")
    public static class ModuleRequestParam {
        private String type;
        private SysModule module;
    }

}
