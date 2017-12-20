package cn.sjj.net.download;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * 保存下载任务的信息
 *
 * @auther 宋疆疆
 * @since 2017/11/28.
 */
public class DownloadTask {

    private ExecutorService         mPool;
    private DownloadListener        mListener;
    private String                  mTargetDir;
    private String                  mUrl;
    private String                  mFileName;
    private String                  mFilePath;
    private boolean                 mAppend;
    private boolean                 mForceDownload;
    private HashMap<String, String> mHeaderMap;

    private DownloadTask(boolean append, String fileName, boolean forceDownload, DownloadListener listener, ExecutorService pool, String targetDir, String url, HashMap<String, String> headerMap) {
        mAppend = append;
        mFileName = fileName;
        mForceDownload = forceDownload;
        mListener = listener;
        mPool = pool;
        mTargetDir = targetDir;
        mUrl = url;
        mHeaderMap = headerMap;
        mFilePath = mTargetDir + File.separator + mFileName;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public ExecutorService getPool() {
        return mPool;
    }

    public DownloadListener getListener() {
        return mListener;
    }

    public String getTargetDir() {
        return mTargetDir;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFileName() {
        return mFileName;
    }

    public boolean isAppend() {
        return mAppend;
    }

    public boolean isForceDownload() {
        return mForceDownload;
    }

    public HashMap<String, String> getHeaderMap() {
        return mHeaderMap;
    }

    public static class Builder {

        protected ExecutorService         pool;
        protected DownloadListener        listener;
        protected String                  targetDir;
        protected String                  url;
        protected String                  fileName;
        protected boolean                 append;
        protected boolean                 forceDownload;
        protected HashMap<String, String> headerMap;

        public DownloadTask build() {
            DownloadTask util = new DownloadTask(append, fileName, forceDownload, listener, pool, targetDir, url, headerMap);
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
         * 下载的回调接口，必须设置
         *
         * @param listener
         * @return
         */
        public Builder setListener(DownloadListener listener) {
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

    }
}
