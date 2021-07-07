package com.project.system.service;

import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.IBusinessService;
import com.project.core.mybatis.util.PageResult;
import com.project.system.model.SysUser;

import java.util.List;

public interface SysUserService extends IBusinessService<SysUser> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(SysUser record);

    int saveWithBlob(SysUser record);

    int saveWithBlob(SysUser record, String[] roleIds);

    int saveWithBlob(SysUser record, String[] roleIds, String[] departments);

    List<SysUser> findUserByCondition(QueryModel queryModel, String deptId, PageResult pageResult) throws Exception;

}