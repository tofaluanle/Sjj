package cn.sjj.base;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import cn.sjj.IStatics;
import cn.sjj.Logger;

/**
 * ContentProvider的通用基类，提供打印生命周期的功能
 *
 * @auther 宋疆疆
 * @date 2016/8/4.
 */
public class BaseContentProvider extends ContentProvider {

    protected static final boolean DEBUG = IStatics.DEBUG;

    @Override
    public boolean onCreate() {
        Logger.v(DEBUG, this + " onCreate");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Logger.v(DEBUG, this + " query");
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Logger.v(DEBUG, this + " getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Logger.v(DEBUG, this + " insert");
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Logger.v(DEBUG, this + " delete");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Logger.v(DEBUG, this + " update");
        return 0;
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + '@' + Integer.toHexString(hashCode());
    }

}
