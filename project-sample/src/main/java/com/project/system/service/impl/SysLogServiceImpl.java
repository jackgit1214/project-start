package com.project.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.core.common.util.UUIDUtil;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.model.SysLog;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.system.dao.SysLogMapper;
import com.project.system.service.SysLogService;
import java.util.List;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SysLogServiceImpl extends AbstractBusinessService<SysLog> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    public BaseDao getDao() {
        return this.sysLogMapper;
    }

    public int delete(String recordId) {
        int rows = this.sysLogMapper.deleteByPrimaryKey(recordId);
        log.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows = 0;
        QueryModel queryModel = new QueryModel();
        if (recordIds==null){
            rows = rows + this.sysLogMapper.deleteByCondition(null);
            log.debug("rows: {}",rows);
            return rows;
        }else {
            for (String id : recordIds) {
                QueryModel.Criteria criteria = queryModel.or();
                criteria.andEqualTo("logID", id);
            }
        }
        rows = rows + this.sysLogMapper.deleteByCondition(queryModel);
        log.debug("rows: {}",rows);
        return rows;
    }
    @SneakyThrows
    public int saveLogInfo(SysLog record) {
        int rows= this.sysLogMapper.insert(record);
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(record));
        log.debug("rows: {}",rows);
        return rows;
    }
}