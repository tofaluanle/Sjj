package cn.sjj.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import cn.sjj.IStatics;
import cn.sjj.Logger;

public class CMDEngine {

	private OutputStream os;
	private InputStream is;
	private Process process;
	private InputStream es;

	private void getRoot() {
		if (null == os) {
			Logger.d("cmd os is null, create new process");
			try {
				process = Runtime.getRuntime().exec(IStatics.SU_PATH);
				os = process.getOutputStream();
				is = process.getInputStream();
				es = process.getErrorStream();
				readIs(is);
				readEs(es);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readIs(final InputStream is) {
		new Thread() {
			@Override
			public void run() {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String result = "";
				try {
					while ((result = br.readLine()) != null) {
						Logger.d("is: " + result);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void readEs(final InputStream is) {
		new Thread() {
			@Override
			public void run() {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String result = "";
				try {
					while ((result = br.readLine()) != null) {
						Logger.w("es: " + result);
						analyzeResult(result);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	private void analyzeResult(String result) {
	}

	public void mountRW(String path) {
		String cmd = "mount -o remount,rw " + path;
		execCMD(cmd);
	}

	public void mountRO(String path) {
		String cmd = "mount -o remount,ro " + path;
		execCMD(cmd);
	}

	public void moveFile(String src, String tar) {
		mkdir(tar);
		String cmd = "busybox cp " + src + " " + tar;
		execCMD(cmd);
		execCMD("busybox chmod 777 " + tar);
		// deleteFile(src);
	}

	private void mkdir(String tar) {
		String mkdir = "mkdir -p " + tar.substring(0, tar.lastIndexOf("/") + 1);
		execCMD(mkdir);
	}

	public void moveDir(String src, String tar) {
		File dir = new File(src);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				moveDir(file.getAbsolutePath(), tar + "/" + file.getName());
			} else {
				moveFile(file.getAbsolutePath(), tar + "/" + file.getName());
			}
		}
	}

	public void deleteFile(String src) {
		String cmd = "busybox rm -rf " + src;
		execCMD(cmd);
	}

	public void execCMD(String cmd) {
		Logger.v(cmd);
		cmd = cmd + "\n";
		getRoot();
		try {
			os.write(cmd.getBytes("UTF-8"));
			os.write("sync\n".getBytes("UTF-8"));
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (null != os) {
				os.flush();
				os.close();
				os = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != is) {
				is.close();
				is = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (null != es) {
				es.close();
				es = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int waitFor() {
		if (null != process) {
			try {
				int waitFor = process.waitFor();
				Logger.d("waitFor: " + waitFor);
				return waitFor;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
}
