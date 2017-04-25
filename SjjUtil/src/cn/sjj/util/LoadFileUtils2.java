package cn.sjj.util;

import android.os.Handler;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import cn.sjj.base.BaseUtil;

/**
 * 下载文件的工具类，不使用okhttp下载，使用HttpUrlConnect进行下载
 * Created by 宋疆疆 on 2016/5/23.
 */
public class LoadFileUtils2 extends BaseUtil {

    //下载的文件的临时后缀
    public static final String TMP_EXT = ".tmp";

    private static List<String> sRunnerList = new ArrayList<>();
    private static Handler      sHandler    = new Handler(sContext.getMainLooper());
    private static HashMap<String, String> sDefaultHeaderMap;

    private LoadFileListener        mListener;
    private String                  mLocalFilePath;
    private long                    mFileLength;
    private long                    mFileWriteLength;
    private boolean                 mIsCancel;
    private ExecutorService         mPool;
    private String                  mTargetDir;
    private String                  mUrl;
    private String                  mFileName;
    private boolean                 mAppend;
    private boolean                 mForceDownload;
    private boolean                 mJudgeExist;
    private HashMap<String, String> mHeaderMap;

    private LoadFileUtils2(boolean append, String fileName, boolean forceDownload, boolean judgeExist, LoadFileListener listener, ExecutorService pool, String targetDir, String url, HashMap<String, String> headerMap) {
        mAppend = append;
        mFileName = fileName;
        mForceDownload = forceDownload;
        mJudgeExist = judgeExist;
        mListener = listener;
        mPool = pool;
        mTargetDir = targetDir;
        mUrl = url;
        mHeaderMap = headerMap;
    }

    /**
     * 你应该使用Builder构造一个LoadFileUtil2的实例，然后调用{@link #download()}
     */
    @Deprecated
    public LoadFileUtils2(ExecutorService pool, LoadFileListener mListener) {
        this.mListener = mListener;
        mPool = pool;
    }

    /**
     * 你应该使用Builder构造一个LoadFileUtil2的实例，然后调用{@link #download()}
     */
    @Deprecated
    public synchronized void download(String targetDir, String url, String fileName) {
        download(targetDir, url, fileName, false);
    }

    /**
     * 你应该使用Builder构造一个LoadFileUtil2的实例，然后调用{@link #download()}
     */
    @Deprecated
    public synchronized void download(String targetDir, String url, String fileName, boolean append) {
        download(targetDir, url, fileName, append, false);
    }

    /**
     * 你应该使用Builder构造一个LoadFileUtil2的实例，然后调用{@link #download()}
     */
    @Deprecated
    public synchronized void download(String targetDir, final String url, String fileName, final boolean append, boolean forceDownload) {
        mTargetDir = targetDir;
        mUrl = url;
        mFileName = fileName;
        download(targetDir, url, fileName, append, forceDownload, false);
    }

    /**
     * 你应该使用Builder构造一个LoadFileUtil2的实例，然后调用{@link #download()}
     */
    @Deprecated
    public synchronized void download(String targetDir, final String url, String fileName, final boolean append, boolean forceDownload, boolean judgeExist) {
        if (!paramInvalid()) {
            mListener.loadFileFail(LoadFileListener.ERROR_INVALID_PARAM);
            return;
        }
        final List<String> list = FileUtil.findFile(targetDir, fileName, true);
        mLocalFilePath = targetDir + File.separator + fileName;
        if (list == null || list.size() == 0) { // 不存在去云端请求
            checkRunnableBeforeDownload(url, append, judgeExist);
        } else {
            final String path = list.get(0);
            if (forceDownload) {
                boolean delete = new File(path).delete();
                checkRunnableBeforeDownload(url, append, judgeExist);
            } else {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.loadFileSuccess(path);
                    }
                });
            }
        }
    }

    private void checkRunnableBeforeDownload(final String url, final boolean append, boolean judgeExist) {
        if (judgeExist) {
            if (sRunnerList.contains(url)) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.loadFileFail(LoadFileListener.ERROR_DOWN_BY_OTHER_WORKER);
                    }
                });
                return;
            }
            sRunnerList.add(url);
            try {
                mPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        downFile(url, append);
                    }
                });
            } catch (RejectedExecutionException e) {
                throw e;
            }
        } else {
            try {
                mPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        downFile(url, append);
                    }
                });
            } catch (RejectedExecutionException e) {
                throw e;
            }
        }
    }

    private boolean paramInvalid() {
        if (mListener == null) {
            throw new RuntimeException("LoadFileListener should not be null");
        }
        if (mPool == null) {
            return false;
        }
        if (TextUtils.isEmpty(mTargetDir)) {
            return false;
        }
        if (TextUtils.isEmpty(mUrl)) {
            return false;
        }
        if (TextUtils.isEmpty(mFileName)) {
            return false;
        }
        return true;
    }

    /**
     * 调用这个方法时，你必须使用Builder来构建LoadFileUtil2
     */
    public synchronized void download() {
        download(mTargetDir, mUrl, mFileName, mAppend, mForceDownload, mJudgeExist);
    }

    private void downFile(String url, boolean append) {
        if (TextUtils.isEmpty(mLocalFilePath)) {
            return;
        }
        InputStream is = null;
        File tmpFile = new File(mLocalFilePath + TMP_EXT);
        if (!append && tmpFile.exists()) {
            boolean delete = tmpFile.delete();
            if (!delete) {
                onDownloadFail(url, tmpFile);
                return;
            }
        }
        HttpURLConnection urlConn = null;
        try {
            boolean create = FileUtil.createFile(tmpFile.getAbsolutePath());
            if (!create) {
                onDownloadFail(url, tmpFile);
                return;
            }
            URL imgUrl = new URL(url);
            urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setDoInput(true);
            mFileWriteLength = tmpFile.length();
            urlConn.setRequestProperty("Range", "bytes=" + mFileWriteLength + "-");
            for (Map.Entry<String, String> header : sDefaultHeaderMap.entrySet()) {
                urlConn.setRequestProperty(header.getKey(), header.getValue());
            }
            if (mHeaderMap != null) {
                for (String key : mHeaderMap.keySet()) {
                    urlConn.setRequestProperty(key, mHeaderMap.get(key));
                }
            }
            urlConn.setConnectTimeout(30 * 1000);
            urlConn.setReadTimeout(30 * 1000);
            urlConn.connect();
            int responseCode = urlConn.getResponseCode();
            if (responseCode == 200 || responseCode == 206) {
                is = urlConn.getInputStream();
                String ContentRange = urlConn.getHeaderField("Content-Range");
                try {
                    mFileLength = Long.parseLong(ContentRange.split("/")[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                    mFileLength = urlConn.getContentLength();
                }
                boolean writeFile = FileUtil.writeFile(tmpFile, is, append, new FileUtil.WriteListener() {
                    @Override
                    public void onWrite(int len) {
                        mFileWriteLength += len;
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mListener.loadFileProgress(mFileLength, mFileWriteLength);
                            }
                        });
                    }

                    @Override
                    public boolean isCancel() {
                        return mIsCancel;
                    }
                });
                if (mIsCancel) {

                } else if (writeFile) {
                    final File realFile = new File(mLocalFilePath);
                    boolean rename = tmpFile.renameTo(realFile);
                    if (rename) {
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mListener.loadFileSuccess(realFile.getAbsolutePath());
                            }
                        });
                    } else {
                        onDownloadFail(url, tmpFile);
                    }
                } else {
                    onDownloadFail(url, tmpFile);
                }
            } else {
                onDownloadFail(url, tmpFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onDownloadFail(url, tmpFile);
        } finally {
            sRunnerList.remove(url);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
    }

    private void onDownloadFail(String url, File tmpFile) {
        FileUtil.deleteFolder(tmpFile.getAbsolutePath());
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.loadFileFail(LoadFileListener.ERROR_FAIL);
            }
        });
    }

    public void cancel() {
        mIsCancel = true;
    }

    public static String getError(int error) {
        String msg = "";
        switch (error) {
            case LoadFileListener.ERROR_INVALID_PARAM:
                msg = "INVALID_PARAM";
                break;
            case LoadFileListener.ERROR_FAIL:
                msg = "FAIL";
                break;
            case LoadFileListener.ERROR_DOWN_BY_OTHER_WORKER:
                msg = "DOWN_BY_OTHER_WORKER";
                break;
        }
        return msg;
    }

    public interface LoadFileListener {

        int ERROR_FAIL                 = 1;                     //下载失败
        int ERROR_DOWN_BY_OTHER_WORKER = 2;     //已经有其他线程在下载同一个url的文件
        int ERROR_INVALID_PARAM        = 3;     //配置参数不正确

        void loadFileProgress(long total, long progress);

        void loadFileSuccess(String filePath);

        void loadFileFail(int error);

    }

    /**
     * 提供一个tag属性，用来防止滑动错位的问题
     */
    public static abstract class LoadFileListenerAdapter implements LoadFileListener {

        protected Object mTag;

        public LoadFileListenerAdapter() {
        }

        public LoadFileListenerAdapter(Object tag) {
            this.mTag = tag;
        }

    }

    public static class Builder {

        private ExecutorService         pool;
        private LoadFileListener        listener;
        private String                  targetDir;
        private String                  url;
        private String                  fileName;
        private boolean                 append;
        private boolean                 forceDownload;
        private boolean                 judgeExist;
        private HashMap<String, String> headerMap;

        public LoadFileUtils2 build() {
            LoadFileUtils2 util = new LoadFileUtils2(append, fileName, forceDownload, judgeExist, listener, pool, targetDir, url, headerMap);
            return util;
        }

        /**
         * 是否断点续传
         *
         * @param append 默认false
         * @return
         */
        public Builder setAppend(boolean append) {
            this.append = append;
            return this;
        }

        /**
         * 下载的文件保存的文件名，必须设置
         *
         * @param fileName
         * @return
         */
        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * 如果本地已经存在此文件，是否强制重新下载
         *
         * @param forceDownload 默认false
         * @return
         */
        public Builder setForceDownload(boolean forceDownload) {
            this.forceDownload = forceDownload;
            return this;
        }

        /**
         * 设置同一个url同时只能有一个任务在下载
         *
         * @param judgeExist 默认false；true时，会对url进行判断，如果已经有一个任务已经在下载此url时，会返回下载失败
         * @return
         */
        public Builder setJudgeExist(boolean judgeExist) {
            this.judgeExist = judgeExist;
            return this;
        }

        /**
         * 下载的回调接口，必须设置
         *
         * @param listener
         * @return
         */
        public Builder setListener(LoadFileListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 下载的子线程池，必须设置
         *
         * @param pool
         * @return
         */
        public Builder setPool(ExecutorService pool) {
            this.pool = pool;
            return this;
        }

        /**
         * 下载文件的存放目录，必须设置
         *
         * @param targetDir
         * @return
         */
        public Builder setTargetDir(String targetDir) {
            this.targetDir = targetDir;
            return this;
        }

        /**
         * 下载的url，必须设置
         *
         * @param url
         * @return
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * 添加头信息到http请求中
         *
         * @param key
         * @param value
         * @return
         */
        public Builder addHeader(String key, String value) {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put(key, value);
            return this;
        }

        /**
         * 添加默认的头信息到http请求中
         *
         * @param key
         * @param value
         * @return
         */
        public static void addDefaultHeader(String key, String value) {
            if (sDefaultHeaderMap == null) {
                sDefaultHeaderMap = new HashMap<>();
            }
            sDefaultHeaderMap.put(key, value);
        }
    }
}
