package com.project.api;

import com.project.core.common.anaotation.DuplicateSubmission;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.Indexitem;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@Api(tags = "数据字典")
@RequestMapping("/index")
public interface IndexApi {

    @GetMapping("data")
    @Operation(summary = "取得指标数据")
    ModelMap data(QueryModel queryModel, Integer page, Integer limit);

    @PostMapping("/save")
    @Operation(summary = "保存指标")
    ModelMap saveIndexitem(Indexitem record);

    @DeleteMapping("/delete")
    @Operation(summary = "删除指标")
    ModelMap delete(@RequestBody Map<String, String>[] ids);
}
