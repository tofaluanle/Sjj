package cn.sjj.engine;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.sjj.util.SystemTool;

/**
 * 管理整个APP的所有缓存文件路径的单例类
 *
 * @author 宋疆疆
 * @since 2017/6/9.
 */
public class CacheDirManager {

    private String                         mInternalDir;
    private String                         mExternalDir;
    private String                         mIComeRootDir;
    private List<OnCacheDirChangeListener> mListenerList;
    private boolean                        mSdCanUse;
    private boolean                        mHasRequest;

    private CacheDirManager() {
    }

    public static CacheDirManager getInstance() {
        return CacheDirManagerHolder.ourInstance;
    }

    public void init(Context context) {
        mIComeRootDir = "";
        mListenerList = new CopyOnWriteArrayList<>();
        mInternalDir = context.getFilesDir().getAbsolutePath() + File.separator;
        mExternalDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        CacheDirPolicy.getInstance().init();
    }

    public void addListener(OnCacheDirChangeListener listener) {
        if (!mListenerList.contains(listener)) {
            mListenerList.add(listener);
        }
    }

    public void removeListener(OnCacheDirChangeListener listener) {
        mListenerList.remove(listener);
    }

    public void setHasRequest(boolean mHasRequest) {
        this.mHasRequest = mHasRequest;
    }

    public boolean isHasRequest() {
        return mHasRequest;
    }

    public String getInternalDir() {
        return mInternalDir;
    }

    public String getExternalDir() {
        return mExternalDir;
    }

    //获取文件的根路径
    public String getIComRootDir() {
        if (!SystemTool.isSDCardExist()) {
            mIComeRootDir = mInternalDir;
        } else if (TextUtils.isEmpty(mIComeRootDir)) {
            mIComeRootDir = mInternalDir;
        }
        return mIComeRootDir;
    }

    //设置SD卡是否可用并且已经赋予相关的权限
    public void setIComRootDir(boolean sdCanUse) {
        mHasRequest = true;
        if (sdCanUse) {
            mIComeRootDir = mExternalDir;
        } else {
            mIComeRootDir = mInternalDir;
        }
        if (mSdCanUse != sdCanUse) {
            for (OnCacheDirChangeListener listener : mListenerList) {
                listener.onChange(mIComeRootDir);
            }
        }
        mSdCanUse = sdCanUse;
    }

    private static class CacheDirManagerHolder {
        private static CacheDirManager ourInstance = new CacheDirManager();
    }

    public interface OnCacheDirChangeListener {
        void onChange(String path);
    }
}
