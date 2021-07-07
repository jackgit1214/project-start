package com.project.system.dao;

import java.util.List;

import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysRoleFunc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ProjectSampleApplication.class)
@Slf4j
class SysRoleFuncMapperTests {

    @Autowired
    private SysRoleFuncMapper sysRoleFuncMapper;


    @Test
    public void testFuncMapper() {
        QueryModel queryModel = new QueryModel();
        queryModel.setDistinct(true);
        List<SysRoleFunc> roleFunc = sysRoleFuncMapper.selectByCondition(null);
        log.info(roleFunc.toString());
    }

}