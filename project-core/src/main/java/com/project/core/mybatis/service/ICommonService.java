package com.project.core.mybatis.service;

import org.springframework.stereotype.Service;


@Service
public interface ICommonService<T> {

    int saveObject(T object);

    int delete(String[] ids);

    int delete(String id);

}
