package com.project.client.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.client.model.OauthClientDetails;
import com.project.client.service.OauthClientDetailsService;
import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.security.model.SysRole;
import com.project.core.web.controller.BaseController;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.List;

@Controller
@RequestMapping("/client")
@Slf4j
public class ClientController extends BaseController {

    private final OauthClientDetailsService oauthClientDetailsServiceImpl;

    private final ClientDetailsService jdbcClientDetailsService;

    @Autowired
    private DataSource dataSource;

    private JdbcClientDetailsService jdbcClientDetailsService1;

    public ClientController(OauthClientDetailsService oauthClientDetailsServiceImpl, ClientDetailsService clientDetailsService) {
        this.oauthClientDetailsServiceImpl = oauthClientDetailsServiceImpl;
        this.jdbcClientDetailsService = clientDetailsService;

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

        this.jdbcClientDetailsService1 = (JdbcClientDetailsService)new JdbcClientDetailsServiceBuilder().dataSource(dataSource).build();

        ObjectMapper objectMapper = new ObjectMapper();

        List<ClientDetails> clientDetails =  jdbcClientDetailsService1.listClientDetails();

        String jsonResult = objectMapper.writeValueAsString(clientDetails);
        log.info(jsonResult);
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