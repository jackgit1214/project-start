package com.project.system.web;

import java.util.Arrays;
import java.util.List;

import com.project.core.common.DTreeData;
import com.project.core.common.SysConstant;
import com.project.core.common.response.BaseResult;
import com.project.core.common.response.ReturnCode;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.controller.BaseController;
import com.project.system.model.SysDept;
import com.project.system.service.SysDeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptServiceImpl;

    @RequestMapping("/index")
    public String index(ModelMap map) {
        String path = "system/dept/index";
        return path;
    }

    @RequestMapping("/edit")
    public String edit(String deptId, String superId, ModelMap map) {
        String path = "system/dept/edit";
        SysDept department = null;
        if (deptId != null && !"".equals(deptId)) {
            department = sysDeptServiceImpl.findObjectById(deptId);
        } else {
            SysDept superDept = sysDeptServiceImpl.findObjectById(superId);
            department = new SysDept();
            department.setSuperId(superId);
            department.setSuperDept(superDept);
        }
        map.put("data", department);
        return path;
    }


    @ResponseBody
    @RequestMapping("/save")
    public Object saveDepartment(SysDept department) {
        ModelMap map = new ModelMap();
        int rows = this.sysDeptServiceImpl.save(department);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Object deleteDepartment(String[] deptIds, String superId) {
        ModelMap map = new ModelMap();
        int rows = this.sysDeptServiceImpl.delete(deptIds, superId);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/departments")
    public Object getDepartments(String superId, String page, String limit) {
        ModelMap map = new ModelMap();
        List<SysDept> departments = this.sysDeptServiceImpl.getDepartmentsBySuperId(superId);
        BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), departments);
        map.addAttribute("status", rtnMsg);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getDepartment")
    public Object getDepartment() {

        ModelMap map = new ModelMap();
        List<DTreeData> data = this.sysDeptServiceImpl.getTreeData();

        BaseResult rtnMsg = new BaseResult(200, ReturnCode.SUCCESS.getMessage(), data);
        map.addAttribute("status", rtnMsg);
        map.addAttribute("data", data);
        return map;
    }

}