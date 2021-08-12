package com.project.system.model;

import com.project.core.mybatis.model.BaseModel;

import com.project.core.mybatis.model.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class SystemRole extends SysRole implements Serializable {

    private String roleDesc;
    private String isSystem;
    private static final long serialVersionUID = 1L;

    @Override
    public Object getPrimaryKey() {
        return this.getRoleId();
    }
}