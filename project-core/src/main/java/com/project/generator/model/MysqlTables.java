package com.project.generator.model;

import com.project.core.mybatis.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

public class MysqlTables extends BaseModel implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_CATALOG
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String tableCatalog;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_SCHEMA
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String tableSchema;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_NAME
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String tableName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_TYPE
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String tableType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.ENGINE
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String engine;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.VERSION
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.ROW_FORMAT
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String rowFormat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_ROWS
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long tableRows;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.AVG_ROW_LENGTH
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long avgRowLength;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.DATA_LENGTH
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long dataLength;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.MAX_DATA_LENGTH
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long maxDataLength;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.INDEX_LENGTH
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long indexLength;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.DATA_FREE
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long dataFree;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.AUTO_INCREMENT
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long autoIncrement;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.CREATE_TIME
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.UPDATE_TIME
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.CHECK_TIME
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Date checkTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_COLLATION
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String tableCollation;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.CHECKSUM
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private Long checksum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.CREATE_OPTIONS
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String createOptions;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TABLES.TABLE_COMMENT
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private String tableComment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TABLES
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_CATALOG
     *
     * @return the value of TABLES.TABLE_CATALOG
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getTableCatalog() {
        return tableCatalog;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_CATALOG
     *
     * @param tableCatalog the value for TABLES.TABLE_CATALOG
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog == null ? null : tableCatalog.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_SCHEMA
     *
     * @return the value of TABLES.TABLE_SCHEMA
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getTableSchema() {
        return tableSchema;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_SCHEMA
     *
     * @param tableSchema the value for TABLES.TABLE_SCHEMA
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema == null ? null : tableSchema.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_NAME
     *
     * @return the value of TABLES.TABLE_NAME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_NAME
     *
     * @param tableName the value for TABLES.TABLE_NAME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_TYPE
     *
     * @return the value of TABLES.TABLE_TYPE
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getTableType() {
        return tableType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_TYPE
     *
     * @param tableType the value for TABLES.TABLE_TYPE
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableType(String tableType) {
        this.tableType = tableType == null ? null : tableType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.ENGINE
     *
     * @return the value of TABLES.ENGINE
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getEngine() {
        return engine;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.ENGINE
     *
     * @param engine the value for TABLES.ENGINE
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setEngine(String engine) {
        this.engine = engine == null ? null : engine.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.VERSION
     *
     * @return the value of TABLES.VERSION
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.VERSION
     *
     * @param version the value for TABLES.VERSION
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.ROW_FORMAT
     *
     * @return the value of TABLES.ROW_FORMAT
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getRowFormat() {
        return rowFormat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.ROW_FORMAT
     *
     * @param rowFormat the value for TABLES.ROW_FORMAT
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setRowFormat(String rowFormat) {
        this.rowFormat = rowFormat == null ? null : rowFormat.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_ROWS
     *
     * @return the value of TABLES.TABLE_ROWS
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getTableRows() {
        return tableRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_ROWS
     *
     * @param tableRows the value for TABLES.TABLE_ROWS
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableRows(Long tableRows) {
        this.tableRows = tableRows;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.AVG_ROW_LENGTH
     *
     * @return the value of TABLES.AVG_ROW_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getAvgRowLength() {
        return avgRowLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.AVG_ROW_LENGTH
     *
     * @param avgRowLength the value for TABLES.AVG_ROW_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setAvgRowLength(Long avgRowLength) {
        this.avgRowLength = avgRowLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.DATA_LENGTH
     *
     * @return the value of TABLES.DATA_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getDataLength() {
        return dataLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.DATA_LENGTH
     *
     * @param dataLength the value for TABLES.DATA_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.MAX_DATA_LENGTH
     *
     * @return the value of TABLES.MAX_DATA_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getMaxDataLength() {
        return maxDataLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.MAX_DATA_LENGTH
     *
     * @param maxDataLength the value for TABLES.MAX_DATA_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setMaxDataLength(Long maxDataLength) {
        this.maxDataLength = maxDataLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.INDEX_LENGTH
     *
     * @return the value of TABLES.INDEX_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getIndexLength() {
        return indexLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.INDEX_LENGTH
     *
     * @param indexLength the value for TABLES.INDEX_LENGTH
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setIndexLength(Long indexLength) {
        this.indexLength = indexLength;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.DATA_FREE
     *
     * @return the value of TABLES.DATA_FREE
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getDataFree() {
        return dataFree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.DATA_FREE
     *
     * @param dataFree the value for TABLES.DATA_FREE
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setDataFree(Long dataFree) {
        this.dataFree = dataFree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.AUTO_INCREMENT
     *
     * @return the value of TABLES.AUTO_INCREMENT
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getAutoIncrement() {
        return autoIncrement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.AUTO_INCREMENT
     *
     * @param autoIncrement the value for TABLES.AUTO_INCREMENT
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setAutoIncrement(Long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.CREATE_TIME
     *
     * @return the value of TABLES.CREATE_TIME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.CREATE_TIME
     *
     * @param createTime the value for TABLES.CREATE_TIME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.UPDATE_TIME
     *
     * @return the value of TABLES.UPDATE_TIME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.UPDATE_TIME
     *
     * @param updateTime the value for TABLES.UPDATE_TIME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.CHECK_TIME
     *
     * @return the value of TABLES.CHECK_TIME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Date getCheckTime() {
        return checkTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.CHECK_TIME
     *
     * @param checkTime the value for TABLES.CHECK_TIME
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_COLLATION
     *
     * @return the value of TABLES.TABLE_COLLATION
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getTableCollation() {
        return tableCollation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_COLLATION
     *
     * @param tableCollation the value for TABLES.TABLE_COLLATION
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableCollation(String tableCollation) {
        this.tableCollation = tableCollation == null ? null : tableCollation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.CHECKSUM
     *
     * @return the value of TABLES.CHECKSUM
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public Long getChecksum() {
        return checksum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.CHECKSUM
     *
     * @param checksum the value for TABLES.CHECKSUM
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.CREATE_OPTIONS
     *
     * @return the value of TABLES.CREATE_OPTIONS
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getCreateOptions() {
        return createOptions;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.CREATE_OPTIONS
     *
     * @param createOptions the value for TABLES.CREATE_OPTIONS
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setCreateOptions(String createOptions) {
        this.createOptions = createOptions == null ? null : createOptions.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TABLES.TABLE_COMMENT
     *
     * @return the value of TABLES.TABLE_COMMENT
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public String getTableComment() {
        return tableComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TABLES.TABLE_COMMENT
     *
     * @param tableComment the value for TABLES.TABLE_COMMENT
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    public void setTableComment(String tableComment) {
        this.tableComment = tableComment == null ? null : tableComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TABLES
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tableCatalog=").append(tableCatalog);
        sb.append(", tableSchema=").append(tableSchema);
        sb.append(", tableName=").append(tableName);
        sb.append(", tableType=").append(tableType);
        sb.append(", engine=").append(engine);
        sb.append(", version=").append(version);
        sb.append(", rowFormat=").append(rowFormat);
        sb.append(", tableRows=").append(tableRows);
        sb.append(", avgRowLength=").append(avgRowLength);
        sb.append(", dataLength=").append(dataLength);
        sb.append(", maxDataLength=").append(maxDataLength);
        sb.append(", indexLength=").append(indexLength);
        sb.append(", dataFree=").append(dataFree);
        sb.append(", autoIncrement=").append(autoIncrement);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", checkTime=").append(checkTime);
        sb.append(", tableCollation=").append(tableCollation);
        sb.append(", checksum=").append(checksum);
        sb.append(", createOptions=").append(createOptions);
        sb.append(", tableComment=").append(tableComment);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TABLES
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MysqlTables other = (MysqlTables) that;
        return (this.getTableCatalog() == null ? other.getTableCatalog() == null : this.getTableCatalog()
                .equals(other.getTableCatalog()))
                && (this.getTableSchema() == null ? other.getTableSchema() == null : this.getTableSchema()
                .equals(other.getTableSchema()))
                && (this.getTableName() == null ? other.getTableName() == null : this.getTableName()
                .equals(other.getTableName()))
                && (this.getTableType() == null ? other.getTableType() == null : this.getTableType()
                .equals(other.getTableType()))
                && (this.getEngine() == null ? other.getEngine() == null : this.getEngine().equals(other.getEngine()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion()
                .equals(other.getVersion()))
                && (this.getRowFormat() == null ? other.getRowFormat() == null : this.getRowFormat()
                .equals(other.getRowFormat()))
                && (this.getTableRows() == null ? other.getTableRows() == null : this.getTableRows()
                .equals(other.getTableRows()))
                && (this.getAvgRowLength() == null ? other.getAvgRowLength() == null : this.getAvgRowLength()
                .equals(other.getAvgRowLength()))
                && (this.getDataLength() == null ? other.getDataLength() == null : this.getDataLength()
                .equals(other.getDataLength()))
                && (this.getMaxDataLength() == null ? other.getMaxDataLength() == null : this.getMaxDataLength()
                .equals(other.getMaxDataLength()))
                && (this.getIndexLength() == null ? other.getIndexLength() == null : this.getIndexLength()
                .equals(other.getIndexLength()))
                && (this.getDataFree() == null ? other.getDataFree() == null : this.getDataFree()
                .equals(other.getDataFree()))
                && (this.getAutoIncrement() == null ? other.getAutoIncrement() == null : this.getAutoIncrement()
                .equals(other.getAutoIncrement()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime()
                .equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime()
                .equals(other.getUpdateTime()))
                && (this.getCheckTime() == null ? other.getCheckTime() == null : this.getCheckTime()
                .equals(other.getCheckTime()))
                && (this.getTableCollation() == null ? other.getTableCollation() == null : this.getTableCollation()
                .equals(other.getTableCollation()))
                && (this.getChecksum() == null ? other.getChecksum() == null : this.getChecksum()
                .equals(other.getChecksum()))
                && (this.getCreateOptions() == null ? other.getCreateOptions() == null : this.getCreateOptions()
                .equals(other.getCreateOptions()))
                && (this.getTableComment() == null ? other.getTableComment() == null : this.getTableComment()
                .equals(other.getTableComment()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TABLES
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTableCatalog() == null) ? 0 : getTableCatalog().hashCode());
        result = prime * result + ((getTableSchema() == null) ? 0 : getTableSchema().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getTableType() == null) ? 0 : getTableType().hashCode());
        result = prime * result + ((getEngine() == null) ? 0 : getEngine().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getRowFormat() == null) ? 0 : getRowFormat().hashCode());
        result = prime * result + ((getTableRows() == null) ? 0 : getTableRows().hashCode());
        result = prime * result + ((getAvgRowLength() == null) ? 0 : getAvgRowLength().hashCode());
        result = prime * result + ((getDataLength() == null) ? 0 : getDataLength().hashCode());
        result = prime * result + ((getMaxDataLength() == null) ? 0 : getMaxDataLength().hashCode());
        result = prime * result + ((getIndexLength() == null) ? 0 : getIndexLength().hashCode());
        result = prime * result + ((getDataFree() == null) ? 0 : getDataFree().hashCode());
        result = prime * result + ((getAutoIncrement() == null) ? 0 : getAutoIncrement().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCheckTime() == null) ? 0 : getCheckTime().hashCode());
        result = prime * result + ((getTableCollation() == null) ? 0 : getTableCollation().hashCode());
        result = prime * result + ((getChecksum() == null) ? 0 : getChecksum().hashCode());
        result = prime * result + ((getCreateOptions() == null) ? 0 : getCreateOptions().hashCode());
        result = prime * result + ((getTableComment() == null) ? 0 : getTableComment().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TABLES
     *
     * @mbg.generated Tue Mar 23 20:08:36 CST 2021
     */
    @Override
    public Object getPrimaryKey() {
        return null;
    }
}