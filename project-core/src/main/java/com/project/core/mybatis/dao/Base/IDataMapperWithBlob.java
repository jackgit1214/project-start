package com.project.core.mybatis.dao.Base;

import java.util.List;

import com.project.core.mybatis.util.PageResult;
import com.project.core.mybatis.model.QueryModel;
import org.apache.ibatis.annotations.Param;

public interface IDataMapperWithBlob<T> extends BaseDao {

    List<T> selectByConditionWithBLOBs(@Param("queryModel") QueryModel queryModel, @Param("page") PageResult<T> page);

    int updateByConditionWithBLOBs(@Param("record") T record, @Param("queryModel") QueryModel queryModel);

    int updateByPrimaryKeyWithBLOBs(T record);

    T selectBlobByPrimaryKey(String id);
}
