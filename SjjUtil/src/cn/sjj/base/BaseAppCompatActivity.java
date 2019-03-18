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
        Logger.i(DEBUG, this + " onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Logger.v(DEBUG, this + " onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Logger.v(DEBUG, this + " onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Logger.v(DEBUG, this + " onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.v(DEBUG, this + " onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.v(DEBUG, this + " onStop");
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Logger.i(DEBUG, this + " onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        Logger.i(DEBUG, this + " onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Logger.i(DEBUG, this + " onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + '@' + Integer.toHexString(hashCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i(DEBUG, this + " onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
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
