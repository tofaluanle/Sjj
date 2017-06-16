package cn.sjj.ktv;

import android.util.Log;

/**
 * 对日志进行管理
 *
 * @author 宋疆疆
 */
public class Logger {

    public static String sTAG = "sjj";

    public static final int LEVEL = 0;
    private static final int VERBOSE = Log.VERBOSE;
    private static final int DEBUG = Log.DEBUG;
    private static final int INFO = Log.INFO;
    private static final int WARN = Log.WARN;
    private static final int ERROR = Log.ERROR;

    private static ILogRecorder sLogRecorder;

    public static ILogRecorder getLogRecorder() {
        return sLogRecorder;
    }

    public static void setLogRecorder(ILogRecorder logRecorder) {
        sLogRecorder = logRecorder;
    }

    public static void setTAG(String sTAG) {
        Logger.sTAG = sTAG;
    }

    public static void syso(String msg) {
        if (LEVEL <= VERBOSE) {
            System.out.println(msg);
        }
    }

    public static void v(String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(sTAG, msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(sTAG, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            Log.i(sTAG, msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            Log.w(sTAG, msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= ERROR) {
            Log.e(sTAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void syso(boolean flag, String msg) {
        if (flag) {
            syso(msg);
        }
    }

    public static void v(boolean flag, String msg) {
        if (flag) {
            v(msg);
        }
    }

    public static void d(boolean flag, String msg) {
        if (flag) {
            d(msg);
        }
    }

    public static void i(boolean flag, String msg) {
        if (flag) {
            i(msg);
        }
    }

    public static void w(boolean flag, String msg) {
        if (flag) {
            w(msg);
        }
    }

    public static void e(boolean flag, String msg) {
        if (flag) {
            e(msg);
        }
    }

    public static void syso(String msg, Throwable t) {
        syso(getLineNumber(t) + msg);
    }

    public static void v(String msg, Throwable t) {
        v(getLineNumber(t) + msg);
    }

    public static void d(String msg, Throwable t) {
        d(getLineNumber(t) + msg);
    }

    public static void i(String msg, Throwable t) {
        i(getLineNumber(t) + msg);
    }

    public static void w(String msg, Throwable t) {
        w(getLineNumber(t) + msg);
    }

    public static void e(String msg, Throwable t) {
        e(getLineNumber(t) + msg);
    }

    public static void v(String tag, String msg, Throwable t) {
        v(tag, getLineNumber(t) + msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        d(tag, getLineNumber(t) + msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        i(tag, getLineNumber(t) + msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        w(tag, getLineNumber(t) + msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        e(tag, getLineNumber(t) + msg);
    }

    public static void syso(boolean flag, String msg, Throwable t) {
        syso(flag, getLineNumber(t) + msg);
    }

    public static void v(boolean flag, String msg, Throwable t) {
        v(flag, getLineNumber(t) + msg);
    }

    public static void d(boolean flag, String msg, Throwable t) {
        d(flag, getLineNumber(t) + msg);
    }

    public static void i(boolean flag, String msg, Throwable t) {
        i(flag, getLineNumber(t) + msg);
    }

    public static void w(boolean flag, String msg, Throwable t) {
        w(flag, getLineNumber(t) + msg);
    }

    public static void e(boolean flag, String msg, Throwable t) {
        e(flag, getLineNumber(t) + msg);
    }

    public static String printStackTrace(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString() + "\n");
        // Don't use getStackTrace() as it calls clone()
        // Get stackTrace, in case stackTrace is reassigned
        StackTraceElement[] stack = e.getStackTrace();
        for (StackTraceElement element : stack) {
            sb.append("\tat " + element + "\n");
        }

        StackTraceElement[] parentStack = stack;
        Throwable throwable = e.getCause();
        while (throwable != null) {
            sb.append("Caused by: ");
            sb.append(throwable + "\n");
            StackTraceElement[] currentStack = throwable.getStackTrace();
            int duplicates = countDuplicates(currentStack, parentStack);
            for (int i = 0; i < currentStack.length - duplicates; i++) {
                sb.append("\tat " + currentStack[i] + "\n");
            }
            if (duplicates > 0) {
                sb.append("\t... " + duplicates + " more" + "\n");
            }
            parentStack = currentStack;
            throwable = throwable.getCause();
        }
        return sb.toString();
    }

    private static int countDuplicates(StackTraceElement[] currentStack, StackTraceElement[] parentStack) {
        int duplicates = 0;
        int parentIndex = parentStack.length;
        for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0; ) {
            StackTraceElement parentFrame = parentStack[parentIndex];
            if (parentFrame.equals(currentStack[i])) {
                duplicates++;
            } else {
                break;
            }
        }
        return duplicates;
    }

    public static String getLineNumber(Throwable e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) {
            return "";
        }
        return "[" + trace[0].getClassName().substring(trace[0].getClassName().lastIndexOf(".") + 1) + "-" + trace[0].getLineNumber() + "] ";
    }

    /**
     * 定义记录日志的功能的接口
     * @auther 宋疆疆
     * @date 2016/5/6.
     */
    public interface ILogRecorder {

        void setLogPath(String path);

        void takeNotes(Throwable e);

        void takeNotes(String message);
    }

}
