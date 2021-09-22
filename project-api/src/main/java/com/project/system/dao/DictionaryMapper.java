package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.system.model.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Mapper
@Repository
public interface DictionaryMapper extends IDataMapperByPage<Dictionary>, IDataMapperCRUD<Dictionary> {

    Set<Dictionary> getChildren(String codeId);

    Dictionary getParent(String superId);
}