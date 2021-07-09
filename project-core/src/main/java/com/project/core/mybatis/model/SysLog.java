package com.project.core.mybatis.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysLog {

    private String logId;
    private Date logTime;
    private String userId;
    private String userName;
    private String requestIp;
    private String requestUrl;
    private String requestClassMethod;
    private Integer executeTime;
    private String modelName;
    private String opeType;
    private String description;
    private Integer logType;
    private String reqParam;
    private String errorCode;
    private String errDescription;


    public String getOperationType(){
//        1 修改 2 删除 3 数据展示 4 登录 5 退出 6其它
        switch (this.opeType){
            case "1":
                return "修改";
            case "2":
                return "删除";
            case "3":
                return "数据展示";
            case "4":
                return "登录";
            case "5":
                return "退出";
            default:
                return "其它";
        }
    }
}