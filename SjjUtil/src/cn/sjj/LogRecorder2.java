package cn.sjj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sjj.util.FileUtil;
import cn.sjj.util.SystemTool;

/**
 * 将log记录到文件里的工具类
 *
 * @auther 宋疆疆
 * @date 2016/5/5.
 */
public class LogRecorder2 implements Logger.ILogRecorder {

    private static final String NOTE_PATH = "log###.txt";
    private static final int MAX_FILE_LENGTH = 100 * 1024;
    //日志文件的最大数量
    private static final int MAX_FILE_COUNT = 50;
    private Context mContext;
    private FileOutputStream mFos;
    private long mWriteCount;
    private SimpleDateFormat mFormatter;
    private int mPid;

    public LogRecorder2() {
        mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        mPid = Process.myPid();
    }

    /**
     * 初始化日志文件
     */
    public void initLogFile(Context context) {
        closeFos();
        mContext = context.getApplicationContext();
        File logFile = getLogFile();
        mWriteCount = logFile.length();
        try {
            mFos = new FileOutputStream(logFile.getAbsolutePath(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getLogFile() {
        int notExist = checkNotExistLogFile();
        int useCount = notExist - 1;
        if (useCount == 0) {
            useCount = MAX_FILE_COUNT + 1;
        }

        File file = new File(getDirPath(), NOTE_PATH.replace("###", useCount + ""));
        if (file.exists()) {
            long length = file.length();
            if (length >= MAX_FILE_LENGTH) {
                useCount = notExist;
                file = new File(getDirPath(), NOTE_PATH.replace("###", useCount + ""));
                try {
                    FileUtil.createFile(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            deleteNextLogFile(useCount);
        } else {
            try {
                FileUtil.createFile(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void deleteNextLogFile(int i) {
        i++;
        if (i > MAX_FILE_COUNT + 1) {
            i = 1;
        }
        File file = new File(getDirPath(), NOTE_PATH.replace("###", i + ""));
        if (file.exists()) {
            FileUtil.deleteFolder(file.getAbsolutePath());
        }
    }

    /**
     * 检查已经有了几个日志文件了
     *
     * @return 不存在的日志编号
     */
    private int checkNotExistLogFile() {
        for (int i = 1; i <= MAX_FILE_COUNT; i++) {
            File file = new File(getDirPath(), NOTE_PATH.replace("###", i + ""));
            if (!file.exists()) {
                return i;
            }
        }
        return MAX_FILE_COUNT + 1;
    }

    @NonNull
    private String getDirPath() {
        return mContext.getFilesDir().getAbsolutePath() + File.separator + SystemTool.getCurProcessName(mContext);
    }

    @Override
    public void takeNotes(Throwable e) {
        takeNotes(Logger.printStackTrace(e));
    }

    @Override
    public void takeNotes(String message) {
        if (mFos == null) {
            Log.e(Logger.sTAG, "why LogRecorder's mFos is null !?");
            return;
        }
        try {
            StringBuilder msg = new StringBuilder();
            msg.append("[ ");
            msg.append(mPid);
            msg.append(" ]");
            msg.append(mFormatter.format(System.currentTimeMillis()));
            msg.append(": ");
            msg.append(message + "\n");
            byte[] buffer = msg.toString().getBytes();
            mWriteCount += buffer.length;
            mFos.write(buffer);
            mFos.flush();
            isOverLength();
        } catch (Exception e) {
            e.printStackTrace();
            initLogFile(mContext);
        }
    }

    private void closeFos() {
        if (mFos != null) {
            try {
                mFos.flush();
                mFos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mFos = null;
        }
    }

    private void isOverLength() {
        if (mWriteCount >= MAX_FILE_LENGTH) {
            initLogFile(mContext);
        }
    }
}
