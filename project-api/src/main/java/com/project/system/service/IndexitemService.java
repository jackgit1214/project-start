package com.project.system.service;

import com.project.core.mybatis.service.IBusinessService;
import com.project.system.model.Indexitem;

import java.util.Map;

public interface IndexitemService extends IBusinessService<Indexitem> {
    int delete(Map<String, String> recordId);

    int delete(Map<String, String>[] recordIds);

    int save(Indexitem record);
}