package cn.manjuu.searchproject.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import cn.manjuu.searchproject.R;
import cn.manjuu.searchproject.activity.MainActivity;
import cn.manjuu.searchproject.fragment.PublishFragment;
import cn.manjuu.searchproject.fragment.SearchFragment;
import cn.manjuu.searchproject.fragment.SettingFragment;
import cn.manjuu.searchproject.util.Logger;

public class NavigationManager {

	private FragmentTransaction mTransaction;
	private FragmentManager mFragmentManager;
	private static NavigationManager mInstance;
	private MainActivity mActivity;
	private SearchFragment mSearchFragment;
	private SettingFragment mSettingFragment;
	private PublishFragment mPublishFragment;
	private Fragment currentFragment;

	public void setActivity(MainActivity mActivity) {
		this.mActivity = mActivity;
	}

	public static NavigationManager getInstance() {
		if (null == mInstance) {
			mInstance = new NavigationManager();
		}
		return mInstance;
	}

	private NavigationManager() {
		super();
	}

	public void init() {
		mFragmentManager = mActivity.getSupportFragmentManager();

		mTransaction = mFragmentManager.beginTransaction();
		mSearchFragment = new SearchFragment();
		mPublishFragment = new PublishFragment();
		mSettingFragment = new SettingFragment();
		mTransaction.add(R.id.fl_content, mSearchFragment);
		mTransaction.add(R.id.fl_content, mPublishFragment);
		mTransaction.add(R.id.fl_content, mSettingFragment);
		mTransaction.hide(mPublishFragment);
		mTransaction.hide(mSettingFragment);
		mTransaction.commit();
		currentFragment = mSearchFragment;
	}

	public void onClick(View v) {
		showFragment(v.getId());
	}

	private void showFragment(int vId) {
		Fragment fragment = getFragment(vId);
		if (null == fragment) {
			Logger.d("fragment is null");
			return;
		}
		Logger.d("fragment: " + fragment.toString() + ", currentFragment: "
				+ currentFragment);
		mTransaction = mFragmentManager.beginTransaction();
		mTransaction.hide(currentFragment);
		mTransaction.show(fragment);
		mTransaction.commit();
		currentFragment = fragment;
	}

	private Fragment getFragment(int vId) {
		switch (vId) {
		case R.id.rl_search:
			if (null == mSearchFragment) {
				Logger.d("create  mSearchFragment");
				mSearchFragment = new SearchFragment();
				mTransaction.add(R.id.fl_content, mSearchFragment);
			}
			return mSearchFragment;
		case R.id.rl_publish:
			if (null == mPublishFragment) {
				Logger.d("create  mPublishFragment");
				mPublishFragment = new PublishFragment();
				mTransaction.add(R.id.fl_content, mPublishFragment);
			}
			return mPublishFragment;
		case R.id.rl_setting:
			if (null == mSettingFragment) {
				Logger.d("create  mSettingFragment");
				mSettingFragment = new SettingFragment();
				mTransaction.add(R.id.fl_content, mSettingFragment);
			}
			return mSettingFragment;
		}
		return null;
	}

}
