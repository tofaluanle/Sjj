package cn.sjj.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DrawableUtil {

	private static final NullDrawable nullDrawable = new NullDrawable();

	public static Drawable getNullDrawable() {
		return nullDrawable;
	}

	public static Drawable getDrawable(String path, Resources res,
			int maxNumOfPixels) {
		Bitmap bitmap = BitmapUtil.tryGetBitmap(path, -1, maxNumOfPixels);
		if (null == bitmap) {
			return null;
		}
		BitmapDrawable db = new BitmapDrawable(res, bitmap);
		return db;
	}

	private static final class NullDrawable extends Drawable {

		@Override
		public void setColorFilter(ColorFilter cf) {
		}

		@Override
		public void setAlpha(int alpha) {
		}

		@Override
		public int getOpacity() {

			return 0;
		}

		@Override
		public void draw(Canvas canvas) {
		}
	}
}
