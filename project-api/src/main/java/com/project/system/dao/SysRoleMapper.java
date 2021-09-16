package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.system.model.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRoleMapper extends IDataMapperByPage<SystemRole>, IDataMapperCRUD<SystemRole> {

    public SystemRole selectByPrimaryKeyPlus(Object id);

    SystemRole getRolesByUserId(Object userId);
}