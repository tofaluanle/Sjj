package cn.sjj.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;

import cn.sjj.Logger;

/**
 * @author 宋疆疆
 * @since 2018/2/21.
 */
public class FileWriter {

    private String           mPreName;
    private String           mSuffixName;
    private long             mMaxFileSize;
    private DecimalFormat    mFileNameFormat;
    private FileOutputStream mFos;
    private long             mWriteLen;
    private String           mDir;

    public FileWriter(String dir, String preName, String suffixName, long maxFileSize, int fileCountDigit) {
        this.mDir = dir;
        this.mPreName = preName;
        this.mSuffixName = suffixName;
        this.mMaxFileSize = maxFileSize;

        String pattern = "";
        for (int i = 0; i < fileCountDigit; i++) {
            pattern += "0";
        }
        mFileNameFormat = new DecimalFormat(pattern);

        createNewFile();
    }

    public void write(String text) {
        if (mFos == null) {
            Logger.e("FileWriter mFos is null, write fail");
            return;
        }

        try {
            byte[] bytes = text.getBytes();
            mFos.write(bytes);
            mWriteLen += bytes.length;
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkOverSize();
    }

    private void checkOverSize() {
        if (mWriteLen >= mMaxFileSize) {
            closeFos();
            mWriteLen = 0;
            createNewFile();
        }
    }

    private void createNewFile() {
        File root = new File(mDir);
        String[] files = root.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith(mPreName) && s.endsWith(mSuffixName);
            }
        });
        try {
            String path = mDir + File.separator + mPreName + mFileNameFormat.format(files.length + 1) + mSuffixName;
            FileUtil.createFile(path);
            mFos = new FileOutputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        closeFos();
    }

    private void closeFos() {
        if (mFos != null) {
            try {
                mFos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mFos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFos = null;
        }
    }

}
