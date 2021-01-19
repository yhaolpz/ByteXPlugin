package com.my.asmsystraceplugin;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.wyh.trace.lib.TraceClass;
import com.wyh.trace.lib.TraceMethod;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author WangYingHao
 */
@TraceClass()
public class App extends Application {

    public static App sApp;
    public final int processors = Runtime.getRuntime().availableProcessors();

    /**
     * 《java 并发编程实战》作者推荐线程数： CPU 核心数 *（1+平均等待时间/平均工作时间）
     */
    private ThreadPoolExecutor mCpuWorkExecutors = new ThreadPoolExecutor(
            processors,
            processors,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), //这里设置的无界队列，队列不会被塞满，所以 maximumPoolSize 参数不会生效
            new CountThreadFactory("CpuWork"));

    private ThreadPoolExecutor mWorkExecutors = new ThreadPoolExecutor(
            processors * 4, processors * 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new CountThreadFactory("Work"));


    static class CountThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public final String name;

        public CountThreadFactory(String name) {
            this.name = name;
        }

        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, name + "#" + mCount.getAndIncrement());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sApp = this;
    }

    @TraceMethod(traceInnerMethod = true)
    @Override
    public void onCreate() {
        super.onCreate();

        doCpuWork();

        doCpuWork();

        doCpuWork();

        doCpuWork();

        doCpuWork();


        mCpuWorkExecutors.shutdown();
        while (true) {
            try {
                if (mCpuWorkExecutors.awaitTermination(10, TimeUnit.MINUTES)) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        doSleepWork();
        doSleepWork();
        doSleepWork();
        doSleepWork();
        doSleepWork();

        doIOWork();
        doIOWork();
        doIOWork();
        doIOWork();
        doIOWork();

        mWorkExecutors.shutdown();
        while (true) {
            try {
                if (mWorkExecutors.awaitTermination(10, TimeUnit.MINUTES)) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doCpuWork() {
        Test.doCpuWork();
//        Test.doCpuWork(mCpuWorkExecutors);
    }

    private void doSleepWork() {
        Test.doSleepWork();
//        Test.doSleepWork(mWorkExecutors);
    }

    private void doIOWork() {
        Test.doIOWork();
//        Test.doIOWork(mWorkExecutors);
    }

}
