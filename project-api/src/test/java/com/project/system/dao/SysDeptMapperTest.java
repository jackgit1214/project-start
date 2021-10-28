package com.project.system.dao;

import com.project.ApiApplication;
import com.project.system.model.SysDept;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class SysDeptMapperTest {

    @Autowired
    public SysDeptMapper sysDeptMapper;

    @Test
    public void testGetDeptdata(){
        List<SysDept> depts =  sysDeptMapper.selectByCondition(null);

        Assert.notNull(depts,"部门数据不空！");
    }
}