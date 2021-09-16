package com.project.system.dao;

import com.project.ApiApplication;
import com.project.core.mybatis.model.QueryModel;
import com.project.system.model.SysUser;
import com.project.system.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiApplication.class)
class SysUserMapperTest {


    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void testGetUser(){
        SysUser user = sysUserMapper.selectByLoginCodePlus("admin");

        Assert.notNull(user,"取得的用户不为空");

        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("logincode","admin");
        List<SysUser> users = sysUserService.findObjects(queryModel);

        Assert.notNull(users,"取得的用户不为空");

    }
}