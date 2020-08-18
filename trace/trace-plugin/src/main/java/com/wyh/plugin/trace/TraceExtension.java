package com.wyh.plugin.trace;

import com.ss.android.ugc.bytex.common.BaseExtension;

public class TraceExtension extends BaseExtension {
    private boolean deleteLineNumber;

    public boolean isDeleteLineNumber() {
        return deleteLineNumber;
    }

    public void setDeleteLineNumber(boolean deleteLineNumber) {
        this.deleteLineNumber = deleteLineNumber;
    }

    @Override
    public String getName() {
        //在gradle中写插件配置dsl时的名字
        //the name of the plugin to configure dsl in gradle
        return "TracePlugin";
    }
}
