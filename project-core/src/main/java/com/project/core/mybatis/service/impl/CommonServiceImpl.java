package com.project.core.mybatis.service.impl;

import org.springframework.stereotype.Service;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.core.mybatis.service.IBusinessService;
import com.project.core.mybatis.service.ICommonService;

import org.springframework.transaction.annotation.Transactional;

//@Service
//@Transactional
public class CommonServiceImpl<T> extends AbstractBusinessService<T> implements
        IBusinessService<T>, ICommonService<T> {

    private BaseDao baseDao;

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public BaseDao getDao() {
        // TODO Auto-generated method stub
        return this.baseDao;
    }

    @Override
    public int saveObject(T object) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(String[] ids) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(String id) {
        // TODO Auto-generated method stub
        return 0;
    }

}
