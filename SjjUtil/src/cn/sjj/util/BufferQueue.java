package cn.sjj.util;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 用来缓存对象的FIFO队列，默认容量100，超过后会废弃掉最旧的对象
 *
 * @author 宋疆疆
 * @since 2018/2/8.
 */
public class BufferQueue<T> implements Runnable {

    private ArrayBlockingQueue<T> mQueue;
    private Thread                mTakeThread;
    private boolean               mEnable;
    private CallBack<T>           mCallBack;

    public BufferQueue(CallBack<T> mCallBack) {
        this.mCallBack = mCallBack;
        init(100);
    }

    public BufferQueue(int capacity, CallBack<T> mCallBack) {
        this.mCallBack = mCallBack;
        init(capacity);
    }

    private void init(int capacity) {
        mEnable = true;
        mQueue = new ArrayBlockingQueue(capacity);
        mTakeThread = new Thread(this, "BufferQueue take Thread@" + this.hashCode());
        mTakeThread.start();
    }

    public void add(T t) {
        boolean offer = mQueue.offer(t);
        if (!offer) {
            mQueue.poll();
            mQueue.offer(t);
        }
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
        if (mEnable) {
            synchronized (this) {
                notify();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                T take = mQueue.take();
                synchronized (this) {
                    if (!mEnable) {
                        wait();
                    }
                }
                mCallBack.onTake(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface CallBack<T> {
        void onTake(T t);
    }
}
