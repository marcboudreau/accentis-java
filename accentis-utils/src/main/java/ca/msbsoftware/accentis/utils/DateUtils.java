package ca.msbsoftware.accentis.utils;

import static java.text.DateFormat.SHORT;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static ThreadLocal<DateFormat> shortDateFormats = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return DateFormat.getDateInstance(SHORT);
		}
	};
	
	private static ThreadLocal<Calendar> calendars = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};
	
	public static String format(Date date) {
		if (null == date)
			return null;
		
		return shortDateFormats.get().format(date);
	}
	
	public static Date parse(String date) throws ParseException {
		if (null == date)
			return null;
		
		return shortDateFormats.get().parse(date);
	}
	
	public static Date createDateAtMidnight(Date date) {
		Calendar calendar = calendars.get();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date createTodayMidnight() {
		return createDateAtMidnight(new Date());
	}
}
