package com.hrms.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel导入配置
 * 配合 {@link com.nuts.hrms.common.ExcelUtils}
 *
 * @author ymsong
 * @create 2018/9/8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportConfig {
    String value() default "";

    /**
     * 字典表ID
     */
    int dict() default 0;

    /**
     * 表中数据
     */
    String[] mapKey() default {};

    /**
     * 转换后数据
     */
    String[] mapValue() default {};

    /**
     * 是否不为空,默认为true
     * true:非空;false:为空
     */
    boolean notNull() default true;

}
