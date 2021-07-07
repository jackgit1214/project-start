package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.system.model.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysRoleUserMapper extends IDataMapperByPage<SysRoleUser>, IDataMapperCRUD<SysRoleUser> {
}