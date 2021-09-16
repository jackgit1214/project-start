package com.project.system.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysRole;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.model.OauthClientDetails;
import com.project.system.service.OauthClientDetailsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

@Controller
@RequestMapping("/client")
@Slf4j
public class ClientController extends BaseController {

    private final OauthClientDetailsService oauthClientDetailsServiceImpl;


    @Autowired
    private DataSource dataSource;

    public ClientController(OauthClientDetailsService oauthClientDetailsServiceImpl) {
        this.oauthClientDetailsServiceImpl = oauthClientDetailsServiceImpl;
    }

    @RequestMapping("/index")
    public String index() {
        return "client/index";
    }

    @RequestMapping("/clients")
    @QueryModelParam
    @ResponseBody
    @SneakyThrows
    public Object dataList(QueryModel queryModel, int page, int limit) {

        ObjectMapper objectMapper = new ObjectMapper();
        ModelMap map = new ModelMap();

        PageResult pageResult = new PageResult<SysRole>(page, limit);
        try {
            this.oauthClientDetailsServiceImpl.findObjectsByPage(queryModel, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @RequestMapping("/edit")
    @DuplicateSubmission(needSaveToken = true)
    public String showEdit(String id,ModelMap map) {
        OauthClientDetails oauthClientDetails = this.oauthClientDetailsServiceImpl.findObjectById(id);
        if (oauthClientDetails == null)
            oauthClientDetails = new OauthClientDetails();
        map.put("data", oauthClientDetails);
        return "client/edit";
    }

    @ResponseBody
    @RequestMapping("/save")
    @DuplicateSubmission(needRemoveToken = true)
    public ModelMap addOrUpdate(OauthClientDetails record) {
        ModelMap modelMap = new ModelMap();
        int rows = this.oauthClientDetailsServiceImpl.save(record);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        modelMap.addAttribute("data", record);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public ModelMap delete( String[] ids) {
        ModelMap modelMap = new ModelMap();
        int rows = this.oauthClientDetailsServiceImpl.delete(ids);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }
}