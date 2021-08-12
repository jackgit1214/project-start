package com.project.sso.service;

import com.project.core.mybatis.dao.Base.BaseDao;
import com.project.core.mybatis.model.DataUtils;
import com.project.core.mybatis.model.FunModule;
import com.project.core.mybatis.service.AbstractBusinessService;
import com.project.core.web.config.ProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
