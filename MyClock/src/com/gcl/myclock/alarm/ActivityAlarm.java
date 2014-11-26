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
import com.gcl.myclock.tools.InvertClock;
import com.gcl.myclock.tools.VibratorUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class ActivityAlarm extends Activity implements OnCompletionListener{

	private static String LOG = "ActivityAlarm";
	private TextView mTextName;
	private ImageView mImage;
	private Clock mClock;
	private MediaPlayer myMediaPlayer = null;
	private WakeLock mWakelock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm);
		mTextName = (TextView) findViewById(R.id.clock_name);
		mImage = (ImageView) findViewById(R.id.clock_img);
		init();
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
				Log.i(LOG, "GetUpClock    mVibrate : "
						+ ((GetUpClock) mClock).mVibrate
						+ "------------------------------------");
				if (((GetUpClock) mClock).mVibrate.equals("true")) {
					vibrate();
				}
			} else if (mClock.mType.equals(CType.CBirth)) {
				mTextName.setText("生日提醒");
				mImage.setBackgroundResource(R.drawable.main_birth_clock_img);
				Log.i(LOG, "GetUpClock    mVibrate : "
						+ ((BirthClock) mClock).mVibrate
						+ "------------------------------------");
				if (((BirthClock) mClock).mVibrate.equals("true")) {
					vibrate();
				}
			} else {
				mTextName.setText("倒计时");
				mImage.setBackgroundResource(R.drawable.main_invert_clock_img);
			}

		}
	}

	private void vibrate() {
		VibratorUtil.VibratorUtil(this, new long[] { 0, 500, 1000 }, true);

	}

	private void playMusic() {
		if (mClock != null) {
			Log.i(LOG, "----------------mClock.path: " + mClock.mPath);
			if (mClock.mPath == null || mClock.mPath.equals("")) {
				myMediaPlayer = MediaPlayer.create(this, R.raw.default_music);
				myMediaPlayer.setOnCompletionListener(this);
				myMediaPlayer.start();
			} else {
				Uri uri = Uri.parse(mClock.mPath);
				if (myMediaPlayer == null) {
					myMediaPlayer = MediaPlayer.create(this, uri);
					myMediaPlayer.setOnCompletionListener(this);
					myMediaPlayer.start();
				}
			}
		} else {
			Log.i(LOG, "----------------mClock is null ");
		}
	}

	public void onOkClick(View v) {
		VibratorUtil.cacel();
		if (myMediaPlayer != null) {
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		updateClockStatus();
		finish();
	}

	public void onSleepClick(View v) {
		VibratorUtil.cacel();
		if (myMediaPlayer != null) {
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		updateClockStatus();
		finish();
	}

	private void updateClockStatus() {
		if (mClock != null) {
			if (mClock.mType.equals(CType.CBirth)) {
				BirthClock clock = (BirthClock) mClock;
				BirthClock clo = new BirthClock(ClockUtils.getCreateTime(),
						"false", clock.mTime, clock.mDay, clock.mLabel,
						clock.mMusic, clock.mVibrate, clock.mPath);
				clo.mCreateTime = clock.mCreateTime;
				((ClockApp) getApplication()).getData().updateBirthClock(clock,
						clo);
			}
			
			if(mClock.mType.equals(CType.CGetUp)){
				doRepeatRingOrChangeStatus();
			}
			if(mClock.mType.equals(CType.CInvert)){
				InvertClock clock = (InvertClock)mClock;
				InvertClock clo = new InvertClock(clock.mCreateTime, "false", clock.mTime, clock.mLabel, clock.mMusic, clock.mPath);
				((ClockApp) getApplication()).getData().updateInvertClock(clock,
						clo);
			}
		}
	}

	private void doRepeatRingOrChangeStatus() {

		GetUpClock clock = (GetUpClock) mClock;
		if (!clock.mRepeat.equals("0")) {
			Calendar calendar = Calendar.getInstance();
			int next = ClockUtils.getNextRepeatDay(ClockUtils
					.getRepeatInts(clock.mRepeat));
			calendar.add(Calendar.DATE, next);
			AlarmTools tools = new AlarmTools(this);
			tools.cancel(clock.mCreateTime);
			int times[] = ClockUtils.getHourAndMin(clock.mTime);
			calendar.set(Calendar.HOUR_OF_DAY, times[0]);
			calendar.set(Calendar.MINUTE, times[1]);
			tools.setAlarm(clock.mCreateTime, false, 0,
					calendar.getTimeInMillis());
		} else {
			GetUpClock clo = new GetUpClock(ClockUtils.getCreateTime(),
					"false", clock.mTime, clock.mLabel,clock.mRepeat,
					clock.mMusic, clock.mVibrate, clock.mSleepTime,clock.mPath);
			clo.mCreateTime = clock.mCreateTime;
			((ClockApp) getApplication()).getData().updateGetUpClock(clock,
					clo);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mWakelock != null) {
			mWakelock.release();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.i(LOG,"-------------------onCompletion------------  ");
		if(myMediaPlayer != null){
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		playMusic();
	}
}
