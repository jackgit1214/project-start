package com.project.core.common.anaotation;

import com.project.core.common.SysConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 记录连接历史
 *
 * @author: lilj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkHistoryAnaotation {


    String linkLevel() default SysConstant.SECOUND_LINK_SIGN;

    String linkName();

    String linkValue();

}
