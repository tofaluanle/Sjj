package cn.sjj.engine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.CopyOnWriteArrayList;

import cn.sjj.Logger;
import cn.sjj.util.NetUtil;

/**
 * 监听网络状态变化的类
 *
 * @auther 宋疆疆
 * @since 2017/9/13.
 */
public class NetworkStatus implements Handler.Callback {

    private static final int WHAT_NET_CHANG_NO_CONNECT = 1;
    private static final int WHAT_NET_CHANG_CONNECT    = 2;

    private Handler                        mHandler;
    private NetReceiver                    mNR;
    private Context                        mContext;
    private CopyOnWriteArrayList<Listener> mListeners;

    public void init(Context context) {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper(), this);
        mListeners = new CopyOnWriteArrayList<>();
        mNR = new NetReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(mNR, filter);
    }

    public void destroy() {
        mListeners.clear();
        mListeners = null;
        try {
            if (mContext != null && mNR != null) {
                mContext.unregisterReceiver(mNR);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        mContext = null;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_NET_CHANG_CONNECT:
                for (Listener listener : mListeners) {
                    listener.onChange(true);
                }
                return true;
            case WHAT_NET_CHANG_NO_CONNECT:
                for (Listener listener : mListeners) {
                    listener.onChange(false);
                }
                return true;
        }
        return false;
    }

    public void addListener(Listener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    private NetworkStatus() {
        if (NetworkStateHolder.ourInstance == null) {
        } else {
            throw new IllegalStateException("instance already create");
        }
    }

    public static NetworkStatus getInstance() {
        return NetworkStateHolder.ourInstance;
    }

    private static class NetworkStateHolder {
        private static NetworkStatus ourInstance = new NetworkStatus();
    }

    private class NetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnect = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            mHandler.removeMessages(WHAT_NET_CHANG_CONNECT);
            mHandler.removeMessages(WHAT_NET_CHANG_NO_CONNECT);
            boolean connected = NetUtil.isNetworkConnected();
            Logger.i("NetReceiver no connect: " + noConnect + ", connected: " + connected);
            if (connected) {
                mHandler.sendEmptyMessageDelayed(WHAT_NET_CHANG_CONNECT, 1 * 1000);
            } else {
                mHandler.sendEmptyMessageDelayed(WHAT_NET_CHANG_NO_CONNECT, 1 * 1000);
            }
        }
    }

    public interface Listener {
        void onChange(boolean connect);
    }
}
