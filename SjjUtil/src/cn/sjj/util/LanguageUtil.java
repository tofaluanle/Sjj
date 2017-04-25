package cn.sjj.util;

import java.util.Locale;

import android.content.Context;

public class LanguageUtil {

	public static boolean isZh(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	public static boolean isEn(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("en"))
			return true;
		else
			return false;
	}
}
