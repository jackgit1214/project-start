package com.project.system.service;

import com.project.core.mybatis.service.IBusinessService;
import com.project.system.model.SysDeptUser;

public interface SysDeptUserService extends IBusinessService<SysDeptUser> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(SysDeptUser record);

    int save(String[] departments, String userId);
}