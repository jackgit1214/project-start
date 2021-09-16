package com.project.system.service.impl;

import com.project.ApiApplication;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SystemRole;
import com.project.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class SysRoleServiceImplTest {


    @Autowired
    public SysRoleService sysRoleServiceImpl;

    @Test
    public void testGetRole(){

        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("roleName","系统管理员");
        List<SystemRole> roles = this.sysRoleServiceImpl.findObjects(queryModel);

        System.out.println(roles.size());
    }

    @Test
    public void testRole(){

//        SystemRole role =  this.sysRoleServiceImpl.getRolePlusById("6b912123f72a465782c91f0f4d330dec");
//        System.out.println(role);

        SystemRole role1 =  this.sysRoleServiceImpl.findObjectById("6b912123f72a465782c91f0f4d330dec");
        System.out.println(role1);
    }
}