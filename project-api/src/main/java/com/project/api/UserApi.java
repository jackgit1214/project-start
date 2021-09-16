package com.project.api;

import com.project.core.common.response.BaseResult;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@Api(tags = "用户管理")
@RequestMapping("/user")
public interface UserApi {


    @Operation(summary = "返回当前登录用户信息", description = "返回当前登录用户信息")
    @GetMapping(value = "/currentUser", produces = {"application/json", "application/xml"})
    ResponseEntity<SysUser> currentUser();

    @Operation(summary = "根据条件查询用户信息", description = "查询条件包含于queryModel中")
    @GetMapping(value = "/users", produces = {"application/json", "application/xml"})
    ResponseEntity<BaseResult> findUsers(
            @Parameter(in = ParameterIn.DEFAULT, description = "map类型参数，如param[username] username为字段名", required = false)  QueryModel queryModel,
            @Parameter(in = ParameterIn.DEFAULT, description = "查询页数", required = true) @RequestParam(value = "page", required = true) int page,
            @Parameter(in = ParameterIn.DEFAULT, description = "每页行数", required = true) @RequestParam(value = "limit", required = true) int limit,
            @Parameter(in = ParameterIn.DEFAULT, description = "部门ID", required = false) @RequestParam(value = "departmentId", required = false) String departmentId);

    @Operation(summary = "根据用户ID返回用户信息", description = "根据用户ID返回用户信息")
    @GetMapping(value = "/{userId}", produces = {"application/json", "application/xml"})
    ResponseEntity<SysUser> findUser(@Parameter(in = ParameterIn.PATH, description = "用户的ID", required = true) @PathVariable("userId") String userId);

    @Operation(summary = "批量删除用户信息", description = "批量删除用户信息")
    @DeleteMapping(value = "/users", produces = {"application/json"})
    ResponseEntity<BaseResult> deleteUsers(String[] userIds);

    @Operation(summary = "更新用户", description = "更新用户信息，根据用户ID.")
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    ResponseEntity<BaseResult> updateUser(
            @Parameter(in = ParameterIn.PATH, description = "需要更新用户的ID", required = true) @PathVariable("userId") String userId,
            @Parameter(in = ParameterIn.DEFAULT, description = "更新的用户对象", required = true)  SysUser user,
            @Parameter(in = ParameterIn.DEFAULT, description = "用户的角色数据", required = false)  String[] userRoles,
            @Parameter(in = ParameterIn.DEFAULT, description = "用户的所属部门", required = false)  String[] departmentIds,
            @Parameter(in = ParameterIn.DEFAULT, description = "用户的图像信息，附件文件", required = false)  @RequestParam("avatarFile") MultipartFile file);

    @Operation(summary = "创建用户", description = "创建用户对象，参数为sysUser")
    @PostMapping(value = "/user")
    ResponseEntity<BaseResult> createUser(
            @Parameter(in = ParameterIn.DEFAULT, description = "更新的用户对象", required = true)  SysUser user,
            @Parameter(in = ParameterIn.DEFAULT, description = "用户的角色数据", required = false)  String[] userRoles,
            @Parameter(in = ParameterIn.DEFAULT, description = "用户的所属部门", required = false)  String[] departmentIds,
            @Parameter(in = ParameterIn.DEFAULT, description = "用户的图像信息，附件文件", required = false)  @RequestParam("avatarFile") MultipartFile file);

    @Operation(summary = "状态更新", description = "根据用户ID及状态信息更新用户部分数据，如激活或禁用用户数据，修改密码等")
    @PutMapping(value = "/{userId}/status", produces = {"application/json", "application/xml"})
    ResponseEntity<SysUser> updateUserStatus(
            @Parameter(in = ParameterIn.PATH, description = "用户的ID", required = true) @PathVariable("userId") String userId,
            @Parameter(in = ParameterIn.DEFAULT, description = "需要更新的状态参数，类型及用户对象", required = true) @RequestBody UserRequestParam param);


    @Operation(summary = "根据list创建多用户对象", description = "")
    @RequestMapping(value = "/user/createWithList", consumes = {"application/json"}, method = RequestMethod.POST)
    ResponseEntity<Void> createUsersWithListInput(@Parameter(in = ParameterIn.DEFAULT, description = "List of user object", required = true) @RequestBody List<SysUser> users);

    @Data
    @ApiModel(value="UserRequestParam",description = "请求用户操作，post数据，主要用于requestBody")
    public static class UserRequestParam {
        @ApiModelProperty("类型，根据类型不同，对用户处理不同操作，3为更改密码")
        private String type;
        private SysUser user;
    }
}
