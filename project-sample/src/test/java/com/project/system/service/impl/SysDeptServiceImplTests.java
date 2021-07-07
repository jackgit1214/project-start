package com.project.system.service.impl;

import java.util.List;

import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysDept;
import com.project.system.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ProjectSampleApplication.class)
@Slf4j
class SysDeptServiceImplTests {

    @Autowired
    public SysDeptService sysDeptServiceImpl;

    @Test
    void findObjects() {
        List<SysDept> departments = sysDeptServiceImpl.getDepartmentsBySuperId("1");
        assertNotNull(departments);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        System.out.println(encode);
        encode = encoder.encode("123456");
        System.out.println(encode);
        encode = encoder.encode("123456");
        System.out.println(encode);
        encode = encoder.encode("123456");
        System.out.println(encode);
    }
}