package com.my.asmsystraceplugin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wyh.trace.lib.TraceClass;
import com.wyh.trace.lib.TraceMethod;

@TraceClass
public class MainActivity extends AppCompatActivity {

    @TraceMethod(trace = false, traceInnerMethod = true)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFinishing();
        isDestroyed();
        Test.test();
    }

}