package cn.sjj.net.download;

/**
 * 下载的回调监听接口
 *
 * @author 宋疆疆
 * @since 2017/11/28.
 */
public interface DownloadListener {

    int ERROR_FAIL          = 1;     //下载失败
    int ERROR_PATH_CONFLICT = 2;     //下载路径冲突
    int ERROR_INVALID_PARAM = 3;     //配置参数不正确

    void onPending();

    void onProgress(long total, long progress);

    void onSuccess(String filePath);

    void onFail(int error);

    void onCancel();

}