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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birthday_clock);
		
		mPicker = (DatePicker)findViewById(R.id.date_picker);
		mLabelText = (TextView)findViewById(R.id.bir_label_edit);
		mBackLayout = (LinearLayout)findViewById(R.id.layout_bir_clock_back);
		mYesLayout = (LinearLayout)findViewById(R.id.layout_bir_clock_yes_btn);
		mRingTimeTxt = (TextView)findViewById(R.id.birth_item_time_value);
		mMusicNameTxt = (TextView)findViewById(R.id.birth_item_sound_value);
		mToggleBtn = (ToggleButton)findViewById(R.id.bir_togglebtn);
		mRingLayout = (LinearLayout)findViewById(R.id.birth_item_sound_layout);
		mRingTimeLayout = (LinearLayout)findViewById(R.id.birth_item_time_layout);
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
			mToggleBtn.setChecked(Boolean.getBoolean(mClock.mStatus));
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
	
	private int[] getDataPickDatas(String day){
		int results[] = {0,0,0};
			String values[] = day.split("/");
//			Log.i(LOG, "----------------values:------------------------" + mClock.mDay);
			results[0] = Integer.parseInt(values[0]);
			results[1] = Integer.parseInt(values[1]) - 1;
			results[2] = Integer.parseInt(values[2]);
		return results;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
			int[] days = getDataPickDatas(clock.mDay);
			c.set(days[0], days[1], days[2]);
			Log.i(LOG, c.toString());
			tools.setAlarm(clock.mCreateTime, false, 0, c.getTimeInMillis());
			
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
//		String birthDay = "";
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
		// TODO Auto-generated method stub
		if(resultCode == 300){
			mMusicNameTxt.setText(data.getCharSequenceExtra("name").toString());
			mMusicPath =  data.getCharSequenceExtra("path").toString();
		}				
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void popTimePickerDialog(){  
        TimePickerDialog.OnTimeSetListener otsl=new TimePickerDialog.OnTimeSetListener(){  
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
//                tv.setText("您设置了时间："+hourOfDay+"时"+minute+"分");  
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
		
}
