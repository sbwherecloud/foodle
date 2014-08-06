package com.belanger.simon.foodle.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("DefaultLocale")
public class FFormat {

	private static Context		context;
	public static final Pattern	VALID_EMAIL_ADDRESS_REGEX	= Pattern.compile(
																	"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
																	Pattern.CASE_INSENSITIVE);

	public static void init(Context context) {
		FFormat.context = context;
	}

	/*
	 * GENERIC
	 */

	public static String trim(String s, int length) {
		if (s == null)
			return "";
		if (s.length() > length) {
			return s.substring(0, length - 3) + "...";
		} else {
			return s;
		}
	}

	public static String dateMediumLength(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		return df.format(date);
	}

	public static String dateMediumLengthNoSeconds(Date date) {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
		return df.format(date);
	}

	public static String dateMediumNoTime(Date date) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return df.format(date);
	}

	public static String timeNoSeconds(Date date) {
		DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
		return df.format(date);
	}

	public static String notNull(String s) {
		if (s == null || s.equals("null")) {
			return "";
		} else {
			return s;
		}
	}

	public static boolean isValidEmail(String emailStr) {
		if (emailStr == null || emailStr.isEmpty()) { return false; }
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
}