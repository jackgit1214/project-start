package com.project.core.mybatis.dao.Base;

import java.util.List;

import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;
import org.apache.ibatis.annotations.Param;

public interface IDataMapperByPage<T> extends BaseDao {

    List<T> selectByCondition(@Param("queryModel") QueryModel queryModel, @Param("page") PageResult<T> page);
}
