package com.project.core.common.converter;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.util.StringUtils;


/**
 * 针对boolean类型进行转换
 * 当表单数据为空时，默认为false
 *
 * @author
 */
public class SpecCustomBooleanEditor extends CustomBooleanEditor {

    private boolean defaultValue;

    /**
     * 初始化，当为空时的缺省值
     *
     * @param defaultValue
     */
    public SpecCustomBooleanEditor(boolean defaultValue) {

        //不允许为空，因为有缺省值
        super(false);
        // TODO Auto-generated constructor stub
        this.defaultValue = defaultValue;
    }

    @Override
    public void setAsText(String _text) throws IllegalArgumentException {

        if (_text == null || !StringUtils.hasLength(_text)) {
            _text = Boolean.toString(this.defaultValue);
        }
        super.setAsText(_text);
    }

}
