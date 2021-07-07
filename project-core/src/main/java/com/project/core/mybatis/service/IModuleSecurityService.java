package com.project.core.mybatis.service;

import java.util.List;

import com.project.core.security.model.FunModule;

/**
 * 模块接口Service ,目的是取得所有可用资源
 *
 * @author lilj
 * @date 2021/09/19 11:09
 * @return
 */
public interface IModuleSecurityService {

    public List<FunModule> getSysModules();

}
