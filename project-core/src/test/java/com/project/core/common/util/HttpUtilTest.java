package com.project.core.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class HttpUtilTest {

    @Test
    void post() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name","");
        parameters.put("rows","50");
        parameters.put("page","2");
        String result = HttpUtil.post("http://fuwu.rsj.beijing.gov.cn/jf2021integralpublic/settlePerson/tablePage",null,parameters);

        log.info(result);
    }
}