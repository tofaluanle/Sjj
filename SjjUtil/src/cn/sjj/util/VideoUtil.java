package cn.sjj.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

/**
 * 视频相关的工具类
 * @author 宋疆疆
 * @date 2016/5/12.
 */
public class VideoUtil {

    public static Bitmap getThumbnail(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        Bitmap bitmap = null;
        try {
            media.setDataSource(path);
            bitmap = media.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static long getDuration(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        long duration = 0;
        try {
            media.setDataSource(path);
            duration = Long.parseLong(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

}
