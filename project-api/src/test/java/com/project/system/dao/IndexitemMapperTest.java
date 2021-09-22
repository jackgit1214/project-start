package com.project.system.dao;

import com.project.ApiApplication;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.Indexitem;
import com.project.system.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class IndexitemMapperTest {

    @Autowired
    private  IndexitemMapper indexitemMapper;

    @Test
    public void testGetIndex(){

        List<Indexitem> items = indexitemMapper.selectByCondition(null);

        Assert.notNull(items,"取得的数据不为空");

    }

}