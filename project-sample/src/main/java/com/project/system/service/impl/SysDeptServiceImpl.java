package com.project.system.service.impl;

import com.project.core.common.DTreeData;
import com.project.core.common.anaotation.SystemLog;
import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.SysDeptMapper;
import com.project.system.model.SysDept;
import com.project.system.service.SysDeptService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysDeptServiceImpl extends AbstractBusinessService<SysDept> implements SysDeptService {

//    @Autowired
//    private DataSourceTransactionManager dataSourceTransactionManager;
//
//    @Autowired
//    private TransactionDefinition transactionDefinition;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public BaseDao getDao() {
        return this.sysDeptMapper;
    }

    public int delete(String recordId) {
        int rows = this.sysDeptMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}", rows);
        return rows;
    }

    public int delete(String[] recordIds) {

//        TransactionStatus transactionStatus= TransactionAspectSupport.currentTransactionStatus();
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds) {
            QueryModel.Criteria criteria = queryModel.or();
            criteria.andEqualTo("DEPTID", id);
        }
        rows = this.sysDeptMapper.deleteByCondition(queryModel);

        this.logger.debug("rows: {}", rows);
        //this.dataSourceTransactionManager.commit();
        return rows;
    }

    @Override
    public int delete(String[] recordIds, String superId) {
        int rows = this.delete(recordIds);
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("superId", superId);
        int count = this.sysDeptMapper.countByCondition(queryModel);
        if (count == 0) { //???????????????????????????
            SysDept superDept = this.sysDeptMapper.selectByPrimaryKey(superId);
            superDept.setEndFlag(1);
            this.sysDeptMapper.updateByPrimaryKey(superDept);
        }
        return rows;
    }

    public int save(SysDept record) {
        int rows = 0;
        if (record.getDeptId() == null || record.getDeptId() == "") {
            String uuid = UUIDUtil.getUUID();
            record.setDeptId(uuid);

            if (record.getSuperId() == null || "".equals(record.getSuperId()))
                record.setSuperId(this.DEPT_ROOT_ID);

            record.setEndFlag(1);//???????????????????????????
            rows = this.sysDeptMapper.insert(record);
            //???????????????????????????
            SysDept superDept = this.sysDeptMapper.selectByPrimaryKey(record.getSuperId());
            if (superDept.getEndFlag() == 1) {
                superDept.setEndFlag(0);
                this.sysDeptMapper.updateByPrimaryKey(superDept);
            }
        } else {
            rows = this.sysDeptMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}", rows);
        return rows;
    }


    /**
     * ?????????????????????????????????????????????
     * [ {"id":"001","title": "?????????","checkArr": "0","parentId": "0"},
     * {"id":"002","title": "?????????","checkArr": "0","parentId": "0"}]
     * ??????????????????Layui dtree??????????????????checkArr????????????????????????????????????????????????
     *
     * @return java.util.List<com.project.system.model.SysDept>
     * @author lilj
     * @date 2021/4/5 10:34
     */
    @Override
    public List<DTreeData> getTreeData() {

        List<DTreeData> treeData = new ArrayList<>();
        //???????????????????????????????????????
        List<SysDept> departments = this.findAllObjects();
        departments.stream().forEach(department -> {
            treeData.add(department);
        });
        return treeData;
    }

    @Override
    public List<DTreeData> getTreeData(String superId) {
        return null;
    }

    @Override
    public List<SysDept> getDepartmentsBySuperId(String superId) {

        if (superId == null || "".equals(superId))
            superId = DEPT_ROOT_ID;

        QueryModel queryModel = new QueryModel();
        //????????????????????????????????????
        queryModel.createCriteria()
                .andEqualTo("superId", superId);
        queryModel.or().andEqualTo("deptId", superId);
        queryModel.setOrderByClause("sequence");
        List<SysDept> departments = this.findObjects(queryModel);

        return departments;
    }

}