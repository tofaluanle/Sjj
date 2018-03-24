package cn.sjj.delete_dir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Delete {

    private static StringBuilder sb;
    private static String mRoot = "/Users/songjj/WorkSpace/";
    private static List<String> sIgnorePath;

    public static void main(String[] args) throws Exception {
        initIgnorePath();

        boolean delete = true;
        if (args.length > 0) {
            switch (args[0]) {
                case "-s":
                    delete = false;
                    break;
                case "-d":
                    delete = true;
                    break;
            }
        }
        sb = new StringBuilder();
        sb.append(new Date(System.currentTimeMillis()) + "\n");

        List<String> paths = new ArrayList<>();
        paths.add(mRoot + "/AndroidStudio");
//        String[] names = new String[]{"bin", "obj", "Debug", "Release", "build"};
        String[] names = new String[]{"build"};
        for (String path : paths) {
            deleteDir(path, names, delete);
        }

        String notePath = mRoot + "/[DeleteIndex/" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(System.currentTimeMillis()) + "_deleteIndex.txt";
        File file = new File(notePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        writeFile(notePath, sb.toString());
    }

    private static void initIgnorePath() {
        sIgnorePath = new ArrayList<>();
        sIgnorePath.add("/Users/songjj/WorkSpace/AndroidStudio/StudyGradle/gradleSrc/");
        sIgnorePath.add("/Users/songjj/WorkSpace/AndroidStudio/Sjj/deletedir/build");
    }

    // 循环遍历指定目录里的文件和文件夹
    public static void deleteDir(String path, String[] names, boolean exDelete) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    if (isIgnorePath(file)) {
                        continue;
                    }

                    boolean isTarDir = false;
                    for (String name : names) {
                        if (name.equals(file.getName())) {
                            boolean delete = false;
                            if (exDelete) {
                                delete = deleteDirectory(file.getAbsolutePath());
                            }
                            System.out.println("delete: " + delete + " " + file.getAbsolutePath());
                            isTarDir = true;
                            sb.append("delete: " + delete + " " + file.getAbsolutePath() + "\n");
                            break;
                        }
                    }
                    if (!isTarDir) {
                        deleteDir(file.getAbsolutePath(), names, exDelete);
                    }
                }

            }
        }
    }

    private static boolean isIgnorePath(File file) {
        for (String path : sIgnorePath) {
            if (file.getAbsolutePath().contains(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean writeFile(String filePath, String contents)
            throws Exception {
        File file = new File(filePath);

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);

        String[] split = contents.split("\n");
        for (String string : split) {
            // System.out.println(string);
            bw.write(string + "\n");
        }

        bw.close();
        osw.close();
        fos.close();
        return true;
    }
}
