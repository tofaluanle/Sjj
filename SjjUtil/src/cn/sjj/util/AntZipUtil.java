package cn.sjj.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class AntZipUtil {

	private static final boolean DEBUG = false;

	public static void unZip(String srcFile, String dest) throws Exception {
		File file = new File(srcFile);
		if (!file.exists()) {
			throw new Exception("解压文件不存在!");
		}
		ZipFile zipFile = new ZipFile(file);
		Enumeration e = zipFile.getEntries();
		while (e.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) e.nextElement();
			if (zipEntry.isDirectory()) {
				String name = zipEntry.getName();
				name = name.substring(0, name.length() - 1);
				File f = new File(dest + "/" + name);
				FileUtil.createDirectory(f.getAbsolutePath(), true, true, true);
			} else {
				File f = new File(dest + "/"
						+ new String(zipEntry.getRawName(), "GB2312"));
				if (DEBUG) {
					System.out.println(f.getName());
				}
				FileUtil.createFile(f.getAbsolutePath(), true, true, true);
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(f);
				int length = 0;
				byte[] b = new byte[1024];
				while ((length = is.read(b, 0, 1024)) != -1) {
					fos.write(b, 0, length);
				}
				is.close();
				fos.close();
			}
		}

		if (zipFile != null) {
			zipFile.close();
		}
	}
}
