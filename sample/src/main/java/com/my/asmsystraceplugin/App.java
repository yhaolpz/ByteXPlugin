package com.my.asmsystraceplugin;

import android.app.Application;
import android.content.Context;

import com.wyh.trace.lib.TraceClass;

/**
 * @author WangYingHao
 */
@TraceClass(methodTrace = CustomSysTrace.class, traceAllMethod = true, traceInnerMethod = false)
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        doWork12();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doWork3();
        doWork4();
    }

    private void doWork12() {
        doWork1();
        doWork2();
    }


    private void doWork1() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doWork2() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doWork3() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doWork4() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
