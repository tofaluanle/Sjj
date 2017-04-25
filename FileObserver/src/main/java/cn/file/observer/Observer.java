package cn.file.observer;

import android.os.FileObserver;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @auther 宋疆疆
 * @date 2015/6/29.
 */
public class Observer {

    public static void main(final String[] args) {
        setProcessName();
        System.out.println(args[0]);
        new Thread() {
            @Override
            public void run() {
                try {
                    String s = FileUtil.readFile(args[0]);
                    System.out.println(s);
                    String[] split = s.split("\n");
                    if (split != null && split.length > 0) {
                        for (String s1 : split) {
                            if (TextUtils.isEmpty(s1)) {
                                continue;
                            }
                            Logger.i("start watching " + s1);
                            MyFileObserver observer = new MyFileObserver(s1);
                            observer.startWatching();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        while (true) {
            SystemClock.sleep(60 * 1000);
        }
    }

    private static void setProcessName() {
        try {
            Method method = ClassUtil.myGetMethod(android.os.Process.class, "setArgV0", String.class);
            method.invoke(null, "file_observer");
        } catch (Exception e) {
            Logger.w("改进程名失败\n" + Log.getStackTraceString(e));
        }
        try {
            Class ddmClazz = ClassUtil.myGetClass("android.ddm.DdmHandleAppName");
            Method method = ClassUtil.myGetMethod(ddmClazz, "setAppName", String.class);
            method.invoke(null, "file_observer");
        } catch (Exception e) {
            Logger.w("改DDMS进程名失败\n" + Log.getStackTraceString(e));

            try {
                Class ddmClazz = ClassUtil.myGetClass("android.ddm.DdmHandleAppName");
                Class uhClazz = ClassUtil.myGetClass("android.os.UserHandle");
                Method method = ClassUtil.myGetMethod(ddmClazz, "setAppName", String.class, int.class);
                Method method2 = ClassUtil.myGetMethod(uhClazz, "myUserId");
                int uid = (Integer) method2.invoke(null);
                method.invoke(null, "file_observer", uid);
            } catch (Exception e1) {
                Logger.w("改DDMS进程名失败\n" + Log.getStackTraceString(e1));
            }
        }
    }

    private static class MyFileObserver extends FileObserver {

        private String mRoot;

        public MyFileObserver(String path) {
            super(path);
            this.mRoot = path;
        }

        @Override
        public void onEvent(int event, String path) {
            String action = "UNKNOWN_ACTION";
            switch (event) {
                case ACCESS:
                    action = "ACCESS";
                    break;
                case MODIFY:
                    action = "MODIFY";
                    break;
                case ATTRIB:
                    action = "ATTRIB";
                    break;
                case CLOSE_WRITE:
                    action = "CLOSE_WRITE";
                    break;
                case CLOSE_NOWRITE:
                    action = "CLOSE_NOWRITE";
                    break;
                case OPEN:
                    action = "OPEN";
                    break;
                case MOVED_FROM:
                    action = "MOVED_FROM";
                    break;
                case MOVED_TO:
                    action = "MOVED_TO";
                    break;
                case DELETE:
                    action = "DELETE";
                    break;
                case CREATE:
                    action = "CREATE";
                    break;
                case DELETE_SELF:
                    action = "DELETE_SELF";
                    break;
                case MOVE_SELF:
                    action = "MOVE_SELF";
                    break;
                default:
                    action += ": " + event;
                    break;
            }
            String msg = path + ": " + action + ", root: " + mRoot;
            Logger.d(msg);
        }
    }
}
