package com.wyh.trace.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * trace 统一处理入口
 *
 * @author WangYingHao
 */
public class TraceRecord {

    private static final Map<String, IMethodTrace> sMethodTraceMap = new HashMap<String, IMethodTrace>();

    public static void onMethodEnter(String traceImplClass,
                                     String className,
                                     String methodName, String methodDesc,
                                     String outerMethod) {
        getMethodTrace(traceImplClass).onMethodEnter(className, methodName, methodDesc, outerMethod);
    }

    public static void onMethodEnd(String traceImplClass,
                                   String className,
                                   String methodName, String methodDesc,
                                   String outerMethod) {
        getMethodTrace(traceImplClass).onMethodEnd(className, methodName, methodDesc, outerMethod);
    }

    private static IMethodTrace getMethodTrace(String traceImplClass) {
        IMethodTrace methodTrace = sMethodTraceMap.get(traceImplClass);
        if (methodTrace == null) {
            try {
                methodTrace = (IMethodTrace) Class.forName(traceImplClass).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (methodTrace == null) {
            methodTrace = new TimeTrace();
        }
        sMethodTraceMap.put(traceImplClass, methodTrace);
        return methodTrace;
    }

}
