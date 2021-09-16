package com.project.api;

import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysLog;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Api(tags = "日志管理",value = "log",description = "日志管理接口")
@RequestMapping("/log")
public interface LogApi {

    @Operation(summary = "删除日志接口", description = "", security = {
            @SecurityRequirement(name = "日志删除授权", scopes = {
                    "read:Logs",
                    "write:Logs"        })    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "无效的日志ID！"),
            @ApiResponse(responseCode = "404", description = "找不到相应日志信息！") })
    @RequestMapping(value = "/{logId}",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteLog(@Parameter(in = ParameterIn.PATH, description = "需要删除的日志ID。", required=true) @PathVariable("logId") String logId,
                                   @Parameter(in = ParameterIn.HEADER, description = "请求Api授权key。" ) @RequestHeader(value="api_key", required=false) String apiKey);


    @Operation(summary = "批量删除日志接口", description = "", security = {
            @SecurityRequirement(name = "日志删除授权", scopes = {
                    "read:pets",
                    "write:pets"        })    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "无效的日志ID！"),
            @ApiResponse(responseCode = "404", description = "找不到相应日志信息！") })
    @RequestMapping(value = "/logs",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteLogs(@Parameter(in = ParameterIn.DEFAULT, description = "根据日志ID批量删除日志信息。", required=true) @PathVariable("logId") String[] logIds,
                                    @Parameter(in = ParameterIn.HEADER, description = "请求Api授权key。" ) @RequestHeader(value="api_key", required=false) String apiKey);


    @Operation(summary = "根据条件查询日志信息", description = "查询条件包含于queryModel中", security = {
            @SecurityRequirement(name = "日志查询授权", scopes = {
                    "read:logs"})    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "操作成功", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SysLog.class)))),
            @ApiResponse(responseCode = "400", description = "查询条件出错") })
    @RequestMapping(value = "/logs",
            produces = { "application/json", "application/xml" },
            method = RequestMethod.GET)
    ResponseEntity<List<SysLog>> findLogs(@Parameter(in = ParameterIn.DEFAULT, description = "map类型参数" ,required=false) @RequestParam(value = "queryModel", required = false) QueryModel queryModel,
                                          @Parameter(in = ParameterIn.DEFAULT, description = "查询页数" ,required=true) @RequestParam(value = "page", required = true) int page,
                                          @Parameter(in = ParameterIn.DEFAULT, description = "每页行数" ,required=true) @RequestParam(value = "limit", required = true) int limit);

    @Operation(summary = "根据ID返回日志信息", description = "日志ID", security = {
            @SecurityRequirement(name = "日志查询授权", scopes = {
                    "read:logs"})    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "操作成功", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SysLog.class)))),
            @ApiResponse(responseCode = "400", description = "无效的日志ID") })
    @RequestMapping(value = "/{logId}",
            produces = { "application/json", "application/xml" },
            method = RequestMethod.GET)
    ResponseEntity<List<SysLog>> findLog(@Parameter(in = ParameterIn.PATH, description = "根据日志ID删除日志信息。", required=true) @PathVariable("logId") String logId, @Parameter(in = ParameterIn.HEADER, description = "" ) @RequestHeader(value="api_key", required=false) String apiKey);

}
