package com.project.system.web;

import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysLog;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.service.SysLogService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/log")
public class LogController extends BaseController {


    private final SysLogService sysLogServiceImpl;

    public LogController(SysLogService sysLogServiceImpl) {
        this.sysLogServiceImpl = sysLogServiceImpl;
    }

    @RequestMapping("/index")
    public String index(ModelMap map) {
        String path = "system/log/index";
        return path;
    }

    @RequestMapping("/edit")
    public String edit(ModelMap map) {
        String path = "system/log/edit";
        return path;
    }

    @ResponseBody
    @RequestMapping("/logs")
    @QueryModelParam
    public Object getLogs(
            QueryModel queryModel, int page, int limit) {
        ModelMap map = new ModelMap();
        PageResult pageResult = new PageResult<SysLog>(page, limit);

        try {
            queryModel.setOrderByClause(" logtime desc");
            this.sysLogServiceImpl.findObjectsByPage(queryModel, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Object deleteLogs(String[] logs) {
        ModelMap map = new ModelMap();
        int rows = this.sysLogServiceImpl.delete(logs);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @Data
    public static class LogRequestParam {
        private String type;
        private SysLog log;
    }

}
