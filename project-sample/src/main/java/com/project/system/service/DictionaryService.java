package com.project.system.service;

import com.project.core.mybatis.service.IBusinessService;
import com.project.system.model.Dictionary;

public interface DictionaryService extends IBusinessService<Dictionary> {
    int delete(String recordId);

    int delete(String[] recordIds);

    int save(Dictionary record);
}