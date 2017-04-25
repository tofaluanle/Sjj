package cn.sjj.util;

import java.io.File;
import java.io.InputStream;

/**
 * 用来执行Command命令
 * @author 宋疆疆
 *
 */

public class CMDExecute {
	private static CMDExecute CMDSELF;

	private CMDExecute() {
	}

	public static void main(String[] args) {
		// 根据shell的screencap -p /data/work/00301BBA02DB.png指令截图
		String localPath = "/data/work/00301BBA02DB.png";
		String[] command = { "/system/bin/screencap", "-p", localPath };
		CMDExecute.getCMDExecuteInstance().run(command, "system/bin/");
		
		String[] command2 = {  "/data/work/update/temp.sh" };
		CMDExecute.getCMDExecuteInstance().run(command2, "/data/work/update/");
	}

	public static CMDExecute getCMDExecuteInstance() {
		if (CMDSELF == null) {
			CMDSELF = new CMDExecute();
		}
		return CMDSELF;
	}

	public synchronized String run(String[] cmd, String workdirectory) {
		String result = "";

		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			InputStream in = null;
			if (workdirectory != null) {
				builder.directory(new File(workdirectory));
				builder.redirectErrorStream(true);
				Process process = builder.start();
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1)
					result = result + new String(re, "utf-8");
			}
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}