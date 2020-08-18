package com.wyh.trace.lib;

/**
 * 方法追踪接口
 *
 * @author WangYingHao
 */
public interface IMethodTrace {

    void onMethodEnter(String className, String methodName, String methodDesc, String outerMethod);

    void onMethodEnd(String className, String methodName, String methodDesc, String outerMethod);
}
