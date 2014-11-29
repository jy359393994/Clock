package com.gcl.myclock.getup;

import com.gcl.myclock.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ActivitySleepTimeSetting extends Activity{
	private static final String LOG = "ActivitySleepTimeSetting";
	private RadioGroup mGroup;
	private RadioButton mFiveBtn;
	private RadioButton mTenBtn;
	private RadioButton mTwentyBtn;
	private RadioButton mThirtyBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(LOG, "-------------onCreate----------------");
		setContentView(R.layout.sleep_time_setting);
		mGroup = (RadioGroup)findViewById(R.id.sleep_time_group);
		mFiveBtn = (RadioButton)findViewById(R.id.five_button);
		mTenBtn = (RadioButton)findViewById(R.id.ten_button);
		mTwentyBtn = (RadioButton)findViewById(R.id.twenty_button);
		mThirtyBtn = (RadioButton)findViewById(R.id.thirty_button);
		Intent it = getIntent();
		int time = it.getIntExtra("value", 10);
		if(time == 10){
			mGroup.check(R.id.ten_button);
		}
		if(time == 5){
			mGroup.check(R.id.five_button);
		}
		if(time == 20){
			mGroup.check(R.id.twenty_button);
		}
		if(time == 30){
			mGroup.check(R.id.thirty_button);
		}
		mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Intent data = new Intent();
				if(checkedId == R.id.five_button){
					data.putExtra("value", 5);
				}
				else if(checkedId == R.id.ten_button){
					data.putExtra("value", 10);
				}
				else if(checkedId == R.id.twenty_button){
					data.putExtra("value", 20);
				}
				else{
					data.putExtra("value", 30);
				}
				setResult(200, data);
				ActivitySleepTimeSetting.this.finish();
			}
		});
		
		
	}

}
