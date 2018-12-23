package cn.sjj.net.download;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.sjj.IStatics;
import cn.sjj.Logger;
import cn.sjj.util.FileUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 下载文件的工具类，不使用okhttp下载，使用HttpUrlConnect进行下载
 *
 * @author 宋疆疆
 * @since 2017/11/28.
 */
public class DownloadEngine {

    private static HashMap<String, String> sDefaultHeaderMap = new HashMap<>();
    private static ExecutorService         mDefaultPool      = Executors.newFixedThreadPool(5);

    protected static Handler sHandler = new Handler(Looper.getMainLooper());

    private long                      mFileLength;
    private long                      mFileWriteLength;
    private boolean                   mIsCancel;
    private ExecutorService           mPool;
    private DownloadListener          mListener;
    private String                    mLocalFilePath;
    private String                    mTargetDir;
    private String                    mUrl;
    private String                    mFileName;
    private boolean                   mAppend;
    private boolean                   mForceDownload;
    private HashMap<String, String>   mHeaderMap;
    private Disposable                mProgressDisposable;
    private ObservableEmitter<String> mNotifyEmitter;
    private boolean                   mIsDone;

    public static void addDefaultHeader(String key, String value) {
        sDefaultHeaderMap.put(key, value);
    }

    public DownloadEngine(boolean append, String fileName, boolean forceDownload, ExecutorService pool, String targetDir, String url, HashMap<String, String> headerMap) {
        mAppend = false;
        mFileName = fileName;
        mForceDownload = forceDownload;
        mPool = pool;
        mTargetDir = targetDir;
        mUrl = url;
        mHeaderMap = headerMap;

        init();
    }

    private void init() {
        if (mPool == null) {
            mPool = mDefaultPool;
        }
        mProgressDisposable = Observable
                .create((ObservableEmitter<String> e) -> {
                    mNotifyEmitter = e;
                })
                .sample(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (!mIsCancel && !mIsDone) {
                        mListener.onProgress(mFileLength, mFileWriteLength);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void setListener(DownloadListener mListener) {
        this.mListener = mListener;
    }

    public synchronized void download() {
        if (!paramInvalid()) {
            sHandler.post(() -> {
                mListener.onFail(DownloadListener.ERROR_INVALID_PARAM);
            });
            destroy();
            return;
        }

        File file = new File(mTargetDir, mFileName);
        mLocalFilePath = mTargetDir + File.separator + mFileName;
        if (!file.exists()) { // 不存在去云端请求
            mPool.execute(() -> {
                downFile();
            });
        } else {
            final String path = file.getAbsolutePath();
            if (mForceDownload) {
                boolean delete = FileUtil.deleteFolder(path);
                if (!delete) {
                    Logger.e("delete fail, path: " + path);
                }
                mPool.execute(() -> {
                    downFile();
                });
            } else {
                sHandler.post(() -> {
                    mListener.onSuccess(path);
                    destroy();
                });
            }
        }
    }

    private void destroy() {
        mIsDone = true;
        mProgressDisposable.dispose();
    }

    private boolean paramInvalid() {
        if (mListener == null) {
            throw new RuntimeException("DownloadListener should not be null");
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

    private void downFile() {
        if (TextUtils.isEmpty(mLocalFilePath)) {
            destroy();
            return;
        }

        InputStream is = null;
        File tmpFile = new File(mLocalFilePath + IStatics.SUFFIX_TMP);
        if (!mAppend && tmpFile.exists()) {
            boolean delete = tmpFile.delete();
            if (!delete) {
                Logger.e("delete fail, path: " + tmpFile);
                onDownloadFail(tmpFile);
                return;
            }
        }

        HttpURLConnection urlConn = null;
        try {
            boolean create = FileUtil.createFile(tmpFile.getAbsolutePath());
            if (!create) {
                Logger.e("create fail, path: " + tmpFile);
                onDownloadFail(tmpFile);
                return;
            }

            URL imgUrl = new URL(mUrl);
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
                boolean writeFile = FileUtil.writeFile(tmpFile, is, mAppend, new FileUtil.WriteListener() {

                    @Override
                    public void onWrite(long total, int len) {
                        mFileWriteLength += len;
                        mNotifyEmitter.onNext("");
                    }

                    @Override
                    public boolean isCancel() {
                        return mIsCancel;
                    }
                });
                mIsDone = true;
                mProgressDisposable.dispose();
                if (mIsCancel) {

                } else if (writeFile) {
                    final File realFile = new File(mLocalFilePath);
                    boolean rename = tmpFile.renameTo(realFile);
                    if (rename) {
                        sHandler.post(() -> {
                            mListener.onSuccess(realFile.getAbsolutePath());
                            destroy();
                        });
                    } else {
                        onDownloadFail(tmpFile);
                    }
                } else {
                    onDownloadFail(tmpFile);
                }
            } else {
                onDownloadFail(tmpFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onDownloadFail(tmpFile);
        } finally {
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

    private void onDownloadFail(File tmpFile) {
        FileUtil.deleteFolder(tmpFile.getAbsolutePath());
        sHandler.post(() -> {
            mListener.onFail(DownloadListener.ERROR_FAIL);
        });
        destroy();
    }

    public void cancel() {
        mIsCancel = true;
    }

    public static String getError(int error) {
        String msg = "";
        switch (error) {
            case DownloadListener.ERROR_INVALID_PARAM:
                msg = "INVALID_PARAM";
                break;
            case DownloadListener.ERROR_FAIL:
                msg = "FAIL";
                break;
            case DownloadListener.ERROR_PATH_CONFLICT:
                msg = "PATH_CONFLICT";
                break;
        }
        return msg;
    }

}
