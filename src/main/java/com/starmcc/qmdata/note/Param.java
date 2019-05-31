package com.starmcc.qmdata.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qm
 * @date 2019/1/9 11:38
 * @Description 字段标识
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 字段别名
     *
     * @return
     */
    String name() default "";

    /**
     * 是否排除该字段
     *
     * @return
     */
    boolean except() default false;
}
