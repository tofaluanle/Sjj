package cn.sjj.net.download;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sjj.util.LazyUtil;

/**
 * 下载管理器，主要提供重复下载的判断逻辑与下载进度共享的逻辑
 *
 * @auther 宋疆疆
 * @since 2017/11/28.
 */
public class DownloadManager {

    private Map<String, List<DownloadTask>> mTaskMap;
    private Map<String, DownloadEngine>     mEngineMap;
    private Map<String, String>             mPathMap;

    private void init() {
        mTaskMap = new HashMap<>();
        mEngineMap = new HashMap<>();
        mPathMap = new HashMap<>();
    }

    public void add(DownloadTask task) {
        String url = mPathMap.get(task.getFilePath());
        if (!TextUtils.isEmpty(url) && !url.equals(task.getUrl())) {
            LazyUtil.runOnUI(() -> {
                task.getListener().onFail(DownloadListener.ERROR_PATH_CONFLICT);
            });
            return;
        }

        LazyUtil.runOnUI(() -> {
            task.getListener().onPending();
        });

        List<DownloadTask> tasks = mTaskMap.get(task.getUrl());
        if (tasks != null) {
            if (!tasks.contains(task)) {
                tasks.add(task);
            }

        } else {
            mPathMap.put(task.getFilePath(), task.getUrl());

            tasks = new ArrayList<>();
            tasks.add(task);
            mTaskMap.put(task.getUrl(), tasks);

            List<DownloadTask> finalTasks = tasks;

            DownloadEngine engine = new DownloadEngine(task.isAppend(), task.getFileName(), task.isForceDownload(), task.getPool(), task.getTargetDir(), task.getUrl(), task.getHeaderMap());
            engine.setListener(new SimpleDownloadListener() {
                @Override
                public void onProgress(long total, long progress) {
                    for (DownloadTask task : finalTasks) {
                        task.getListener().onProgress(total, progress);
                    }
                }

                @Override
                public void onSuccess(String filePath) {
                    for (DownloadTask task : finalTasks) {
                        task.getListener().onSuccess(filePath);
                    }
                    mPathMap.remove(task.getFilePath());
                    mTaskMap.remove(task.getUrl());
                    mEngineMap.remove(task.getUrl());
                }

                @Override
                public void onFail(int error) {
                    for (DownloadTask task : finalTasks) {
                        task.getListener().onFail(error);
                    }
                    mPathMap.remove(task.getFilePath());
                    mTaskMap.remove(task.getUrl());
                    mEngineMap.remove(task.getUrl());
                }

            });
            engine.download();

            mEngineMap.put(task.getUrl(), engine);
        }
    }

    public void stop(String url) {
        DownloadEngine engine = mEngineMap.remove(url);
        if (engine != null) {
            engine.cancel();

            List<DownloadTask> tasks = mTaskMap.remove(url);
            for (DownloadTask downloadTask : tasks) {
                LazyUtil.runOnUI(() -> {
                    downloadTask.getListener().onCancel();
                });
                mPathMap.remove(downloadTask.getFilePath());
            }
        }
    }

    private DownloadManager() {
        if (DownloadManagerHolder.ourInstance == null) {
            init();
        } else {
            throw new IllegalStateException("instance already create");
        }
    }

    public static DownloadManager getInstance() {
        return DownloadManagerHolder.ourInstance;
    }

    private static class DownloadManagerHolder {
        private static DownloadManager ourInstance = new DownloadManager();
    }
}
