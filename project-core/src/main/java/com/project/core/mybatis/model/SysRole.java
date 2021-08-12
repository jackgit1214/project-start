package com.project.core.mybatis.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"modules"})
public class SysRole extends BaseModel implements Serializable {

    public SysRole() {
    }

    private String roleId;

    private String roleName;

    private Set<FunModule> modules;

    public SysRole(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }


}