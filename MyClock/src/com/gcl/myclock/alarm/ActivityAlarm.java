package com.gcl.myclock.alarm;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class ActivityAlarm extends Activity implements OnCompletionListener {

	private static String LOG = "ActivityAlarm";
	private TextView mTextName;
	private ImageView mImage;
	private Clock mClock;
	private MediaPlayer myMediaPlayer = null;
	private WakeLock mWakelock;
	private HomeWatcherReceiver mHomeKeyReceiver = null;
	private boolean isAlive = true;

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
		// updateClockStatus();
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
				findViewById(R.id.clock_layout_sleep).setVisibility(View.GONE);
				Log.i(LOG, "GetUpClock    mVibrate : "
						+ ((BirthClock) mClock).mVibrate
						+ "------------------------------------");
				if (((BirthClock) mClock).mVibrate.equals("true")) {
					vibrate();
				}
			} else {
				mTextName.setText("倒计时");
				mImage.setBackgroundResource(R.drawable.main_invert_clock_img);
				findViewById(R.id.clock_layout_sleep).setVisibility(View.GONE);
			}

		}
	}

	private void vibrate() {
		Log.i(LOG, "---------------------vibrate------------");
		VibratorUtil.VibratorUtil(this, new long[] { 0, 500, 1000 }, true);

	}

	private void playMusic() {
		Log.i(LOG, "---------------------playMusic------------");
		if (mClock != null) {
			Log.i(LOG, "----------------mClock.path: " + mClock.mPath);
			// Log.i(LOG, "----------------mClock.path: " +
			// mClock.mPath==null?"空":mClock.mPath);
			if (mClock.mPath == null || "null".equals(mClock.mPath)) {
				myMediaPlayer = MediaPlayer.create(this, R.raw.default_music);
				myMediaPlayer.setOnCompletionListener(this);
				myMediaPlayer.start();
			} else {
				Uri uri = Uri.parse(mClock.mPath);
				if (myMediaPlayer == null) {
					myMediaPlayer = MediaPlayer.create(this, uri);
					if (myMediaPlayer == null) {
						Log.i(LOG, "----------------myMediaPlayer is null ");
					} else {
						myMediaPlayer.setOnCompletionListener(this);
						myMediaPlayer.start();
					}
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
		isAlive = false;
		finish();
	}

	public void onSleepClick(View v) {
		VibratorUtil.cacel();
		if (myMediaPlayer != null) {
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		// updateClockStatus();
		doHaveASleep();
		isAlive = false;
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

			if (mClock.mType.equals(CType.CGetUp)) {
				doRepeatRingOrChangeStatus();
			}
			if (mClock.mType.equals(CType.CInvert)) {
				InvertClock clock = (InvertClock) mClock;
				InvertClock clo = new InvertClock(clock.mCreateTime, "false",
						clock.mTime, clock.mLabel, clock.mMusic, clock.mPath);
				((ClockApp) getApplication()).getData().updateInvertClock(
						clock, clo);
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
			calendar.set(Calendar.SECOND, 0);	
			tools.setAlarm(clock.mCreateTime, false, 0,
					calendar.getTimeInMillis());
		} else {
			GetUpClock clo = new GetUpClock(ClockUtils.getCreateTime(),
					"false", clock.mTime, clock.mLabel, clock.mRepeat,
					clock.mMusic, clock.mVibrate, clock.mSleepTime, clock.mPath);
			clo.mCreateTime = clock.mCreateTime;
			((ClockApp) getApplication()).getData()
					.updateGetUpClock(clock, clo);
		}

	}

	private void doHaveASleep() {
		if (mClock != null) {
			if (mClock.mType.equals(CType.CGetUp)) {
				GetUpClock clock = (GetUpClock) mClock;
				int internalTime = Integer.parseInt(clock.mSleepTime);
				Calendar c = Calendar.getInstance(Locale.CHINA);
				c.add(Calendar.MINUTE, internalTime);
				c.set(Calendar.SECOND, 0);	
				AlarmTools tools = new AlarmTools(this);
				tools.cancel(clock.mCreateTime);
				tools.setAlarm(clock.mCreateTime, false, 0, c.getTimeInMillis());
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
//		registerHomeKeyReceiver(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mWakelock != null) {
			mWakelock.release();
		}
//		unregisterHomeKeyReceiver(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(LOG, "-------------------onStop------------ isAlive: " + isAlive);
		if(isAlive){
			VibratorUtil.cacel();
			if (myMediaPlayer != null) {
				myMediaPlayer.release();
				myMediaPlayer = null;
			}
			doHaveASleep();
			isAlive = false;
			finish();
		}
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.i(LOG, "-------------------onCompletion------------  ");
		if (myMediaPlayer != null) {
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		playMusic();
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//
//		if ((event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_BACK))) {
//			Log.i(LOG,
//					"-------------------ACTION_UP------------  "
//							+ event.getKeyCode());
//			VibratorUtil.cacel();
//			if (myMediaPlayer != null) {
//				myMediaPlayer.release();
//				myMediaPlayer = null;
//			}
//			doHaveASleep();
//			finish();
//		}
//		return super.dispatchKeyEvent(event);
//	}

	private void registerHomeKeyReceiver(Context context) {
		Log.i(LOG, "registerHomeKeyReceiver");
		mHomeKeyReceiver = new HomeWatcherReceiver();
		final IntentFilter homeFilter = new IntentFilter(
				Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

		context.registerReceiver(mHomeKeyReceiver, homeFilter);
	}

	private void unregisterHomeKeyReceiver(Context context) {
		Log.i(LOG, "unregisterHomeKeyReceiver");
		if (null != mHomeKeyReceiver) {
			context.unregisterReceiver(mHomeKeyReceiver);
		}
	}

	private class HomeWatcherReceiver extends BroadcastReceiver {
		private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
		private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
		private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
		private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.i(LOG, "onReceive: action: " + action);
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				// android.intent.action.CLOSE_SYSTEM_DIALOGS
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				Log.i(LOG, "reason: " + reason);

				if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
					// 短按Home键
					Log.i(LOG, "homekey");
					VibratorUtil.cacel();
					if (myMediaPlayer != null) {
						myMediaPlayer.release();
						myMediaPlayer = null;
					}
					doHaveASleep();
					finish();

				} else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
					// 长按Home键 或者 activity切换键
					Log.i(LOG, "long press home key or activity switch");

				} else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
					// 锁屏
					Log.i(LOG, "lock");
				} else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
					// samsung 长按Home键
					Log.i(LOG, "assist");
				}

			}
		}

	}
}
