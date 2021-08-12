package com.project.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.FunModule;
import com.project.core.mybatis.model.QueryModel;

import com.project.sso.service.SimpleModuleSecurityServiceImpl;
import com.project.system.dao.SysModuleMapper;
import com.project.system.model.SysModule;
import com.project.system.service.SysModuleService;
import com.project.system.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class SysModuleServiceImpl extends SimpleModuleSecurityServiceImpl<SysModule> implements SysModuleService {

    private final SysModuleMapper sysModuleMapper;
    private final SysRoleService sysRoleServiceImpl;
    private static final String MODULE_SEQ_NAME = "module_seq";

    public SysModuleServiceImpl(SysModuleMapper sysModuleMapper, SysRoleService sysRoleServiceImpl) {
        this.sysModuleMapper = sysModuleMapper;
        this.sysRoleServiceImpl = sysRoleServiceImpl;
    }

    /**
     * 从数据库取出所有系统模块，需要时加上内置模块
     *
     * @return java.util.List<com.project.core.security.model.FunModule>
     * @author lilj
     * @date 2021/27/19 15:27
     */
    @Override
    public List<FunModule> getSysModules() {

        List<FunModule> modules = new ArrayList<>();
        //从数据库取出代码生成模块
        modules.addAll(this.selectByConditionPlus(null));
        List<FunModule> codeModule = super.getSysModules();
        if (codeModule != null)
            modules.addAll(codeModule);
        return modules;
    }

    @Override
    public List<SysModule> selectByConditionPlus(QueryModel queryModel) {

        List<SysModule> sysModules = this.sysModuleMapper.selectByConditionPlus(queryModel);
        return sysModules;
    }

    private int deleteRelatedData(String[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("funcid", id);
        }
        rows = rows + sysModuleMapper.deleteByCondition(queryModel);
        log.info("关联关系表数据:{}", rows);
        return rows;
    }

    @Override
    public int delete(String recordId) {
        int rows = this.sysModuleMapper.deleteByPrimaryKey(recordId);
        log.info("删除关联表数据: {}", rows);
        String[] records = {recordId};
        this.deleteRelatedData(records);
        return rows;
    }

    @Override
    public int delete(String[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("funcid", id);
        }
        rows = this.sysModuleMapper.deleteByCondition(queryModel);
        log.info("删除模块表数据: {}", rows);
        rows = rows + this.deleteRelatedData(recordIds);
        return rows;
    }

    @Override
    public int save(SysModule record) {
        int rows = 0;
        if (record.getFunId() == null || "".equals(record.getFunId())) {
            int idValue = this.getNextVal(MODULE_SEQ_NAME);
            record.setFunId(String.format("%04d", idValue));
            rows = this.sysModuleMapper.insert(record);
            sysRoleServiceImpl.updateRoleFun(SysRoleService.ADMINISTRATOR_ROLE_ID, record.getFunId());
        } else {
            rows = this.sysModuleMapper.updateByPrimaryKey(record);
        }
        log.info("更新模块表数据：{}", rows);
        return rows;
    }

    public BaseDao getDao() {
        return this.sysModuleMapper;
    }

}