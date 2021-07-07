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
public @interface FormBean {


    /**
     *
     * 指定请求参数的前缀和暴露到模型对象的名字供视图使用
     *
     * <p>1、绑定请求参数到模型，绑定规则<br/>
     *  如请求表单：<br>
     *  <pre class="code">
     *    <input name="user.name" value="aaa" /><br>
     *    <input name="user.type" value="bbb" /><br>
     *  </pre>
     *  则请求处理方法：<br>
     *  <pre class="code">
     *    @RequestMapping(value = "/test")
     *    public String test(@FormModel("student") Student student) //这样将绑定  student.name student.type两个参数
     *  </pre>
     */
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
