package com.project.core.mybatis.dao.Base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.project.ProjectCoreApplication.class)
@Slf4j
public class CommSeqMapperTests {

    private static final String KEY_NAME = "seqName";
    private static final String VALUE_NAME = "seqValue";

    @Autowired
    private CommSeqMapper commSeqMapper;

    @Test
    public void testfun() {
        Map<String, Integer> map = commSeqMapper.getCurrVal("module_seq111");

        log.info(map.get("value").toString());


    }

}