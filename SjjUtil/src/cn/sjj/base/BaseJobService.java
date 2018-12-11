package cn.sjj.base;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.sjj.IStatics;
import cn.sjj.Logger;

/**
 * Service的通用基类，提供打印生命周期的功能
 *
 * @author 宋疆疆
 * @date 2016/3/14.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BaseJobService extends JobService {

    protected static final boolean DEBUG = IStatics.DEBUG;

    private ExecutorService mPool;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v(DEBUG, this + " Create");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.v(DEBUG, this + " onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Logger.v(DEBUG, this + " onStopJob");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.v(DEBUG, this + " StartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.v(DEBUG, this + " Unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Logger.v(DEBUG, this + " Rebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPool != null) {
            mPool.shutdown();
            mPool = null;
        }
        Logger.v(DEBUG, this + " Destroy");
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + '@' + Integer.toHexString(hashCode());
    }

    public ExecutorService getFixPool() {
        return getFixPool(5);
    }

    public ExecutorService getFixPool(int count) {
        if (mPool != null) {
            return mPool;
        }
        mPool = Executors.newFixedThreadPool(count);
        return mPool;
    }

}
