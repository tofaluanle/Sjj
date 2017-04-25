package com.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;

public class HackerPPT {

    static String orgPath = "C:\\Users\\songjj\\Desktop\\hackppt\\olivephone-sdk-ppt-android-1.6-universial-trial-1.0.0.jar";
    static String outPath = "C:\\Users\\songjj\\Desktop\\hackppt";

    public static void main(String[] args) throws Exception {
//        method1();
        method2();
        method3();
    }

    private static void method3() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        CtClass cc = cp.get("com.olivephone.office.powerpoint.view.PersentationView");

        CtField[] methods = cc.getDeclaredFields();
        for (CtField m : methods) {
            System.out.println(m.getSignature() + ", name: " + m.getName());
            m.getFieldInfo().setAccessFlags(AccessFlag.PROTECTED);
        }

        cc.writeFile(outPath);
    }
    private static void method2() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        CtClass cc = cp.get("com.olivephone.office.powerpoint.view.SlideView");

        CtMethod[] methods = cc.getDeclaredMethods();
        for (CtMethod m : methods) {
//            System.out.println(m.getSignature() + ", longName: " + m.getLongName());
        }

        CtMethod m = cc.getMethod("a", "(Landroid/graphics/Canvas;)V");
        m.getMethodInfo().setAccessFlags(AccessFlag.PUBLIC);
        cc.writeFile(outPath);
    }

    private static void method1() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        CtClass cc = cp.get("com.olivephone.office.powerpoint.view.PersentationView");

        CtMethod[] methods = cc.getDeclaredMethods();
        for (CtMethod m : methods) {
//            System.out.println(m.getSignature() + ", longName: " + m.getLongName());
        }
//            CtMethod m = cc.getDeclaredMethod("b");
        CtMethod m = cc.getMethod("onDraw", "(Landroid/graphics/Canvas;)V");

        System.out.println(m.getMethodInfo().getLineNumber(0));
        System.out.println(m.getMethodInfo().getLineNumber(1));
        System.out.println(m.getMethodInfo().getLineNumber(2));
        System.out.println(m.getMethodInfo().getLineNumber(3));
        System.out.println(m.getMethodInfo().getLineNumber(4));
        System.out.println(m.getMethodInfo().getLineNumber(5));

//        m.insertBefore("System.out.println(\"sjj_hack2\");");
//            m.insertAt(0, "System.out.println(\"insert 0\");");
//        m.insertAt(1151, "return;");
        cc.writeFile(outPath);
    }

}
