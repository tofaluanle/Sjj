package cn.manjuu.searchproject.net;

import android.content.Context;
import android.widget.Toast;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.statics.IStatics;
import cn.manjuu.searchproject.util.NetUtil;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class SAsyncHttpClient extends AsyncHttpClient {

	private Context mContext;
	private boolean isNetAvailable;

	public SAsyncHttpClient(Context mContext) {
		super();
		this.mContext = mContext;
		init();
	}

	private void init() {
		setTimeout(IStatics.TIME_OUT);
		isNetAvailable = NetUtil.isNetworkAvalible(mContext);
	}

	@Override
	public RequestHandle post(String url, RequestParams params,
			ResponseHandlerInterface responseHandler) {
		if (!isNetAvailable) {
			showToastNoNetwork();
			return null;
		}
		return super.post(url, params, responseHandler);
	}

	private void showToastNoNetwork() {
		Toast.makeText(mContext, mContext.getString(R.string.no_network),
				Toast.LENGTH_LONG).show();
	}

}
