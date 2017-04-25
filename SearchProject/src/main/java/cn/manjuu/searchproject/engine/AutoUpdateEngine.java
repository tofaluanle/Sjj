package cn.manjuu.searchproject.engine;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.activity.MainActivity;
import cn.manjuu.searchproject.service.DownloadService;
import cn.manjuu.searchproject.statics.IStatics;
import cn.manjuu.searchproject.util.Logger;
import cn.manjuu.searchproject.util.NetUtil;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AutoUpdateEngine {

	private MainActivity mActivity;
	private static final int FORCE_UPDATE = 0;
	private static final int CAN_UPDATE = 1;
	private static final int NO_UPDATE = 2;
	private int action;
	private int lastVersion;
	private String info;
	private String url;
	private String md5;

	public AutoUpdateEngine(MainActivity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	public void checkUpdateInfo() {
		if (!NetUtil.isNetworkAvalible(mActivity)) {
			Logger.w("no network, don't check update info");
			return;
		}
		int version = getVersion();
		RequestParams params = new RequestParams();
		params.put("action", "update");
		params.put("version", version + "");
		Logger.d(params.toString());

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(IStatics.TIME_OUT);
		client.post(IStatics.URL_SERVER, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						super.onSuccess(arg0, arg1, body);
						String msg = new String(body);
						Logger.d(msg);

						analyzeResponse(msg);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable e) {
						super.onFailure(arg0, arg1, arg2, e);

						e.printStackTrace();

						getUpdateInfoFail();
					}

				});
	}

	private void analyzeResponse(String msg) {
		try {
			JSONObject object = new JSONObject(msg);
			action = object.getInt("action");
			lastVersion = object.getInt("last_version");
			info = object.getString("info");
			url = object.getString("url");
			md5 = object.has("md5") ? object.getString("md5") : "-1";
			switch (action) {
			case FORCE_UPDATE:
				showForceDialog();
				break;
			case CAN_UPDATE:
				showNormalDialog();
				break;
			case NO_UPDATE:

				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			getUpdateInfoFail();
		}
	}

	private void getUpdateInfoFail() {
		Toast.makeText(mActivity,
				mActivity.getString(R.string.check_update_fail),
				Toast.LENGTH_SHORT).show();
	}

	private int getVersion() {
		PackageManager pm = mActivity.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(mActivity.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private void showNormalDialog() {
		new AlertDialog.Builder(mActivity)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.dialog_update_title)
				.setMessage(info)
				.setPositiveButton(R.string.dialog_update_positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								downloadAPK();
							}

						})
				.setNegativeButton(R.string.dialog_update_negative,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).show();
	}

	public void showForceDialog() {
		new AlertDialog.Builder(mActivity)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.dialog_update_title)
				.setMessage(info)
				.setPositiveButton(R.string.dialog_update_positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								downloadAPK();
								mActivity.finish();
							}
						})
				.setNegativeButton(R.string.dialog_update_exit,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mActivity.finish();
							}
						}).show();

	}

	private void downloadAPK() {
		Intent intent = new Intent(mActivity, DownloadService.class);
		intent.putExtra("url", url);
		mActivity.startService(intent);
	}

}
