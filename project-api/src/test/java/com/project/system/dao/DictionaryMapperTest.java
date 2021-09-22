package com.project.system.dao;

import com.project.ApiApplication;
import com.project.system.model.Dictionary;
import com.project.system.model.Indexitem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class DictionaryMapperTest {
    @Autowired
    private  DictionaryMapper dictionaryMapper;

    @Test
    public void testGetIndex(){

        List<Dictionary> items = dictionaryMapper.selectByCondition(null);

        Assert.notNull(items,"取得的数据不为空");

    }
}