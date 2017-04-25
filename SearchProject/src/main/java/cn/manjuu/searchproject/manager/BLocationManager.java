package cn.manjuu.searchproject.manager;

import android.content.Context;
import android.widget.Toast;

import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.util.Logger;
import cn.manjuu.searchproject.util.NetUtil;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class BLocationManager {

	private static BLocationManager mInstance;
	// 定位相关
	private LocationClient mLocClient;
	private BDLocationListener mListener;
	private Context mContext;

	public static BLocationManager getInstance() {
		if (null == mInstance) {
			mInstance = new BLocationManager();
		}
		return mInstance;
	}

	private BLocationManager() {
		super();
	}

	public void init(Context context) {
		mContext = context;
		mLocClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(999);
		mLocClient.setLocOption(option);
	}

	public void registerListener(BDLocationListener listener) {
		mLocClient.unRegisterLocationListener(mListener);
		mLocClient.registerLocationListener(listener);
		mListener = listener;
	}

	public boolean getLoc() {
		if (!NetUtil.isNetworkAvalible(mContext)) {
			Toast.makeText(mContext, mContext.getString(R.string.no_network),
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (mLocClient.isStarted()) {
			mLocClient.requestLocation();
		} else {
			Logger.d("location start");
			mLocClient.start();
		}
		return true;
	}

	public void stop() {
		mLocClient.unRegisterLocationListener(mListener);
		if (mLocClient.isStarted()) {
			mLocClient.stop();
		}
	}
}
