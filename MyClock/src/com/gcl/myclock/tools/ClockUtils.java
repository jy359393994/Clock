package com.gcl.myclock.tools;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ClockUtils {
	
	public static String getCreateTime(){
		String createtime = null;
		Calendar calendar = new GregorianCalendar();
		createtime = Long.toString(calendar.getTimeInMillis());
		return createtime;
	}
	
	public static String getRepeat(int[] results){
		String repeatstr = null;
		int repeat = 0;
		for(int i = 0;i<results.length;i++){
			if(results[i] == 0){
				repeat &=(~(1<<i));
			}
			else{
				repeat |= (1<<i);
			}
		}
		repeatstr = String.valueOf(repeat);
		return repeatstr;
					
	}
	
	public static int[] getRepeatInts(String strrepeat){
		int [] values = {0,0,0,0,0,0,0};
		int results = Integer.parseInt(strrepeat);
		for(int i=0;i<values.length;i++){
			values[i]=(results&(1<<i)) == 0 ? 0 : 1;
		}
		
		return values;
	}
	
	public static int[] getHourAndMin(String time) {
		int[] values = { 0, 0 };
		String[] times = time.split(":");
		values[0] = Integer.parseInt(times[0]);
		values[1] = Integer.parseInt(times[1]);
		return values;
	}
	
	//得到下一个闹钟日期与今天的的差值
	public static int getNextRepeatDay(int[] repeats){
		int next = 0;
		Calendar c = Calendar.getInstance(Locale.CHINA);
		int dayOfWeek = c.DAY_OF_WEEK;
		boolean isAllRedeayFindNext = false;
		for(int i = dayOfWeek + 1;i < 7;i++){
			if(repeats[i] == 1){
				isAllRedeayFindNext = true;
				next = i - dayOfWeek;
				break;
			}
		}
		if(!isAllRedeayFindNext){
			for(int i = 0; i<= dayOfWeek;i++){
				if(repeats[i] == 1){
					isAllRedeayFindNext = true;
					next = i - dayOfWeek + 7;
					break;
				}
			}
		}
		return next;
	}
	

    public static Calendar getCalendarForHourAndMinus(String time)
    {
		if (null == time)
		{
			return new GregorianCalendar();
		}
		
		String[] times = time.split(":|：");
		if (times == null || times.length != 2)
		{
			return new GregorianCalendar();
		}
		
		int hour = Integer.parseInt(times[0]);
		int minus = Integer.parseInt(times[1]);
		
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minus);
				
		return calendar;
    }
    
    public static int getHourAndMinAndSec(String time) {
    	int sec;
		int[] values = { 0, 0, 0};
		String[] times = time.split(":");
		values[0] = Integer.parseInt(times[0]);
		values[1] = Integer.parseInt(times[1]);
		values[2] = Integer.parseInt(times[2]);
		sec = values[0] * 3600 + values[1] * 60 + values[2];
		
		return sec;
	}
    
    
}
