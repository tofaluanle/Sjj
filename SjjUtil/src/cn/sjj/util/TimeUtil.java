package cn.sjj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static long getTimestamp(String date) throws ParseException {
		Date date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
		Date date2 = new SimpleDateFormat("yyyyMMddHHmmss")
				.parse("19700101080000");
		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : date2.getTime() - date1.getTime();
		return l;
	}

}
