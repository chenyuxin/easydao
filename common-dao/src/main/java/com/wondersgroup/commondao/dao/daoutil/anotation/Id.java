package com.wondersgroup.commondao.dao.daoutil.anotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 判断字段是否主键
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Id {
}
