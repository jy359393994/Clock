package com.gcl.myclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.gcl.myclock.alarm.AlarmTools;
import com.gcl.myclock.tools.BirthClock;
import com.gcl.myclock.tools.Clock;
import com.gcl.myclock.tools.Clock.CType;
import com.gcl.myclock.tools.ClockAdapter;
import com.gcl.myclock.tools.ClockAdapter.PositonViewListener;
import com.gcl.myclock.tools.ClockUtils;
import com.gcl.myclock.tools.GetUpClock;
import com.gcl.myclock.tools.InvertClock;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.ext.SatelliteMenu.SateliteClickedListener;


public class ActivityMyClock extends Activity implements OnClickListener,OnItemClickListener,PositonViewListener,OnCheckedChangeListener{
	
	private static final String TAG = "ActivityMyClock";
	private ImageView mMenuBtn;
	private ImageView mRecordBtn;
	private SatelliteMenu mSateMenu;
	private ListView mListView;
	private List<Clock> mClocks;
	private ClockAdapter mAdapter;
	private ToggleButton mToggleButton;
	private boolean mIsChangeable = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "---------------- onCreate--------------");
		 UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_my_clock);
		mToggleButton = (ToggleButton)findViewById(R.id.main_change_togglebtn);
		mToggleButton.setOnClickListener(this);
		mToggleButton.setOnCheckedChangeListener(this);
		mMenuBtn = (ImageView)findViewById(R.id.btn_menu);
		mMenuBtn.setOnClickListener(this);
		mSateMenu = (SatelliteMenu)findViewById(R.id.img_tools_pop_clock);
		mRecordBtn = (ImageView)findViewById(R.id.imgs_tools_record);
		mRecordBtn.setOnClickListener(this);
		mListView = (ListView)findViewById(R.id.main_clock_list);
		mListView.setOnItemClickListener(this);
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(1, R.drawable.empty));
		items.add(new SatelliteMenuItem(2, R.drawable.getup_clock_enter));
		items.add(new SatelliteMenuItem(3, R.drawable.invert_clock_enter));        
        items.add(new SatelliteMenuItem(4, R.drawable.birth_clock_enter));	
        mSateMenu.addItems(items);
        mSateMenu.setOnItemClickedListener(new SateliteClickedListener(){

			@Override
			public void eventOccured(int id) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Clicked on " + id);
				Intent it = null;
				switch(id){
				case 2:
					it = new Intent(ActivityMyClock.this, ActivityGetUpClock.class);
					startActivity(it);
					break;
				case 3: 
					it = new Intent(ActivityMyClock.this, ActivityInvertClock.class);
					startActivity(it);
					break;
				case 4:
					it = new Intent(ActivityMyClock.this, ActivityBirthdayClock.class);
					startActivity(it);
					break;
				}
			}
        	
        });
       
        mAdapter = new ClockAdapter(this);
        mAdapter.setClickListener(this);
        
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "------------in onClick------------");
		switch(v.getId()){
		
		case R.id.main_change_togglebtn:
			Log.i(TAG, "------------in onClick------------main_change_togglebtn");
			break;
			
		case R.id.btn_menu:
			Intent it = new Intent(ActivityMyClock.this,ActivityMenu.class);
			startActivity(it);
			break;
		case R.id.imgs_tools_record:
			Log.i(TAG, "------------record------------");
			
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "------------onResume------------");
		mClocks = ((ClockApp)getApplication()).getData().getAllClocks();
		mAdapter.addAllClocks(mClocks);
        mListView.setAdapter(mAdapter);
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "------------onStop------------");
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "------------onRestart------------");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position < 0 || position >= mClocks.size()){
			return;
		}
		Clock clock = mClocks.get(position);
		if(clock.mType.equals(CType.CBirth)){
			ActivityBirthdayClock.enterBirthClock((BirthClock)clock, this);
		}
		if(clock.mType.equals(CType.CGetUp)){
			ActivityGetUpClock.enterGetUpClock((GetUpClock)clock, this);
		}
		
		if(clock.mType.equals(CType.CInvert)){
			ActivityInvertClock.enterInvertClock((InvertClock)clock, this);
		}
		
	}

	@Override
	public void clickPositionTB(int position) {
		Log.i(TAG, "------------clickPositionTB------------position: " + position);
		String status = "false";
		AlarmTools tools = new AlarmTools(this);
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		if(mClocks.size() > 0){
			if(mClocks.get(position).mStatus.equals("true")){
				status = "false";
			}
			else{
				status = "true";
			}
			if(mClocks.get(position).mType.equals(CType.CGetUp)){
				GetUpClock old = (GetUpClock)mClocks.get(position);
				GetUpClock newclock = new GetUpClock(old.mCreateTime, status, old.mTime, 
						old.mLabel, old.mRepeat, old.mMusic, old.mVibrate, old.mSleepTime,old.mPath);
				((ClockApp)getApplication()).getData().updateGetUpClock(old, newclock);				
				int curH = calendar.get(Calendar.HOUR_OF_DAY);;
				int curM = calendar.get(Calendar.MINUTE);
				int times[] = ClockUtils.getHourAndMin(old.mTime);
				if (curH > times[0] || curH == times[0] && curM > times[1]) {
					Log.i(TAG, "--------------------------明天的闹钟--------------");
					calendar.add(Calendar.DATE, 1);
				}
				calendar.set(Calendar.HOUR_OF_DAY, times[0]);
				calendar.set(Calendar.MINUTE, times[1]);
				Log.i(TAG, "---------------------------status: -------------------" + status);
				if(status.equals("true")){
					Log.i(TAG, "--------------设置alarm -------------------------");
					tools.setAlarm(old.mCreateTime, false, 0, calendar.getTimeInMillis());
				}
				else{
					Log.i(TAG, "--------------取消alarm -------------------------");
					tools.cancel(old.mCreateTime);
				}
			}
			else if(mClocks.get(position).mType.equals(CType.CBirth)){
				BirthClock old = (BirthClock)mClocks.get(position);
				BirthClock newclock = new BirthClock(old.mCreateTime, status, old.mTime,old.mDay, 
						old.mLabel, old.mMusic, old.mVibrate,old.mPath);
				((ClockApp)getApplication()).getData().updateBirthClock(old, newclock);
				Calendar c = ClockUtils.getCalendarForHourAndMinus(old.mTime);
				Log.i(TAG, "-----------------------------old.mDay: " + old.mDay);
				int[] days = ClockUtils.getDataPickDatas(old.mDay);
				Log.i(TAG, "------------days[0] :     " + days[0] + "------------days[1]: " + days[1] + "------------days[2]: " + days[2]);
				
				Log.i(TAG, c.toString());				
				if(status.equals("true")){
					if(!ClockUtils.isBirthHasPassed(days, old.mTime)){
						c.set(days[0], days[1], days[2]);
						tools.setAlarm(old.mCreateTime, false, 0, calendar.getTimeInMillis());
					}
					else{
						Toast.makeText(this, "生日闹钟已过期！！！", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					tools.cancel(old.mCreateTime);
				}
			}
			else{
				InvertClock old = (InvertClock)mClocks.get(position);
				InvertClock newclock = new InvertClock(old.mCreateTime, status, old.mTime, 
						old.mLabel, old.mMusic,old.mPath);
				((ClockApp)getApplication()).getData().updateInvertClock(old, newclock);
			}
			mClocks.get(position).mStatus = status;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChangeable) {
		Log.i(TAG, "---------------onCheckedChanged---------isChecked: " + isChangeable);
		mAdapter.modifyStatus(isChangeable);
		mIsChangeable = isChangeable;
	}

	@Override
	public void clickPositionDelBtn(int position) {
		Log.i(TAG, "--------------------------------position-----------" + position);
		Clock clock = mClocks.get(position);
		((ClockApp)getApplication()).getData().deleteClock(clock);
	}

}
