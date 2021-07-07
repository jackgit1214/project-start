package com.project.generator.service.impl;

import com.project.core.common.SysConstant;
import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.core.mybatis.util.PageResult;
import com.project.generator.dao.MysqlTablesMapper;
import com.project.generator.model.MysqlTables;
import com.project.generator.service.MysqlTablesService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MysqlTablesServiceImpl extends AbstractBusinessService<MysqlTables> implements MysqlTablesService {

    private final MysqlTablesMapper mysqlTablesMapper;

    private final DataSource dataSource;

    public MysqlTablesServiceImpl(MysqlTablesMapper mysqlTablesMapper, DataSource dataSource) {
        this.mysqlTablesMapper = mysqlTablesMapper;
        this.dataSource = dataSource;
    }

    public BaseDao getDao() {
        return this.mysqlTablesMapper;
    }

    @Override
    public List<MysqlTables> getDatabaseTables(String database) throws Exception {
        if (database == null || "".equals(database)) {
            Connection conn = dataSource.getConnection();
            database = conn.getCatalog().toString();
            conn.close();
        }
        QueryModel queryModel = new QueryModel();
        PageResult<MysqlTables> page = new PageResult<MysqlTables>(1, 1000);
        queryModel.createCriteria().andEqualTo("TABLE_SCHEMA", database);

        this.findObjectsByPage(queryModel, page);

        return page.getPageDatas();
    }

}