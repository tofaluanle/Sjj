package cn.sjj.util;

import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 对文件和文件夹的操作的支持类
 *
 * @author 宋疆疆
 */
public class FileUtil {

    private static final boolean DEBUG = false;

    // 循环遍历指定目录里的文件和文件夹
    public static void lsFiles(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    System.out.println("dir: " + file.getName());
                }
                if (file.isFile()) {
                    System.out.println("file: " + file.getName());
                }
            }
        }
    }

    // 复制文件夹
    public static void copyDirectory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        createDirectory(targetDir, true, true, true);
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(
                        new File(targetDir).getAbsolutePath() + File.separator
                                + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectory(dir1, dir2);
            }
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;

        if (!targetFile.exists()) {
            createFile(targetFile.getAbsolutePath(), true, true, true);
        }

        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    // 复制文件
    public static void copyFile(String srcPath, String tarPath)
            throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        File sourceFile = new File(srcPath);
        File targetFile = new File(tarPath);
        if (!targetFile.exists()) {
            createFile(targetFile.getAbsolutePath(), true, true, true);
        }

        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile,
                                String oldCode, String tarCode) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        FileInputStream is = null;
        FileOutputStream os = null;

        if (!targetFile.exists()) {
            createFile(targetFile.getAbsolutePath(), true, true, true);
        }
        try {
            // 新建文件输入流并对它进行缓冲
            is = new FileInputStream(sourceFile);
            inBuff = new BufferedInputStream(is);

            // 新建文件输出流并对它进行缓冲
            os = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(os);

            // 缓冲数组
            byte[] b = new byte[1024 * 2];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                byte[] b2 = new String(b, 0, len, oldCode).getBytes(tarCode);
                outBuff.write(b2, 0, b2.length);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
            if (os != null)
                os.close();
            if (is != null)
                is.close();
        }
    }

    // 验证字符串是否为正确路径名的正则表达式
    private static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";

    // 通过 sPath.matches(matches) 方法的返回值判断是否正确
    // sPath 为路径字符串

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return true;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
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
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            flag = file.delete();
        }
        return flag;
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
        if (!dirFile.exists()) {
            return true;
        }
        if (!dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files == null || files.length <= 0) {
            return dirFile.delete();
        }
        if (null != files) {
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
     * 创建文件夹，赋予所有人权限
     *
     * @param path
     * @return
     */
    public static boolean createDirectory(String path) {
        File dir = new File(path);
        boolean isSuccess = true;
        File parentFile = dir.getParentFile();
        if (parentFile.exists()) {
            if (!parentFile.isDirectory()) {
                isSuccess &= deleteFolder(parentFile.getAbsolutePath());
                isSuccess &= createDirectory(parentFile.getAbsolutePath());
            }
        } else {
            isSuccess &= createDirectory(parentFile.getAbsolutePath());
        }

        if (!dir.exists()) {
            isSuccess &= dir.mkdirs();
        } else if (!dir.isDirectory()) {
            isSuccess &= deleteFolder(dir.getAbsolutePath());
            if (isSuccess) {
                isSuccess &= createDirectory(dir.getAbsolutePath());
            }
        } else {
            return dir.canExecute() & dir.canRead() & dir.canWrite();
        }
        return isSuccess;
    }

    /**
     * 创建文件夹，赋予所有人权限
     *
     * @param path
     * @param readable
     * @param writable
     * @param executable
     * @return
     */
    public static boolean createDirectory(String path, boolean readable, boolean writable, boolean executable) {
        boolean isSuccess = true;
        File dir = new File(path);
        File parentFile = dir.getParentFile();
        if (parentFile.exists()) {
            if (!parentFile.isDirectory()) {
                isSuccess &= deleteFolder(parentFile.getAbsolutePath());
                isSuccess &= createDirectory(parentFile.getAbsolutePath(), readable, writable, executable);
            }
        } else {
            isSuccess &= createDirectory(parentFile.getAbsolutePath(), readable, writable, executable);
        }

        if (!dir.exists()) {
            isSuccess &= dir.mkdirs();
            if (readable) {
                isSuccess &= dir.setReadable(true, false);
            }
            if (writable) {
                isSuccess &= dir.setWritable(true, false);
            }
            if (executable) {
                isSuccess &= dir.setExecutable(true, false);
            }
        } else if (!dir.isDirectory()) {
            isSuccess &= deleteFolder(dir.getAbsolutePath());
            if (isSuccess) {
                isSuccess &= createDirectory(dir.getAbsolutePath(), readable, writable, executable);
            }
        } else {
            if (readable) {
                isSuccess &= dir.canRead();
            }
            if (writable) {
                isSuccess &= dir.canWrite();
            }
            if (executable) {
                isSuccess &= dir.canExecute();
            }
        }
        if (DEBUG) {
            System.out.println("dir create " + isSuccess + ", " + path);
        }
        return isSuccess;
    }

    /**
     * 创建文件，赋予所有人权限
     *
     * @param path
     * @return
     */
    public static boolean createFile(String path) throws IOException {
        boolean isSuccess = true;
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            if (!parentFile.isDirectory()) {
                isSuccess &= deleteFolder(parentFile.getAbsolutePath());
                isSuccess &= createDirectory(parentFile.getAbsolutePath());
            }
        } else {
            isSuccess &= createDirectory(parentFile.getAbsolutePath());
        }

        if (!file.exists()) {
            isSuccess &= file.createNewFile();
        } else if (!file.isFile()) {
            isSuccess &= deleteFolder(file.getAbsolutePath());
            if (isSuccess) {
                isSuccess &= createFile(file.getAbsolutePath());
            }
        } else {
            return file.canRead() & file.canWrite();
        }
        return isSuccess;
    }

    /**
     * 创建文件，赋予所有人需要的权限
     *
     * @param path
     * @param readable
     * @param writable
     * @param executable
     * @return
     */
    public static boolean createFile(String path, boolean readable, boolean writable, boolean executable) throws IOException {
        boolean isSuccess = true;
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            if (!parentFile.isDirectory()) {
                isSuccess &= deleteFolder(parentFile.getAbsolutePath());
                isSuccess &= createDirectory(parentFile.getAbsolutePath(), readable, writable, executable);
            }
        } else {
            isSuccess &= createDirectory(parentFile.getAbsolutePath(), readable, writable, executable);
        }

        if (!file.exists()) {
            isSuccess &= file.createNewFile();
            if (readable) {
                isSuccess &= file.setReadable(true, false);
            }
            if (writable) {
                isSuccess &= file.setWritable(true, false);
            }
            if (executable) {
                isSuccess &= file.setExecutable(true, false);
            }
        } else if (!file.isFile()) {
            isSuccess &= deleteFolder(file.getAbsolutePath());
            if (isSuccess) {
                isSuccess &= createFile(file.getAbsolutePath(), readable, writable, executable);
            }
        } else {
            if (readable) {
                isSuccess &= file.canRead();
            }
            if (writable) {
                isSuccess &= file.canWrite();
            }
            if (executable) {
                isSuccess &= file.canExecute();
            }
        }
        return isSuccess;
    }

    /**
     * 重命名文件名字
     *
     * @param oldPath
     * @param newPath
     * @return true 重命名成功
     */
    public static boolean rename(String oldPath, String newPath) {
        File file = new File(oldPath);
        if (file.exists()) {
            File dest = new File(newPath);
            return file.renameTo(dest);
        }
        return false;
    }

    /**
     * 获取文件夹的大小
     *
     * @return
     * @throws Exception
     * @author 宋疆疆
     * @date 2013-11-28 下午2:37:03
     */
    public static long getDirSize(String path) {
        long size = 0;
        File file = new File(path);
        File flist[] = file.listFiles();
        if (flist == null) {
            return size;
        }
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDirSize(flist[i].getAbsolutePath());
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 递归求取目录里的文件个数
     *
     * @param path
     * @return
     * @author 宋疆疆
     * @date 2013-11-28 下午2:48:55
     */
    public static long getFileCount(String path) {
        long size = 0;
        File f = new File(path);
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileCount(flist[i].getAbsolutePath());
                size--;
            }
        }
        return size;
    }

    /**
     * 递归求取目录里的文件夹个数
     *
     * @param path
     * @return
     * @author 宋疆疆
     * @date 2013-11-28 下午2:48:55
     */
    public static long getDirCount(String path) {
        long size = 0;
        File f = new File(path);
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getDirCount(flist[i].getAbsolutePath());
                size++;
            }
        }
        return size;
    }

    /**
     * 找出指定目录里的文件或文件夹的位置，会递归查找子目录
     *
     * @param dirPath
     * @param fileName
     * @return
     * @author 宋疆疆
     * @date 2017-10-11 16:06:31
     */
    public static List<String> findFile(String dirPath, String fileName) {
        List<String> paths = new ArrayList<>();
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length <= 0) {
                return paths;
            }

            for (File file : listFiles) {
                if (file.getName().equals(fileName)) {
                    paths.add(file.getAbsolutePath());
                }
                if (file.isDirectory()) {
                    List<String> tempPaths = findFile(file.getAbsolutePath(), fileName);
                    if (tempPaths != null && 0 != tempPaths.size()) {
                        paths.addAll(tempPaths);
                    }
                }
            }
        }
        return paths;
    }

    /**
     * 找出指定目录里的带有指定的扩展名的文件
     *
     * @param dirPath
     * @param extension
     * @param onlyCurrentDir
     * @return
     * @author 宋疆疆
     * @date 2014年6月13日 上午9:42:32
     */
    public static List<String> findFileByEx(String dirPath, String extension,
                                            boolean onlyCurrentDir) {
        List<String> paths = new ArrayList<String>();
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length <= 0) {
                return paths;
            }
            for (File file : listFiles) {
                if (file.getName().endsWith(extension)) {
                    paths.add(file.getAbsolutePath());
                }
                if (file.isDirectory() && !onlyCurrentDir) {
                    List<String> tempPaths = findFileByEx(
                            file.getAbsolutePath(), extension, onlyCurrentDir);
                    if (tempPaths != null && 0 != tempPaths.size()) {
                        paths.addAll(tempPaths);
                    }
                }
            }
        }
        return paths;
    }

    /**
     * 获取可用空间,只针对android文件系统的状态
     *
     * @param path
     * @return
     * @author 宋疆疆
     * @date 2013-11-28 下午5:41:08
     */
    public static long getAvailable(String path) {
        StatFs sf = new StatFs(path);
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        return availCount * blockSize;
    }

    /**
     * 移动文件或文件夹到指定目录
     *
     * @param from
     * @param to
     * @return
     * @author 宋疆疆
     * @date 2014年5月27日 下午1:44:40
     */
    public static boolean moveFile(String from, String to) {// 移动指定文件夹内的全部文件
        boolean flag = true;
        File src = new File(from);
        File tar = new File(to);
        if (!src.exists()) {
            if (DEBUG) {
                System.out.println(from + " not found, false");
            }
            return false;
        }
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files == null || files.length == 0) {
                if (!tar.exists()) {
                    flag &= src.renameTo(tar);
                } else {
                    deleteFolder(from);
                }
            } else {
                for (File file : files) {
                    flag &= moveFile(file.getAbsolutePath(), tar.getAbsolutePath() + File.separator + file.getName());
                }
                deleteFolder(from);
            }
        } else {
            if (tar.exists()) {
                deleteFolder(tar.getAbsolutePath());
            }
            createDirectory(tar.getParent());
            flag &= src.renameTo(tar);
        }
        if (DEBUG) {
            System.out.println("rename " + from);
            System.out.println("to     " + to);
            System.out.println(flag);
        }
        return flag;
    }

    public static boolean isEmpty(String path) {
        File src = new File(path);
        if (src.isFile()) {
            return false;
        }
        String[] list = src.list();
        return list == null || list.length == 0;
    }

    public static boolean isEmpty(File file) {
        if (file.isFile()) {
            return false;
        }
        String[] list = file.list();
        return list == null || list.length == 0;
    }

    public static String readFile(InputStream is) throws Exception {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        br.close();
        isr.close();
        return sb.toString();
    }

    public static String readFile(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        String s = readFile(fis);
        fis.close();
        return s;
    }

    public static boolean writeFile(String filePath, String contents) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            createFile(filePath);
        }
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);

        String[] split = contents.split("\n");
        int index = 0;
        for (String string : split) {
            if (index == 0) {
                index = 1;
                bw.write(string);
            } else {
                bw.write("\n" + string);
            }
        }
        bw.close();
        osw.close();
        fos.close();
        return true;
    }

    public static boolean writeFileAppend(String filePath, String contents)
            throws Exception {
        File file = new File(filePath);

        FileOutputStream fos = new FileOutputStream(file, true);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);

        String[] split = contents.split("\n");
        int index = 0;
        for (String string : split) {
            if (index == 0) {
                index = 1;
                bw.write(string);
            } else {
                bw.write("\n" + string);
            }
        }

        bw.close();
        osw.close();
        fos.close();
        return true;

    }

    public static boolean writeFile(String filePath, InputStream is, WriteListener listener) {
        File file = new File(filePath);
        return writeFile(file, is, listener);
    }

    public static boolean writeFile(String filePath, InputStream is) {
        File file = new File(filePath);
        return writeFile(file, is);
    }

    public static boolean writeFile(File file, InputStream is) {
        return writeFile(file, is, null);
    }

    public static boolean writeFile(File file, InputStream is, WriteListener listener) {
        return writeFile(file, is, false, listener);
    }

    public static boolean writeFile(File file, InputStream is, boolean append, WriteListener listener) {
        FileOutputStream fos = null;
        try {
            createFile(file.getAbsolutePath());
            fos = new FileOutputStream(file, append);
            byte[] buf = new byte[1024];
            int len = 0;
            long totalLen = 0;
            while (!listener.isCancel() && (len = is.read(buf)) != -1) {
                totalLen += len;
                fos.write(buf, 0, len);
                if (listener != null) {
                    listener.onWrite(totalLen, len);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public interface WriteListener {
        /**
         * @param total 已经写入的总字节数
         * @param len   本次回调写入的字节数
         */
        void onWrite(long total, int len);

        boolean isCancel();
    }

    public static boolean deleteText(String filePath, String contents)
            throws Exception {
        String readFile = readFile(filePath);
        File file = new File(filePath);

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);

        String[] split = readFile.split("\n");
        for (String string : split) {
            // System.out.println(string);
            if (string.equals(contents))
                continue;
            bw.write(string + "\n");
        }

        bw.close();
        osw.close();
        fos.close();
        return true;

    }

    /**
     * 将文件的名字里追加指定内容
     *
     * @param file
     * @param append
     * @return 追加之后的名字
     */
    public static String appendName(File file, String append) {
        String name = file.getName();
        StringBuilder sb = new StringBuilder();
        if (name.contains(".")) {
            String[] split = name.split("\\.");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    sb.append(split[i]);
                    sb.append(append);
                } else {
                    sb.append(".");
                    sb.append(split[i]);
                }
            }
        } else {
            sb.append(name);
            sb.append(append);
        }
        return sb.toString();
    }

    public static boolean isFile(String path) {
        return isFile(new File(path));
    }

    public static boolean isDir(String path) {
        return isDir(new File(path));
    }

    public static boolean isFile(File file) {
        if (!file.exists()) {
            return false;
        }
        return file.isFile();
    }

    public static boolean isDir(File file) {
        if (!file.exists()) {
            return false;
        }
        return file.isDirectory();
    }

}
