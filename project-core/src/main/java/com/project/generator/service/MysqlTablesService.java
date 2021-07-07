package com.project.generator.service;

import com.project.core.mybatis.service.IBusinessService;
import com.project.generator.model.MysqlTables;

import java.sql.SQLException;
import java.util.List;

public interface MysqlTablesService extends IBusinessService<MysqlTables> {

    public List<MysqlTables> getDatabaseTables(String database) throws Exception;

}
