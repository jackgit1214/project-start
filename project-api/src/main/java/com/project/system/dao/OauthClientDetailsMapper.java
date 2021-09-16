package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.core.mybatis.dao.Base.IDataMapperWithBlob;
import com.project.system.model.OauthClientDetails;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OauthClientDetailsMapper extends IDataMapperByPage<OauthClientDetails>, IDataMapperCRUD<OauthClientDetails>, IDataMapperWithBlob<OauthClientDetails> {
}