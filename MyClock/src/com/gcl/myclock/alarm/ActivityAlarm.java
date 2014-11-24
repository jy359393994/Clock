package com.gcl.myclock.alarm;

import com.gcl.myclock.ClockApp;
import com.gcl.myclock.R;
import com.gcl.myclock.tools.Clock;
import com.gcl.myclock.tools.Clock.CType;
import com.gcl.myclock.tools.VibratorUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityAlarm extends Activity {

	private TextView mTextName;
	private ImageView mImage;
	private Button mSleepBtn;
	private Button mOkBtn;
	private Clock mClock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm);
		mTextName = (TextView) findViewById(R.id.clock_name);
		mImage = (ImageView) findViewById(R.id.clock_img);
		mSleepBtn = (Button) findViewById(R.id.clock_btn_sleep);
		mOkBtn = (Button) findViewById(R.id.clock_btn_ok);
		init();
		
		VibratorUtil.VibratorUtil(this, new long[]{0,500,1000}, true);
	}

	private void init() {
		if (((ClockApp) getApplication()).getData().getAllClocks() == null
				|| ((ClockApp) getApplication()).getData().getAllClocks()
						.size() == 0) 
		{
			((ClockApp) getApplication()).getData().LoadAllClocks();
		}
		
		Intent it = getIntent();
		String createTime = it.getStringExtra("createtime");
		mClock = ((ClockApp) getApplication()).getData().getClockFromCt(createTime);
		if(mClock != null){
			if(mClock.mType.equals(CType.CGetUp)){
				mTextName.setText("闹钟");
				mImage.setBackgroundResource(R.drawable.main_getup_clock_img);
			}
			else if(mClock.mType.equals(CType.CBirth)){
				mTextName.setText("生日提醒");
				mImage.setBackgroundResource(R.drawable.main_birth_clock_img);
			}
			else{
				mTextName.setText("倒计时");
				mImage.setBackgroundResource(R.drawable.main_invert_clock_img);
			}
				
		}
	}
	
	public void onOkClick(View v){
		finish();
		VibratorUtil.cacel();
	}

	public void onSleepClick(View v){
		finish();
	}
}
