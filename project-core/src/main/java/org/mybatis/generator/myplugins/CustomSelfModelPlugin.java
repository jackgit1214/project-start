package org.mybatis.generator.myplugins;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.springframework.util.StringUtils;

public class CustomSelfModelPlugin extends PluginAdapter {

    // private String implements;

    @Override
    public boolean validate(List<String> warnings) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {

        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.Builder");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");
        topLevelClass.addImportedType("lombok.EqualsAndHashCode");
        topLevelClass.addImportedType("lombok.ToString");
        topLevelClass.addImportedType("com.fasterxml.jackson.annotation.JsonIgnoreProperties");

        //添加domain的注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper = true)");
        topLevelClass.addAnnotation("@JsonIgnoreProperties(value = {\"handler\"})");
        topLevelClass.addAnnotation("@ToString");


        //添加domain的注释
//        topLevelClass.addJavaDocLine("/**");
//        topLevelClass.addJavaDocLine("* Created by Mybatis Generator on " + date2Str(new Date()));
//        topLevelClass.addJavaDocLine("*/");

        addSuperClassMethod(introspectedTable, topLevelClass);
        return true;
    }
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }


    //
    // @Override
    // public void initialized(IntrospectedTable introspectedTable) {
    // String oldType = introspectedTable.getExampleType();
    //
    // introspectedTable.getBaseRecordType()
    //
    // introspectedTable.setExampleType(oldType);
    // }

    /*
     * @Override
     * public boolean modelPrimaryKeyClassGenerated(TopLevelClass
     * topLevelClass, IntrospectedTable introspectedTable) {
     * System.out.println("--------------------------------");
     * System.out.println(topLevelClass.getType()); //
     * introspectedTable.addPrimaryKeyColumn(columnName); return true; }
     */

    private void addSuperClassMethod(IntrospectedTable introspectedTable,
                                     TopLevelClass topLevelClass) {

        Method methodPrimaryKey = new Method();
        methodPrimaryKey.setVisibility(JavaVisibility.PUBLIC);
        methodPrimaryKey.setReturnType(FullyQualifiedJavaType
                .getObjectInstance());
        methodPrimaryKey.setName("getPrimaryKey"); //$NON-NLS-1$
        if (introspectedTable.isJava5Targeted()) {
            methodPrimaryKey.addAnnotation("@Override"); //$NON-NLS-1$
        }
        List<IntrospectedColumn> pcolumns = introspectedTable
                .getPrimaryKeyColumns();

        topLevelClass.getMethods();

        context.getCommentGenerator().addGeneralMethodComment(methodPrimaryKey,
                introspectedTable);

        if (pcolumns.size() > 0) {
            final StringBuffer codeLine = new StringBuffer();
            codeLine.append("return ");
            pcolumns.forEach(keyColumn -> {
                String tmpColumn = keyColumn.getJavaProperty();
                codeLine.append(" this.get" + StringUtils.capitalize(tmpColumn) + "() +");
            });
            int len = codeLine.toString().length() - 1;
            methodPrimaryKey.addBodyLine(codeLine.toString().substring(0, len) + ";");
        } else {
            methodPrimaryKey.addBodyLine("return null;");
        }
        topLevelClass.addMethod(methodPrimaryKey);
    }
}
