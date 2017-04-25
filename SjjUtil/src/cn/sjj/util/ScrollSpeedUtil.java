package cn.sjj.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Gallery;
import cn.sjj.widget.FixedSpeedScroller;

/**
 * 用来修改可滑动控件的滑动速度的工具类
 * 
 * @author 宋疆疆
 * @date 2014-5-6 下午2:27:59
 */
public class ScrollSpeedUtil {

	public static void controlGallerySpeed(Context context, Gallery gl,
			int speed) {
		try {
			Field mField;
			Field mFling;
			Object fling = null;
			Class<?>[] classes = Gallery.class.getDeclaredClasses();
			mFling = Gallery.class.getDeclaredField("mFlingRunnable");
			mFling.setAccessible(true);
			for (Class<?> class1 : classes) {
				if (class1.toString().contains("Fling")) {
					mField = class1.getDeclaredField("mScroller");
					mField.setAccessible(true);
					fling = class1.getConstructor(Gallery.class)
							.newInstance(gl);

					FixedSpeedScroller mScroller = new FixedSpeedScroller(
							context, new AccelerateDecelerateInterpolator());
					mScroller.setmDuration(speed); // 2000ms
					mField.set(fling, mScroller);
				}
			}
			mFling.set(gl, fling);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void controlViewPagerSpeed(Context context, ViewPager vp,
			int speed) {
		try {
			Field mField;

			mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);

			FixedSpeedScroller mScroller = new FixedSpeedScroller(context,
					new AccelerateDecelerateInterpolator());
			mScroller.setmDuration(speed); // 2000ms
			mField.set(vp, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
