package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.core.mybatis.model.FunModule;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface SysModuleMapper extends IDataMapperByPage<SysModule>, IDataMapperCRUD<SysModule> {
    List<SysModule> selectByConditionPlus(@Param("queryModel") QueryModel queryModel);

    Set<FunModule> selectModuleByRoleId(@Param("roleId") String roleId);

    Set<SysModule> selectChild(@Param("superModid") String superModid);
}