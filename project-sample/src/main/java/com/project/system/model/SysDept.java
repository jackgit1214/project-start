package com.project.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.project.core.common.DTreeData;
import com.project.core.mybatis.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"handler"})
@ToString
public class SysDept extends BaseModel implements Serializable, DTreeData {


    private static final long serialVersionUID = -8602615422796036147L;
    private String deptId;
    private String deptName;

    private String superId;
    private int sequence;
    private int endFlag;
    private String deptDesc;

    private SysDept superDept;

    @Override
    public Object getPrimaryKey() {
        return this.getDeptId();
    }

    @Override
    public String getId() {
        return this.deptId;
    }

    @Override
    public String getParentId() {
        return this.superId;
    }

    @Override
    public String getTitle() {
        return this.deptName;
    }

    @Override
    public Object getCheckArr() {
        Map<String, String> check = new HashMap<>();
        check.put("type", "0");
        check.put("checked", "0");
        return check;
    }

    public String getSuperDeptName() {
        if (this.superDept == null)
            return "";
        return this.superDept.getDeptName();
    }
}