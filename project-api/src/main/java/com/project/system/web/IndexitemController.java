package com.project.system.web;

import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.model.Indexitem;
import com.project.system.service.IndexitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/indexitem")
public class IndexitemController extends BaseController {
    @Autowired
    private IndexitemService indexitemServiceImpl;

    @RequestMapping("/index")
    public String index() {
        return "system/indexitem/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @QueryModelParam
    public ModelMap data(QueryModel queryModel, Integer page, Integer limit) {
        ModelMap modelMap = new ModelMap();
        PageResult<Indexitem> pageResult = new PageResult<Indexitem>(page,limit);
        try {this.indexitemServiceImpl.findObjectsByPage(queryModel,pageResult);} catch(Exception e) {e.printStackTrace();}
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @RequestMapping("/edit")
    @DuplicateSubmission(needSaveToken=true)
    public String edit(String id, ModelMap map) {
        Indexitem indexitem = this.indexitemServiceImpl.findObjectById(id);
        if (indexitem == null)
          indexitem = new Indexitem();
        map.put("data",indexitem);
        return "system/indexitem/edit";
    }

    @ResponseBody
    @RequestMapping("/save")
    @DuplicateSubmission(needRemoveToken=true)
    public ModelMap saveIndexitem(Indexitem record) {
        ModelMap modelMap = new ModelMap();
        int rows = this.indexitemServiceImpl.save(record);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public ModelMap delete(@RequestBody Map<String, String>[] ids) {
        ModelMap modelMap = new ModelMap();
        int rows = this.indexitemServiceImpl.delete(ids);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }
}