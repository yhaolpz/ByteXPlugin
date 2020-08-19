package com.my.asmsystraceplugin;

import androidx.core.os.TraceCompat;

import com.wyh.trace.lib.IMethodTrace;

/**
 * systrace 追踪处理
 * <p>
 * 命令举例： python  systrace.py   -t  5  -a  com.my.asmsystraceplugin  -o  browser.html  sched gfx view wm am app
 * <p>
 * -t 5 ： 开始追踪程序 5s，在此期间你应该执行完 TraceCompat 调用
 * -a com.my.asmsystraceplugin ：要追踪的应用包名
 * -o  browser.html ：输出文件
 *
 * @author WangYingHao
 */
public class CustomSysTrace implements IMethodTrace {

    @Override
    public void onMethodEnter(String className, String methodName, String methodDesc, String outerMethod) {
        TraceCompat.beginSection(className + "#" + methodName);
    }

    @Override
    public void onMethodEnd(String className, String methodName, String methodDesc, String outerMethod) {
        TraceCompat.endSection();
    }
}
