package com.project.resources;

import com.project.core.common.converter.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        // 注册int,float,long,double类型转换

        binder.registerCustomEditor(int.class, new CustomIntegerEditor());
        binder.registerCustomEditor(Integer.class, new CustomIntegerEditor());
        binder.registerCustomEditor(float.class, new CustomFloatEditor());
        binder.registerCustomEditor(double.class, new CustomDoubleEditor());
        binder.registerCustomEditor(long.class, new CustomLongEditor());
        binder.registerCustomEditor(short.class, new CustomShortEditor());
        // 注册boolean类型转换，缺省值为false
        binder.registerCustomEditor(boolean.class, new SpecCustomBooleanEditor(
                false));

        // 注册日期转换
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, true));

    }
}
