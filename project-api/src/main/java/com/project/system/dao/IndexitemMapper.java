package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.system.model.Indexitem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
@Repository
public interface IndexitemMapper extends IDataMapperByPage<Indexitem>, IDataMapperCRUD<Indexitem> {

    Set<Indexitem>  getChildren(String indexId);

    Indexitem  getParent(String indexId);
}