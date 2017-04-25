package com.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class HackerExcel {

    static String androidPath = "D:\\WorkSoft\\Android\\android-sdk-windows\\platforms\\android-23\\android.jar";
    static String orgPath = "C:\\Users\\songjj\\Desktop\\hackExcel\\olivephone-sdk-excel-android-4.0-universal-trial-1.0.0.jar";
    static String outPath = "C:\\Users\\songjj\\Desktop\\hackExcel";

    public static void main(String[] args) throws Exception {
        method1();
    }

    private static void method1() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(orgPath);
        cp.insertClassPath(androidPath);
        CtClass cc = cp.get("com.olivephone.office.excel.view.TableView");

        CtMethod[] methods = cc.getDeclaredMethods();
        for (CtMethod m : methods) {
//            System.out.println(m.getSignature() + ", longName: " + m.getLongName());
        }
        CtMethod m = cc.getMethod("a", "(Landroid/graphics/Canvas;Landroid/graphics/Paint;Lcom/olivephone/office/excel/view/r;)V");
        m.insertBefore("return;");

         m = cc.getMethod("b", "(Landroid/graphics/Canvas;Landroid/graphics/Paint;Lcom/olivephone/office/excel/view/r;)V");
        m.insertBefore("return;");

        cc.writeFile(outPath);
    }

}
