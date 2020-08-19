package com.wyh.plugin.trace;

import com.ss.android.ugc.bytex.common.BaseExtension;

/**
 * todo DSL 方式配置，支持追踪无法编码的三方 lib 类
 */
public class TraceExtension extends BaseExtension {

    private String defaultMethodTraceClass;

    public String defaultMethodTraceClass() {
        return defaultMethodTraceClass;
    }

    public void defaultMethodTraceClass(String defaultMethodTraceClass) {
        this.defaultMethodTraceClass = defaultMethodTraceClass;
    }

    @Override
    public String getName() {
        //在gradle中写插件配置dsl时的名字
        //the name of the plugin to configure dsl in gradle
        return "TracePlugin";
    }
}
