package com.starmcc.qmdata.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author starmcc
 * @version 2019/7/17 21:22
 * 排序 order by
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {

    /**
     * sql value
     *
     * @return value
     */
    String value() default "";

}
