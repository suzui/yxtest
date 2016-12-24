package utils;

import java.util.Date;

import play.templates.JavaExtensions;

public class DateUtils {
	public static String getDateFromMilliSecond(Long sec) {
		if (sec != null) {
			Date d = new Date(sec);
			return JavaExtensions.format(d, "yyyy/MM/dd hh:mm");
		}
		return "";
	}

	public static String formatDate(Long date, String format) {
		if (date == null) {
			return null;
		}
		return JavaExtensions.format(new Date(date), format);
	}

	public static String formatDate(Date date, String format) {
		if (date == null) {
			return null;
		}
		return JavaExtensions.format(date, format);
	}
}
