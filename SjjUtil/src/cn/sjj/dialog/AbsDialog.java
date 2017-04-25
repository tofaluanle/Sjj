package cn.sjj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

/**
 * 对话框的抽象类，提供自动隐藏的功能
 *
 * @author 宋疆疆
 * @date 2015-06-25
 */
public abstract class AbsDialog extends Dialog {

    private static final int HIDE_TIME = 10 * 1000;
    protected Context mContext;
    private HideRunnable hideR;
    private Handler mHandler = new Handler();

    public AbsDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        hideR = new HideRunnable();

        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                mHandler.removeCallbacks(hideR);
                mHandler.postDelayed(hideR, HIDE_TIME);
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        mHandler.postDelayed(hideR, HIDE_TIME);
    }

    @Override
    public void onDetachedFromWindow() {
        mHandler.removeCallbacks(hideR);
    }

    /**
     * 自动隐藏对话框的线程
     */
    private final class HideRunnable implements Runnable {

        @Override
        public void run() {
            if (isShowing()) {
                dismiss();
            }
        }

    }

}
