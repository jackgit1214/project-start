package com.project.core.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.core.mybatis.service.IModuleSecurityService;
import com.project.core.security.model.DataUtils;
import com.project.core.security.model.FunModule;
import com.project.core.security.model.SysRole;
import com.project.core.web.config.ProjectConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lilj
 * @date 2021/03/19
 **/
@Service
public class SimpleModuleSecurityServiceImpl<T> extends AbstractBusinessService<T> implements IModuleSecurityService {


    @Autowired
    protected ProjectConfig projectConfig;


    @Override
    public List<FunModule> getSysModules() {

        List<FunModule> modules = new ArrayList<>();
        if (projectConfig.getConfigure().isCodeTool()) {
            modules.addAll(DataUtils.initGenCodeModule());
            return modules;
        }
        return null;
    }


    @Override
    public BaseDao getDao() {
        return null;
    }
}
