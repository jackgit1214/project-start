package com.project.system.service;

import com.project.core.mybatis.service.IBusinessService;
import com.project.system.model.SysRoleUser;

public interface SysRoleUserService extends IBusinessService<SysRoleUser> {

    public int Save(String[] roleIds, String userId);

}