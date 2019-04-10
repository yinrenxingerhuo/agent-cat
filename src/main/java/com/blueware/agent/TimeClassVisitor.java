package com.blueware.agent;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;

//定义扫描待修改class的visitor，visitor就是访问者模式
public class TimeClassVisitor extends ClassVisitor {
    private String className;
    private String annotationDesc;
    private Method[] ms;

    public TimeClassVisitor(ClassVisitor cv, String className) {
        super(Opcodes.ASM5, cv);
        this.className = className;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.annotationDesc = desc;
        return super.visitAnnotation(desc, visible);
    }

    //扫描到每个方法都会进入，参数详情下一篇博文详细分析
    @Override
    public MethodVisitor visitMethod(int access, final String name, final String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (annotationDesc != null && annotationDesc.equals(Type.getDescriptor(ExclusiveTime.class))) {
            AnnotationVisitor an = cv.visitAnnotation("", false);
            final String key = className + name + desc;
            checkIsClass(className);
            //过来待修改类的构造函数
            if (!name.equals("<init>") && mv != null) {
                if(ms != null) {
                    for (Method m : ms) {
                        if(m.getName().equals(name)) {
                            if(m.isAnnotationPresent(MethodCode.class)) {
                                mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
                                    //方法进入时获取开始时间
                                    @Override
                                    public void onMethodEnter() {
                                        this.visitLdcInsn(className);
                                        this.visitLdcInsn(name);
                                        this.visitLdcInsn(desc);
                                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/ProcessUtil", "setStartTime", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                                                false);
                                    }

                                    //方法退出时获取结束时间并计算执行时间
                                    @Override
                                    public void onMethodExit(int opcode) {
                                        this.visitLdcInsn(key);
                                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/ProcessUtil", "setEndTime", "(Ljava/lang/String;)V", false);
                                    }
                                };
                            }
                            if(m.isAnnotationPresent(EventCode.class)) {
                                mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
                                    //方法进入时获取开始时间
                                    @Override
                                    public void onMethodEnter() {
                                        this.visitLdcInsn(className);
                                        this.visitLdcInsn(name);
                                        this.visitLdcInsn(desc);
                                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/ProcessUtil", "eventBegin", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                                                false);
                                    }

                                    //方法退出时获取结束时间并计算执行时间
                                    @Override
                                    public void onMethodExit(int opcode) {
                                        this.visitLdcInsn(key);
                                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/ProcessUtil", "eventEnd", "(Ljava/lang/String;)V", false);
                                    }
                                };
                            }
                        }
                    }
                }
            }
        }
        return mv;
    }

    private void checkIsClass(String className) {

        className = className.replace("/", ".");
        Class<?> c = new MyClassLoader().findClass(className);
        ms = c.getDeclaredMethods();
    }
}
