package com.project.system.service.impl;

import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.IndexitemMapper;
import com.project.system.model.Indexitem;
import com.project.system.service.IndexitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@Transactional
public class IndexitemServiceImpl extends AbstractBusinessService<Indexitem> implements IndexitemService {
    @Autowired
    private IndexitemMapper indexitemMapper;

    public BaseDao getDao() {
        return this.indexitemMapper;
    }

    public int delete(Map<String, String> recordId) {
        int rows = this.indexitemMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(Map<String, String>[] recordIds) {
        int rows=0;
        for (Map<String,String> id : recordIds){
            
            rows = rows + this.indexitemMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Indexitem record) {
        int rows=0;
        String uuid = UUIDUtil.getUUID();
        if (record.isNew()) {
            if (!StringUtils.hasLength(record.getIndexid()))  record.setIndexid(uuid);
            if (!StringUtils.hasLength(record.getId()))  record.setId(uuid);
            rows = this.indexitemMapper.insert(record);
        } else {
            rows = this.indexitemMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }
}