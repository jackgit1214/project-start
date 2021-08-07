package com.project.system;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.myplugins.CustomPagePlugin;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UtilTest {

    @Test
    public void testFreemarkerUtilPath(){

        String path = CustomPagePlugin.class.getClassLoader().getResource("bin").toString();
        log.info(path);
        String path1 = CustomPagePlugin.class.getClassLoader().getResource("").toString();
        log.info(path1);
    }
}
