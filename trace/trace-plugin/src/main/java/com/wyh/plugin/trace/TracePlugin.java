package com.wyh.plugin.trace;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.TransformConfiguration;
import com.ss.android.ugc.bytex.common.flow.main.Process;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;

import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

@PluginConfig("trace-plugin")
public class TracePlugin extends CommonPlugin<TraceExtension, TraceContext> {
    @Override
    protected TraceContext getContext(Project project, AppExtension android, TraceExtension extension) {
        return new TraceContext(project, android, extension);
    }

    @Override
    public void traverse(@Nonnull String relativePath, @Nonnull ClassNode node) {
        super.traverse(relativePath, node);
    }

    @Override
    public boolean transform(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        //我们需要修改字节码，所以需要注册一个ClassVisitor
        //We need to modify the bytecode, so we need to register a ClassVisitor
        chain.connect(new TraceClassVisitor(context, extension));
        return super.transform(relativePath, chain);
    }

    @Override
    public void traverseAndroidJar(@Nonnull String relativePath, @Nonnull ClassNode node) {
        super.traverseAndroidJar(relativePath, node);
    }

    @Override
    public int flagForClassReader(Process process) {
        //解决 LocalVariablesSorter only accepts expanded frames
        return ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES | ClassReader.EXPAND_FRAMES;
    }

    @Nonnull
    @Override
    public TransformConfiguration transformConfiguration() {
        return new TransformConfiguration() {
            @Override
            public boolean isIncremental() {
                //插件默认是增量的，如果插件不支持增量，需要返回false
                //The plugin is incremental by default.It should return false if incremental is not supported by the plugin
                return true;
            }
        };
    }
}
