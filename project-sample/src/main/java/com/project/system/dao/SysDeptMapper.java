package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.system.model.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysDeptMapper extends IDataMapperByPage<SysDept>, IDataMapperCRUD<SysDept> {
}