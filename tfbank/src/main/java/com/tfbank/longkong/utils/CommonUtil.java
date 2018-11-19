package com.tfbank.longkong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

	/**
	 * 将时间戳转为字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String conLong2Date(long time, String format) {
		
		if (format == null || (format = format.trim()).length() == 0) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		
		String returnValue = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = new Date(time);
			returnValue = sdf.format(date);
		} catch (Exception e) {
			
		}
		
		return returnValue;
	}
	
	public static Map<String, String> convertWeekByDate(Date time) {
		Map<String, String> reMap = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());
		reMap.put("START", imptimeBegin);
		System.out.println("所在周星期一的日期：" + imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		reMap.put("END", imptimeEnd);
		System.out.println("所在周星期日的日期：" + imptimeEnd);
		return reMap;
	}
	
	public static int converMin2Hour (int min) {
		if (min % 60==0) {
			return min / 60;
		}
		return min / 60 + 1;
	}
	
	public static long conDate2Long (String time, String format) {
		long returnValue = 0L;
		try {
			SimpleDateFormat sdf= new SimpleDateFormat(format);
			Date dt = sdf.parse(time);
			returnValue = dt.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	/**
	 * 获取指定时间当天的毫秒数
	 * @param millis 当天毫秒数
	 * @param y 需要减去是的年
	 * @param month 需要减去的月
	 * @param d 需要减去的日
	 * @return
	 */
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
	
	public static void main (String args[]) {
		long nowTme = System.currentTimeMillis();
		long hourTime = CommonUtil.conDate2Long(CommonUtil.conLong2Date(nowTme, "yyyy-MM-dd HH:00:00"), "yyyy-MM-dd HH:mm:ss");
		System.out.println(hourTime);
		long ehourTime = hourTime + 60 * 60 * 1000;
		System.out.println(ehourTime);
		
	}
}
