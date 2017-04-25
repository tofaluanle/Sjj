package cn.sjj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import cn.sjj.Logger;
import cn.sjj.util.NetUtil;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null) {
			Toast.makeText(context,
					"Active Network Type : " + activeNetInfo.getTypeName(),
					Toast.LENGTH_LONG).show();
		}
		if (mobNetInfo != null) {
			Toast.makeText(context,
					"Mobile Network Type : " + mobNetInfo.getTypeName(),
					Toast.LENGTH_LONG).show();
		}
		Logger.i("no connect: "
				+ intent.getBooleanExtra(
						ConnectivityManager.EXTRA_NO_CONNECTIVITY, false));

		Logger.i("connect: " + NetUtil.isNetworkAvalible(context));
	}

}
