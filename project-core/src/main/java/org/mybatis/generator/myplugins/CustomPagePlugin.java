package org.mybatis.generator.myplugins;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.util.FileUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.TableConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class CustomPagePlugin extends PluginAdapter {

    private String targetDirectory;
    private String jsDirectory;
    private String publicJsName;
    private String templatePath = "/bin";
    private String jsPackage = "$.SystemApp";
    private TableConfiguration tableConfiguration;
    private boolean isHandleDuplicateSubmission = false;
    // private String project;
    private String pojoUrl;
    private String indexFileTemplate = "index.ftl";
    private String editFileTemplate = "edit.ftl";
    private String tableName;
    private String pageVarName;
    private String actionName;
    public CustomPagePlugin() {
        super();

    }

    @Override
    public boolean validate(List<String> warnings) {

        targetDirectory = properties.getProperty("targetDirectory");
        // project = properties.getProperty("targetProject");
        templatePath = properties.getProperty("templatePath");
        //jsPackage = properties.getProperty("jsPackage");
        jsDirectory = properties.getProperty("jsDirectory");
        pageVarName = properties.getProperty("pageVarName");
        actionName = properties.getProperty("actionName");
        //publicJsName = properties.getProperty("publicJsName");
        String isHandleDS = properties.getProperty("isHandleDuplicateSubmission");
        if ("true".equals(isHandleDS))
            this.isHandleDuplicateSubmission = true;
        else
            this.isHandleDuplicateSubmission = false;

        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

        FreemarkerUtils.setConfigurationTemplatePath(templatePath);
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        tableConfiguration = introspectedTable.getTableConfiguration();
        boolean isCreateAction = tableConfiguration.isCreateActionEnabled();
        // ?????????action?????????????????????
        if (!isCreateAction)
            return null;
        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        String table = introspectedTable.getBaseRecordType();
        introspectedTable.getRemarks();
        tableName = table.replaceAll(this.pojoUrl + ".", "");

        //this.generateJsFile();
        this.generateIndexFile(introspectedTable);
        this.generateEditFile(introspectedTable);
        return introspectedTable.getGeneratedJavaFiles();

    }

    private void generateIndexFile(IntrospectedTable introspectedTable) {
        tableConfiguration = introspectedTable.getTableConfiguration();
        String pageIndex = this.targetDirectory + "/" + tableName.toLowerCase() + "/" + "index.html";
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        List<IntrospectedColumn> pkcolumns = introspectedTable.getPrimaryKeyColumns();
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (pkcolumns.size() > 0) {
            IntrospectedColumn pkcolumn = pkcolumns.get(0);
            map.put("pkcolumn", pkcolumn.getJavaProperty());
        }

        map.put("moduleName", this.tableName.toLowerCase());
        map.put("pageVar", tableConfiguration.getProperty("alias"));
        map.put("actionName", tableConfiguration.getProperty("actionName"));
        map.put("modelName", this.toLowerCase(this.tableName));
        map.put("module_chinaese", introspectedTable.getRemarks());
        map.put("columns", introspectedTable.getNonPrimaryKeyColumns());
        map.put("keyColumns",introspectedTable.getPrimaryKeyColumns());
        FreemarkerUtils.createFile(this.indexFileTemplate, map, pageIndex);

    }

    private void generateEditFile(IntrospectedTable introspectedTable) {

        String pageedit = this.targetDirectory + "/" + tableName.toLowerCase() + "/" + "edit.html";
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("jsPackageName", this.jsPackage);
        map.put("pageVar", tableConfiguration.getProperty("alias"));
        map.put("actionName", tableConfiguration.getProperty("actionName"));
        map.put("moduleName", this.tableName.toLowerCase());
        map.put("modelName", this.toLowerCase(this.tableName));
        map.put("module_chinaese", introspectedTable.getRemarks());
        if (this.isHandleDuplicateSubmission)
            map.put("isHandleDS", true);
        map.put("columns", introspectedTable.getNonPrimaryKeyColumns());
        map.put("keyColumns",introspectedTable.getPrimaryKeyColumns());

        FreemarkerUtils.createFile(this.editFileTemplate, map, pageedit);
    }

    /**
     * BaseUsers to baseUsers
     *
     * @param tableName
     * @return
     */
    protected String toLowerCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * BaseUsers to baseUsers
     *
     * @param tableName
     * @return
     */
    protected String toUpperCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    static class FreemarkerUtils {
        private static Configuration cfg = new Configuration();

        /**
         * ??????freemarker????????? freemarker????????????classpath,????????????ServletContext??????.
         *
         * @return ??????Configuration??????
         */

        private static Configuration getConfiguration() {

            cfg.setEncoding(Locale.getDefault(), "UTF-8");

            // ????????????????????????
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            // ?????????????????????//?????????????????????${a.b.c.d}?????????????????????????????????
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

            return cfg;
        }

        public static void setConfigurationTemplatePath(String file) {
//             cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            String path = FreemarkerUtils.class.getClassLoader().getResource("bin").toString();
            log.info(path);
            cfg.setClassForTemplateLoading(FreemarkerUtils.class,file);
            URLTemplateLoader classTemplateLoader = (ClassTemplateLoader)cfg.getTemplateLoader();

//            try {
//                Object obj = classTemplateLoader.findTemplateSource("bin/index.ftl");
//
//                log.info("======================================");
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }

        /**
         * @param ftl        ???????????????,??????????????????????????????templates??????,??????/module/view.ftl
         *                   templates/module/view.ftl
         * @param data       ????????????
         * @param targetFile ?????????????????????????????????,???????????????????????????,?????? "jsp/user/1.html"
         * @return
         */
        public static boolean createFile(String ftl, Map<String, Object> data, String targetFile) {

            try {
                // ??????Template??????
                Configuration cfg = FreemarkerUtils.getConfiguration();
                Template template = cfg.getTemplate("bin/"+ftl);

                template.setEncoding("UTF-8");
                FileUtils fileUtils = FileUtils.getFileUtils();
                File file = new File(targetFile);
                String filePath = file.getAbsolutePath();
                log.info(filePath);
                if (!file.exists())
                    fileUtils.createNewFile(file, true);
                // ??????????????????

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

                template.process(data, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (TemplateException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public static boolean createFile(String ftl, Map<String, Object> data, String targetFile,
                                         boolean isMerageable) {

            try {
                // ??????Template??????
                Configuration cfg = FreemarkerUtils.getConfiguration();
                Template template = cfg.getTemplate("bin/"+ftl);

                template.setEncoding("UTF-8");

                FileUtils file1 = FileUtils.getFileUtils();

                File file = new File(targetFile);
                if (!file.exists())
                    file1.createNewFile(file, true);
                // ??????????????????
                Writer writer = new StringWriter();

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));

                template.process(data, writer);
                out.write(writer.toString());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (TemplateException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
