package com.gcl.myclock.tools;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.util.Log;

public class ClockUtils {
	private static final String LOG = "ClockUtils";
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
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
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
    
	public static int[] getDataPickDatas(String day){
		int results[] = {0,0,0};
			String values[] = day.split("/");
			results[0] = Integer.parseInt(values[0]);
			results[1] = Integer.parseInt(values[1]) - 1;
			results[2] = Integer.parseInt(values[2]);
		return results;
	}  
	
	public static boolean isBirthHasPassed(int times[],String strtime){
		boolean isPassed = false;
		Calendar calendar = new GregorianCalendar();	
		int cury = calendar.get(Calendar.YEAR);
		int curmon = calendar.get(Calendar.MONTH);
		int curd = calendar.get(Calendar.DAY_OF_MONTH);
		int curh = calendar.get(Calendar.HOUR_OF_DAY);
		int curm = calendar.get(Calendar.MINUTE);
		Log.i(LOG, " cury : curmon : curd  " + cury + ":" + curmon + ":" + curd);
		Log.i(LOG, "times[0]: " + times[0] + " times[1] : " + times[1] + " times[2] : " + times[2]);
		Log.i(LOG, "curh: curm: " + curh + ":" + curm);
		
		if(cury > times[0] || cury == times[0] && curmon > times[1] || cury == times[0] && curmon == times[1] && curd > times[2] ){
			isPassed = true;
		}
		else if(cury == times[0] && curmon == times[1] && curd == times[2]){
			int mhs[] = getHourAndMin(strtime);
			Log.i(LOG, "mhs[0]: mhs[1]: " + mhs[0] + ":" + mhs[1]);
			if(curh < mhs[0] || curh == times[0] && curm < mhs[1]){
				isPassed = false;
			}
			else{
				isPassed = true;
			}
		}
		Log.i(LOG, "---------------isPassed: " + isPassed);
		return isPassed;
	}
	
    
}
