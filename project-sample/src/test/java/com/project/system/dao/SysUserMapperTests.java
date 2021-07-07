package com.project.system.dao;

import java.util.List;

import com.project.system.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ProjectSampleApplication.class)
@Slf4j
public class SysUserMapperTests {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void testSelectByLoginCodePlus() {
        SysUser user = sysUserMapper.selectByLoginCodePlus("admin");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());

        assertNotNull(user.getRoles());
        log.info(user.getRoles().toString());
    }

    @Test
    public void testLazyLoad() {
        List<SysUser> users = sysUserMapper.testLazyLoad("test%");
        assertNotNull(users);
        List<SysUser> users1 = sysUserMapper.testLazyLoad("test%");
        assertNotNull(users1);

    }
}