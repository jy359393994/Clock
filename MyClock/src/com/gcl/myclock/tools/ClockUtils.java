package com.gcl.myclock.tools;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
	

    public static Calendar getCalendarForHourAndMinus(String time)
    {
		if (null == time)
		{
			return new GregorianCalendar();
		}
		
		String[] times = time.split(":|£º");
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
}
