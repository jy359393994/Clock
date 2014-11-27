package com.gcl.myclock;

import java.util.Calendar;
import java.util.TimeZone;

import com.gcl.myclock.alarm.AlarmTools;
import com.gcl.myclock.tools.BirthClock;
import com.gcl.myclock.tools.ClockUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ActivityBirthdayClock extends Activity implements OnClickListener{
	
	private static String LOG = "ActivityBirthdayClock";
	private DatePicker mPicker;
	private TextView mLabelText;
	private LinearLayout mBackLayout;
	private LinearLayout mYesLayout;
	private TextView mRingTimeTxt;
	private TextView mMusicNameTxt;
	private String mMusicPath;
	private ToggleButton mToggleBtn;
	private String mToggleBtnStatus = "false";
	private String mCreatClockTime;
	private static BirthClock mClock;
	private LinearLayout mRingLayout;
	private TimePickerDialog mDialog;
	private LinearLayout mRingTimeLayout;
	private TextView mTitle;
	
	
	public static void enterBirthClock(BirthClock clock,Context context){
		Log.i(LOG, "------enterBirthClock--------");
		if(clock == null){
			return;
		}						
		mClock = clock;
		Intent it = new Intent(context,ActivityBirthdayClock.class);
		context.startActivity(it);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birthday_clock);
		
		mPicker = (DatePicker)findViewById(R.id.date_picker);
		mLabelText = (TextView)findViewById(R.id.bir_label_edit);
		mBackLayout = (LinearLayout)findViewById(R.id.layout_bir_clock_back);
		mYesLayout = (LinearLayout)findViewById(R.id.layout_bir_clock_yes_btn);
		mRingTimeTxt = (TextView)findViewById(R.id.birth_item_time_value);
		mMusicNameTxt = (TextView)findViewById(R.id.birth_item_sound_value);
		mToggleBtn = (ToggleButton)findViewById(R.id.bir_togglebtn);
		mToggleBtn.setOnClickListener(this);
		mRingLayout = (LinearLayout)findViewById(R.id.birth_item_sound_layout);
		mRingTimeLayout = (LinearLayout)findViewById(R.id.birth_item_time_layout);
		mTitle = (TextView)findViewById(R.id.bir_clock_title);
		mRingTimeLayout.setOnClickListener(this);
		mRingLayout.setOnClickListener(this);
		mBackLayout.setOnClickListener(this);
		mYesLayout.setOnClickListener(this);
		initDatasFromMain();
	}

	private void initDatasFromMain(){
		if(mClock != null){
			int picks[] = getDataPickDatas();
			mPicker.init(picks[0], picks[1], picks[2], null);
			mLabelText.setText(mClock.mLabel);
			mRingTimeTxt.setText(mClock.mTime);
			mMusicNameTxt.setText(mClock.mMusic);
			mMusicPath = mClock.mPath;
			mToggleBtn.setChecked(Boolean.parseBoolean(mClock.mVibrate));
			mTitle.setText("编辑闹钟");
		}
	}
	private int[] getDataPickDatas(){
		int results[] = {0,0,0};
		if(mClock.mDay != null){
			String values[] = mClock.mDay.split("/");
			Log.i(LOG, "----------------values:------------------------" + mClock.mDay);
			results[0] = Integer.parseInt(values[0]);
			results[1] = Integer.parseInt(values[1]) - 1;
			results[2] = Integer.parseInt(values[2]);
		}
		return results;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.layout_bir_clock_back:
			finish();
			break;
			
		case R.id.bir_togglebtn:
			if(mToggleBtn.isChecked()){
				mToggleBtnStatus = "true";
			}
			else{
				mToggleBtnStatus = "false";
			}
			break;
		case R.id.layout_bir_clock_yes_btn:
			BirthClock clock = new BirthClock(ClockUtils.getCreateTime(), "true", mRingTimeTxt.getText().toString(), 
					getBirthDay(),mLabelText.getText().toString(), mMusicNameTxt.getText().toString(), mToggleBtnStatus,mMusicPath);
//			Log.i(LOG, c.toString());
			Log.i(LOG, "mToggleBtnStatus　:---------------------" + "  " + mToggleBtnStatus);
			if(mClock != null){
				clock.mCreateTime = mClock.mCreateTime;
				((ClockApp)getApplication()).getData().updateBirthClock(mClock, clock);
				
			}
			else{			
				((ClockApp)getApplication()).getData().addNewBirthClock(clock);
				
			}
			AlarmTools tools = new AlarmTools(this);
			tools.cancel(clock.mCreateTime);
			Calendar c = ClockUtils.getCalendarForHourAndMinus(clock.mTime);
			int[] days = ClockUtils.getDataPickDatas(clock.mDay);
			if(!ClockUtils.isBirthHasPassed(days, clock.mTime)){
				c.set(days[0], days[1], days[2]);
				Log.i(LOG, c.toString());
				tools.setAlarm(clock.mCreateTime, false, 0, c.getTimeInMillis());
			}
			else{
				Toast.makeText(this, "生日闹钟已过期！！！可点击编辑，编辑闹钟时间", Toast.LENGTH_SHORT).show();
				BirthClock clo = new BirthClock(ClockUtils.getCreateTime(), "false", mRingTimeTxt.getText().toString(), 
						getBirthDay(),mLabelText.getText().toString(), mMusicNameTxt.getText().toString(), mToggleBtnStatus,mMusicPath);
				clo.mCreateTime = clock.mCreateTime;
				((ClockApp)getApplication()).getData().updateBirthClock(clock, clo);
				
			}
			
			finish();					
			break;
		case R.id.birth_item_sound_layout:
			Intent it = new Intent(ActivityBirthdayClock.this,ActivitySelectMusic.class);
			startActivityForResult(it, 101);
			break;
		case R.id.birth_item_time_layout:
			popTimePickerDialog();
			if(mDialog != null){
				mDialog.show();
			}
			break;
		
		}
	}
	
	private String getBirthDay(){
		StringBuffer birthDay = new StringBuffer();
		int year = mPicker.getYear();
		int month = mPicker.getMonth() + 1;
		int day = mPicker.getDayOfMonth();
		birthDay.append("" + year + "/");
		if(month < 10){
			birthDay.append("0" + month + "/");
		}
		else{
			birthDay.append(month + "/");
		}
		if(day < 10){
			birthDay.append("0" + day);
		}
		else{
			birthDay.append(day + "");
		}
		return birthDay.toString();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 300){
			mMusicNameTxt.setText(data.getCharSequenceExtra("name").toString());
			mMusicPath =  data.getCharSequenceExtra("path").toString();
		}				
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void popTimePickerDialog(){  
        TimePickerDialog.OnTimeSetListener otsl=new TimePickerDialog.OnTimeSetListener(){  
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
                mDialog.dismiss();
                String h = String.format("%02d", hourOfDay);
                String m = String.format("%02d", minute);
                mRingTimeTxt.setText(h + ":" + m);
            }  
        };  
        Calendar calendar=Calendar.getInstance(TimeZone.getDefault());  
        int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);  
        int minute=calendar.get(Calendar.MINUTE);  
        mDialog=new TimePickerDialog(this,otsl,hourOfDay,minute,true);  
        
    }  
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mClock != null){
			mClock = null;
		}
		
		super.onDestroy();
	}
		
}
