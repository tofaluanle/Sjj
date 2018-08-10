package cn.sjj.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @auther 宋疆疆
 * @since 2018/1/1.
 */
public class GZipUtil {

    public static byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return compress(bytes);
    }

    public static byte[] compress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        return compress(str.getBytes());
    }

    public static byte[] compress(byte[] originBytes) {
        if (originBytes == null || originBytes.length == 0) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(originBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            LazyUtil.close(out);
            LazyUtil.close(gzip);
        }

        byte[] bytes = out.toByteArray();
        return bytes;
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = null;
        try {
            ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            LazyUtil.close(out);
            LazyUtil.close(ungzip);
            LazyUtil.close(in);
        }

        return out.toByteArray();
    }
}
