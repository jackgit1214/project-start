package com.project.system.service.impl;

import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.SysDeptUserMapper;
import com.project.system.model.SysDeptUser;
import com.project.system.service.SysDeptUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysDeptUserServiceImpl extends AbstractBusinessService<SysDeptUser> implements SysDeptUserService {
    @Autowired
    private SysDeptUserMapper sysDeptUserMapper;

    public BaseDao getDao() {
        return this.sysDeptUserMapper;
    }

    public int delete(String recordId) {
        int rows = this.sysDeptUserMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}", rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds) {
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("ID", id);
            rows = rows + this.sysDeptUserMapper.deleteByPrimaryKey(id);
        }
        this.logger.debug("rows: {}", rows);
        return rows;
    }

    public int save(SysDeptUser record) {
        int rows = 0;
        if (record.getId() == null || record.getId() == "") {
            String uuid = UUIDUtil.getUUID();
            record.setId(uuid);
            rows = this.sysDeptUserMapper.insert(record);
        } else {
            rows = this.sysDeptUserMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}", rows);
        return rows;
    }

    @Override
    public int save(String[] departments, String userId) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        queryModel.createCriteria().andEqualTo("userid", userId);
        //删除原有部门关系
        this.sysDeptUserMapper.deleteByCondition(queryModel);
        if (departments != null) {
            for (String deptId : departments) {
                SysDeptUser sysDeptUser = new SysDeptUser();
                sysDeptUser.setDeptid(deptId);
                sysDeptUser.setUserid(userId);
                rows = rows + this.save(sysDeptUser);
            }
        }
        return 0;
    }
}