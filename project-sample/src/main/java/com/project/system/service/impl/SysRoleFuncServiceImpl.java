package com.project.system.service.impl;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.SysRoleFuncMapper;
import com.project.system.model.SysRoleFunc;
import com.project.system.service.SysRoleFuncService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysRoleFuncServiceImpl extends AbstractBusinessService<SysRoleFunc> implements SysRoleFuncService {
    @Autowired
    private SysRoleFuncMapper sysRoleFuncMapper;

    public BaseDao getDao() {
        return this.sysRoleFuncMapper;
    }
}