package com.wyh.trace.lib;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.wyh.trace.lib.IMethodTrace;

import java.util.HashMap;

/**
 * 默认的方法耗时追踪处理
 *
 * @author WangYingHao
 */
public class TimeTrace implements IMethodTrace {

    private HashMap<String, Long> mTimeTraceMap = new HashMap<String, Long>();

    @Override
    public void onMethodEnter(String className, String methodName, String methodDesc, String outerMethod) {
        final String methodKey = getMethodKey(className, methodName, methodDesc, outerMethod);
        mTimeTraceMap.put(methodKey, SystemClock.elapsedRealtime());
    }

    @Override
    public void onMethodEnd(String className, String methodName, String methodDesc, String outerMethod) {
        final String methodKey = getMethodKey(className, methodName, methodDesc, outerMethod);
        Long time = mTimeTraceMap.get(methodKey);
        time = SystemClock.elapsedRealtime() - time;
        Log.d("TimeTrace-Method", "cost [" + time + "ms], " + methodKey);
    }

    /**
     * 获取一次调用的唯一标识，兼容多线程
     */
    private String getMethodKey(String className, String methodName, String methodDesc, String outerMethod) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(Thread-")
                .append(Thread.currentThread().getId())
                .append(") ")
                .append(className)
                .append("#")
                .append(methodName);
        if (!TextUtils.isEmpty(outerMethod)) {
            stringBuilder.append(" [in-")
                    .append(outerMethod)
                    .append("] ");
        }
        stringBuilder.append(methodDesc);
        return stringBuilder.toString();
    }

}
