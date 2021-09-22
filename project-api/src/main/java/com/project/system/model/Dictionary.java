package com.project.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.core.mybatis.model.BaseModel;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * 字典数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"handler"})
@ToString
public class Dictionary extends BaseModel implements Serializable {
    /**
     * codeid
     *
     */
    private String codeid;

    /**
     * 类型
     *
     */
    private String codetype;

    /**
     * 描述
     *
     */
    private String content;

    /**
     * 类型值代码
     *
     */
    private String code;

    /**
     * 代码名
     *
     */
    private String codename;

    /**
     * 上级代码
     *
     */
    private String superid;

    /**
     * 级别
     *
     */
    private Short leve;

    /**
     * 排列顺序 
     *
     */
    private Short orderby;

    /**
     * applyto
     *
     */
    private Integer applyto;

    /**
     * REMARK
     *
     */
    private String remark;

    private Dictionary superDictionary;

    private Set<Dictionary> children;

    /**
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_dictionary
     */
    @Override
    public Object getPrimaryKey() {
        return  this.getCodeid() ;
    }
}