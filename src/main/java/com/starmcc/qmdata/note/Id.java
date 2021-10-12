package com.starmcc.qmdata.note;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author starmcc
 * @version 2019/1/9 11:28
 * 主键id标识
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

    /**
     * 设置该字段的别名
     *
     * @return name
     */
    String name() default "";

}
