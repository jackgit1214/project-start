package org.mybatis.generator.myplugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.*;

import org.springframework.stereotype.Repository;

public class CustomMapperDaoPlugin extends PluginAdapter {

    private FullyQualifiedJavaType modelClass;
    private FullyQualifiedJavaType mapper;
    private FullyQualifiedJavaType repository;

    private List<String> interfaces = new ArrayList<String>();

    private final String pageDaoInterfaces = "com.project.core.mybatis.dao.Base.IDataMapperByPage";
    private final String daoCRUDInterfaces = "com.project.core.mybatis.dao.Base.IDataMapperCRUD";
    private final String clubDaoInterfaces = "com.project.core.mybatis.dao.Base.IDataMapperWithBlob";

    protected enum CustomMethodAttribute {
        SELECTBYCONDITION, DELETEBYCONDITION, COUNTBYCONDITION, UPDATEBYCONDITIONSELECTIVE, UPDATEBYCONDITION
        //,selectByConditionWithBLOBs,updateByConditionWithBLOBs,updateByPrimaryKeyWithBLOBs,selectBlobByPrimaryKey
        , SELECTBYCONDITIONWITHBLOBS, UPDATEBYCONDITIONWITHBLOBS, UPDATEBYPRIMARYKEYWITHBLOBS, SELECTBLOBBYPRIMARYKEY
    }

    private Map<CustomMethodAttribute, String> internalAttributes;

    public CustomMapperDaoPlugin() {
        super();
        this.internalAttributes = new HashMap<CustomMethodAttribute, String>();
        internalAttributes.put(CustomMethodAttribute.SELECTBYCONDITION, "selectByCondition");
        internalAttributes.put(CustomMethodAttribute.DELETEBYCONDITION, "deleteByCondition");
        internalAttributes.put(CustomMethodAttribute.COUNTBYCONDITION, "countByCondition");
        internalAttributes.put(CustomMethodAttribute.UPDATEBYCONDITIONSELECTIVE, "updateByConditionSelective");
        internalAttributes.put(CustomMethodAttribute.UPDATEBYCONDITION, "updateByCondition");
        internalAttributes.put(CustomMethodAttribute.SELECTBYCONDITIONWITHBLOBS, "selectByConditionWithBLOBs");
        internalAttributes.put(CustomMethodAttribute.UPDATEBYCONDITIONWITHBLOBS, "updateByConditionWithBLOBs");
        internalAttributes.put(CustomMethodAttribute.UPDATEBYPRIMARYKEYWITHBLOBS, "updateByPrimaryKeyWithBLOBs");
        internalAttributes.put(CustomMethodAttribute.SELECTBLOBBYPRIMARYKEY, "selectBlobByPrimaryKey");

    }

    @Override
    public boolean validate(List<String> warnings) {
        // TODO Auto-generated method stub
        mapper = new FullyQualifiedJavaType(
                "org.apache.ibatis.annotations.Mapper");
        repository = new FullyQualifiedJavaType(
                "org.springframework.stereotype.Repository");
        return true;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {

        this.addClassInterface(introspectedTable, interfaze);
        interfaze.addImportedType(mapper);
        interfaze.addAnnotation("@Mapper");
        interfaze.addImportedType(repository);
        interfaze.addAnnotation("@Repository");

        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.modelClass = topLevelClass.getType();

        return true;
    }

    private void addClassInterface(IntrospectedTable introspectedTable, Interface interfaze) {

        List<IntrospectedColumn> blobColumns = introspectedTable.getBLOBColumns();

        this.interfaces.add(this.pageDaoInterfaces);
        this.interfaces.add(this.daoCRUDInterfaces);
        if (introspectedTable.hasBLOBColumns())
            this.interfaces.add(this.clubDaoInterfaces);

        for (String inerf : interfaces) {

            FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(
                    inerf + "<" + modelClass.getShortName() + ">");

            FullyQualifiedJavaType shortSuper = new FullyQualifiedJavaType(
                    superInterface.getShortName());
            // List<String> imports = superInterface.getImportList();
            interfaze.addImportedType(superInterface);
            interfaze.addSuperInterface(shortSuper);
            //interfaze.addSuperInterface(superInterface.);
            interfaze.addImportedType(modelClass);
        }

    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze,
                                                                    IntrospectedTable introspectedTable) {

        List<IntrospectedColumn> blobColumns = introspectedTable.getBLOBColumns();

        if (blobColumns.size() > 0)
            return false;
        // TODO Auto-generated method stub
        return super.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
                                                        IntrospectedTable introspectedTable) {
        // TODO Auto-generated method stub
        // return super.clientInsertSelectiveMethodGenerated(method, interfaze,
        // introspectedTable);
        return false;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
                                                           IntrospectedTable introspectedTable) {
        // TODO Auto-generated method stub
        // return super.clientDeleteByPrimaryKeyMethodGenerated(method,
        // interfaze,
        // introspectedTable);
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
                                               IntrospectedTable introspectedTable) {
        // TODO Auto-generated method stub
        // return super.clientInsertMethodGenerated(method, interfaze,
        // introspectedTable);
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
                                                           IntrospectedTable introspectedTable) {
        // TODO Auto-generated method stub
        // return super.clientSelectByPrimaryKeyMethodGenerated(method,
        // interfaze,
        // introspectedTable);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze,
                                                                    IntrospectedTable introspectedTable) {
        // TODO Auto-generated method stub
        // return super.clientUpdateByPrimaryKeySelectiveMethodGenerated(method,
        // interfaze, introspectedTable);

        return false;
    }

    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
                                                                       IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        // XmlElement element = new XmlElement("!-- public method --"); //$NON-NLS-1$
        // context.getCommentGenerator().addComment(element);
        //
        // document.getRootElement().addElement(element);

        this.addSelectByCondition(document, introspectedTable);
        this.addDeleteByCondition(document, introspectedTable);
        this.addCountByCondition(document, introspectedTable);
        this.addUpdateByConditionSelective(document, introspectedTable);
        this.addUpdateByCondition(document, introspectedTable);
        if (introspectedTable.hasBLOBColumns()) {
            this.addSelectByConditionWithBLOBs(document, introspectedTable);
        }

        return true;
    }

    //,selectByConditionWithBLOBs,updateByConditionWithBLOBs,updateByPrimaryKeyWithBLOBs,selectBlobByPrimaryKey

    /**
     * 添加 blob字段的condition，2019-09-21
     *
     * @param document
     * @param introspectedTable
     */
    private void addSelectByConditionWithBLOBs(Document document, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                internalAttributes.get(CustomMethodAttribute.SELECTBYCONDITIONWITHBLOBS)));

        answer.addAttribute(new Attribute("resultMap", introspectedTable.getResultMapWithBLOBsId())); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select")); //$NON-NLS-1$
        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement
                .addAttribute(new Attribute("test", "queryModel != null and queryModel.distinct")); //$NON-NLS-1$ //$NON-NLS-2$
        ifElement.addElement(new TextElement("distinct")); //$NON-NLS-1$
        answer.addElement(ifElement);

        StringBuilder sb = new StringBuilder();

        XmlElement includeEle = new XmlElement("include"); //$NON-NLS-1$
        includeEle.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getBaseColumnListId()));
        answer.addElement(includeEle);

        XmlElement includeEleBlob = new XmlElement("include"); //$NON-NLS-1$
        includeEleBlob.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getBlobColumnListId()));
        answer.addElement(new TextElement(",")); //$NON-NLS-1$
        answer.addElement(includeEleBlob);
        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        answer.addElement(this.generateWhereElement());

        ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement
                .addAttribute(new Attribute("test", "queryModel != null and queryModel.orderByClause != null")); //$NON-NLS-1$ //$NON-NLS-2$
        ifElement.addElement(new TextElement("order by ${queryModel.orderByClause}")); //$NON-NLS-1$
        answer.addElement(ifElement);

        document.getRootElement().addElement(answer);
    }


    /**
     * 添加UpdateByConditionWithBLOBs,更新时，可以一起更新blob字段
     *
     * @param document
     * @param introspectedTable
     */
    private void addUpdateByConditionWithBLOBs(Document document, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", internalAttributes
                .get(CustomMethodAttribute.UPDATEBYCONDITIONWITHBLOBS))); //$NON-NLS-1$

        // answer.addAttribute(new Attribute("parameterType", "map")); //$NON-NLS-1$
        // //$NON-NLS-2$
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        // Iterator<IntrospectedColumn> iter = ListUtilities
        // .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())
        // .iterator();

        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();

        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")); //$NON-NLS-1$

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        answer.addElement(this.generateUpdateWhereElement(document, introspectedTable));

        document.getRootElement().addElement(answer);
    }

    /**
     * 添加selectByCondition
     *
     * @param document
     * @param introspectedTable
     */
    private void addSelectByCondition(Document document, IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                internalAttributes.get(CustomMethodAttribute.SELECTBYCONDITION)));

        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId())); //$NON-NLS-1$
        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select")); //$NON-NLS-1$
        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement
                .addAttribute(new Attribute("test", "queryModel != null and queryModel.distinct")); //$NON-NLS-1$ //$NON-NLS-2$
        ifElement.addElement(new TextElement("distinct")); //$NON-NLS-1$
        answer.addElement(ifElement);

        StringBuilder sb = new StringBuilder();

        XmlElement includeEle = new XmlElement("include"); //$NON-NLS-1$
        includeEle.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getBaseColumnListId()));

        answer.addElement(includeEle);

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        answer.addElement(this.generateWhereElement());

        ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement
                .addAttribute(new Attribute("test", "queryModel != null and queryModel.orderByClause != null")); //$NON-NLS-1$ //$NON-NLS-2$
        ifElement.addElement(new TextElement("order by ${queryModel.orderByClause}")); //$NON-NLS-1$
        answer.addElement(ifElement);

        document.getRootElement().addElement(answer);
    }

    /**
     * 添加addDeleteByCondition
     *
     * @param document
     * @param introspectedTable
     */
    private void addDeleteByCondition(Document document, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                internalAttributes.get(CustomMethodAttribute.DELETEBYCONDITION)));

        context.getCommentGenerator().addComment(answer);

        answer.addElement(
                new TextElement("delete from " + introspectedTable
                        .getAliasedFullyQualifiedTableNameAtRuntime())); // $NON-NLS-1$

        answer.addElement(this.generateWhereElement());

        document.getRootElement().addElement(answer);
    }

    /**
     * 添加addCountByCondition
     *
     * @param document
     * @param introspectedTable
     */
    private void addCountByCondition(Document document, IntrospectedTable introspectedTable) {

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", //$NON-NLS-1$
                internalAttributes.get(CustomMethodAttribute.COUNTBYCONDITION)));
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());

        answer.addElement(new TextElement(sb.toString())); // $NON-NLS-1$

        answer.addElement(this.generateWhereElement());

        document.getRootElement().addElement(answer);
    }

    /**
     * 添加addUpdateByConditionSelective
     *
     * @param document
     * @param introspectedTable
     */
    private void addUpdateByConditionSelective(Document document, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(
                new Attribute("id", internalAttributes
                        .get(CustomMethodAttribute.UPDATEBYCONDITIONSELECTIVE))); //$NON-NLS-1$

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        answer.addElement(dynamicElement);
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        // List<IntrospectedColumn> columns = ListUtilities
        // .removeGeneratedAlwaysColumns(introspectedColumns);

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {

            //
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("record.")); //$NON-NLS-1$
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")); //$NON-NLS-1$
            sb.append(',');

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        answer.addElement(this.generateUpdateWhereElement(document, introspectedTable));

        document.getRootElement().addElement(answer);
    }

    /**
     * 添加addupdateByCondition
     *
     * @param document
     * @param introspectedTable
     */
    private void addUpdateByCondition(Document document, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", internalAttributes
                .get(CustomMethodAttribute.UPDATEBYCONDITION))); //$NON-NLS-1$

        // answer.addAttribute(new Attribute("parameterType", "map")); //$NON-NLS-1$
        // //$NON-NLS-2$
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        // Iterator<IntrospectedColumn> iter = ListUtilities
        // .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())
        // .iterator();

        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();

        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.")); //$NON-NLS-1$

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        answer.addElement(this.generateUpdateWhereElement(document, introspectedTable));

        document.getRootElement().addElement(answer);
    }

    private XmlElement generateWhereElement() {
        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement.addAttribute(new Attribute("test", "queryModel != null")); //$NON-NLS-1$ //$NON-NLS-2$
        XmlElement includeEle = new XmlElement("include"); //$NON-NLS-1$
        includeEle.addAttribute(new Attribute("refid", //$NON-NLS-1$
                "public.Where_Clause"));
        ifElement.addElement(includeEle); // $NON-NLS-1$

        return ifElement;
    }

    private XmlElement generateUpdateWhereElement(Document document, IntrospectedTable introspectedTable) {
        XmlElement ifElement = new XmlElement("if"); //$NON-NLS-1$
        ifElement.addAttribute(new Attribute("test", "queryModel != null")); //$NON-NLS-1$ //$NON-NLS-2$

        XmlElement includeElement = new XmlElement("include"); //$NON-NLS-1$
        includeElement.addAttribute(new Attribute("refid", //$NON-NLS-1$
                "public.Update_By_Where_Clause"));
        ifElement.addElement(includeElement);

        return ifElement;
    }

}
