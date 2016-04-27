package org.dis.sheet02;

import java.util.Date;

public final class Util {

	public static String NullToValue(String value) {
		return NullToValue(value, "");
	}
	
	public static boolean NullToValue(boolean value) {
		return NullToValue(value, false);
	}
	
	public static int NullToValue(int value) {
		return NullToValue(value, 0);
	}
	
	public static double NullToValue(double value) {
		return NullToValue(value, 0d);
	}
	
	public static Date NullToValue(Date value) {
		return NullToValue(value, new Date());
	}
	
	public static <T> T NullToValue(T value, T nullReplacement) {
		return value != null ? value : nullReplacement;
	}
}
