package com.project.system.service;

import com.project.core.mybatis.model.SysLog;
import com.project.core.mybatis.service.IBusinessService;
import com.project.core.mybatis.service.LogDetailService;

import java.util.List;

public interface SysLogService extends IBusinessService<SysLog>, LogDetailService {
    int delete(String recordId);

    int delete(String[] recordIds);

}