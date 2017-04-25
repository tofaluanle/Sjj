package cn.sjj.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个公用的线程池单例类
 * @auther 宋疆疆
 * @date 2016/5/13.
 */
public class SubThreadPool {

    private ExecutorService mPool;


    private SubThreadPool() {
        init();
    }

    public static SubThreadPool getInstance() {
        return SubThreadPoolHolder.ourInstance;
    }

    private void init() {
        mPool = Executors.newFixedThreadPool(5);
    }

    public void execute(Runnable run) {
        mPool.execute(run);
    }

    private static class SubThreadPoolHolder {
        private static SubThreadPool ourInstance = new SubThreadPool();
    }
}
