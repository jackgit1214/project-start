package com.project.client.dao;

import com.project.client.model.OauthClientDetails;
import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.core.mybatis.dao.Base.IDataMapperWithBlob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OauthClientDetailsMapper extends IDataMapperByPage<OauthClientDetails>, IDataMapperCRUD<OauthClientDetails>, IDataMapperWithBlob<OauthClientDetails> {
}