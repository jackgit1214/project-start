package com.project.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.project.core.mybatis.service.IBusinessService;
import com.project.system.model.SysModule;
import com.project.system.model.SysUser;
import com.project.system.model.SystemRole;

public interface SysRoleService extends IBusinessService<SystemRole> {

    //系统管理员角色ID，授权唯一，不能删除。
    final static String ADMINISTRATOR_ROLE_ID = "6b912123f72a465782c91f0f4d330dec";

    SystemRole getRolePlusById(String id);

    Map<String, List<SysModule>> getRoleModules();

    int delete(String recordId);

    int delete(String[] recordIds);

    int save(SystemRole record);

    int save(SystemRole record, String[] roleFun);

    int updateRoleFun(String roleId, String funId);
}