package com.project.system.service;

import com.project.core.mybatis.service.IBusinessService;
import com.project.core.mybatis.service.ILayuiTreeService;
import com.project.system.model.SysDept;

import java.util.List;

public interface SysDeptService extends IBusinessService<SysDept>, ILayuiTreeService {

    public final String DEPT_ROOT_ID = "xxx";

    public List<SysDept> getDepartmentsBySuperId(String superId);

    int delete(String recordId);

    int delete(String[] recordIds);

    int save(SysDept record);

    int delete(String[] deptIds, String superId);
}