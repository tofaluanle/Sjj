package cn.sjj;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;

import cn.sjj.util.FileUtil;
import cn.sjj.util.SystemTool;

/**
 * 将log记录到文件里的工具类
 *
 * @auther 宋疆疆
 * @date 2016/5/5.
 */
public class LogRecorder2 implements Logger.ILogRecorder {

    private static final String NOTE_PATH       = "log###.txt";
    private static final int    MAX_FILE_LENGTH = 100 * 1024;
    private static final int    MAX_FILE_COUNT  = 50;       //日志文件的最大数量

    private FileOutputStream           mFos;
    private long                       mWriteCount;
    private String                     mLogPath;
    private ArrayBlockingQueue<String> mBlockQueue;
    private boolean                    mPathChanged;
    private WriteThread                mWriteThread;

    public LogRecorder2(Context context) {
        mLogPath = context.getFilesDir().getAbsolutePath() + File.separator + SystemTool.getCurProcessName() + File.separator;
        initLogFile();
        mBlockQueue = new ArrayBlockingQueue<>(1000);
        mWriteThread = new WriteThread();
        mWriteThread.start();
    }

    /**
     * 初始化日志文件
     */
    private void initLogFile() {
        closeFos();
        File logFile = getLogFile();
        try {
            mFos = new FileOutputStream(logFile.getAbsolutePath(), true);
            mWriteCount = logFile.length();
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

        File file = new File(mLogPath, NOTE_PATH.replace("###", useCount + ""));
        if (file.exists()) {
            long length = file.length();
            if (length >= MAX_FILE_LENGTH) {
                useCount = notExist;
                file = new File(mLogPath, NOTE_PATH.replace("###", useCount + ""));
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
        File file = new File(mLogPath, NOTE_PATH.replace("###", i + ""));
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
            File file = new File(mLogPath, NOTE_PATH.replace("###", i + ""));
            if (!file.exists()) {
                return i;
            }
        }
        return MAX_FILE_COUNT + 1;
    }

    @Override
    public void setLogPath(String path) {
        this.mLogPath = path;
        mPathChanged = true;
    }

    @Override
    public void takeNotes(Throwable e) {
        takeNotes(Logger.printStackTrace(e));
    }

    @Override
    public void takeNotes(String message) {
        boolean offer = mBlockQueue.offer(message);
        if (!offer) {
            Log.e(Logger.sTAG, "LogRecord2's BlockQueue offer fail");
        }
    }

    private void closeFos() {
        if (mFos != null) {
            try {
                mFos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mFos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mFos = null;
        }
    }

    private void isOverLength() {
        if (mWriteCount >= MAX_FILE_LENGTH) {
            initLogFile();
        }
    }

    private class WriteThread extends Thread {

        public WriteThread() {
            super("LogRecord2 write thread");
        }

        @Override
        public void run() {
            super.run();
            StringBuilder msg = new StringBuilder();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            msg.append("[ ");
            msg.append(Process.myPid());
            msg.append(" ]");
            while (true) {
                try {
                    String message = mBlockQueue.take();
                    if (mPathChanged) {
                        initLogFile();
                    }
                    if (mFos == null) {
                        Log.e(Logger.sTAG, "why LogRecorder's mFos is null !?");
                        continue;
                    }
                    msg.delete(msg.indexOf("]") + 1, msg.length());
                    msg.append(format.format(System.currentTimeMillis()));
                    msg.append(": ");
                    msg.append(message + "\n");
                    byte[] buffer = msg.toString().getBytes();
                    mWriteCount += buffer.length;
                    mFos.write(buffer);
                    mFos.flush();
                    isOverLength();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    initLogFile();
                }
            }
        }

    }
}
