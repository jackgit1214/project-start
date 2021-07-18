package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.core.mybatis.model.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysLogMapper extends IDataMapperByPage<SysLog>, IDataMapperCRUD<SysLog> {
}