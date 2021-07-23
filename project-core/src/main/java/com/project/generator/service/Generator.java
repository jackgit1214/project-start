package com.project.generator.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.project.core.web.config.ProjectConfig;
import com.project.generator.model.GeneratorActionProperties;
import com.project.generator.model.GeneratorDaoProperties;
import com.project.generator.model.GeneratorPageProperties;
import com.project.generator.model.GeneratorProperties;
import com.project.generator.model.GeneratorServiceProperties;
import com.project.generator.web.CodeController;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.exception.InvalidConfigurationException;

import org.mybatis.generator.myplugins.CustomCommentGenerator;
import org.mybatis.generator.myplugins.RemarksCommentGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * 代码生成工具
 *
 * @author lilj
 * @date 2021/03/26
 **/
public abstract class Generator {

    @Autowired
    protected ProjectConfig projectConfig;

    protected Context createContextConfigure(GeneratorProperties generatorProperties) {

        Context context = this.createContext();
        this.addTableAndModelConfigurations(context, generatorProperties);
        return context;
    }

    protected Context createContext() {

        Context context = new Context(null);
        context.setId("CustomContext");

        CustomCommentGenerator customCommentGenerator = new CustomCommentGenerator();
        context.setCommentGenerator(customCommentGenerator);

        //加载缺省组件
        List<String> defaultPlugins = projectConfig.getCode().getDefaultPlugins();
        defaultPlugins.forEach(s -> {
            PluginConfiguration plugin = new PluginConfiguration();
            plugin.setConfigurationType(s);
            context.addPluginConfiguration(plugin);
        });

        //数据库连接
        JDBCConnectionConfiguration jdbcConfiguration = new JDBCConnectionConfiguration();
        jdbcConfiguration.setConnectionURL(projectConfig.getCode().getJdbcConnectConnectionURL());
        jdbcConfiguration.setDriverClass(projectConfig.getCode().getJdbcConnectDriverClass());
        jdbcConfiguration.setPassword(projectConfig.getCode().getJdbcConnectPassword());
        jdbcConfiguration.setUserId(projectConfig.getCode().getJdbcConnectUserId());
        context.setJdbcConnectionConfiguration(jdbcConfiguration);

        return context;
    }

    protected void addTableAndModelConfigurations(Context context, GeneratorProperties generatorProperties) {
        context.setJavaModelGeneratorConfiguration(this.getModelConfigure(generatorProperties));
        List<Map<String,String>> tableNames = generatorProperties.getTableNames();
        tableNames.forEach((table) -> {
            String tableName = table.get("tableName");
            String domainName = table.get("domain");
            String alias = table.get("alias");
            String actionName = table.get("actionName");
            TableConfiguration tc = this.getTableConfiguration(context, tableName, domainName,alias);
            tc.addProperty("actionName",actionName);
            if (StringUtils.hasLength(alias))
                tc.addProperty("alias",StringUtils.capitalize(alias));
            else
                tc.addProperty("alias",tableName);
            context.addTableConfiguration(tc);
        });

    }

    protected TableConfiguration getTableConfiguration(Context context, String tableName, String domainName,String alias) {
        //添加表配置
        TableConfiguration tc = new TableConfiguration(context);
        tc.setTableName(tableName);
        if (StringUtils.hasLength(domainName))
            tc.setDomainObjectName(StringUtils.capitalize(domainName));

        tc.setCountByExampleStatementEnabled(false);
        tc.setUpdateByExampleStatementEnabled(false);
        tc.setDeleteByExampleStatementEnabled(false);
        tc.setSelectByExampleStatementEnabled(false);
        tc.setConfiguredModelType(ModelType.FLAT.toString());
        //禁止了，不设置会不生成base_column
//		tc.setSelectByPrimaryKeyStatementEnabled(false);
        return tc;
    }

    protected JavaClientGeneratorConfiguration getDaoConfiguration(GeneratorDaoProperties daoP) {
        return this.getDaoConfiguration(daoP.getTargetProject(), daoP.getProjectName(), daoP.getTargetPackage());
    }

    protected JavaClientGeneratorConfiguration getDaoConfiguration(String projectPath, String projectName, String packagePath) {
        JavaClientGeneratorConfiguration daoConfiguration = new JavaClientGeneratorConfiguration();

        if (projectPath == null || "".equals(projectPath))
            projectPath = "src/main/java";
        daoConfiguration.setTargetProject(projectPath);
        if (packagePath == null || "".equals(packagePath))
            packagePath = "com." + projectName + ".dao";

        daoConfiguration.setTargetPackage(packagePath);
        daoConfiguration.setConfigurationType("XMLMAPPER");

        return daoConfiguration;
    }

    protected SqlMapGeneratorConfiguration getSqlMapConfiguration(GeneratorDaoProperties daoP) {
        return this.getSqlMapConfiguration(daoP.getMapperTargetProject(), daoP.getMapperTargetPackage());
    }

    protected SqlMapGeneratorConfiguration getSqlMapConfiguration(String mapperPath, String mapperPackage) {

        SqlMapGeneratorConfiguration sqlMapConfiguration = new SqlMapGeneratorConfiguration();
        if (mapperPackage == null || "".equals(mapperPackage))
            mapperPackage = "mapper";

        sqlMapConfiguration.setTargetPackage(mapperPackage);
        if (mapperPath == null || "".equals(mapperPath))
            mapperPath = "src/main/resources";

        sqlMapConfiguration.setTargetProject(mapperPath);

        return sqlMapConfiguration;
    }

    protected PluginConfiguration getActionPlugin(GeneratorActionProperties actionP) {
        return this.getActionPlugin(actionP.getTargetProject(), actionP.getProjectName(), actionP.getTargetPackage(), actionP.getServicePack());
    }

    protected PluginConfiguration getActionPlugin(String projectPath, String projectName, String packagePath, String servicePack) {
        PluginConfiguration actionPlugin = new PluginConfiguration();
        actionPlugin.addProperty("isHandleDuplicateSubmission", "true");
        actionPlugin.addProperty("enabledUpdate", "true");
        actionPlugin.addProperty("enabledDelete", "true");
        if (projectPath == null || "".equals(projectPath))
            projectPath = "src/main/java";
        actionPlugin.addProperty("targetProject", projectPath);

        if (packagePath == null || "".equals(packagePath))
            packagePath = "com." + projectName + ".web";
        if (servicePack == null || "".equals(servicePack))
            servicePack = "com." + projectName + ".service";
        actionPlugin.setConfigurationType(projectConfig.getCode().getActionPlugin());
        actionPlugin.addProperty("targetPackage", packagePath);
        actionPlugin.addProperty("servicePack", servicePack);

        return actionPlugin;
    }

    protected PluginConfiguration getServicePlugin(GeneratorServiceProperties serviceP) {
        return this
                .getServicePlugin(serviceP.getTargetProject(), serviceP.getProjectName(), serviceP.getTargetPackage());
    }

    protected PluginConfiguration getServicePlugin(String projectPath, String projectName, String packagePath) {
        PluginConfiguration servicePlugin = new PluginConfiguration();
        servicePlugin.setConfigurationType(projectConfig.getCode().getServicePlugin());
        if (projectPath == null || "".equals(projectPath))
            projectPath = "src/main/java";
        servicePlugin.addProperty("targetProject", projectPath); //"src/main/java"
        servicePlugin.addProperty("enabledUpdate", "true");
        servicePlugin.addProperty("enabledDelete", "true");

        if (packagePath == null || "".equals(packagePath))
            packagePath = "com." + projectName + ".service";

        servicePlugin.addProperty("targetPackage", packagePath);
        servicePlugin.addProperty("implementationPackage", packagePath + ".impl");

        return servicePlugin;
    }

    protected PluginConfiguration getPagePlugin(GeneratorPageProperties pageP) {
        return this.getPagePlugin(pageP.getTargetDirectory());
    }

    protected PluginConfiguration getPagePlugin(String targetDirectory) {
        PluginConfiguration pagePlugin = new PluginConfiguration();
        pagePlugin.setConfigurationType(projectConfig.getCode().getPagePlugin());
        //pagePlugin.addProperty("publicJsName","system");
        //pagePlugin.addProperty("jsDirectory","src/main/webapp/resources/js");
        if (targetDirectory == null || "".equals(targetDirectory))
            targetDirectory = "src/main/resources/templates";

        pagePlugin.addProperty("targetDirectory", targetDirectory); //"src/main/webapp/WEB-INF"
        pagePlugin.addProperty("templatePath","/");
        pagePlugin.addProperty("isHandleDuplicateSubmission", "true");
        return pagePlugin;
    }

    protected JavaModelGeneratorConfiguration getModelConfigure(GeneratorProperties modelP) {
        return this.getModelConfigure(modelP.getTargetProject(), modelP.getProjectName(), modelP.getTargetPackage());
    }

    protected JavaModelGeneratorConfiguration getModelConfigure(String projectPath, String projectName, String packagePath) {
        JavaModelGeneratorConfiguration javaModelConfiguration = new JavaModelGeneratorConfiguration();
        javaModelConfiguration.addProperty("trimStrings", "true");
        javaModelConfiguration.addProperty("rootClass", "com.project.core.mybatis.model.BaseModel");
        javaModelConfiguration.addProperty("useActualColumnNames", "true");
        javaModelConfiguration.addProperty("enableSubPackages", "true");
        if (projectPath == null || "".equals(projectPath))
            projectPath = "src/main/java";
        javaModelConfiguration.setTargetProject(projectPath); // 通常情况下为：src/main/java

        if (packagePath == null || "".equals(packagePath))
            packagePath = "com." + projectName + ".model";
        javaModelConfiguration.setTargetPackage(packagePath); //

        return javaModelConfiguration;
    }

    public abstract void generateCode(String tableName, String projectName) throws Exception;

    public abstract void generateCode(GeneratorProperties modelP, GeneratorDaoProperties daoP, GeneratorServiceProperties serviceP,
                                      GeneratorActionProperties actionP, GeneratorPageProperties pageP) throws Exception;
}
