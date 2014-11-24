package com.gcl.myclock.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmTools {
	
	private static final String ACTION = "com.gcl.myclock.alarm";
	private Context mContext;
	
	public AlarmTools(Context context){
		mContext = context;
	}
	
	
	public void setAlarm(String ct,boolean isRepeat,long repeatInterval,long ringTime){
		Intent intent=new Intent();
		intent.setAction(ACTION);
		intent.putExtra("createtime", ct);
		
		PendingIntent pi = PendingIntent.getBroadcast(mContext, ct.hashCode(), intent, 0);
		AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		if (isRepeat)
        {
        	am.setRepeating(AlarmManager.RTC_WAKEUP, ringTime, repeatInterval, pi);
        }
        else
        {
        	am.set(AlarmManager.RTC_WAKEUP, ringTime, pi);
        }
		
	}
		
	public void cancel(String ct){
		Intent intent=new Intent();
		intent.setAction(ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, ct.hashCode(), intent, 0);
		AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
	

}
