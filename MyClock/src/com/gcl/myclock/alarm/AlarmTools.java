package com.gcl.myclock.alarm;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmTools {
	
	private static final String ACTION = "com.gcl.myclock.alarm";
	private static final String LOG = "AlarmTools";
	private Context mContext;
	
	public AlarmTools(Context context){
		mContext = context;
	}
	
	
	public void setAlarm(String ct,boolean isRepeat,long repeatInterval,long ringTime){
		Intent intent=new Intent();
		intent.setAction(ACTION);
		intent.putExtra("createtime", ct);
		Log.i(LOG, "-------------------ct : " + ct);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, ct.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		if (isRepeat)
        {
        	am.setRepeating(AlarmManager.RTC_WAKEUP, ringTime, repeatInterval, pi);
        }
        else
        {
        	am.set(AlarmManager.RTC_WAKEUP, ringTime, pi);
        }
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(ringTime);
		String dateStr = dateFormat.format(date);
		Log.i(LOG, "-------------------------------------dateStr:--------    " +  dateStr);
		
	}
		
	public void cancel(String ct){
		Log.i(LOG, "-------------------------------------cancel:--------    ");
		Intent intent=new Intent();
		intent.setAction(ACTION);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, ct.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		am.cancel(pi);
	}
	

}
