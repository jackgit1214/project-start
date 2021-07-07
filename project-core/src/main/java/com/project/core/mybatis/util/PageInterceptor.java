package com.project.core.mybatis.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
@Slf4j
public class PageInterceptor implements Interceptor {

    private int defaultPageSize;
    //private PageResult page = new PageResult(0, this.defaultPageSize);

    public Object intercept(Invocation invocation) throws Throwable {
        PageResult page = new PageResult(0, this.defaultPageSize);
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation
                    .getTarget();
            MetaObject metaStatementHandler = SystemMetaObject
                    .forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
            // 可以分离出最原始的的目标类)
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }

//			RowBounds rowBounds = (RowBounds) metaStatementHandler
//					.getValue("delegate.rowBounds");
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
                    .getValue("delegate.mappedStatement");
            BoundSql boundSql = (BoundSql) metaStatementHandler
                    .getValue("delegate.boundSql");

            DefaultParameterHandler defaultParameterHandler = (DefaultParameterHandler) metaStatementHandler
                    .getValue("delegate.parameterHandler");
            Object object = defaultParameterHandler.getParameterObject();
            String sql = boundSql.getSql();

            if (object instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) object;
                if (paramMap.containsKey("page")) {
                    page = (PageResult) paramMap.get("page");
                } else {
                    page = null;
                    return invocation.proceed();
                }
            } else {
                page = null;
                return invocation.proceed();
            }
            // 分页参数作为参数对象parameterObject的一个属性
            Connection connection = (Connection) invocation.getArgs()[0];
            // 重设分页参数里的总页数等
            int pos = sql.indexOf(" limit ");
            //sql语句中存在limit时默认为
            String pageSql = "";
            if (pos < 0 && page != null) {
                setPageParameter(sql, connection, mappedStatement, boundSql, page);
                // 重写sql
                pageSql = buildPageSql(sql, page);
                // 重写分页sql
                metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
            }

            Object result = invocation.proceed();
            return result;
        } else if (invocation.getTarget() instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            List<?> datas = (ArrayList<?>) result;
            if (page != null)
                page.setPageDatas(datas);
            return result;
        }
        return null;
    }

    public Object plugin(Object target) {
        if (target instanceof StatementHandler
                || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    public void setProperties(Properties properties) {
        log.debug(properties.toString());
    }

    /**
     * 修改原SQL为分页SQL
     *
     * @param sql
     * @param page
     * @return
     */
    private String buildPageSql(String sql, PageResult page) {

        StringBuilder pageSql = new StringBuilder(200);
        int pos = sql.indexOf(" limit ");
        if (pos > 0) {
            pageSql.append(sql, 0, pos)
                    .append(" limit ")
                    .append(page.getStartRow()).append(",")
                    .append(page.getPageSize());

            String sqlAfter = sql.substring(pos + 7);
            int paramPos = sqlAfter.indexOf("?");
            if (paramPos >= 0) {
                paramPos = sqlAfter.indexOf("?", paramPos + 1);
            }
            pageSql.append(sqlAfter.substring(paramPos + 1));
        } else {
            pageSql.append(sql);
            pageSql.append(" limit ").append(page.getStartRow()).append(",")
                    .append(page.getPageSize());
        }
        return pageSql.toString();
    }

    /**
     * 获取总记录数
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private void setPageParameter(String sql, Connection connection,
                                  MappedStatement mappedStatement, BoundSql boundSql, PageResult page) {
        // 记录总记录数
        String countSql = "select count(0) from (" + sql + ") aliasPageTable";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),
                    countSql, boundSql.getParameterMappings(),
                    boundSql.getParameterObject());

            //modifiy by lilj 2020.4.14 去除了
//			MetaObject countBsObject = SystemMetaObject.forObject(countBS);
//			MetaObject boundSqlObject = SystemMetaObject.forObject(boundSql);
//			countBsObject.setValue("metaParameters",
//					boundSqlObject.getValue("metaParameters"));
            //针对附加参数进行处理，将原SQL中条件参数复制到新的计算行数的sql中
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            parameterMappings.forEach(param -> {
                String paramName = param.getProperty();
                if (boundSql.hasAdditionalParameter(paramName)) {
                    countBS.setAdditionalParameter(paramName, boundSql.getAdditionalParameter(paramName));
                }
            });
            ParameterHandler parameterHandler = new DefaultParameterHandler(
                    mappedStatement, boundSql.getParameterObject(), countBS);
            parameterHandler.setParameters(countStmt);
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setTotalSize(totalCount);
            int totalPage = totalCount / page.getPageSize()
                    + ((totalCount % page.getPageSize() == 0) ? 0 : 1);
            page.setLastPage(totalPage);
            page.caculatePage(page.getCurPage());
        } catch (SQLException e) {
            log.error("Ignore this exception", e);
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("Ignore this exception", e);
            }
            try {
                countStmt.close();
            } catch (SQLException e) {
                log.error("Ignore this exception", e);
            }
        }
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

}
