package com.project.core.common.converter;

import java.beans.PropertyEditorSupport;


import org.springframework.util.StringUtils;


/**
 * 针对Float类型进行转换
 * 当表单数据为空时，默认为0
 *
 * @author
 */
public class CustomFloatEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String _text) throws IllegalArgumentException {

        if (_text == null || !StringUtils.hasLength(_text) || _text.trim().equals("null")) {
            _text = "0";
        }
        setValue(Float.parseFloat(_text));
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
}
