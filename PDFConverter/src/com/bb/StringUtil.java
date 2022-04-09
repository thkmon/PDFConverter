package com.bb;

import java.net.URLEncoder;

public class StringUtil {
	
	
	public static int parseIntForAmount(String str) {
		if (str != null && str.indexOf(",") > -1) {
			str = str.replace(",", "");
		}
		return parseInt(str, 0);
	}
	
	
	public static int parseInt(String str) {
		return parseInt(str, 0);
	}
	
	
	public static int parseInt(String str, int defaultValue) {
		if (str == null) {
			return defaultValue;
		}

		int result = defaultValue;

		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
			result = defaultValue;
		}
		
		return result;
	}
	
	public static String encodeUTF8(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		String result = "";
		
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			result = "";
		}
		
		return result;
	}
	
	
	public static String parseString(Object obj) {
		if (obj == null) {
			return "";
		}

		String result = "";
		
		try {
			result = String.valueOf(obj);
			
			if (result == null) {
				result = "";
			}
			
		} catch (Exception e) {
			result = "";
		}
		
		return result;
	}
	
	
	public static String escapeParam(Object obj) {
		if (obj == null) {
			return "";
		}

		String result = "";
		
		try {
			result = String.valueOf(obj);
			
			if (result == null) {
				result = "";
			}
			
		} catch (Exception e) {
			result = "";
		}
		
		return result;
	}
	
	
	public static String lpad(int target, int maxLen, String padChar) {
		return lpad(String.valueOf(target), maxLen, padChar);
	}
	
	
	public static String lpad(String target, int maxLen, String padChar) {
		if (target == null) {
			target = "";
		}
		
		if (padChar == null || padChar.length() == 0) {
			padChar = "0";
		} else if (padChar.length() > 1) {
			padChar = padChar.substring(0, 1);
		}
		
		String result = "";
		if (target.length() < maxLen) {
			int padCount = maxLen - target.length();
			for (int i=0; i<padCount; i++) {
				result += padChar;
			}
			
			result += target;
			
		} else {
			result = target;
		}
		
		return result;
	}
	
	
	/**
	 * 월 로 변환 (1 ~ 12 까지만 값 허용)
	 * 
	 * @param str
	 * @return
	 */
	public static int parseMonth(String str) {
		int month = parseInt(str);
		
		if (month < 1) {
			month = 1;
		} else if (month > 12) {
			month = 12;
		}

		return month;
	}
	
	
	/**
	 * 연도 로 변환 (1000 ~ 9999 까지만 값 허용)
	 * 
	 * @param str
	 * @return
	 */
	public static int parseYear(String str) {
		int year = parseInt(str);
		
		if (year < 1000) {
			year = 1000;
		} else if (year > 9999) {
			year = 9999;
		}

		return year;
	}
	
	
	/**
	 * 0 오류를 방지하는 나누기
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(double a, double b) {
		if (a <= 0.0) {
			return 0;
		}
		
		if (b <= 0.0) {
			return 0;
		}
		
		return a / b;
	}
}