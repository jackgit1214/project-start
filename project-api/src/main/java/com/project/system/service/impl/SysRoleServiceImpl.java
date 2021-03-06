package com.project.system.service.impl;

import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.FunModule;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.SysModuleMapper;
import com.project.system.dao.SysRoleFuncMapper;
import com.project.system.dao.SysRoleMapper;
import com.project.system.dao.SysRoleUserMapper;
import com.project.system.model.SysModule;
import com.project.system.model.SysRoleFunc;
import com.project.system.model.SystemRole;
import com.project.system.service.SysRoleFuncService;
import com.project.system.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SysRoleServiceImpl extends AbstractBusinessService<SystemRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleFuncService sysRoleFuncServiceImpl;
    private final SysRoleFuncMapper sysRoleFuncMapper;
    private final SysRoleUserMapper sysRoleUserMapper;
    private final SysModuleMapper sysModuleMapper;

    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper, SysRoleFuncService sysRoleFuncServiceImpl, SysRoleFuncMapper sysRoleFuncMapper, SysRoleUserMapper sysRoleUserMapper, SysModuleMapper sysModuleMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysRoleFuncServiceImpl = sysRoleFuncServiceImpl;
        this.sysRoleFuncMapper = sysRoleFuncMapper;
        this.sysRoleUserMapper = sysRoleUserMapper;
        this.sysModuleMapper = sysModuleMapper;
    }


    public BaseDao getDao() {
        return this.sysRoleMapper;
    }

    @Override
    public SystemRole getRolePlusById(String id) {
        SystemRole role = sysRoleMapper.selectByPrimaryKeyPlus(id);
        return role;
    }


    @Override
    public Map<String, List<SysModule>> getRoleModules() {
        List<SysRoleFunc> roleFuncs = this.sysRoleFuncServiceImpl.findAllObjects();
        List<SysModule> allModules = this.sysModuleMapper.selectByCondition(null);
        Map<String, List<SysModule>> modules = new HashMap<>();

        roleFuncs.forEach(roleFunc -> {
            String funId = roleFunc.getFuncid();
            String roleId = roleFunc.getRoleid();
            List<SysModule> tmpModules = null;
            if (!modules.containsKey(roleId)) {
                tmpModules = new ArrayList<>();
                modules.put(roleId, tmpModules);
            } else
                tmpModules = modules.get(roleId);
            List<SysModule> tmpMod = allModules.stream().filter(item -> item.getFunId().equals(funId))
                    .collect(Collectors.toList());
            tmpModules.addAll(tmpMod);
        });

        return modules;
    }

    @Override
    public int delete(String recordId) {
        int rows = this.sysRoleMapper.deleteByPrimaryKey(recordId);
        log.info("????????????????????????: {}", rows);
        String[] roles = {recordId};
        this.deleteRelatedData(roles);
        return rows;
    }

    @Override
    public int delete(String[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("roleId", id);
        }
        rows = this.sysRoleMapper.deleteByCondition(queryModel);
        log.info("?????????????????????: {}", rows);
        this.deleteRelatedData(recordIds);
        return rows;
    }

    private int deleteRelatedData(String[] roleIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : roleIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("roleId", id);
        }
        rows = sysRoleFuncMapper.deleteByCondition(queryModel);
        rows = rows + sysRoleUserMapper.deleteByCondition(queryModel);
        log.info("???????????????????????????:{}", rows);
        return rows;
    }

    @Override
    public int save(SystemRole record) {
        int rows = 0;
        if (record.getRoleId() == null || "".equals(record.getRoleId())) {
            String uuid = UUIDUtil.getUUID();
            record.setRoleId(uuid);
            rows = this.sysRoleMapper.insert(record);
        } else {
            rows = this.sysRoleMapper.updateByPrimaryKey(record);
        }
        log.info("????????????????????????{}", rows);
        return rows;
    }

    @Override
    public int save(SystemRole record, String[] roleFun) {
        int rows = this.save(record);
        if (roleFun != null){
            this.updateRoleFun(record.getRoleId(), roleFun);
            Set<FunModule> modules = this.sysModuleMapper.selectModuleByRoleId(record.getRoleId());
            record.setModules(modules);
        }

        return rows;
    }

    private int updateRoleFun(String roleId, String[] funIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("roleId", roleId);
        //?????????????????????????????????
        this.sysRoleFuncMapper.deleteByCondition(queryModel);
        //?????????????????????ID
        for (String funId : funIds) {
            SysRoleFunc roleFunc = new SysRoleFunc();
            roleFunc.setFuncid(funId);
            roleFunc.setRoleid(roleId);
            rows = rows + this.sysRoleFuncMapper.insert(roleFunc);
        }
        log.info("??????????????????????????????{}", rows);
        return rows;
    }

    public int updateRoleFun(String roleId, String funId) {
        int rows = 0;
        SysRoleFunc roleFunc = new SysRoleFunc();
        roleFunc.setFuncid(funId);
        roleFunc.setRoleid(roleId);
        rows = rows + this.sysRoleFuncMapper.insert(roleFunc);
        log.info("??????????????????????????????{}?????????ID???{},??????ID???{}", rows, roleId, funId);
        return rows;
    }
}