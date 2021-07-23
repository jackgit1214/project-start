package com.project.generator.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.generator.model.GeneratorActionProperties;
import com.project.generator.model.GeneratorDaoProperties;
import com.project.generator.model.GeneratorPageProperties;
import com.project.generator.model.GeneratorProperties;
import com.project.generator.model.GeneratorServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lilj
 * @date 2021/03/26
 **/
@Component
@Slf4j
public class ConcreteGeneratorImpl extends Generator {

    @Override
    public void generateCode(String tableName, String projectName) throws Exception {
        List<String> warnings = new ArrayList<String>();
        Configuration config = new Configuration();
        Context context = this.createContext();

        context.addTableConfiguration(this.getTableConfiguration(context, tableName, "",""));
        context.setJavaModelGeneratorConfiguration(this.getModelConfigure("", projectName, ""));
        if (projectConfig.getCode().isGenerateDao()) {
            context.setJavaClientGeneratorConfiguration(this.getDaoConfiguration("", projectName, ""));
            context.setSqlMapGeneratorConfiguration(this.getSqlMapConfiguration("", ""));
        }
        if (projectConfig.getCode().isGenerateService()) {
            context.addPluginConfiguration(this.getServicePlugin("", projectName, ""));
        }
        if (projectConfig.getCode().isGenerateAction()) {
            context.addPluginConfiguration(this.getActionPlugin("", projectName, "", ""));
        }
        if (projectConfig.getCode().isGeneratePage()) {
            context.addPluginConfiguration(this.getPagePlugin(""));
        }
        execute(warnings, config, context);
    }


    @Override
    public void generateCode(GeneratorProperties modelP, GeneratorDaoProperties daoP, GeneratorServiceProperties serviceP,
                             GeneratorActionProperties actionP, GeneratorPageProperties pageP) throws Exception {
        ///增加项目路径测试

        List<String> warnings = new ArrayList<String>();
        Configuration config = new Configuration();

//      String classPath = (this.getClass()).getProtectionDomain().getCodeSource().getLocation().getFile();
//		String classEntry = classPath+"bin/mysql-connector-java-5.1.30.jar";
//		config.addClasspathEntry(classEntry);

        Context context = this.createContextConfigure(modelP);
        if (daoP.isGenerating()) {
            context.setJavaClientGeneratorConfiguration(this.getDaoConfiguration(daoP));
            context.setSqlMapGeneratorConfiguration(this.getSqlMapConfiguration(daoP));
        }
        if (serviceP.isGenerating()) {
            context.addPluginConfiguration(this.getServicePlugin(serviceP));
        }
        if (actionP.isGenerating()) {
            context.addPluginConfiguration(this.getActionPlugin(actionP));
        }
        if (pageP.isGenerating()) {
            context.addPluginConfiguration(this.getPagePlugin(pageP));
        }
        execute(warnings, config, context);
    }

    private void execute(List<String> warnings, Configuration config, Context context) throws InvalidConfigurationException, SQLException, IOException, InterruptedException {
        config.addContext(context);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

        myBatisGenerator.generate(null, null, null, true);
        warnings.forEach(warn -> {
            log.info(warn);
        });
    }
}
