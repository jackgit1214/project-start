package com.project.system.service.impl;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.SysRoleUserMapper;
import com.project.system.model.SysRoleUser;
import com.project.system.service.SysRoleUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysRoleUserServiceImpl extends AbstractBusinessService<SysRoleUser> implements SysRoleUserService {

    private final SysRoleUserMapper sysRoleUserMapper;

    public SysRoleUserServiceImpl(SysRoleUserMapper sysRoleUserMapper) {
        this.sysRoleUserMapper = sysRoleUserMapper;
    }

    public BaseDao getDao() {
        return this.sysRoleUserMapper;
    }

    @Override
    public int Save(String[] roleIds, String userId) {

        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("userid", userId);
        int rows = 0;
        //删除原有授权数据
        this.sysRoleUserMapper.deleteByCondition(queryModel);
        if (roleIds != null) {
            for (String roleId : roleIds) {
                SysRoleUser roleUser = new SysRoleUser();
                roleUser.setRoleid(roleId);
                roleUser.setUserid(userId);
                rows = rows + this.sysRoleUserMapper.insert(roleUser);
            }
        }

        return rows;
    }

}