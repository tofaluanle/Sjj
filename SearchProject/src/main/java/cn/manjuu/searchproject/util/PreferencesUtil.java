package cn.manjuu.searchproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {

	public static final String PUBLISH_TITLE = "publish_titles";
	public static final String PUBLISH_CONTENT = "publish_content";
	public static final String PUBLISH_CONTACT = "publish_contact";
	public static final String PUBLISH_CONTACT_TYPE = "publish_contact_type";
	public static final String PUBLISH_TAG = "publish_tag";
	public static final String PUBLISH_TAG2 = "publish_tag2";
	public static final String PUBLISH_TAG3 = "publish_tag3";

	public static final String SETTING_RANGE = "setting_range";
	public static final String SETTING_MAP = "setting_map";

	Context context;

	public PreferencesUtil(Context context) {
		this.context = context;
	}

	/**
	 * 从SharedPreferences对象中获取int值.
	 * 
	 * @param context
	 *            上下文 .
	 * @param name
	 *            键名.
	 * @param defalut
	 *            默认值.
	 * @return 若键名存在,则返回键值,否则返回默认.
	 */
	public int getIntPref(String name, int defalut) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);// 设置读写权限
		return prefs.getInt(name, defalut);
	}

	/**
	 * 设置一个sharedPreferences中的key值对.
	 * 
	 * @param context
	 *            上下文.
	 * @param name
	 *            键名.
	 * @param value
	 *            键值.
	 */
	public void setIntPref(String name, int value) {// name
													// 就是key字符串,value是设置的值
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		Editor ed = prefs.edit();
		ed.putInt(name, value);
		ed.commit();
	}

	/**
	 * 从SharedPreferences对象中获取String值.
	 * 
	 * @param context
	 *            上下文 .
	 * @param name
	 *            键名.
	 * @param defalut
	 *            默认值.
	 * @return
	 */
	public String getStringPref(String name, String defalut) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);// 设置读写权限
		return prefs.getString(name, defalut);
	}

	/**
	 * 设置一个sharedPreferences中的key值对.
	 * 
	 * @param context
	 *            上下文.
	 * @param name
	 *            键名.
	 * @param value
	 *            键值.
	 */
	public void setStringPref(String name, String value) {// name
		// 就是key字符串,value是设置的值
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		Editor ed = prefs.edit();
		ed.putString(name, value);
		ed.commit();
	}

	/**
	 * 从SharedPreferences对象中获取String值.
	 * 
	 * @param context
	 *            上下文 .
	 * @param name
	 *            键名.
	 * @param defalut
	 *            默认值.
	 * @return
	 */
	public long getLongPref(String name, Long defalut) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);// 设置读写权限
		return prefs.getLong(name, defalut);
	}

	/**
	 * 设置一个sharedPreferences中的key值对.
	 * 
	 * @param context
	 *            上下文.
	 * @param name
	 *            键名.
	 * @param value
	 *            键值.
	 */
	public void setLongPref(String name, Long value) {// name
		// 就是key字符串,value是设置的值
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg,
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		Editor ed = prefs.edit();
		ed.putLong(name, value);
		ed.commit();
	}
}
