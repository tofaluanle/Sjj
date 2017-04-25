package cn.manjuu.searchproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import cn.manjuu.searchproject.MainApplication;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.statics.IStatics;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 演示MapView的基本用法
 */
public class MyBaiDuMap extends Activity {

	private MapView mMapView = null;
	private MapController mMapController = null;
	MKMapViewListener mMapListener = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainApplication app = (MainApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(IStatics.bdKey,
					new MainApplication.MyGeneralListener());
		}
		setContentView(R.layout.my_baidu_map);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapController.enableClick(true);
		mMapController.setZoom(16);

		GeoPoint p;
		double cLat = 39.945;
		double cLon = 116.404;
		Intent intent = getIntent();
		mLat = intent.getDoubleExtra("latitude", cLat) * 1E6;
		mLon = intent.getDoubleExtra("longitude", cLon) * 1E6;
		p = new GeoPoint((int) mLat, (int) mLon);

		mMapController.setCenter(p);

		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(MyBaiDuMap.this, title, Toast.LENGTH_SHORT)
							.show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
			}

			@Override
			public void onMapAnimationFinish() {
			}

			@Override
			public void onMapLoadFinish() {

			}
		};
		mMapView.regMapViewListener(MainApplication.getInstance().mBMapManager,
				mMapListener);
		
		initOverlay();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	// ///////////////////////覆盖物相关////////////
	private MyOverlay mOverlay = null;
	private double mLat = 0;
	private double mLon = 0;

	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			return false;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			return false;
		}

	}

	public void initOverlay() {
		mOverlay = new MyOverlay(getResources().getDrawable(
				R.drawable.icon_gcoding), mMapView);
		GeoPoint p = new GeoPoint((int) mLat, (int) mLon);
		OverlayItem item = new OverlayItem(p, "", "");
		mOverlay.addItem(item);
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();
	}

	// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑覆盖物相关↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

}
