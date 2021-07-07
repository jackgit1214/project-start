package com.project.system.service.impl;

import com.project.core.security.model.UserInfo;
import com.project.system.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ProjectSampleApplication.class)
@Slf4j
class SysUserServiceImplTests {

    @Autowired
    private SysUserServiceImpl sysUserServiceImpl;

    @Test
    void findUserByLoginName() {
        UserInfo user = this.sysUserServiceImpl.findUserByLoginName("admin");
        log.info(user.getRoles().toString());
    }

    @Test
    void findUserById() {
        UserInfo user = this.sysUserServiceImpl.findObjectById("106347662fef4cdca603384e194307b5");
        log.info(user.getRoles().toString());

        UserInfo user1 = this.sysUserServiceImpl.findObjectById("106347662fef4cdca603384e194307b5");
        log.info(user1.getRoles().toString());
    }

    @Test
    void findUserByLoginId() {
        UserInfo user = this.sysUserServiceImpl.findUserByLoginName("admin");
        log.info(user.getRoles().toString());

        UserInfo user1 = this.sysUserServiceImpl.findUserByLoginName("admin");
        log.info(user1.getRoles().toString());
    }
}