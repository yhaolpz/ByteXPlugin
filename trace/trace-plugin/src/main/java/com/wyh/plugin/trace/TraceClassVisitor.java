package com.wyh.plugin.trace;

import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class TraceClassVisitor extends BaseClassVisitor {

    public static final String TAG = "TraceClassVisitor";

    private TraceContext mTraceContext;
    private TraceExtension mTraceExtension;

    private ClassAnnotationVisitor mClassAnnotationVisitor;

    private boolean mTraceClassFlag;

    private String mClassName;

    public TraceClassVisitor(TraceContext context, TraceExtension extension) {
        this.mTraceContext = context;
        this.mTraceExtension = extension;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
    }


    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.contains(TraceLibConst.ANNOTATION_CLASS)) {
            mTraceClassFlag = true;
            mClassAnnotationVisitor = new ClassAnnotationVisitor(
                    Opcodes.ASM5, super.visitAnnotation(descriptor, visible));
            return mClassAnnotationVisitor;
        } else {
            return super.visitAnnotation(descriptor, visible);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mTraceClassFlag) {
            return new TraceMethodVisitor(api, mv, access, name, descriptor, mTraceContext);
        } else {
            return mv;
        }
    }

    /**
     * 方法
     */
    class TraceMethodVisitor extends AdviceAdapter {

        private TraceContext mTraceContext;
        private MethodAnnotationVisitor mMethodAnnotationVisitor;
        private String mMethodName;
        private String mMethodDesc;

        protected TraceMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, TraceContext context) {
            super(api, methodVisitor, access, name, descriptor);
            this.mTraceContext = context;
            this.mMethodName = name;
            this.mMethodDesc = descriptor;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.contains(TraceLibConst.ANNOTATION_METHOD)) {
                mMethodAnnotationVisitor = new MethodAnnotationVisitor(
                        Opcodes.ASM5, super.visitAnnotation(descriptor, visible));
                return mMethodAnnotationVisitor;
            }
            return super.visitAnnotation(descriptor, visible);
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            if (traceMethod()) {
                mv.visitLdcInsn(mClassAnnotationVisitor.methodTrace);
                mv.visitLdcInsn(mClassName);
                mv.visitLdcInsn(mMethodName);
                mv.visitLdcInsn(mMethodDesc);
                mv.visitLdcInsn("");
                mv.visitMethodInsn(INVOKESTATIC, TraceLibConst.CLASS_RECORD,
                        TraceLibConst.CLASS_RECORD_METHOD_I,
                        TraceLibConst.CLASS_RECORD_METHOD_DESC, false);
            }
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            if (traceMethod()) {
                mv.visitLdcInsn(mClassAnnotationVisitor.methodTrace);
                mv.visitLdcInsn(mClassName);
                mv.visitLdcInsn(mMethodName);
                mv.visitLdcInsn(mMethodDesc);
                mv.visitLdcInsn("");
                mv.visitMethodInsn(INVOKESTATIC, TraceLibConst.CLASS_RECORD,
                        TraceLibConst.CLASS_RECORD_METHOD_O,
                        TraceLibConst.CLASS_RECORD_METHOD_DESC, false);
            }
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (traceInnerMethod()) {
                mv.visitLdcInsn(mClassAnnotationVisitor.methodTrace);
                mv.visitLdcInsn(mClassName);
                mv.visitLdcInsn(name);
                mv.visitLdcInsn(mMethodDesc);
                mv.visitLdcInsn(mMethodName);
                mv.visitMethodInsn(INVOKESTATIC, TraceLibConst.CLASS_RECORD,
                        TraceLibConst.CLASS_RECORD_METHOD_I,
                        TraceLibConst.CLASS_RECORD_METHOD_DESC, false);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            if (traceInnerMethod()) {
                mv.visitLdcInsn(mClassAnnotationVisitor.methodTrace);
                mv.visitLdcInsn(mClassName);
                mv.visitLdcInsn(name);
                mv.visitLdcInsn(mMethodDesc);
                mv.visitLdcInsn(mMethodName);
                mv.visitMethodInsn(INVOKESTATIC, TraceLibConst.CLASS_RECORD,
                        TraceLibConst.CLASS_RECORD_METHOD_O,
                        TraceLibConst.CLASS_RECORD_METHOD_DESC, false);
                mTraceContext.getLogger().d(TAG, "visitMethodInsn opcode=" + opcode + " owner=" + owner + " name=" + name);
            }
        }

        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
            mTraceContext.getLogger().d(TAG, "visitInsn opcode=" + opcode);
        }

        /**
         * 是否要追踪方法
         */
        private boolean traceMethod() {
            boolean ans;
            if (mClassAnnotationVisitor.traceAllMethod) {
                //追踪条件：没有方法注解或方法注解未禁止 trace
                ans = mMethodAnnotationVisitor == null || mMethodAnnotationVisitor.trace;
            } else {
                //追踪条件：有方法注解且方法注解为 include
                ans = mMethodAnnotationVisitor != null && mMethodAnnotationVisitor.trace;
            }
            return ans;
        }

        /**
         * 是否要追踪方法内部的方法
         */
        private boolean traceInnerMethod() {
            boolean ans;
            if (mClassAnnotationVisitor.traceInnerMethod) {
                //追踪条件：没有方法注解或方法注解未禁止开启内部方法追踪
                ans = mMethodAnnotationVisitor == null || mMethodAnnotationVisitor.traceInnerMethod;
            } else {
                //追踪条件：有方法注解且方法注解中开启内部方法追踪
                ans = mMethodAnnotationVisitor != null && mMethodAnnotationVisitor.traceInnerMethod;
            }
            return ans;
        }
    }

    /**
     * 类注解
     */
    class ClassAnnotationVisitor extends AnnotationVisitor {
        String methodTrace = "";
        boolean traceAllMethod = false;
        boolean traceInnerMethod = false;

        public ClassAnnotationVisitor(int api, AnnotationVisitor annotationVisitor) {
            super(api, annotationVisitor);
        }

        @Override
        public void visit(String name, Object value) {
            super.visit(name, value);
            if ("traceInnerMethod".equals(name)) {
                traceInnerMethod = (boolean) value;
            }
            if ("traceAllMethod".equals(name)) {
                traceAllMethod = (boolean) value;
            }
            if ("methodTrace".equals(name)) {
                methodTrace = ((Type) value).getClassName();
            }
        }
    }

    /**
     * 方法注解
     */
    class MethodAnnotationVisitor extends AnnotationVisitor {
        boolean trace = true;
        boolean traceInnerMethod = false;

        public MethodAnnotationVisitor(int api, AnnotationVisitor annotationVisitor) {
            super(api, annotationVisitor);
        }

        @Override
        public void visit(String name, Object value) {
            super.visit(name, value);
            if ("traceInnerMethod".equals(name)) {
                traceInnerMethod = (boolean) value;
            }
            if ("trace".equals(name)) {
                trace = (boolean) value;
            }
        }
    }
}
