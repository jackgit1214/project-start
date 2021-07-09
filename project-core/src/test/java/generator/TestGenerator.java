package generator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.project.core.web.config.ProjectConfig;
import com.project.generator.service.Generator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = com.project.ProjectCoreApplication.class)
public class TestGenerator {

    @Autowired
    private ProjectConfig projectConfig;

    @Autowired
    private Generator generator;


    @Test
    public void testGeneratorUtil() {
        try {
            generator.generateCode("comm_code", "TestProject");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testPath() {
        File project = new File("src/main/resources");
        System.out.println(project.isDirectory());
        log.info(project.getAbsolutePath());
        System.getProperties().forEach((pro, a) -> {
            System.out.print(pro.toString() + ":");
            System.out.println(a.toString());
        });
    }

    @Test
    public void test() throws Exception {
       // log.info(projectConfig.toString());
        List<String> warnings = new ArrayList<String>();
        Configuration config = new Configuration();
        Context context = new Context(null);
        context.setId("CustomContext");

        //加载缺省组件
        List<String> defaultPlugins = projectConfig.getCode().getDefaultPlugins();
        defaultPlugins.forEach(s -> {
            PluginConfiguration plugin = new PluginConfiguration();
            plugin.setConfigurationType(s);
            context.addPluginConfiguration(plugin);
        });

        //是否生成dao,dao与mapper一起绑定生成
        if (projectConfig.getCode().isGenerateDao()) {
            JavaClientGeneratorConfiguration daoConfiguration = new JavaClientGeneratorConfiguration();

            daoConfiguration.setTargetPackage("com.project.generator.dao");
            daoConfiguration.setTargetProject("src/main/java");
            daoConfiguration.setConfigurationType("XMLMAPPER");
            context.setJavaClientGeneratorConfiguration(daoConfiguration);

            SqlMapGeneratorConfiguration sqlMapConfiguration = new SqlMapGeneratorConfiguration();
            sqlMapConfiguration.setTargetPackage("mapper");
            sqlMapConfiguration.setTargetProject("src/main/resources");

            context.setSqlMapGeneratorConfiguration(sqlMapConfiguration);
        }
        //是否生成service
        if (projectConfig.getCode().isGenerateService()) {
            PluginConfiguration servicePlugin = new PluginConfiguration();
            servicePlugin.setConfigurationType(projectConfig.getCode().getServicePlugin());

            servicePlugin.addProperty("targetPackage", "com.project.generator.service");
            servicePlugin.addProperty("implementationPackage", "com.project.generator.service.impl");
            servicePlugin.addProperty("targetProject", "src/main/java");
            servicePlugin.addProperty("enabledUpdate", "false");
            servicePlugin.addProperty("enabledDelete", "false");

            context.addPluginConfiguration(servicePlugin);
        }
        //是否生成action
        if (projectConfig.getCode().isGenerateAction()) {
            PluginConfiguration actionPlugin = new PluginConfiguration();
            actionPlugin.setConfigurationType(projectConfig.getCode().getActionPlugin());

            actionPlugin.addProperty("targetPackage", "com.project.generator.web");
            actionPlugin.addProperty("servicePack", "com.project.generator.service");
            actionPlugin.addProperty("targetProject", "src/main/java");
            actionPlugin.addProperty("isHandleDuplicateSubmission", "true");
            actionPlugin.addProperty("enabledUpdate", "false");
            actionPlugin.addProperty("enabledDelete", "false");

            context.addPluginConfiguration(actionPlugin);
        }

        //是否生成page
        if (projectConfig.getCode().isGeneratePage()) {
            PluginConfiguration pagePlugin = new PluginConfiguration();
            pagePlugin.setConfigurationType(projectConfig.getCode().getPagePlugin());
            pagePlugin.addProperty("publicJsName", "system");
            pagePlugin.addProperty("jsDirectory", "src/main/webapp/resources/js");
            pagePlugin.addProperty("targetDirectory", "src/main/webapp/WEB-INF");
            pagePlugin.addProperty("templatePath", "src/test/resources/template");
            pagePlugin.addProperty("templateJSFile", "modulejs_simple.ftl");
            pagePlugin.addProperty("jsPackage", "$.SystemApp");
            pagePlugin.addProperty("isHandleDuplicateSubmission", "true");
            context.addPluginConfiguration(pagePlugin);
        }

        //数据库连接
        JDBCConnectionConfiguration jdbcConfiguration = new JDBCConnectionConfiguration();
        jdbcConfiguration.setConnectionURL(projectConfig.getCode().getJdbcConnectConnectionURL());
        jdbcConfiguration.setDriverClass(projectConfig.getCode().getJdbcConnectDriverClass());
        jdbcConfiguration.setPassword(projectConfig.getCode().getJdbcConnectPassword());
        jdbcConfiguration.setUserId(projectConfig.getCode().getJdbcConnectUserId());
        context.setJdbcConnectionConfiguration(jdbcConfiguration);

        //mode配置
        JavaModelGeneratorConfiguration javaModelConfiguration = new JavaModelGeneratorConfiguration();
        javaModelConfiguration.setTargetProject("src/main/java");
        javaModelConfiguration.setTargetPackage("com.project.generator.model");
        javaModelConfiguration.addProperty("enableSubPackages", "true");
        javaModelConfiguration.addProperty("trimStrings", "true");
        javaModelConfiguration.addProperty("rootClass", "com.project.core.mybatis.model.BaseModel");
        javaModelConfiguration.addProperty("useActualColumnNames", "true");
        context.setJavaModelGeneratorConfiguration(javaModelConfiguration);

        //添加表配置
        TableConfiguration tc = new TableConfiguration(context);
        //tc.setSchema("netbook");
        tc.setTableName("netbook");
        //tc.setDomainObjectName("MysqlTables");
        tc.setCountByExampleStatementEnabled(false);
        tc.setUpdateByExampleStatementEnabled(false);
        tc.setDeleteByExampleStatementEnabled(false);
        tc.setSelectByExampleStatementEnabled(false);
        //tc.setSelectByPrimaryKeyStatementEnabled(tr);
        context.addTableConfiguration(tc);

        config.addContext(context);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(2, e.getErrors().size());
            throw e;
        }
    }


//    @Test
    public void testGenerateMyBatis() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bin/generatorConfig.xml");

        Configuration config = cp.parseConfiguration(inputStream);
//		String classPath = this.getClass().getClassLoader().getResource("").getPath();
//		String classEntry = classPath +"/bin/mysql-connector-java-5.1.30.jar";
//		config.addClasspathEntry(classEntry);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

            myBatisGenerator.generate(null, null, null, true);
        } catch (InvalidConfigurationException e) {
            assertEquals(2, e.getErrors().size());
            throw e;
        }
    }

}
