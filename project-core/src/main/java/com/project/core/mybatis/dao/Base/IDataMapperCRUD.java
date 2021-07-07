package com.project.core.mybatis.dao.Base;

import java.util.List;

import com.project.core.mybatis.model.QueryModel;
import org.apache.ibatis.annotations.Param;

public interface IDataMapperCRUD<T> extends BaseDao {

    int insert(T record);

    int insertSelective(T record);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    int updateByConditionSelective(@Param("record") T record,
                                   @Param("queryModel") QueryModel queryModel);

    int updateByCondition(@Param("record") T record,
                          @Param("queryModel") QueryModel queryModel);

    int deleteByCondition(@Param("queryModel") QueryModel queryModel);

    int deleteByPrimaryKey(Object recordId);

    List<T> selectByCondition(@Param("queryModel") QueryModel queryModel);

    int countByCondition(@Param("queryModel") QueryModel queryModel);

    T selectByPrimaryKey(Object id);
}
