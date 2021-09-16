package com.project.system.service;

import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.IBusinessService;
import com.project.system.model.SysModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysModuleService extends IBusinessService<SysModule> {
    List<SysModule> selectByConditionPlus(@Param("queryModel") QueryModel queryModel);

    int delete(String recordId);

    int delete(String[] recordIds);

    int save(SysModule record);
}