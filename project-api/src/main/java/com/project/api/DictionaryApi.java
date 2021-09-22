package com.project.api;

import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.response.BaseResult;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(tags = "数据字典")
@RequestMapping("/dictionary")
public interface DictionaryApi {

    @GetMapping("/dictionaries")
    @Operation(summary = "取得字典数据", description = "根据上级节点，取得部门信息")
    ResponseEntity<BaseResult> data(QueryModel queryModel, Integer page, Integer limit);

    @PostMapping("/dictionary")
    @Operation(summary = "字典保存", description = "更新字典信息")
    ResponseEntity<BaseResult> saveDictionary(@RequestBody Dictionary record);

    @DeleteMapping("/dictionaries")
    @Operation(summary = "删除字典")
    ResponseEntity<BaseResult> delete(String[] ids);

}
