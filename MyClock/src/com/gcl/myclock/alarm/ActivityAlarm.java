package com.gcl.myclock.alarm;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

import org.apache.http.client.utils.CloneUtils;

import com.gcl.myclock.ClockApp;
import com.gcl.myclock.R;
import com.gcl.myclock.tools.BirthClock;
import com.gcl.myclock.tools.Clock;
import com.gcl.myclock.tools.Clock.CType;
import com.gcl.myclock.tools.ClockUtils;
import com.gcl.myclock.tools.GetUpClock;
import com.gcl.myclock.tools.VibratorUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;

public class ActivityAlarm extends Activity {

	private static String LOG = "ActivityAlarm";
	private TextView mTextName;
	private ImageView mImage;
	private Button mSleepBtn;
	private Button mOkBtn;
	private Clock mClock;
	private MediaPlayer myMediaPlayer = null;

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

//		VibratorUtil.VibratorUtil(this, new long[] { 0, 500, 1000 }, true);
//		vibrate();
		playMusic();
	}

	private void init() {
		if (((ClockApp) getApplication()).getData().getAllClocks() == null
				|| ((ClockApp) getApplication()).getData().getAllClocks()
						.size() == 0) {
			((ClockApp) getApplication()).getData().LoadAllClocks();
		}

		Intent it = getIntent();
		String createTime = it.getStringExtra("createtime");
		mClock = ((ClockApp) getApplication()).getData().getClockFromCt(
				createTime);
		if (mClock != null) {
			if (mClock.mType.equals(CType.CGetUp)) {
				mTextName.setText("闹钟");
				mImage.setBackgroundResource(R.drawable.main_getup_clock_img);
				if(((GetUpClock)mClock).mVibrate.equals("true")){
					vibrate();
				}
			} else if (mClock.mType.equals(CType.CBirth)) {
				mTextName.setText("生日提醒");
				mImage.setBackgroundResource(R.drawable.main_birth_clock_img);
				if(((BirthClock)mClock).mVibrate.equals("true")){
					vibrate();
				}
			} else {
				mTextName.setText("倒计时");
				mImage.setBackgroundResource(R.drawable.main_invert_clock_img);
			}

		}
	}

	private void vibrate(){
		VibratorUtil.VibratorUtil(this, new long[] { 0, 500, 1000 }, true);			

	}
	private void playMusic() {
		if (mClock != null) {
			Log.i(LOG, "----------------mClock.path: " + mClock.mPath);
			if(mClock.mPath == null || mClock.mPath.equals("")){
				return;
			}
			Uri uri = Uri.parse(mClock.mPath);
			myMediaPlayer = MediaPlayer.create(this, uri);
			myMediaPlayer.start();
			Log.i(LOG, "-----------------------------4-------");
		}
		else{
			Log.i(LOG, "----------------mClock is null ");
		}
	}

	public void onOkClick(View v) {
		doSetRepeatRing();
		finish();
		VibratorUtil.cacel();
		if(myMediaPlayer != null){
			myMediaPlayer.release();
		}		
	}

	public void onSleepClick(View v) {
		finish();
		if(myMediaPlayer != null){
			myMediaPlayer.release();
		}
	}
	
	private void doSetRepeatRing(){
		if(mClock != null){
			if (mClock.mType.equals(CType.CGetUp)) {
				GetUpClock clock = (GetUpClock)mClock;
				if(!clock.mRepeat.equals("0")){
					Calendar calendar = Calendar.getInstance();
					int next = ClockUtils.getNextRepeatDay(ClockUtils.getRepeatInts(clock.mRepeat));
					calendar.add(Calendar.DATE, next);
					AlarmTools tools = new AlarmTools(this);
					tools.cancel(clock.mCreateTime);
					int times[] = ClockUtils.getHourAndMin(clock.mTime);
					calendar.set(Calendar.HOUR_OF_DAY, times[0]);
					calendar.set(Calendar.MINUTE, times[1]);
					tools.setAlarm(clock.mCreateTime, false, 0, calendar.getTimeInMillis());
				}
			}
		}
	}
}
