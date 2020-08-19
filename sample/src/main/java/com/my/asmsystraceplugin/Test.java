package com.my.asmsystraceplugin;

import com.wyh.trace.lib.TraceClass;
import com.wyh.trace.lib.TraceMethod;

/**
 * @author WangYingHao
 */
@TraceClass(methodTrace = CustomTimeTrace.class, traceAllMethod = false, traceInnerMethod = false)
class Test {

    @TraceMethod(trace = true, traceInnerMethod = true)
    public static void test() {
        test1();
        test2();
    }

    public static void test1() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
