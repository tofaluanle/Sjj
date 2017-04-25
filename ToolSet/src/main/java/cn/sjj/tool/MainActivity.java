package cn.sjj.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Camera m_Camera;
	private Camera.Parameters mParameters;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listDialog();
	}

	public void listDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("小工具");
		final String[] items = new String[] { "数据", "手电筒" };
		builder.setItems(items, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					setMobileDataEnabled();
					finish();
					break;
				case 1:
					cameraFlash();
					break;
				}
			}

		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void setMobileDataEnabled() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		cm.setMobileDataEnabled(!cm.getMobileDataEnabled());
	}

	private void cameraFlash() {
		m_Camera = Camera.open();
		mParameters = m_Camera.getParameters();

		if (m_Camera.getParameters().getFlashMode()
				.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
			Toast.makeText(this, "关闭手电筒", Toast.LENGTH_SHORT).show();
			close(null);
		} else {
			Toast.makeText(this, "开打手电筒", Toast.LENGTH_SHORT).show();
			open(null);
		}
	}

	public void open(View v) {
		if (m_Camera.getParameters().getFlashMode()
				.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
			return;
		}
		mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		m_Camera.setParameters(mParameters);
	}

	public void close(View v) {
		if (m_Camera.getParameters().getFlashMode()
				.equals(Camera.Parameters.FLASH_MODE_OFF)) {
			return;
		}
		mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		m_Camera.setParameters(mParameters);
	}

	@Override
	protected void onDestroy() {
		if (null != m_Camera) {
			m_Camera.release();
		}
		super.onDestroy();
	}
}