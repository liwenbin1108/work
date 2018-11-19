package com.loncomip.demo;

import java.util.Calendar;


public class Test {

	public static void main(String[] args) {
		long millis = System.currentTimeMillis();
	}
	
	public static long getDateMillis(long millis, int y,int month,int d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		calendar.add(Calendar.YEAR, -y);
		calendar.add(Calendar.MONTH, -month);
		calendar.add(Calendar.DATE, -d);
		calendar.set(Calendar.HOUR_OF_DAY , 0);
		calendar.set(Calendar.MINUTE , 0);
		calendar.set(Calendar.SECOND , 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

}
