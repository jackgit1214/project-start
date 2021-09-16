package com.project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Api(tags = "统计数据")
@RequestMapping("/stats")
public interface StatsApi {

    @Operation(summary = "数据量", description = "返回数据库表的数据量")
    @GetMapping(value = "/tableInfo", produces = {"application/json", "application/xml"})
    ResponseEntity<Map<String,StatsInfo>> tableCount();

    @Operation(summary = "系统日志", description = "返回最近使用的系统日志")
    @GetMapping(value = "/log", produces = {"application/json", "application/xml"})
    ResponseEntity<Object> systemLog();

    @Data
    @ApiModel(value="StatsInfo",description = "统计信息model")
    public static class StatsInfo {
        @ApiModelProperty("表名")
        private String tableName;
        @ApiModelProperty("中文名")
        private String tableChName;
        @ApiModelProperty("行数")
        private Integer count;
        public StatsInfo(String tableName, String tableChName, Integer count) {
            this.tableName = tableName;
            this.tableChName = tableChName;
            this.count = count;
        }

        public static StatsInfo builder(String tableName, String tableChName, Integer count){
            return new StatsInfo(tableName,tableChName,count);
        }
    }



}
