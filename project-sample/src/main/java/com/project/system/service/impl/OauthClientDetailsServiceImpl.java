package com.project.system.service.impl;

import com.project.client.dao.OauthClientDetailsMapper;
import com.project.system.model.OauthClientDetails;
import com.project.system.service.OauthClientDetailsService;
import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.service.AbstractBusinessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OauthClientDetailsServiceImpl extends AbstractBusinessService<OauthClientDetails> implements OauthClientDetailsService {

    private final static String OAUTH_TYPE ="authorization_code,refresh_token";
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    public BaseDao getDao() {
        return this.oauthClientDetailsMapper;
    }

    public int delete(String recordId) {
        int rows = this.oauthClientDetailsMapper.deleteByPrimaryKey(recordId);
        this.logger.debug("rows: {}",rows);
        return rows;
    }

    public int delete(String[] recordIds) {
        int rows=0;
        QueryModel queryModel = new QueryModel();
        for (String id : recordIds){
            QueryModel.Criteria criteria = queryModel.createCriteria();
            criteria.andEqualTo("client_id",id);
            rows = rows + this.oauthClientDetailsMapper.deleteByPrimaryKey(id);}
            this.logger.debug("rows: {}",rows);
            return rows;
        }

    public int save(OauthClientDetails record) {
        int rows=0;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        record.setClientSecret(encoder.encode(record.getClientSecret()));
        if (record.getAuthorizedGrantTypes()==null || "".equals(record.getAuthorizedGrantTypes()))
            record.setAuthorizedGrantTypes(OAUTH_TYPE);
        OauthClientDetails client = this.oauthClientDetailsMapper.selectByPrimaryKey(record.getClientId());
        if (client==null) {
            rows = this.oauthClientDetailsMapper.insert(record);
        } else {
            rows = this.oauthClientDetailsMapper.updateByPrimaryKey(record);
        }
        this.logger.debug("rows: {}",rows);
        return rows;
    }
}