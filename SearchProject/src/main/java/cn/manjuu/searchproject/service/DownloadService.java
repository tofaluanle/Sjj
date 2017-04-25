package cn.manjuu.searchproject.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.statics.IStatics;
import cn.manjuu.searchproject.util.Logger;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadService extends Service {

	private static final int NOTIFY_ID = 0;
	private static final int REQUEST_ID_DOWNLOADING = 0;
	private static final int REQUEST_ID_DOWNLOAD_END = 1;
	private String url;
	private File file;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		url = intent.getStringExtra("url");
		downloadAPK();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void downloadAPK() {
		makeNotify(0);
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(IStatics.TIME_OUT);
		Logger.d(url);
		String[] allowedContentTypes = new String[] { ".*" };
		client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

			private int oldSize;

			@Override
			public void onSuccess(byte[] fileData) {
				Toast.makeText(DownloadService.this, "下载结束", 0).show();

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					file = new File(Environment.getExternalStorageDirectory(),
							IStatics.dirName + "/search.apk");
				} else {
					file = new File(DownloadService.this.getFilesDir(),
							"search.apk");
				}
				FileOutputStream fos = null;
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					fos = new FileOutputStream(file);
					fos.write(fileData);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				makeNotify(100);
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);
				Logger.v("step: " + bytesWritten + ", totalSize: " + totalSize);

				if ((bytesWritten - oldSize) > totalSize / 10) {
					int progress = (int) (bytesWritten * 100l / totalSize);
					Logger.d("progress: " + progress);
					makeNotify(progress);
					oldSize = bytesWritten;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable e) {
				super.onFailure(statusCode, headers, binaryData, e);
				Toast.makeText(DownloadService.this, "下载失败", 0).show();
				e.printStackTrace();
			}
		});

	}

	private void makeNotify(int progress) {
		NotificationManager nm = (NotificationManager) DownloadService.this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent();
		if (100 == progress) {
			intent.putExtra("path", file.getAbsolutePath());
			Logger.d("path: " + file.getAbsolutePath());
		}
		intent.setAction(IStatics.ACTION_INSTALL);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				DownloadService.this, REQUEST_ID_DOWNLOADING, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Builder builder = new NotificationCompat.Builder(DownloadService.this);
		RemoteViews contentView = new RemoteViews(
				DownloadService.this.getPackageName(), R.layout.notify_update);
		contentView.setImageViewResource(R.id.imageView1,
				R.drawable.ic_launcher);
		contentView.setProgressBar(R.id.pb_notify_update, 100, progress, false);
		contentView.setTextViewText(R.id.tv_notify_update_progress, progress
				+ "%");
		if (100 == progress) {
			contentView.setTextViewText(R.id.tv_notify_update_title,
					DownloadService.this
							.getString(R.string.notify_udpate_title_completed));
		} else {
			contentView.setTextViewText(R.id.tv_notify_update_title,
					DownloadService.this
							.getString(R.string.notify_udpate_title));
		}

		builder.setContentTitle(
				DownloadService.this.getString(R.string.notify_udpate_title))
				.setContentIntent(pendingIntent).setWhen(0)
				.setSmallIcon(R.drawable.ic_launcher).setContent(contentView);
		if (100 == progress) {
			builder.setOngoing(false);
			builder.setAutoCancel(true);
		} else {
			builder.setOngoing(true);
		}

		Notification notification = builder.build();

		nm.notify(NOTIFY_ID, notification);
	}

}
