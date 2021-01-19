package com.wyh.trace.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法注解
 *
 * @author WangYingHao
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface TraceMethod {

    /**
     * 是否要追踪此方法
     */
    boolean trace() default true;

    /**
     * 是否要追踪方法调用的内部方法
     */
    boolean traceInnerMethod() default false;
}
