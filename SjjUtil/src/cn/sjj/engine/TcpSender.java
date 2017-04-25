package cn.sjj.engine;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import cn.sjj.Logger;

public class TcpSender {

	private String ip;
	private int port;
	private String msg;

	public TcpSender(String ip, int port, String msg) {
		Logger.d("send ip: " + ip + ",port: " + port + ", msg: " + msg);
		this.ip = ip;
		this.port = port;
		this.msg = msg;
	}

	public void sendMsg() {
		new Thread() {
			@Override
			public void run() {
				OutputStream os = null;
				BufferedOutputStream bos = null;
				DataOutputStream dos = null;
				Socket socket = null;
				try {
					socket = new Socket(ip, port);
					os = socket.getOutputStream();
					// bos = new BufferedOutputStream(os);
					// dos = new DataOutputStream(bos);
					// dos.writeUTF(msg);
					os.write(msg.getBytes());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					closeStream(os);
					closeStream(bos);
					closeStream(dos);
					try {
						if (socket != null) {
							socket.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void closeStream(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
