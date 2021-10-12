package com.starmcc.qmdata.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author starmcc
 * @version 2019/1/9 11:32
 * 数据表标识
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 表名
     *
     * @return tableName
     */
    String name() default "";

    /**
     * 数据库风格
     *
     * @return tableStyle
     */
    Style style() default Style.UNDERLINE;
}
