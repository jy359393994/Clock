package com.gcl.myclock;

import com.gcl.myclock.getup.ActivitySleepTimeSetting;
import com.gcl.myclock.getup.ActivityWeekday;
import com.gcl.myclock.tools.ClockUtils;
import com.gcl.myclock.tools.GetUpClock;
import com.gcl.myclock.tools.VibratorUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.text.GetChars;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class ActivityGetUpClock extends Activity implements OnClickListener {

	private static final String LOG = "ActivityGetUpClock";
	private LinearLayout mRepeatLayout;
	private ToggleButton mToggleBtn;
	private MyHandler mHandler;
	private LinearLayout mBackLayout;
	private LinearLayout mYesImgLayout;
	private String mStrTextRepeat = "永不";
	private String mStrRepeat = "0";
	private int[] mRepeats = { 0, 0, 0, 0, 0, 0, 0 };
	private String mToggleBtnStatus = "false";
	private TextView mRepeatResultTxt;
	private TextView mMusicNameText;
	private String mMusicPath;
	private EditText mLabelEdit;
	private TextView mSleepTimeTxt;
	private TimePicker mPicker;
	private LinearLayout mSleepTimeLayout;
	private static GetUpClock mClock;
	private LinearLayout mMusicLayout;

	private static final int CACLE_VIBRATE = 0;

	public static void enterGetUpClock(GetUpClock clock, Context context) {
		if (clock == null) {
			return;
		}
		mClock = clock;
		Intent it = new Intent(context, ActivityGetUpClock.class);
		context.startActivity(it);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getup_clock);
		mRepeatLayout = (LinearLayout) findViewById(R.id.getup_item_repeat_layout);
		mRepeatLayout.setOnClickListener(this);
		mToggleBtn = (ToggleButton) findViewById(R.id.getup_clock_ToggleButton);
		mToggleBtn.setOnClickListener(this);
		mHandler = new MyHandler();
		mBackLayout = (LinearLayout) findViewById(R.id.layout_getup_clock_back);
		mYesImgLayout = (LinearLayout) findViewById(R.id.layout_getup_clock_yes_btn);
		mBackLayout.setOnClickListener(this);
		mYesImgLayout.setOnClickListener(this);
		mRepeatResultTxt = (TextView) findViewById(R.id.getup_item_repeat_dates);
		mMusicNameText = (TextView) findViewById(R.id.getup_item_sound_value);
		mLabelEdit = (EditText) findViewById(R.id.getup_clock_label_edit);
		mSleepTimeTxt = (TextView) findViewById(R.id.getup_item_time_value);
		mSleepTimeLayout = (LinearLayout) findViewById(R.id.getup_item_time_layout);
		mSleepTimeLayout.setOnClickListener(this);
		mPicker = (TimePicker) findViewById(R.id.time_picker);
		mMusicLayout = (LinearLayout)findViewById(R.id.getup_item_sound_layout);
		mMusicLayout.setOnClickListener(this);
		initDataFromMain();
	}

	private void initDataFromMain() {
		if (mClock != null) {
			setDataPickTime();
			createRepeatStr(ClockUtils.getRepeatInts(mClock.mRepeat));
			mMusicNameText.setText(mClock.mMusic);
			mLabelEdit.setText(mClock.mLabel);
			mSleepTimeTxt.setText(mClock.mSleepTime + "分钟");
			mToggleBtn.setChecked(Boolean.getBoolean(mClock.mVibrate));
			mMusicPath = mClock.mPath;
		}

	}

	private void setDataPickTime() {
		int results[] = { 0, 0 };
		String values[] = mClock.mTime.split(":");
		results[0] = Integer.parseInt(values[0]);
		results[1] = Integer.parseInt(values[1]);
		// return results;
		mPicker.setCurrentHour(results[0]);
		mPicker.setCurrentMinute(results[1]);
	}

	// private String getRepeatText(){
	// str = createRepeatStr(ClockUtils.getRepeatInts(mClock.mRepeat));
	// return str;
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i(LOG, "--------------onClick-------------");
		Intent it = null;
		switch (v.getId()) {
		case R.id.getup_item_repeat_layout:
			it = new Intent(ActivityGetUpClock.this, ActivityWeekday.class);
			it.putExtra("status", mRepeats);
			for (int i = 0; i < 7; i++) {
				Log.i(LOG, "i:  " + i + "mRepeats: " + mRepeats[i]);
			}
			startActivityForResult(it, 102);
			break;
		case R.id.getup_clock_ToggleButton:
			if (mToggleBtn.isChecked()) {
				mToggleBtnStatus = "true";
				long[] values = { 0, 200, 500 };
				VibratorUtil.VibratorUtil(this, values, true);
				mHandler.sendEmptyMessageDelayed(CACLE_VIBRATE, 60000);
			} else {
				mToggleBtnStatus = "false";
			}
			break;
		case R.id.layout_getup_clock_back:
			finish();
			break;
		case R.id.layout_getup_clock_yes_btn:

			mStrRepeat = ClockUtils.getRepeat(mRepeats);
			GetUpClock clock = new GetUpClock(ClockUtils.getCreateTime(),
					"true", createClockingTime(), mLabelEdit.getText()
							.toString(), mStrRepeat, mMusicNameText.getText()
							.toString(), mToggleBtnStatus, mSleepTimeTxt
							.getText().toString().replace("分钟", ""),mMusicPath);
			if (mClock != null) {
				clock.mCreateTime = mClock.mCreateTime;
				((ClockApp) getApplication()).getData().updateGetUpClock(
						mClock, clock);
			} else {
				((ClockApp) getApplication()).getData().addNewGetUpClock(clock);
			}
			finish();
			break;
		case R.id.getup_item_time_layout:
			it = new Intent(ActivityGetUpClock.this,
					ActivitySleepTimeSetting.class);
			it.putExtra("value", 10);
			startActivityForResult(it, 100);
			break;
		case R.id.getup_item_sound_layout:
			it = new Intent(ActivityGetUpClock.this,ActivitySelectMusic.class);
			startActivityForResult(it, 104);
			break;

		}

	}

	private String createClockingTime() {
		StringBuffer str = new StringBuffer();
		int h = mPicker.getCurrentHour();
		int m = mPicker.getCurrentMinute();
		if (h <= 9) {
			str.append("0" + h);
		} else {
			str.append("" + h);
		}
		if (m <= 9) {
			str.append(":0" + m);
		} else {
			str.append(":" + m);
		}
		return str.toString();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i(LOG, "--------------onActivityResult-------------resultCode: "
				+ resultCode);
		if (202 == resultCode) {
			mRepeats = data.getIntArrayExtra("status");
			for (int i = 0; i < 7; i++) {
				Log.i(LOG, "i:  " + i + "mRepeats: " + mRepeats[i]);
			}
			createRepeatStr(mRepeats);
			mRepeatResultTxt.setText(mStrTextRepeat);
		}
		if (200 == resultCode) {
			mSleepTimeTxt.setText(data.getIntExtra("value", 10) + "分钟");
		}
		if(resultCode == 300){
			mMusicNameText.setText(data.getCharSequenceExtra("name").toString());
			mMusicPath =  data.getCharSequenceExtra("path").toString();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void createRepeatStr(int[] results) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < results.length; i++) {
			if (i == 0 && results[i] == 1) {
				str.append("一、");
			}
			if (i == 1 && results[i] == 1) {
				str.append("二、");
			}
			if (i == 2 && results[i] == 1) {
				str.append("三、");
			}
			if (i == 3 && results[i] == 1) {
				str.append("四、");
			}
			if (i == 4 && results[i] == 1) {
				str.append("五、");
			}
			if (i == 5 && results[i] == 1) {
				str.append("六、");
			}
			if (i == 6 && results[i] == 1) {
				str.append("日、");
			}
		}
		mStrTextRepeat = str.toString();
		if (mStrTextRepeat.endsWith("、")) {
			mStrTextRepeat = mStrTextRepeat.substring(0,
					mStrTextRepeat.length() - 1);
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case CACLE_VIBRATE:
				VibratorUtil.cacel();
				break;
			}
		}

	}

}
