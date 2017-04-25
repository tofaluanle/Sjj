package com.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

public class HackerWord {

    static String orgPath = "C:\\Users\\songjj\\Desktop\\test\\olivephone-sjj.jar";

    public static void main(String[] args) throws Exception {
        method1();
//        method2();
//        method3();
        method4();
    }

    private static void method4() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        //获得类文件名
        CtClass cc = cp.get("com.olivephone.office.word.d.c.c");

        CtMethod[] methods = cc.getDeclaredMethods();
        for (CtMethod m : methods) {
//                System.out.println(m.getSignature() + ", longName: " + m.getLongName());
        }
        CtMethod m = cc.getMethod("a", "(Ljava/lang/CharSequence;IIFF)V");
        m.insertBefore("if (charsequence != null) {\n" +
                "            boolean isIgnore = charsequence.toString().equals(\"This is a trial version.\") || charsequence.toString().equals(\"www.olivephone.com\") || charsequence.toString().equals(\"sales@olivephone.com\");\n" +
                "            if (isIgnore) {\n" +
                "                return;\n" +
                "            }\n" +
                "        }");
        m.insertBefore("System.out.println(\"sjj_hack5\");");
        cc.writeFile("C:\\Users\\songjj\\Desktop\\test");
    }

    private static void method3() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        //获得类文件名
        CtClass cc = cp.get("com.olivephone.office.word.a.a.e");

        CtMethod m = cc.getMethod("a", "(II[Lcom/olivephone/office/word/a/a/m;)V");
        m.insertBefore("System.out.println(\"sjj_hack5\");");
        m.insertAt(1126, "this.alu = true;");
        m.insertAt(1126, "System.out.println(\"sjj_hack3\");");
        m.insertAt(1235, "System.out.println(\"sjj_hack4\");");

        cc.writeFile("C:\\Users\\songjj\\Desktop\\test");
    }

    private static void method2() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        //获得类文件名
        CtClass cc = cp.get("com.olivephone.api.a");

//获得要修改的方法名
        CtField[] methods = cc.getDeclaredFields();
        for (CtField m : methods) {
            System.out.println(m.getSignature() + ", name: " + m.getName());
            cc.removeField(m);
        }
        cc.addField(CtField.make("public static byte[] lR = new byte[1];", cc));
        cc.addField(CtField.make("public static String lT = \"\";", cc));
        cc.addField(CtField.make("public static String lU = \"\";", cc));
        cc.addField(CtField.make("public static String lV = \"\";", cc));
        cc.addField(CtField.make("public static String lW = \"\";", cc));
        cc.writeFile("C:\\Users\\songjj\\Desktop\\test");
    }

    private static void method1() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        //获得类文件名
        CtClass cc = cp.get("com.olivephone.office.word.d.b.f");

//获得要修改的方法名
        CtMethod[] methods = cc.getDeclaredMethods();
        for (CtMethod m : methods) {
//                System.out.println(m.getSignature() + ", longName: " + m.getLongName());
        }
//            CtMethod m = cc.getDeclaredMethod("b");
        CtMethod m = cc.getMethod("b", "(IIIII)V");
//        System.out.println(m.getSignature() + ", longName: " + m.getLongName());
        //这里是将返回结果改成true
        m.insertBefore("System.out.println(\"sjj_hack2\");");
//            m.insertAt(0, "System.out.println(\"insert 0\");");
        m.insertAt(1151, "return;");
        cc.writeFile("C:\\Users\\songjj\\Desktop\\test");
    }

}
