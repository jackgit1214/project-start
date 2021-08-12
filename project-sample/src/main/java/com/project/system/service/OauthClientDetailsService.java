package com.project.system.service;

import com.project.system.model.OauthClientDetails;
import com.project.core.mybatis.service.IBusinessService;

public interface OauthClientDetailsService extends IBusinessService<OauthClientDetails> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(OauthClientDetails record);
}