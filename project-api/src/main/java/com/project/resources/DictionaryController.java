package com.project.resources;

import com.project.api.DictionaryApi;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.model.Dictionary;
import com.project.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DictionaryController extends BaseController implements DictionaryApi {

    @Autowired
    private DictionaryService dictionaryServiceImpl;

    @Override
    @QueryModelParam
    public ResponseEntity<BaseResult> data(QueryModel queryModel, Integer page, Integer limit) {
        PageResult<Dictionary> pageResult = new PageResult<Dictionary>(page, limit);
        try {
            queryModel.setOrderByClause("orderby asc");
            this.dictionaryServiceImpl.findObjectsByPage(queryModel, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult.getPageDatas());
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    public ResponseEntity<BaseResult> saveDictionary( Dictionary record) {
        int rows = this.dictionaryServiceImpl.save(record);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);
    }

    @Override
    public ResponseEntity<BaseResult> delete(String[] ids) {
        int rows = this.dictionaryServiceImpl.delete(ids);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);
    }


}