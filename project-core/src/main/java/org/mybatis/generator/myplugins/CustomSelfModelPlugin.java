package org.mybatis.generator.myplugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

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

        // List<GeneratedJavaFile> generatedJavaFiles = introspectedTable
        // .getGeneratedJavaFiles();

        addSuperClassMethod(introspectedTable, topLevelClass);
        return true;
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
                List<Method> methods = topLevelClass.getMethods();

                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase("get" + tmpColumn))
                        codeLine.append(" this." + method.getName() + "() +");

                }
            });
            int len = codeLine.toString().length() - 1;
            methodPrimaryKey.addBodyLine(codeLine.toString().substring(0, len) + ";");
        } else {
            methodPrimaryKey.addBodyLine("return null;");
        }
        topLevelClass.addMethod(methodPrimaryKey);
    }
}
