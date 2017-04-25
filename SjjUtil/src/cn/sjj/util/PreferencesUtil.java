package cn.sjj.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * SharedPreferences的操作封装类
 */
public class PreferencesUtil {

    public static final String NAME_FOREVER = "forever";

    public static SharedPreferences prefsForever;

    private static SharedPreferences prefs;

    public static void init(Context context) {
        String pkgName = context.getPackageName();
        prefs = context.getSharedPreferences(pkgName, Context.MODE_PRIVATE);
        prefsForever = context.getSharedPreferences(NAME_FOREVER, Context.MODE_PRIVATE);
    }

    public synchronized static int getIntPref(String name, int defaults) {
        return getIntPref(prefs, name, defaults);
    }

    public synchronized static int getIntPref(SharedPreferences sp, String name, int defaults) {
        return sp.getInt(name, defaults);
    }

    public synchronized static boolean setIntPref(String name, int value) {
        return setIntPref(prefs, name, value);
    }

    public synchronized static boolean setIntPref(SharedPreferences sp, String name, int value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(name, value);
        return ed.commit();
    }

    public synchronized static boolean getBooleanPref(String name, boolean defaults) {
        return getBooleanPref(prefs, name, defaults);
    }

    public synchronized static boolean getBooleanPref(SharedPreferences sp, String name, boolean defaults) {
        return sp.getBoolean(name, defaults);
    }

    public synchronized static boolean setBooleanPref(String name, boolean value) {
        return setBooleanPref(prefs, name, value);
    }

    public synchronized static boolean setBooleanPref(SharedPreferences sp, String name, boolean value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(name, value);
        return ed.commit();
    }

    public synchronized static String getStringPref(String name, String defaults) {
        return getStringPref(prefs, name, defaults);
    }

    public synchronized static String getStringPref(SharedPreferences sp, String name, String defaults) {
        return sp.getString(name, defaults);
    }

    public synchronized static boolean setStringPref(String name, String value) {
        return setStringPref(prefs, name, value);
    }

    public synchronized static boolean setStringPref(SharedPreferences sp, String name, String value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(name, value);
        return ed.commit();
    }

    public synchronized static long getLongPref(String name, Long defaults) {
        return getLongPref(prefs, name, defaults);
    }

    public synchronized static long getLongPref(SharedPreferences sp, String name, Long defaults) {
        return sp.getLong(name, defaults);
    }

    public synchronized static boolean setLongPref(String name, Long value) {
        return setLongPref(prefs, name, value);
    }

    public synchronized static boolean setLongPref(SharedPreferences sp, String name, Long value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong(name, value);
        return ed.commit();
    }

    public synchronized static Set<String> getStringSet(String name, Set<String> defaults) {
        return getStringSet(prefs, name, defaults);
    }

    public synchronized static Set<String> getStringSet(SharedPreferences sp, String name, Set<String> defaults) {
        return sp.getStringSet(name, defaults);
    }

    public synchronized static boolean setStringSet(String name, Set<String> values) {
        return setStringSet(prefs, name, values);
    }

    public synchronized static boolean setStringSet(SharedPreferences sp, String name, Set<String> values) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putStringSet(name, values);
        return ed.commit();
    }

    public synchronized static boolean remove(String key) {
        return remove(prefs, key);
    }

    public synchronized static boolean remove(SharedPreferences sp, String key) {
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(key);
        return ed.commit();
    }

    public synchronized static boolean clear() {
        return clear(prefs);
    }

    public synchronized static boolean clear(SharedPreferences sp) {
        SharedPreferences.Editor ed = sp.edit();
        ed.clear();
        return ed.commit();
    }

    public synchronized static boolean contains(String key) {
        return contains(prefs, key);
    }

    public synchronized static boolean contains(SharedPreferences sp, String key) {
        return sp.contains(key);
    }

}
