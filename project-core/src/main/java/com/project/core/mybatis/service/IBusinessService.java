package com.project.core.mybatis.service;

import java.util.List;

import com.project.core.mybatis.model.QueryModel;
import com.project.core.mybatis.util.PageResult;

public interface IBusinessService<T> {

    List<T> findAllObjects();

    T findObjectById(Object object);

    List<T> findObjects(QueryModel queryModel);

    T singleObject(QueryModel queryModel) throws Exception;

    PageResult<T> findObjectsByPage(QueryModel queryModel, PageResult<T> page) throws Exception;

    /**
     * 取得指定序列的下一个值
     *
     * @param sequenceName
     * @return
     */
    int getNextVal(String sequenceName);

    /**
     * 取得指定序列的当前值
     *
     * @param sequenceName
     * @return
     */
    int getCurVal(String sequenceName);

}
