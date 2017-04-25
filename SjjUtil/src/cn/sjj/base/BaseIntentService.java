package cn.sjj.base;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.sjj.IStatics;
import cn.sjj.Logger;

/**
 * @auther 宋疆疆
 * @date 2016/3/14.
 */
public class BaseIntentService extends IntentService {

    protected static final boolean DEBUG = IStatics.DEBUG;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v(DEBUG, this + " Create");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.v(DEBUG, this + " Bind");
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.v(DEBUG, this + " onHandleIntent");

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
        Logger.v(DEBUG, this + " Destroy");
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + '@' + Integer.toHexString(hashCode());
    }
}
