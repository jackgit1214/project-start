package com.project.core.mybatis.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//public class QueryModel extends PropertyChangeSupportUtil {
public class QueryModel {
    protected String orderByClause;

    protected boolean distinct;

    public Map<String, String> param;

    public Map<String, String> param_condition;

    protected transient List<Criteria> oredCriteria;

    public QueryModel() {
        oredCriteria = new ArrayList<Criteria>();

    }

    public QueryModel(Map<String, String> param) {
        super();
        oredCriteria = new ArrayList<Criteria>();
        this.param = param;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public String condition;

    @SuppressWarnings("unchecked")
    public String getCondition() {

        StringBuffer tmpcon = new StringBuffer("where ");
        List<Criteria> criterias = this.oredCriteria;
        if (!criterias.isEmpty()) {
            for (Criteria criteria : criterias) {
                if (criteria.isValid()) {
                    tmpcon.append(" (");
                    for (Criterion tmpC : criteria.criteria) {
                        if (tmpC.noValue) {
                            tmpcon.append(" and " + tmpC.condition);
                        }
                        if (tmpC.isSingleValue()) {
                            tmpcon.append(" and " + tmpC.condition + " "
                                    + tmpC.value);
                        }
                        if (tmpC.isBetweenValue()) {
                            tmpcon.append(" and " + tmpC.condition + " "
                                    + tmpC.value + " and " + tmpC.secondValue);
                        }
                        if (tmpC.isListValue()) {
                            tmpcon.append(" and " + tmpC.condition + "(");
                            List<String> values = (List<String>) tmpC.value;
                            for (String value : values) {
                                tmpcon.append("'" + value + "',");
                            }
                            tmpcon.append(")");
                        }
                    }
                    tmpcon.append(")");
                }
            }
        }

        return tmpcon.toString();
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value,
                                    String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property
                        + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        /**
         * 针对in与not in条件，自定义，in后的sql语句
         *
         * @param condition
         * @param value
         * @param property
         * @param isSql     是否是sql语句
         */

        protected void addCriterion(String condition, String value,
                                    String property, boolean isSql) {

            if (!isSql)
                this.addCriterion(condition, value, property);
            else {
                if (value == null) {
                    throw new RuntimeException("Value for " + property
                            + " cannot be null");
                }
                criteria.add(new Criterion(condition, value, property, isSql));
            }

        }

        protected void addCriterion(String condition, Object value1,
                                    Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property
                        + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIsNull(String fieldName) {
            addCriterion(fieldName + " is null");
            return (Criteria) this;
        }

        public Criteria andIsNotNull(String fieldName) {
            addCriterion(fieldName + " is not null");
            return (Criteria) this;
        }

        public Criteria andEqualTo(String fieldName, Object value) {
            addCriterion(fieldName + " =", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andNotEqualTo(String fieldName, Object value) {
            addCriterion(fieldName + " <>", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andGreaterThan(String fieldName, Object value) {
            addCriterion(fieldName + " >", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andGreaterThanOrEqualTo(String fieldName, Object value) {
            addCriterion(fieldName + " >=", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andLessThan(String fieldName, Object value) {
            addCriterion(fieldName + " <", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andLessThanOrEqualTo(String fieldName, Object value) {
            addCriterion(fieldName + " <=", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andLike(String fieldName, Object value) {
            addCriterion(fieldName + " like", value, "id");
            return (Criteria) this;
        }

        public Criteria andNotLike(String fieldName, Object value) {
            addCriterion(fieldName + " not like", value, fieldName);
            return (Criteria) this;
        }

        public Criteria andIn(String fieldName, List<Object> values) {
            addCriterion(fieldName + " in", values, fieldName);
            return (Criteria) this;
        }

        public Criteria andNotIn(String fieldName, String sql) {
            addCriterion(fieldName + " not in", sql, fieldName, true);
            return (Criteria) this;
        }

        public Criteria andIn(String fieldName, String sql) {
            addCriterion(fieldName + " in", sql, fieldName, true);
            return (Criteria) this;
        }

        public Criteria andNotIn(String fieldName, List<Object> values) {
            addCriterion(fieldName + " not in", values, fieldName);
            return (Criteria) this;
        }

        public Criteria andIdBetween(String fieldName, Object value1,
                                     Object value2) {
            addCriterion(fieldName + " between", value1, value2, fieldName);
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String fieldName, Object value1,
                                        Object value2) {
            addCriterion(fieldName + " not between", value1, value2, fieldName);
            return (Criteria) this;
        }

    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean sqlValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        public boolean isSqlValue() {
            return sqlValue;
        }

        public void setSqlValue(boolean sqlValue) {
            this.sqlValue = sqlValue;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value, String typeHandler,
                            boolean isSql) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            this.setSqlValue(true);

        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue,
                            String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

    }

    public Map<String, String> getParam() {
        return param;
    }

    /**
     * 只作为like参数
     *
     * @param param
     */
    public void setParam(Map<String, String> param) {

        this.param = param;
        // this.reInitCriteria();
        // firePropertyChange("param", null, param);
    }

    public void reInitCriteria() {

        if (param == null)
            return;
        Criteria criteria = this.createCriteria();
        Iterator<Map.Entry<String, String>> iter = param.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter
                    .next();
            String key = entry.getKey();
            String val = entry.getValue();
            if (val != null && !"".equals(val)) {// 空值不作处理
                int pos = val.indexOf("/=/"); //包含这个符号则为等于，否则like
                if (pos < 0)
                    criteria.andLike(key, "%" + val + "%");
                else
                    criteria.andEqualTo(key, val.substring(0, pos));
            }
        }
    }

}
