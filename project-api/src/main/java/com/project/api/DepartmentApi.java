package com.project.api;

import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysDept;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(tags = "部门管理")
@RequestMapping("/department")
public interface DepartmentApi {

    @PostMapping("/department")
    @Operation(summary = "更新部门", description = "更新部门信息，参数为部门对象")
    ResponseEntity<BaseResult> saveDepartment(@RequestBody SysDept department);

    @DeleteMapping("/department/delete")
    @Operation(summary = "删除部门", description = "根据部门ID，删除部门信息，并更新父节点属性，是否末节点")
    ResponseEntity<BaseResult> deleteDepartment(String[] deptIds, String superId);

    @GetMapping("/departments")
    @Operation(summary = "取得部门信息", description = "根据上级节点，取得部门信息")
    ResponseEntity<BaseResult> getDepartments(
            @Parameter(in = ParameterIn.DEFAULT, description = "map类型参数，如param[username] username为字段名", required = false)  QueryModel queryModel,
            @Parameter(in = ParameterIn.DEFAULT, description = "上级部门ID", required = false) @RequestParam(value = "superId", required = false,defaultValue = "xxx") String superId,
            @Parameter(in = ParameterIn.DEFAULT, description = "是否包含当前部门", required = false) @RequestParam(value = "includeSelf", required = false,defaultValue = "false")  boolean includeSelf);

    @GetMapping("/departments/tree")
    @Operation(summary = "取得部门树结构信息", description = "取得部门树结构信息")
    ResponseEntity<BaseResult> getDepartment();


}
