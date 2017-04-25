package cn.sjj;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 将log记录到文件里的工具类
 *
 * @auther 宋疆疆
 * @date 2016/5/5.
 */
public class LogRecorder {

    private static final String NOTE_PATH = "log###.txt";
    private static int useFileCount;

    private static Context mContext;

    public static void takeNotes(Throwable e) {
        FileOutputStream fos = null;
        try {
            fos = mContext
                    .openFileOutput(getLogFileName(), Context.MODE_APPEND);
            String msg = "\n\n";
            msg += android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss",
                    System.currentTimeMillis());
            msg += "====================================================";
            msg += "\n\n";
            msg += Logger.printStackTrace(e);
            byte[] buffer = msg.getBytes();
            fos.write(buffer);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                fos = null;
            }
        }
    }

    public static void takeNotes(String message) {
        FileOutputStream fos = null;
        try {
            fos = mContext
                    .openFileOutput(getLogFileName(), Context.MODE_APPEND);
            String msg = "\n\n";
            msg += android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss",
                    System.currentTimeMillis());
            msg += "====================================================";
            msg += "\n\n";
            msg += message;
            byte[] buffer = msg.getBytes();
            fos.write(buffer);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                fos = null;
            }
        }
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    /**
     * 初始化日志文件
     *
     * @param count 日志文件的最大数量
     */
    public static void initLogFile(int count) {
        int notExistCount = checkNotExistLogFile(count);
        useFileCount = notExistCount;
        deleteLogFile(count, notExistCount);
    }

    /**
     * 检查已经有了几个日志文件了
     *
     * @param count 日志文件的最大数量
     * @return 不存在的日志编号
     */
    private static int checkNotExistLogFile(int count) {
        for (int i = 1; i <= count; i++) {
            File file = new File(mContext.getFilesDir(), NOTE_PATH.replace(
                    "###", i + ""));
            if (!file.exists())
                return i;
        }
        return count + 1;
    }

    /**
     * 根据最大日志量和当前不存在的日志编号，删除最旧的日志
     *
     * @param count
     * @param notExistCount
     */
    private static void deleteLogFile(int count, int notExistCount) {
        int deleteCount = 0;
        if ((count + 1) == notExistCount) {
            deleteCount = 1;
        } else {
            deleteCount = notExistCount + 1;
        }
        File deleteFile = new File(mContext.getFilesDir(), NOTE_PATH.replace(
                "###", deleteCount + ""));
        deleteFile.delete();
    }

    /**
     * 得到应该使用的日志文件的名字
     *
     * @return
     */
    private static String getLogFileName() {
        return NOTE_PATH.replace("###", useFileCount + "");
        // return NOTE_PATH.replace("###",
        // DateFormat.format("_yyyy_MM_dd", System.currentTimeMillis()));
    }

}
