package com.cennavi.core.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created by sunpengyan on 2018/5/5.
 * 如果使用BaseDao下的方法时，与表中字段名称不对应的属性需要用注解@IgnoreColumn修饰
 * 使用方式 在属性名上 加 @IgnoreColumn("")
 * 主要为保存类方法，查询类部分未加，需要查询类方法联系我
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreColumn {
    String value();
}