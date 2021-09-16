package com.project.system.dao;

import com.project.core.mybatis.dao.Base.IDataMapperByPage;
import com.project.core.mybatis.dao.Base.IDataMapperCRUD;
import com.project.core.mybatis.dao.Base.IDataMapperWithBlob;
import com.project.system.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysUserMapper extends IDataMapperByPage<SysUser>, IDataMapperCRUD<SysUser>, IDataMapperWithBlob<SysUser> {
    public SysUser selectByLoginCodePlus(String loginName);

    public List<SysUser> testLazyLoad(String loginName);
}