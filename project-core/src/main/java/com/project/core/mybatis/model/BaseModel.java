package com.project.core.mybatis.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * @author lilj
 */
@MappedSuperclass
public abstract class BaseModel {

    protected boolean selected;

    @Transient
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    /**
     * 比较主键值是否相同
     *
     * @param obj
     * @return
     */
    @Transient
    public boolean equalsPK(Object obj) {
        if (obj == null)// 对象为空不比较
            return false;
        // 类型不同不必进行比较
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        // 不是BaseEO，不必比较
        if (!(obj instanceof BaseModel)) {
            return false;
        }

        BaseModel eo = (BaseModel) obj;

        if (getPrimaryKey() != null && eo.getPrimaryKey() != null) {
            return getPrimaryKey().equals(eo.getPrimaryKey());
        } else {
            return false;
        }

    }

    @Transient
    public boolean isNew() {
        return (this.getPrimaryKey() == null);
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Transient
    public abstract Object getPrimaryKey();
}
