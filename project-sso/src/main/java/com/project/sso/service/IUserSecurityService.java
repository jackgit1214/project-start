package com.project.sso.service;

import com.project.core.mybatis.model.UserInfo;

public interface IUserSecurityService {

    /**
     * 根据登录名，取得用户信息
     * 用户信息包含
     *
     * @param loginName
     * @return com.project.core.security.model.UserInfo
     * @author lilj
     * @date 2021/48/19 15:48
     */
    UserInfo findUserByLoginName(String loginName);

    /**
     * 修改密码
     *
     * @return
     * @author lilj
     * @date 2021/5/28 17:31
     */

    int editUserPwd(String userId, String newPwd);
}
