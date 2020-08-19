package com.my.asmsystraceplugin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wyh.trace.lib.TraceClass;
import com.wyh.trace.lib.TraceMethod;

@TraceClass
public class MainActivity extends AppCompatActivity {

    /**
     * 不追踪此方法，但是追踪其内部调用的所有方法，
     */
    @TraceMethod(trace = false, traceInnerMethod = true)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFinishing();
        isDestroyed();
        Test.test();
    }

}