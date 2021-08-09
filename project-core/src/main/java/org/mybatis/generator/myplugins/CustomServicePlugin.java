package org.mybatis.generator.myplugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

public class CustomServicePlugin extends PluginAdapter {

    private final String IBUSINESSSERVICE = "com.project.core.mybatis.service.IBusinessService";
    private final String ABSTRACTBUSINESSSERVICE = "com.project.core.mybatis.service.AbstractBusinessService";

    private final String UUIDUTIL = "com.project.core.common.util.UUIDUtil";
    private final String BASEDAO = "com.project.core.mybatis.dao.Base.BaseDao";
    private final FullyQualifiedJavaType QUERYMODEL = new FullyQualifiedJavaType(
            "com.project.core.mybatis.model.QueryModel");

    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType daoType;
    private FullyQualifiedJavaType interfaceType;
    private FullyQualifiedJavaType pojoType;
    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType service;
    private FullyQualifiedJavaType transactional;
    private FullyQualifiedJavaType returnType;
    private String servicePack;
    private String serviceImplPack;
    private String project;
    private String pojoUrl;

    private List<Method> methods;

    /**
     * 是否添加注解
     */
    private boolean enableAnnotation = true;

    private boolean enableUpdate = true;
    private boolean enableDelete = true;

    public CustomServicePlugin() {
        super();
        methods = new ArrayList<Method>();
    }

    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(properties
                .getProperty("enableAnnotation")))
            enableAnnotation = StringUtility.isTrue(properties
                    .getProperty("enableAnnotation"));

        String enabledUpdate = properties.getProperty("enabledUpdate");
        if (StringUtility.stringHasValue(enabledUpdate))
            this.enableUpdate = StringUtility.isTrue(enabledUpdate);

        String enabledDelete = properties.getProperty("enabledDelete");
        if (StringUtility.stringHasValue(enabledDelete))
            this.enableDelete = StringUtility.isTrue(enabledDelete);

        servicePack = properties.getProperty("targetPackage");
        serviceImplPack = properties.getProperty("implementationPackage");
        project = properties.getProperty("targetProject");

        pojoUrl = context.getJavaModelGeneratorConfiguration()
                .getTargetPackage();

        if (enableAnnotation) {
            autowired = new FullyQualifiedJavaType(
                    "org.springframework.beans.factory.annotation.Autowired");
            service = new FullyQualifiedJavaType(
                    "org.springframework.stereotype.Service");
            transactional = new FullyQualifiedJavaType(
                    "org.springframework.transaction.annotation.Transactional");
        }
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();

        String table = introspectedTable.getBaseRecordType();
        String tableName = table.replaceAll(this.pojoUrl + ".", "");
        interfaceType = new FullyQualifiedJavaType(servicePack + "."
                + tableName + "Service");

        // mybatis
        daoType = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());

        // logger.info(toLowerCase(daoType.getShortName()));
        serviceType = new FullyQualifiedJavaType(serviceImplPack + "."
                + tableName + "ServiceImpl");

        pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName);

        listType = new FullyQualifiedJavaType("java.util.List");
        Interface interface1 = new Interface(interfaceType);
        TopLevelClass topLevelClass = new TopLevelClass(serviceType);
        // 导入必要的类
        addImport(interface1, topLevelClass);

        // 接口
        addService(interface1, introspectedTable, tableName, files);
        // 实现类
        addServiceImpl(topLevelClass, introspectedTable, tableName, files);
        // addLogger(topLevelClass);

        return files;
    }

    /**
     * 添加接口
     *
     * @param tableName
     * @param files
     */
    protected void addService(Interface interface1,
                              IntrospectedTable introspectedTable, String tableName,
                              List<GeneratedJavaFile> files) {

        interface1.setVisibility(JavaVisibility.PUBLIC);
        StringBuffer sb = new StringBuffer();
        sb.append(this.IBUSINESSSERVICE);
        sb.append("<");
        sb.append(tableName);
        sb.append(">");
        interface1.addSuperInterface(new FullyQualifiedJavaType(sb.toString()));

        if (enableDelete) {
            Method method = this.deleteMethod(introspectedTable, tableName);
            method.removeAllBodyLines();
            interface1.addMethod(method);
            method = this.deletesMethod(introspectedTable, tableName);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }

        if (enableUpdate) {
            Method method = this.generateSaveMethod(introspectedTable,
                    tableName);
            method.removeAllBodyLines();
            interface1.addMethod(method);
        }

        GeneratedJavaFile file = new GeneratedJavaFile(interface1, project,
                context.getJavaFormatter());
        files.add(file);
    }

    /**
     * 添加实现类
     *
     * @param introspectedTable
     * @param tableName
     * @param files
     */

    protected void addServiceImpl(TopLevelClass topLevelClass,
                                  IntrospectedTable introspectedTable, String tableName,
                                  List<GeneratedJavaFile> files) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.ABSTRACTBUSINESSSERVICE);
        sb.append("<");
        sb.append(tableName);
        sb.append(">");
        topLevelClass.setSuperClass(sb.toString());

        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 设置实现的接口
        topLevelClass.addSuperInterface(interfaceType);

        if (enableAnnotation) {
            topLevelClass.addAnnotation("@Service");
            topLevelClass.addImportedType(service);
            topLevelClass.addAnnotation("@Transactional");
            topLevelClass.addImportedType(this.transactional);
        }
        // 添加引用dao
        addField(topLevelClass, tableName);
        // 添加方法
        this.addSuperMethod(topLevelClass);
        if (enableDelete) {
            Method method = this.deleteMethod(introspectedTable, tableName);
            topLevelClass.addMethod(method);
            method = this.deletesMethod(introspectedTable, tableName);
            topLevelClass.addMethod(method);
        }

        if (enableUpdate) {
            Method method = this.generateSaveMethod(introspectedTable,
                    tableName);
            topLevelClass.addMethod(method);
        }
        // 生成文件
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project,
                context.getJavaFormatter());
        files.add(file);
    }

    /**
     * 添加字段
     *
     * @param topLevelClass
     */
    protected void addField(TopLevelClass topLevelClass, String tableName) {
        // 添加 dao
        Field field = new Field();
        field.setName(toLowerCase(daoType.getShortName())); // 设置变量名
        topLevelClass.addImportedType(daoType);
        field.setType(daoType); // 类型
        field.setVisibility(JavaVisibility.PRIVATE);
        if (enableAnnotation) {
            field.addAnnotation("@Autowired");
        }
        topLevelClass.addField(field);
    }

    /**
     * 根据ID 删除记录的方法。删除仅支持针对ID,删除，复合主键删除不支持
     *
     * @param introspectedTable
     * @param tableName
     * @return
     */
    private Method deleteMethod(IntrospectedTable introspectedTable,
                                String tableName) {
        Method method = new Method();
        method.setName("delete");

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        List<IntrospectedColumn> pcolumns = introspectedTable
                .getPrimaryKeyColumns();

        // 单主键
        if (pcolumns.size() == 1) {
            IntrospectedColumn column = pcolumns.get(0);
            String columnType = column.getJdbcTypeName();
            if ("varchar".equals(columnType.toLowerCase())) {
                method.addParameter(new Parameter(new FullyQualifiedJavaType(
                        "java.lang.String"), "recordId"));
            } else {
                method.addParameter(new Parameter(new FullyQualifiedJavaType(
                        "java.lang.Integer"), "recordId"));
            }
        }else if (pcolumns.size() >1){
            method.addParameter(new Parameter(new FullyQualifiedJavaType("Map<String,String>"), "recordId"));
        }

        method.setVisibility(JavaVisibility.PUBLIC);
        StringBuilder sb = new StringBuilder();
        sb.append("int rows = this.");
        sb.append(getDaoShort());
        sb.append("deleteByPrimaryKey");
        sb.append("(");
        sb.append("recordId");
        sb.append(");");
        method.addBodyLine(sb.toString());
        method.addBodyLine("this.logger.debug(\"rows: {}\",rows);");
        method.addBodyLine("return rows;");
        return method;
    }

    /**
     * 根据 参数条件删除记录的方法 删除仅支持针对ID
     *
     * @param introspectedTable
     * @param tableName
     * @return
     */
    private Method deletesMethod(IntrospectedTable introspectedTable,
                                 String tableName) {
        Method method = new Method();
        method.setName("delete");

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        // 单主键
        List<IntrospectedColumn> pcolumns = introspectedTable
                .getPrimaryKeyColumns();
        String paramType = "String";
        FullyQualifiedJavaType tmpType=null;
        if (pcolumns.size() ==1) {
            IntrospectedColumn column = pcolumns.get(0);
            String columnType = column.getJdbcTypeName();
            if ("varchar".equals(columnType.toLowerCase())) {
                tmpType = new FullyQualifiedJavaType("java.lang.String[]");
            } else {
                tmpType =new FullyQualifiedJavaType("java.lang.Integer[]");
                paramType = "Integer";
            }
        }else if (pcolumns.size() >1){
            tmpType =new FullyQualifiedJavaType("Map<String,String>[]");

            paramType = "Map<String,String>";
        }
        method.addParameter(new Parameter(tmpType, "recordIds"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("int rows=0;");
        method.addBodyLine("for (" + paramType + " id : recordIds){");
        StringBuilder sb = new StringBuilder();
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append("rows = rows + this.");
        sb.append(this.getDaoShort());
        sb.append("deleteByPrimaryKey(id);");
        sb.append("}");
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        method.addBodyLine("this.logger.debug(\"rows: {}\",rows);");
        method.addBodyLine("return rows;");
        return method;
    }

    private Method generateSaveMethod(IntrospectedTable introspectedTable,
                                      String tableName) {
        Method method = new Method();
        method.setName("save");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        FullyQualifiedJavaType modelClass = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());
        method.addParameter(new Parameter(modelClass, "record"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("int rows=0;");
        method.addBodyLine("String uuid = UUIDUtil.getUUID();");
        StringBuilder sb = new StringBuilder();
        sb.append("if (record.isNew()) {");
        method.addBodyLine(sb.toString());
        List<IntrospectedColumn> pcolumns = introspectedTable
                .getPrimaryKeyColumns();

        pcolumns.forEach(keyColumn->{
            sb.setLength(0);
            String columnType = keyColumn.getJdbcTypeName();
            String columnName = keyColumn.getJavaProperty();
            sb.append("if (!StringUtils.hasLength(record.get"+ StringUtils.capitalize(columnName)+"()))  ");
            sb.append("record.set"+StringUtils.capitalize(columnName));
            if ("varchar".equals(columnType.toLowerCase()))
                sb.append("(uuid);");
            else
                sb.append("(0);");
            method.addBodyLine(sb.toString());
        });

        sb.setLength(0);
        sb.append("rows = this.");
        sb.append(this.getDaoShort());
        sb.append("insert(record);");
        method.addBodyLine(sb.toString());
        method.addBodyLine("} else {");
        sb.setLength(0);
        sb.append("rows = this.");
        sb.append(this.getDaoShort());
        sb.append("updateByPrimaryKey(record);");
        method.addBodyLine(sb.toString());
        method.addBodyLine("}");
        method.addBodyLine("this.logger.debug(\"rows: {}\",rows);");
        method.addBodyLine("return rows;");
        return method;
    }

    /**
     * type 的意义 pojo 1 key 2 example 3 pojo+example 4
     */
    private String addParams(IntrospectedTable introspectedTable,
                             Method method, int type1) {
        switch (type1) {
            case 1:
                method.addParameter(new Parameter(pojoType, "record"));
                return "record";
            case 2:
                if (introspectedTable.getRules().generatePrimaryKeyClass()) {
                    FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                            introspectedTable.getPrimaryKeyType());
                    method.addParameter(new Parameter(type, "key"));
                } else {
                    for (IntrospectedColumn introspectedColumn : introspectedTable
                            .getPrimaryKeyColumns()) {
                        FullyQualifiedJavaType type = introspectedColumn
                                .getFullyQualifiedJavaType();
                        method.addParameter(new Parameter(type, introspectedColumn
                                .getJavaProperty()));
                    }
                }
                StringBuffer sb = new StringBuffer();
                for (IntrospectedColumn introspectedColumn : introspectedTable
                        .getPrimaryKeyColumns()) {
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                return sb.toString();
            case 3:
                // method.addParameter(new Parameter(pojoCriteriaType, "example"));
                return "example";
            case 4:

                method.addParameter(0, new Parameter(pojoType, "record"));
                // method.addParameter(1, new Parameter(pojoCriteriaType,
                // "example"));
                return "record, example";
            default:
                break;
        }
        return null;
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

    private void addSuperMethod(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("getDao");
        method.setReturnType(new FullyQualifiedJavaType("BaseDao"));
        StringBuffer sb = new StringBuffer();
        sb.append("return this.");
        sb.append(toLowerCase(daoType.getShortName()));
        sb.append(";");
        method.addBodyLine(sb.toString());

        topLevelClass.addMethod(method);
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
    private void addImport(Interface interfaces, TopLevelClass topLevelClass) {
        interfaces.addImportedType(pojoType);

        interfaces.addImportedType(listType);
        interfaces.addImportedType(new FullyQualifiedJavaType(
                this.IBUSINESSSERVICE));
        interfaces.addImportedType(new FullyQualifiedJavaType(
                "java.util.HashMap"));
        interfaces.addImportedType(new FullyQualifiedJavaType(
                "java.util.Map"));

        topLevelClass.addImportedType(daoType);
        topLevelClass.addImportedType(this.QUERYMODEL);
        topLevelClass.addImportedType(new FullyQualifiedJavaType(
                this.ABSTRACTBUSINESSSERVICE));
        topLevelClass
                .addImportedType(new FullyQualifiedJavaType(this.UUIDUTIL));

        topLevelClass.addImportedType(new FullyQualifiedJavaType(this.BASEDAO));
        topLevelClass.addImportedType(interfaceType);

        topLevelClass.addImportedType(pojoType);
        topLevelClass.addImportedType(listType);
        if (enableAnnotation) {
            topLevelClass.addImportedType(service);
            topLevelClass.addImportedType(autowired);
        }
        topLevelClass.addImportedType("java.util.HashMap");
        topLevelClass.addImportedType("java.util.Map");
        topLevelClass.addImportedType("org.springframework.util.StringUtils");
    }

    private String getDaoShort() {
        return toLowerCase(daoType.getShortName()) + ".";
    }

}
