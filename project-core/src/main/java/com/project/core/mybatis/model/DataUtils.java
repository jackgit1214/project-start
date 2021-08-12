package com.project.core.mybatis.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 内置角色与模块
 *
 * @author lilj
 * @date 2021/04/21
 **/
public class DataUtils {

    public static SysRole role = new SysRole("SuperAdmin99999", "SuperAdmin");

    public static Set<FunModule> initGenCodeModule() {
        Set<SysRole> roles = new HashSet<>();
        List<FunModule> child = new ArrayList<>();
        Set<FunModule> modules = new HashSet<>();
        FunModule funModule = new FunModule();
        funModule.setFunId("sys_func9999");
        funModule.setFunName("系统开发工具");
        funModule.setFunOrder(999);
        funModule.setFunType(1);
        funModule.setFunIcon("fa fa-qrcode");
        funModule.setModDesc("代码生成工具主菜单");
        funModule.setSuperModId("0");
        funModule.setRoles(roles);
        funModule.setChild(child);
        modules.add(funModule);


        FunModule funModule1 = new FunModule();
        funModule1.setFunId("sys_func888");
        funModule1.setFunName("代码生成");
        funModule1.setFunOrder(999);
        funModule1.setModDesc("代码生成");
        funModule1.setFunType(1);
        funModule1.setSuperModId("sys_func9999");
        funModule1.setUrlLink("/code/index");
        funModule1.setFunIcon("fa fa-file-code-o");
        funModule1.setRoles(roles);

        funModule.getChild().add(funModule1);
        modules.add(funModule1);

        FunModule funModule2 = new FunModule();
        funModule2.setFunId("sys_func801");
        funModule2.setFunName("图标库");
        funModule2.setFunOrder(999);
        funModule2.setFunType(1);
        funModule2.setModDesc("图标库");
        funModule2.setSuperModId("sys_func9999");
        funModule2.setUrlLink("/icon/icon.html");
        funModule2.setFunIcon("fa fa fa-dot-circle-o");
        funModule2.setRoles(roles);
        modules.add(funModule2);
        funModule.getChild().add(funModule2);
        role.setModules(modules);
        //roles.add(role);
        return modules;
    }
}
