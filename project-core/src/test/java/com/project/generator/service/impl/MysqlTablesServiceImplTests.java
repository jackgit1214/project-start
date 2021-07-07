package com.project.generator.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.project.core.mybatis.util.PageResult;
import com.project.generator.model.MysqlTables;
import com.project.generator.service.MysqlTablesService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.project.ProjectCoreApplication.class)
@Slf4j
public class MysqlTablesServiceImplTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    private MysqlTablesService mysqlTablesServiceImpl;

    @Test
    void findAllObjects() throws Exception {
        log.info(this.getClass().getResource("/").toString());
        log.info(dataSource.toString());
        Connection con = dataSource.getConnection();
        log.info(con.getCatalog().toString());
        DatabaseMetaData dmt = con.getMetaData();

        log.info(dmt.getCatalogs().toString());

        log.info("-------------------------------------");
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bin/generatorConfig.xml");
        Configuration config = cp.parseConfiguration(inputStream);

        log.info("-------------------------------------");
    }

    @Test
    void findObjectById() {
    }

    @Test
    void findObjects() {
    }

    @Test
    void singleObject() {
    }

    @Test
    void findObjectsByPage() {
        PageResult<MysqlTables> page = new PageResult<MysqlTables>(1, 15);
        try {
            this.mysqlTablesServiceImpl.findObjectsByPage(null, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(page.toString());
    }

    @Test
    void testFindObjectsByPage() {
    }
}