package com.gcl.myclock;


import java.util.Calendar;
import java.util.Locale;

import com.gcl.myclock.alarm.AlarmTools;
import com.gcl.myclock.tools.BirthClock;
import com.gcl.myclock.tools.ClockUtils;
import com.gcl.myclock.tools.DialogEditName;
import com.gcl.myclock.tools.GetUpClock;
import com.gcl.myclock.tools.InvertClock;
import com.identify.timepicker.NumericWheelAdapter;
import com.identify.timepicker.OnWheelChangedListener;
import com.identify.timepicker.OnWheelClickedListener;
import com.identify.timepicker.OnWheelScrollListener;
import com.identify.timepicker.WheelView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ActivityInvertClock extends Activity implements OnClickListener{
	private static final String LOG = "ActivityInvertClock";
	private boolean timeChanged = false;
	private boolean timeScrolled = false;
	
	private Button mBtnYes;
	private Button mBtnNo;
	private WheelView mHour;
	private WheelView mMin;
	private WheelView mSec;
	private TextView mTextClockName;
	private TextView mTextMusicName;
	private String mMusicPath;
	private LinearLayout mClockNameLayout;
	private LinearLayout mMusicNameLayout;
	private CustomLabelImg mButtonOne;
	private CustomLabelImg mButtonTwo;
	private CustomLabelImg mButtonThree;
	private static InvertClock mClock;
	private LinearLayout mBackLayout;
	
	public static void enterInvertClock(InvertClock clock,Context context){
		if(clock == null)			
		{
			return;
		}
		mClock = clock;
		Intent it = new Intent(context, ActivityInvertClock.class);
		context.startActivity(it);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invert_clock);
		
		mTextClockName = (TextView)findViewById(R.id.invert_item_clockname_value);
		mTextMusicName = (TextView)findViewById(R.id.invert_item_music_value);
		mClockNameLayout = (LinearLayout)findViewById(R.id.invert_item_clockname_layout);
		mMusicNameLayout = (LinearLayout)findViewById(R.id.invert_item_music_layout);
		mBackLayout = (LinearLayout)findViewById(R.id.layout_invert_clock_back);
		mBackLayout.setOnClickListener(this);
		mClockNameLayout.setOnClickListener(this);
		mMusicNameLayout.setOnClickListener(this);
		
		mHour = (WheelView) findViewById(R.id.invert_picker_hour);
		mHour.setViewAdapter(new NumericWheelAdapter(this, 0, 23,"%02d"));
		mHour.setCyclic(true);
		
		mMin = (WheelView) findViewById(R.id.invert_picker_mins);
		mMin.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		mMin.setCyclic(true);
		
		mSec = (WheelView) findViewById(R.id.invert_picker_sends);
		mSec.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		mSec.setCyclic(true);
		
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		mHour.setCurrentItem(calendar.get(Calendar.HOUR));
		mMin.setCurrentItem(calendar.get(Calendar.MINUTE));
		mSec.setCurrentItem(calendar.get(Calendar.SECOND));
        
        addChangingListener(mMin, "min");
		addChangingListener(mHour, "hour");
	
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					timeChanged = true;
					timeChanged = false;
				}
			}
		};
		mHour.addChangingListener(wheelListener);
		mMin.addChangingListener(wheelListener);
		
		OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        mHour.addClickingListener(click);
        mMin.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				timeChanged = true;
				timeChanged = false;
			}
		};
		
		mHour.addScrollingListener(scrollListener);
		mMin.addScrollingListener(scrollListener);

		mBtnYes = (Button)findViewById(R.id.invert_btn_yes);
		mBtnNo = (Button)findViewById(R.id.invert_btn_no);
		mBtnYes.setOnClickListener(this);
		mBtnNo.setOnClickListener(this);
		mButtonOne = (CustomLabelImg)findViewById(R.id.invert_customlable_img_one);
		mButtonTwo = (CustomLabelImg)findViewById(R.id.invert_customlable_img_two);
		mButtonThree = (CustomLabelImg)findViewById(R.id.invert_customlable_img_three);
		mButtonOne.setOnClickListener(this);
		mButtonTwo.setOnClickListener(this);
		mButtonThree.setOnClickListener(this);
		initDataFromMain();
	}

	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
			}
		});
	}

	private void initDataFromMain(){
		if(mClock != null)
		{
			String times[] = mClock.mTime.split(":");
			mHour.setCurrentItem(Integer.parseInt(times[0]));
			mMin.setCurrentItem(Integer.parseInt(times[1]));
			mSec.setCurrentItem(Integer.parseInt(times[2]));
			mTextClockName.setText(mClock.mLabel);
			mTextMusicName.setText(mClock.mMusic);
			mMusicPath = mClock.mPath;
			
		}
	}
	
	
	@SuppressLint("DefaultLocale")
	private String createTimeStr(){
		StringBuffer time = new StringBuffer();
		time.append(String.format("%02d", mHour.getCurrentItem()));
		time.append(":" + String.format("%02d", mMin.getCurrentItem()));
		time.append(":" + String.format("%02d", mSec.getCurrentItem()));
		return time.toString();
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.invert_btn_yes:
			InvertClock c = new InvertClock(ClockUtils.getCreateTime(), "true", createTimeStr(), mTextClockName.getText().toString(), mTextMusicName.getText().toString(),mMusicPath);

			if(mClock != null){
				c.mCreateTime = mClock.mCreateTime;
				((ClockApp)getApplication()).getData().updateInvertClock(mClock, c);
			}
			else{
			((ClockApp)getApplication()).getData().addNewInvertClock(c);
			}			
			AlarmTools tools = new AlarmTools(this);
			tools.cancel(c.mCreateTime);
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			int sec = ClockUtils.getHourAndMinAndSec(c.mTime);
			calendar.add(Calendar.SECOND, sec);
			tools.setAlarm(c.mCreateTime, false, 0, calendar.getTimeInMillis());
			finish();
			break;
		case R.id.invert_btn_no:
			finish();
			break;
		case R.id.invert_item_music_layout:
			Intent it = new Intent(ActivityInvertClock.this,ActivitySelectMusic.class);
			startActivityForResult(it, 103);
			break;
		case R.id.invert_item_clockname_layout:
			popEditDialog();
			break;
			
		case R.id.invert_customlable_img_one:
			mTextClockName.setText(mButtonOne.getText().toString());
			break;
		case R.id.invert_customlable_img_two:
			mTextClockName.setText(mButtonTwo.getText().toString());
			
			break;	
		case R.id.invert_customlable_img_three:
			mTextClockName.setText(mButtonThree.getText().toString());
			
			break;
		case R.id.layout_invert_clock_back:
			finish();
			break;
		}
		
	}
	
	
	private void popEditDialog(){
		
		final DialogEditName editDialog = new DialogEditName(this);
		editDialog.setParameters(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				editDialog.dismiss();
				String s = editDialog.getInputString();
				if (s.equals(""))
				{
					Toast.makeText(ActivityInvertClock.this, "«Î ‰»Îƒ÷÷”√˚≥∆", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Log.i(LOG, "input: " + s);
					mTextClockName.setText(s);
				}
			}
		}, new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				editDialog.dismiss();
			}
		}, "ƒ÷÷”√˚≥∆");
		
		editDialog.showDialog();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 300){
			mTextMusicName.setText(data.getCharSequenceExtra("name").toString());
			mMusicPath =  data.getCharSequenceExtra("path").toString();
		}				
		super.onActivityResult(requestCode, resultCode, data);
	}

}
