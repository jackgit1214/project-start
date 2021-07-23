package org.mybatis.generator.myplugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

public class CustomCommentGenerator extends DefaultCommentGenerator {

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass,
                                     IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");
        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System
                    .getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" * " + remarkLine);
            }
        }

        addJavadocTag(topLevelClass, false);
        topLevelClass.addJavaDocLine(" */");
    }

    public void addFieldComment(Field field,
                                IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        field.addJavaDocLine("/**");
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System
                    .getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" * " + remarkLine);
            }
        }
        field.addJavaDocLine(" *");

        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        field.addJavaDocLine("/**");
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }

    protected void addJavadocTag(JavaElement javaElement,
                                 boolean markAsDoNotDelete) {
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$ }
            String s = getDateString();
            if (s != null) {
                sb.append(' ');
                sb.append(s);
            }
            javaElement.addJavaDocLine(sb.toString());
        }
    }

    protected String getDateString() {
        return "";
        // return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
