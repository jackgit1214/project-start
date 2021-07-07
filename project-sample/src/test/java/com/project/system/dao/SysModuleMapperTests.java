package com.project.system.dao;

import com.project.system.model.SysModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = com.ProjectSampleApplication.class)
@Slf4j
class SysModuleMapperTests {

    @Autowired
    public SysModuleMapper sysModuleMapper;

    @Test
    public void testSelectById() {
        SysModule module = sysModuleMapper.selectByPrimaryKey("400");

        Assert.notNull(module.getParentModule(), "上级模块不为空");

    }

    @Test
    void selectByConditionPlus() {
    }
}