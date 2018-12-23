package cn.sjj;

import android.util.Log;

/**
 * 对日志进行管理
 *
 * @author 宋疆疆
 */
public class Logger {

    public static       String sTAG          = "sjj";
    public static final String UN_EXPECT_LOG = "why goto here !? ";

    private static final int VERBOSE = Log.VERBOSE;
    private static final int DEBUG   = Log.DEBUG;
    private static final int INFO    = Log.INFO;
    private static final int WARN    = Log.WARN;
    private static final int ERROR   = Log.ERROR;

    private static ILogRecorder sLogRecorder;

    private static int sPrintLevel = 0;
    private static int sWriteLevel = 0;

    public static ILogRecorder getLogRecorder() {
        return sLogRecorder;
    }

    public static void setLogRecorder(ILogRecorder logRecorder) {
        sLogRecorder = logRecorder;
    }

    public static void setTAG(String sTAG) {
        Logger.sTAG = sTAG;
    }

    public static void setPrintLevel(int sPrintLevel) {
        Logger.sPrintLevel = sPrintLevel;
    }

    public static void setWriteLevel(int sWriteLevel) {
        Logger.sWriteLevel = sWriteLevel;
    }

    public static void syso(String msg) {
        syso(sPrintLevel <= VERBOSE, sTAG, msg, null);
    }

    public static void v(String msg) {
        v(sPrintLevel <= VERBOSE, sTAG, msg, null);
    }

    public static void d(String msg) {
        d(sPrintLevel <= DEBUG, sTAG, msg, null);
    }

    public static void i(String msg) {
        i(sPrintLevel <= INFO, sTAG, msg, null);
    }

    public static void w(String msg) {
        w(sPrintLevel <= WARN, sTAG, msg, null);
    }

    public static void e(String msg) {
        e(sPrintLevel <= ERROR, sTAG, msg, null);
    }

    public static void syso(String tag, String msg) {
        syso(sPrintLevel <= VERBOSE, tag, msg, null);
    }

    public static void v(String tag, String msg) {
        v(sPrintLevel <= VERBOSE, tag, msg, null);
    }

    public static void d(String tag, String msg) {
        d(sPrintLevel <= DEBUG, tag, msg, null);
    }

    public static void i(String tag, String msg) {
        i(sPrintLevel <= INFO, tag, msg, null);
    }

    public static void w(String tag, String msg) {
        w(sPrintLevel <= WARN, tag, msg, null);
    }

    public static void e(String tag, String msg) {
        e(sPrintLevel <= ERROR, tag, msg, null);
    }

    public static void syso(boolean flag, String msg) {
        syso(flag, sTAG, msg, null);
    }

    public static void v(boolean flag, String msg) {
        v(flag, sTAG, msg, null);
    }

    public static void d(boolean flag, String msg) {
        d(flag, sTAG, msg, null);
    }

    public static void i(boolean flag, String msg) {
        i(flag, sTAG, msg, null);
    }

    public static void w(boolean flag, String msg) {
        w(flag, sTAG, msg, null);
    }

    public static void e(boolean flag, String msg) {
        e(flag, sTAG, msg, null);
    }

    public static void syso(String msg, Throwable t) {
        syso(sPrintLevel <= VERBOSE, sTAG, msg, t);
    }

    public static void v(String msg, Throwable t) {
        v(sPrintLevel <= VERBOSE, sTAG, msg, t);
    }

    public static void d(String msg, Throwable t) {
        d(sPrintLevel <= DEBUG, sTAG, msg, t);
    }

    public static void i(String msg, Throwable t) {
        i(sPrintLevel <= INFO, sTAG, msg, t);
    }

    public static void w(String msg, Throwable t) {
        w(sPrintLevel <= WARN, sTAG, msg, t);
    }

    public static void e(String msg, Throwable t) {
        e(sPrintLevel <= ERROR, sTAG, msg, t);
    }

    public static void syso(String tag, String msg, Throwable t) {
        syso(sPrintLevel <= VERBOSE, tag, msg, t);
    }

    public static void v(String tag, String msg, Throwable t) {
        v(sPrintLevel <= VERBOSE, tag, msg, t);
    }

    public static void d(String tag, String msg, Throwable t) {
        d(sPrintLevel <= DEBUG, tag, msg, t);
    }

    public static void i(String tag, String msg, Throwable t) {
        i(sPrintLevel <= INFO, tag, msg, t);
    }

    public static void w(String tag, String msg, Throwable t) {
        w(sPrintLevel <= WARN, tag, msg, t);
    }

    public static void e(String tag, String msg, Throwable t) {
        e(sPrintLevel <= ERROR, tag, msg, t);
    }

    public static void syso(boolean flag, String msg, Throwable t) {
        syso(flag, sTAG, msg, t);
    }

    public static void v(boolean flag, String msg, Throwable t) {
        v(flag, sTAG, msg, t);
    }

    public static void d(boolean flag, String msg, Throwable t) {
        d(flag, sTAG, msg, t);
    }

    public static void i(boolean flag, String msg, Throwable t) {
        i(flag, sTAG, msg, t);
    }

    public static void w(boolean flag, String msg, Throwable t) {
        w(flag, sTAG, msg, t);
    }

    public static void e(boolean flag, String msg, Throwable t) {
        e(flag, sTAG, msg, t);
    }

    public static void syso(boolean flag, String tag, String msg, Throwable t) {
        if (flag && sWriteLevel <= VERBOSE) {
            if (sLogRecorder != null) {
                sLogRecorder.takeNotes(msg);
            }
        }
        if (flag && sPrintLevel <= VERBOSE) {
            System.out.println(tag + ": " + msg + " " + getLineNumber(t));
        }
    }

    public static void v(boolean flag, String tag, String msg, Throwable t) {
        if (flag && sWriteLevel <= VERBOSE) {
            if (sLogRecorder != null) {
                sLogRecorder.takeNotes(msg);
            }
        }
        if (flag && sPrintLevel <= VERBOSE) {
            Log.v(tag, msg + " " + getLineNumber(t));
        }
    }

    public static void d(boolean flag, String tag, String msg, Throwable t) {
        if (flag && sWriteLevel <= DEBUG) {
            if (sLogRecorder != null) {
                sLogRecorder.takeNotes(msg);
            }
        }
        if (flag && sPrintLevel <= DEBUG) {
            Log.d(tag, msg + " " + getLineNumber(t));
        }
    }

    public static void i(boolean flag, String tag, String msg, Throwable t) {
        if (flag && sWriteLevel <= INFO) {
            if (sLogRecorder != null) {
                sLogRecorder.takeNotes(msg);
            }
        }
        if (flag && sPrintLevel <= INFO) {
            Log.i(tag, msg + " " + getLineNumber(t));
        }
    }

    public static void w(boolean flag, String tag, String msg, Throwable t) {
        if (flag && sWriteLevel <= WARN) {
            if (sLogRecorder != null) {
                sLogRecorder.takeNotes(msg);
            }
        }
        if (flag && sPrintLevel <= WARN) {
            Log.w(tag, msg + " " + getLineNumber(t));
        }
    }

    public static void e(boolean flag, String tag, String msg, Throwable t) {
        if (flag && sWriteLevel <= ERROR) {
            if (sLogRecorder != null) {
                sLogRecorder.takeNotes(msg);
            }
        }
        if (flag && sPrintLevel <= ERROR) {
            Log.e(tag, msg + " " + getLineNumber(t));
        }
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
        if (e == null) {
            return "";
        }
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0) {
            return "";
        }
        return "[" + trace[0].getClassName().substring(trace[0].getClassName().lastIndexOf(".") + 1) + "-" + trace[0].getLineNumber() + "] ";
    }

    /**
     * 定义记录日志的功能的接口
     *
     * @author 宋疆疆
     * @date 2016/5/6.
     */
    public interface ILogRecorder {

        void setLogPath(String path);

        void takeNotes(Throwable e);

        void takeNotes(String message);
    }

}
