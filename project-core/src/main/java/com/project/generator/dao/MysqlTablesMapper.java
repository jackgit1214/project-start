package com.project.generator.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.generator.model.MysqlTables;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MysqlTablesMapper extends IDataMapperByPage<MysqlTables>, IDataMapperCRUD<MysqlTables> {

}