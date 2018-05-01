package cn.sjj.tool;

import java.io.FileInputStream;

/**
 * @auther 宋疆疆
 * @since 2018/4/25.
 */
public class FileCharsetConvert {

    static String path;
    static String oldCharset;
    static String newCharset;

    public static void main(String[] args) throws Exception {
        path = args[0];
        oldCharset = args[1];
        newCharset = args[2];

        FileInputStream fis = new FileInputStream(path);
        if (oldCharset.equals("UTF-8+")) {
            fis.read();
            fis.read();
            fis.read();
            oldCharset = "UTF-8";
        }
        try {
            String content = FileUtil.readFile(fis, oldCharset);
            FileUtil.writeFile(path, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
