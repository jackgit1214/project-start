package com.project.system.web;

import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.model.Dictionary;
import com.project.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {
    @Autowired
    private DictionaryService dictionaryServiceImpl;

    @RequestMapping("/index")
    public String index() {
        return "system/dictionary/index";
    }

    @RequestMapping("data")
    @ResponseBody
    @QueryModelParam
    public ModelMap data(QueryModel queryModel, Integer page, Integer limit) {
        ModelMap modelMap = new ModelMap();
        PageResult<Dictionary> pageResult = new PageResult<Dictionary>(page,limit);
        try {this.dictionaryServiceImpl.findObjectsByPage(queryModel,pageResult);} catch(Exception e) {e.printStackTrace();}
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @RequestMapping("/edit")
    @DuplicateSubmission(needSaveToken=true)
    public String edit(String id, ModelMap map) {
        Dictionary dictionary = this.dictionaryServiceImpl.findObjectById(id);
        if (dictionary == null)
          dictionary = new Dictionary();
        map.put("data",dictionary);
        return "system/dictionary/edit";
    }

    @ResponseBody
    @RequestMapping("/save")
    @DuplicateSubmission(needRemoveToken=true)
    public ModelMap saveDictionary(Dictionary record) {
        ModelMap modelMap = new ModelMap();
        int rows = this.dictionaryServiceImpl.save(record);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public ModelMap delete(String[] ids) {
        ModelMap modelMap = new ModelMap();
        int rows = this.dictionaryServiceImpl.delete(ids);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        modelMap.addAttribute("status", rtnMsg);
        return modelMap;
    }
}