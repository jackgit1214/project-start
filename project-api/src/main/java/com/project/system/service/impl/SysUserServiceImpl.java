package com.project.system.service.impl;

import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.UserInfo;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.core.mybatis.util.PageResult;
import com.project.core.web.config.ProjectConfig;
import com.project.system.dao.SysDeptMapper;
import com.project.system.dao.SysRoleUserMapper;
import com.project.system.dao.SysUserMapper;
import com.project.system.model.SysUser;
import com.project.system.service.SysDeptService;
import com.project.system.service.SysDeptUserService;
import com.project.system.service.SysRoleUserService;
import com.project.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class SysUserServiceImpl extends AbstractBusinessService<SysUser> implements SysUserService {

    private final SysRoleServiceImpl sysRoleServiceImpl;
    private final SysRoleUserService sysRoleUserServiceImpl;
    private final SysRoleUserMapper sysRoleUserMapper;
    private final SysUserMapper sysUserMapper;
    private final SysDeptUserService sysDeptUserServiceImpl;
    private final SysDeptMapper sysDeptMapper;

    public SysUserServiceImpl(SysRoleServiceImpl sysRoleServiceImpl, SysRoleUserService sysRoleUserServiceImpl,
                              SysUserMapper sysUserMapper, ProjectConfig projectConfig, SysRoleUserMapper sysRoleUserMapper,
                              SysDeptUserService sysDeptUserServiceImpl,SysDeptMapper sysDeptMapper) {
        this.sysRoleServiceImpl = sysRoleServiceImpl;
        this.sysRoleUserServiceImpl = sysRoleUserServiceImpl;
        this.sysUserMapper = sysUserMapper;
        this.sysRoleUserMapper = sysRoleUserMapper;
        this.sysDeptUserServiceImpl = sysDeptUserServiceImpl;
        this.sysDeptMapper = sysDeptMapper;
    }

    @Override
    public BaseDao getDao() {
        return this.sysUserMapper;
    }


    @Override
    public int delete(String recordId) {
        int rows = this.sysUserMapper.deleteByPrimaryKey(recordId);
        log.info("删除用户表数据: {}", rows);
        String[] users = {recordId};
        this.deleteRelatedData(users);
        return rows;
    }

    private int deleteRelatedData(String[] userIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : userIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("userId", id);
        }
        rows = rows + sysRoleUserMapper.deleteByCondition(queryModel);
        log.info("关联关系表数据:{}", rows);
        return rows;
    }

    @Override
    public int delete(String[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("userId", id);
        }
        rows = this.sysUserMapper.deleteByCondition(queryModel);
        log.info("删除用户表数据: {}", rows);
        rows = rows + this.deleteRelatedData(recordIds);
        return rows;
    }

    @Override
    public int save(SysUser record) {
        int rows = 0;
        if (record.getUserId() == null || "".equals(record.getUserId())) {
            String uuid = UUIDUtil.getUUID();
            record.setUserId(uuid);
            rows = this.sysUserMapper.insert(record);
        } else {

            rows = this.sysUserMapper.updateByPrimaryKey(record);
        }
        log.info("更新用户表数据：{}", rows);
        return rows;
    }

    @Override
    public int saveWithBlob(SysUser record) {
        int rows = 0;
        if (record.getUserId() == null || "".equals(record.getUserId())) {
            String uuid = UUIDUtil.getUUID();
            record.setUserId(uuid);
            rows = this.sysUserMapper.insert(record);
        } else {
            //rows = this.sysUserMapper.updateByPrimaryKeyWithBLOBs(record);
            rows = this.sysUserMapper.updateByPrimaryKeySelective(record);
        }
        log.info("更新用户表数据：{}", rows);
        return rows;
    }

    @Override
    public int saveWithBlob(SysUser record, String[] roleIds) {
        int rows = this.saveWithBlob(record);
        rows = rows + this.sysRoleUserServiceImpl.Save(roleIds, record.getUserId());
        return rows;
    }

    @Override
    public int saveWithBlob(SysUser record, String[] roleIds, String[] departments) {
        int rows = this.saveWithBlob(record, roleIds);
        rows = rows + this.sysDeptUserServiceImpl.save(departments, record.getUserId());
        record.setDepartments(this.sysDeptMapper.getDepartmentsByUserId(record.getUserId()));
        return rows;
    }

    @Override
    public List<SysUser> findUserByCondition(QueryModel queryModel, String depId, PageResult page) throws Exception {
        if (depId != null && !"".equals(depId)) {
            if (queryModel.getOredCriteria().size() > 0) {
                queryModel.getOredCriteria().get(0).andIn("userid", String.format(FIND_ALL_DEPARTMENT_BYDEPTID, depId));
            } else
                queryModel.createCriteria().andIn("userid", String.format(FIND_ALL_DEPARTMENT_BYDEPTID, depId));
        }

        List<SysUser> pageData =  sysUserMapper.selectByConditionWithBLOBs(queryModel, page);
        //this.findObjectsByPage(queryModel, page);
        page.setPageDatas(pageData);
        List<SysUser> sysUsers = page.getPageDatas();
        return sysUsers;
    }

    @Override
    public UserInfo findUserByLoginName(String loginName) {
        SysUser user = this.sysUserMapper.selectByLoginCodePlus(loginName);

        return user;
    }

    private static final String FIND_ALL_DEPARTMENT_BYDEPTID = "SELECT userid FROM sys_dept_user where deptid in (\n"
            + "SELECT DATa.deptid FROM( \n"
            + "SELECT \n"
            + "@ids as _ids, \n"
            + "( SELECT @ids := GROUP_CONCAT(deptid) \n"
            + "FROM sys_dept \n"
            + "WHERE FIND_IN_SET(superid, @ids) \n"
            + ") as cids, \n"
            + "@l := @l+1 as level \n"
            + "FROM sys_dept, \n"
            + "(SELECT @ids :='%s', @l := 0 ) b \n"
            + "WHERE @ids IS NOT NULL \n"
            + ") deptid, sys_dept DATA \n"
            + "WHERE FIND_IN_SET(DATA.deptid, _ids) \n"
            + ")";


}