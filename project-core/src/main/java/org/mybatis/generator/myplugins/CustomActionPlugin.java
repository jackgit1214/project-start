package org.mybatis.generator.myplugins;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

public class CustomActionPlugin extends PluginAdapter {

    private final String CONTROLLER = "org.springframework.stereotype.Controller";
    private final String MODELMAP = "org.springframework.ui.ModelMap";
    private final String REQUESTMAPPING = "org.springframework.web.bind.annotation.RequestMapping";
    private final String REQUESTMETHOD = "org.springframework.web.bind.annotation.RequestMethod";
    private final String REQUESTPARAM = "org.springframework.web.bind.annotation.RequestParam";
    private final String RESPONSEBODY = "org.springframework.web.bind.annotation.ResponseBody";
    private final String MODELANDVIEW = "org.springframework.web.servlet.ModelAndView";
    private final String BASECONTROLLER = "com.project.core.web.controller.BaseController";
    private final String DS = "com.project.core.common.anaotation.DuplicateSubmission";
    private final String QUERYMODEL = "com.project.core.mybatis.model.QueryModel";
    private boolean isHandleDuplicateSubmission = false;
    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType controllerType;
    private FullyQualifiedJavaType queryModelType;
    private FullyQualifiedJavaType pojoType;
    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType controller;
    private FullyQualifiedJavaType modelAndViewType;
    private FullyQualifiedJavaType modelMapType;
    private FullyQualifiedJavaType baseController;
    private FullyQualifiedJavaType DuplicateSubmission;
    private FullyQualifiedJavaType stringType = new FullyQualifiedJavaType("java.lang.String");
    private String servicePack;
    private String controllerPack;
    private String project;
    private String actionName;
    private String pojoUrl;
    private boolean enableUpdate = true;
    private boolean enableDelete = true;

    @SuppressWarnings("unused")
    private List<Method> methods;

    public CustomActionPlugin() {
        super();
        methods = new ArrayList<Method>();
    }

    @Override
    public boolean validate(List<String> warnings) {

        servicePack = properties.getProperty("servicePack");
        controllerPack = properties.getProperty("targetPackage");
        project = properties.getProperty("targetProject");
        actionName = properties.getProperty("actionName");
        String isHandleDS = properties.getProperty("isHandleDuplicateSubmission");

        if ("true".equals(isHandleDS))
            this.isHandleDuplicateSubmission = true;
        else
            this.isHandleDuplicateSubmission = false;

        String enabledUpdate = properties.getProperty("enabledUpdate");
        if (StringUtility.stringHasValue(enabledUpdate))
            this.enableUpdate = StringUtility.isTrue(enabledUpdate);

        String enabledDelete = properties.getProperty("enabledDelete");
        if (StringUtility.stringHasValue(enabledDelete))
            this.enableDelete = StringUtility.isTrue(enabledDelete);

        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

        autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
        controller = new FullyQualifiedJavaType(this.CONTROLLER);
        baseController = new FullyQualifiedJavaType(this.BASECONTROLLER);
        modelAndViewType = new FullyQualifiedJavaType(this.MODELANDVIEW);
        modelMapType = new FullyQualifiedJavaType(this.MODELMAP);
        this.queryModelType = new FullyQualifiedJavaType(this.QUERYMODEL);

        DuplicateSubmission = new FullyQualifiedJavaType(this.DS);
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
        String table = introspectedTable.getBaseRecordType();

        boolean isCreateAction = introspectedTable.getTableConfiguration().isCreateActionEnabled();

        if (!isCreateAction)
            return null;
        String tableName = table.replaceAll(this.pojoUrl + ".", "");

        controllerType = new FullyQualifiedJavaType(this.controllerPack + "." + tableName + "Controller");
        pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);

        serviceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");

        listType = new FullyQualifiedJavaType("java.util.List");

        TopLevelClass topLevelClass = new TopLevelClass(controllerType);
        // 导入必要的类
        addImport(topLevelClass);

        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addAnnotation("@Controller");
        topLevelClass.addAnnotation("@RequestMapping(\"/" + tableName.toLowerCase() + "\")");

        topLevelClass.setSuperClass(this.baseController.getShortName());

        this.addField(topLevelClass, tableName);
        topLevelClass.addMethod(this.generateIndexMethod(introspectedTable, tableName));
        topLevelClass.addMethod(this.generateDataListMethod(introspectedTable, tableName));
        if (this.enableUpdate) {
            topLevelClass.addMethod(this.generateShowEditMethod(introspectedTable, tableName));
            topLevelClass.addMethod(this.generateAddOrUpdateMethod(introspectedTable, tableName));
        }
        if (this.enableDelete) {
            topLevelClass.addMethod(this.generateDeleteMethod(introspectedTable, tableName));
        }


        // 生成文件
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project, context.getJavaFormatter());
        file.getFormattedContent();
        files.add(file);
        return files;
    }

    /**
     * 添加字段
     *
     * @param topLevelClass
     */
    protected void addField(TopLevelClass topLevelClass, String tableName) {
        // 添加 dao
        Field field = new Field();
        field.setName(toLowerCase(this.serviceType.getShortName()) + "Impl"); // 设置变量名

        field.setType(this.serviceType); // 类型
        field.setVisibility(JavaVisibility.PRIVATE);

        field.addAnnotation("@Autowired");

        topLevelClass.addField(field);
    }

    private Method generateDataListMethod(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("data");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(this.modelMapType);
        method.addAnnotation("@RequestMapping(\"data\")");
        method.addAnnotation("@ResponseBody");
        method.addAnnotation("@QueryModelParam");
        method.addParameter(new Parameter(this.queryModelType, "queryModel"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "page"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "limit"));

        method.addBodyLine("ModelMap modelMap = new ModelMap();");

        StringBuffer sb = new StringBuffer();
        sb.append("PageResult<").append(tableName).append("> pageResult = new PageResult<").append(tableName)
                .append(">(page,limit);");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("try {").append("this.").append(this.getServiceShort()).append("findObjectsByPage(queryModel,pageResult);")
                .append("} catch(Exception e) {").append("e.printStackTrace();").append("}");
        method.addBodyLine(sb.toString());

        method.addBodyLine("BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), pageResult);");
        method.addBodyLine("modelMap.addAttribute(\"status\", rtnMsg);");

        method.addBodyLine("return modelMap;");
        return method;
    }

    private Method generateIndexMethod(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("index");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(this.stringType);
        method.addAnnotation("@RequestMapping(\"/index\")");
        method.addBodyLine("return \""+tableName.toLowerCase()+"/index\";");
        return method;
    }

    private Method generateShowEditMethod(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("edit");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(this.stringType);
        method.addAnnotation("@RequestMapping(\"/edit\")");

        if (this.isHandleDuplicateSubmission) {
            method.addAnnotation("@DuplicateSubmission(needSaveToken=true)");
        }

        List<IntrospectedColumn> pcolumns = introspectedTable.getPrimaryKeyColumns();
        String paramType = "String";

        String lowerTableName = this.toLowerCase(tableName);
        Parameter parameter = null;
        if (pcolumns.size() > 0) {
            IntrospectedColumn column = pcolumns.get(0);
            String columnType = column.getJdbcTypeName();
            if ("varchar".equals(columnType.toLowerCase())) {
                parameter = new Parameter(new FullyQualifiedJavaType("java.lang.String"), "id");
            } else {
                parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "id");
            }
        }
        method.addParameter(parameter);
        method.addParameter(new Parameter(modelMapType,"map"));

        StringBuffer sb = new StringBuffer();
        sb.append(tableName).append(" ").append(lowerTableName).append(" = this.").append(this.getServiceShort())
                .append("findObjectById(id);");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("if (").append(lowerTableName).append(" == null)");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("  ").append(lowerTableName).append(" = ").append("new ").append(tableName).append("();");
        method.addBodyLine(sb.toString());
//
        sb.setLength(0);
        sb.append("map.put(\"data").append("\",").append(lowerTableName).append(");");
        method.addBodyLine(sb.toString());

        method.addBodyLine("return \""+lowerTableName+"/edit\";");
        return method;
    }

    private Method generateAddOrUpdateMethod(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("save"+ StringUtils.capitalize(tableName));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(this.modelMapType);
        method.addAnnotation("@ResponseBody");
        method.addAnnotation("@RequestMapping(\"/save\")");
        if (this.isHandleDuplicateSubmission) {
            method.addAnnotation("@DuplicateSubmission(needRemoveToken=true)");
        }

        method.addParameter(new Parameter(this.pojoType, "record"));

        method.addBodyLine("ModelMap modelMap = new ModelMap();");
        StringBuffer sb = new StringBuffer();

        sb.append("int rows = this.").append(this.getServiceShort()).append("save(record);");
        method.addBodyLine(sb.toString());
        method.addBodyLine("BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);");
        method.addBodyLine("modelMap.addAttribute(\"status\", rtnMsg);");

        method.addBodyLine("return modelMap;");

        return method;
    }

    private Method generateDeleteMethod(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("delete");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(this.modelMapType);
        method.addAnnotation("@ResponseBody");
        method.addAnnotation("@RequestMapping(\"/delete\")");

        List<IntrospectedColumn> pcolumns = introspectedTable.getPrimaryKeyColumns();
        String paramType = "String";
        Parameter parameter = null;
        if (pcolumns.size() > 0) {
            IntrospectedColumn column = pcolumns.get(0);
            String columnType = column.getJdbcTypeName();
            if ("varchar".equals(columnType.toLowerCase())) {
                parameter = new Parameter(new FullyQualifiedJavaType("java.lang.String[]"), "ids");
            } else {
                parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer[]"), "ids");
            }
            method.addParameter(parameter);
        }

        method.addBodyLine("ModelMap modelMap = new ModelMap();");
        StringBuffer sb = new StringBuffer();

        sb.append("int rows = this.").append(this.getServiceShort()).append("delete(ids);");
        method.addBodyLine(sb.toString());
        method.addBodyLine("BaseResult rtnMsg = new BaseResult(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMessage(), rows);");
        method.addBodyLine("modelMap.addAttribute(\"status\", rtnMsg);");
        method.addBodyLine("return modelMap;");
        return method;
    }

    protected void addComment(JavaElement field, String comment) {
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        comment = comment.replaceAll("\n", "<br>\n\t * ");
        sb.append(comment);
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
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

    /**
     * 导入需要的类
     */
    private void addImport(TopLevelClass topLevelClass) {

        if (this.isHandleDuplicateSubmission) {
            topLevelClass.addImportedType(this.DuplicateSubmission);
        }

        topLevelClass.addImportedType(this.serviceType);
        topLevelClass.addImportedType(this.QUERYMODEL);
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.CONTROLLER));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.MODELANDVIEW));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.MODELMAP));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.REQUESTMAPPING));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.REQUESTMETHOD));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.REQUESTPARAM));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.RESPONSEBODY));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.project.core.common.response.BaseResult"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.project.core.common.SysConstant"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.project.core.mybatis.util.PageResult"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.project.core.common.anaotation.QueryModelParam"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.project.core.common.response.BaseResult"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.project.core.common.response.ReturnCode"));

        topLevelClass.addImportedType(this.baseController);
        topLevelClass.addImportedType(pojoType);
        topLevelClass.addImportedType(listType);
        topLevelClass.addImportedType(this.controller);
        topLevelClass.addImportedType(autowired);

    }

    private String getServiceShort() {

        return toLowerCase(serviceType.getShortName()) + "Impl."; // 实现类以impl结尾
    }

}
