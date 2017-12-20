package cn.sjj.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import cn.sjj.IStatics;
import cn.sjj.Logger;

/**
 * Activity的基类，提供一些框架性的代码
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    protected static final boolean DEBUG    = IStatics.DEBUG;
    protected static final boolean UI_DEBUG = IStatics.UI_DEBUG;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    protected abstract void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(DEBUG, this + " onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.v(DEBUG, this + " onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.v(DEBUG, this + " onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.v(DEBUG, this + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.v(DEBUG, this + " onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.v(DEBUG, this + " onStop");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logger.i(DEBUG, this + " onRestoreInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(DEBUG, this + " onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.i(DEBUG, this + " onNewIntent");
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + '@' + Integer.toHexString(hashCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(DEBUG, this + " onActivityResult");
    }

    protected void findView() {
    }

    protected void registerListener() {
    }

    protected void runAfterOnResume(Runnable run) {
        mUIHandler.post(() -> {
            mUIHandler.post(run);
        });
    }

}
