package com.project.system.service.impl;

import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.DictionaryMapper;
import com.project.system.model.Dictionary;
import com.project.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DictionaryServiceImpl extends AbstractBusinessService<Dictionary> implements DictionaryService {
    @Autowired
    private DictionaryMapper dictionaryMapper;

    public BaseDao getDao() {
        return this.dictionaryMapper;
    }

    public int delete(String recordId) {
        int rows = this.dictionaryMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("codeid",id);
            rows = rows + this.dictionaryMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(Dictionary record) {
        int rows=0;
        if (record.getCodeid()==null || record.getCodeid()=="") {
            String uuid = UUIDUtil.getUUID();
            record.setCodeid(uuid);
            rows = this.dictionaryMapper.insert(record);
        } else {
            rows = this.dictionaryMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }
}