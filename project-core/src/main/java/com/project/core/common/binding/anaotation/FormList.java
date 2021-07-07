/**
 *
 */
package com.project.core.common.binding.anaotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lilj
 * <p>绑定请求参数到模型，并且暴露到模型中供页面使用</p>
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormList {

    String value() default "";

    /** 是否校验表单重复提交*/
    boolean valid() default false;

    /**
     * 校验表单的token参数 * @return
     */
    String token() default "ftoken";

    /** 是否使用校验码校验*/
    boolean captcha() default false;

}
