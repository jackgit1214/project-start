package com.project.core.mybatis.dao.Base;

import java.util.List;

import com.project.core.mybatis.model.QueryModel;
import org.apache.ibatis.annotations.Param;


public interface IDataMapper<T> extends BaseDao {

    int countByCondition(@Param("queryModel") QueryModel queryModel);

    int deleteByCondition(@Param("queryModel") QueryModel queryModel);

    List<T> selectByCondition(@Param("queryModel") QueryModel queryModel);

    int updateByConditionSelective(@Param("record") T record, @Param("queryModel") QueryModel queryModel);

    int updateByCondition(@Param("record") T record, @Param("queryModel") QueryModel queryModel);

    T selectByPrimaryKey(Object id);

}
