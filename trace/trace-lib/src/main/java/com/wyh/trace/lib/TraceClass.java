package com.wyh.trace.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类注解，表示要追踪此类中的方法
 * <p>
 * 为了提高处理性能，需要 trace 的类必需使用此注解标注
 *
 * @author WangYingHao
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TraceClass {

    /**
     * 方法 trace 的实现类，需继承自 {@link IMethodTrace} 接口
     */
    Class methodTrace() default TimeTrace.class;

    /**
     * 是否要追踪所有方法
     */
    boolean traceAllMethod() default false;

    /**
     * 是否要追踪方法内部调用的方法
     */
    boolean traceInnerMethod() default false;
}
