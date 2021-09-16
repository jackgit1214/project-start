package com.project.resources;

import com.project.api.DepartmentApi;
import com.project.core.common.DTreeData;
import com.project.core.common.anaotation.QueryModelParam;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.web.controller.BaseController;
import com.project.system.model.SysDept;
import com.project.system.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SysDeptController extends AbstractController implements DepartmentApi {

    @Autowired
    private SysDeptService sysDeptServiceImpl;

    public ResponseEntity<BaseResult> saveDepartment(SysDept department) {
        int rows = this.sysDeptServiceImpl.save(department);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), department);
       return ResponseEntity.ok(rtnMsg);
    }

    public ResponseEntity<BaseResult> deleteDepartment(String[] deptIds, String superId) {
        int rows = this.sysDeptServiceImpl.delete(deptIds, superId);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        return ResponseEntity.ok(rtnMsg);

    }
    @Override
    @QueryModelParam
    public ResponseEntity<BaseResult> getDepartments(QueryModel queryModel,String superId, boolean includeSelf) {
        List<SysDept> departments = this.sysDeptServiceImpl.getDepartmentsBySuperId(queryModel,superId,includeSelf);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), departments);
        return ResponseEntity.ok(rtnMsg);
    }

    public ResponseEntity<BaseResult> getDepartment() {

        List<DTreeData> data = this.sysDeptServiceImpl.getTreeData();
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), data);
        return ResponseEntity.ok(rtnMsg);
    }

}