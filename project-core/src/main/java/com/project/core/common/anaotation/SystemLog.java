package com.project.core.common.anaotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String description() default "";
    String moduleId() default "";
    OpeType opeType() default OpeType.DISPLAY;

    enum OpeType {
        SPACE(0),EDIT(1),DEL(2), DISPLAY(3),LOGIN(4), LOGOUT(5),OTHER(6);
        OpeType(int value) {

        }
    };


}
