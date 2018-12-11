package cn.sjj.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.sjj.Logger;


/**
 * @author 宋疆疆
 * @date 2016/5/6.
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.v(this + " onReceive");
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + '@' + Integer.toHexString(hashCode());
    }
}
