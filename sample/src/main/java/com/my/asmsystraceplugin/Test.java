package com.my.asmsystraceplugin;

import com.wyh.trace.lib.TraceClass;

/**
 * @author WangYingHao
 */
@TraceClass(methodTrace = CustomTimeTrace.class, traceAllMethod = true)
class Test {

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
