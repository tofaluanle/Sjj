package cn.sjj.engine;

/**
 * APP的缓存文件路径的变化时，要进行相应的逻辑处理，写在此类里
 *
 * @author 宋疆疆
 * @since 2017/6/12.
 */
public class CacheDirPolicy implements CacheDirManager.OnCacheDirChangeListener {

    private CacheDirPolicy() {
    }

    public static CacheDirPolicy getInstance() {
        return CacheDirPolicyHolder.ourInstance;
    }

    public void init() {
        CacheDirManager.getInstance().addListener(this);
    }

    @Override
    public void onChange(String path) {
    }

    private static class CacheDirPolicyHolder {
        private static CacheDirPolicy ourInstance = new CacheDirPolicy();
    }
}
