package com.wyh.plugin.trace;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.BaseContext;

import org.gradle.api.Project;

public class TraceContext extends BaseContext<TraceExtension> {
    public TraceContext(Project project, AppExtension android, TraceExtension extension) {
        super(project, android, extension);
    }
}
