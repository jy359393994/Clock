package com.gcl.myclock.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent it = new Intent(context,ActivityAlarm.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		it.putExtra("createtime", intent.getStringExtra("createtime"));
		context.startActivity(it);
		
	}

}
