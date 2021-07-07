package com.project.core.security.model;

import java.util.List;
import java.util.Set;

import com.project.core.mybatis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"roles", "child"})
public class FunModule extends BaseModel {

    public final static String ROOT_SUPER_ID = "0";
    protected String funId;

    protected String funName;

    protected Integer funType;

    protected String modDesc;

    protected Integer isUse;

    public String superModId;

    protected String funIcon;

    protected Integer funOrder;

    protected String urlLink;

    protected Integer system;

    protected Set<SysRole> roles;

    protected List<FunModule> child;


    @Override
    public Object getPrimaryKey() {
        return null;
    }
}