package com.integration.arouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Wang
 * @version 1.0
 * @date 2020/11/17 - 19:41
 * @Description com.integration.arouter_annotation
 */
@Target(ElementType.TYPE)//类 的
@Retention(RetentionPolicy.CLASS)//编译期的 XUtil 是运行期的
public @interface ARouter {
    //
    String path();

    //设置默认值 ""
    String group() default "";

}