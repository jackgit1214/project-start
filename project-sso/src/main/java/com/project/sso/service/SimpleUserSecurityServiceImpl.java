package com.project.sso.service;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.DataUtils;
import com.project.core.mybatis.model.SysRole;
import com.project.core.mybatis.model.UserInfo;
import com.project.core.mybatis.service.AbstractBusinessService;

import com.project.core.web.config.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lilj
 * @date 2021/03/19
 **/
@Service
@Slf4j
public class SimpleUserSecurityServiceImpl<T extends UserInfo> extends AbstractBusinessService<T> implements IUserSecurityService {

    private static UserInfo user = new UserInfo();

    protected final ProjectConfig projectConfig;

    public SimpleUserSecurityServiceImpl(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    @Override
    public UserInfo findUserByLoginName(String loginName) {

        if (!"admin".equals(loginName)) return null;
        if (projectConfig.getConfigure().isCodeTool()) {
            this.addBuildInFun(user);
        }
        return user;
    }

    @Override
    public int editUserPwd(String userId, String newPwd) {
        return 0;
    }

    public void addBuildInFun(UserInfo user) {
        Set<SysRole> roles = new HashSet<>();
        roles.add(DataUtils.role);
        if (user.getRoles() != null)
            user.getRoles().addAll(roles);
        else
            user.setRoles(roles);
    }

    //这里初始用户名密码
    {
        user.setUserName("admin");
        user.setPassword("$2a$04$obyfauXycW5A0EHouNL6EOj96E6Sncan9/B1rX4R4dYxiEWnq7qwq");
    }

    @Override
    public BaseDao getDao() {
        return null;
    }
}
