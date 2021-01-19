package com.my.asmsystraceplugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author WangYingHao
 */
class Test {

    public static void doCpuWork() {
        doCpuWork(null);
    }

    public static void doSleepWork() {
        doSleepWork(null);
    }

    public static void doIOWork() {
        doIOWork(null);
    }

    public static void doCpuWork(ThreadPoolExecutor executor) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - time > 100) {
                        break;
                    }
                }
            }
        };
        if (executor == null) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }

    public static void doSleepWork(ThreadPoolExecutor executor) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        if (executor == null) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }

    public static void doIOWork(ThreadPoolExecutor executor) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                File cacheDir = App.sApp.getCacheDir();
                File testFile = new File(cacheDir, "test");
                try {
                    FileOutputStream stream = new FileOutputStream(testFile);
                    stream.write(new Date().toString().getBytes());
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        if (executor == null) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }


}
