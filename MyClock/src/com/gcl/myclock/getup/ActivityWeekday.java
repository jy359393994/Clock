package com.gcl.myclock.getup;

import com.gcl.myclock.R;
import com.gcl.myclock.tools.Config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ActivityWeekday extends Activity implements OnClickListener{
	
	
	private static final String LOG = "ActivityWeekday";
	private RelativeLayout mMonLayout;
	private RelativeLayout mTuesLayout;
	private RelativeLayout mWedLayout;
	private RelativeLayout mThurLayout;
	private RelativeLayout mFriLayout;
	private RelativeLayout mSatuLayout;
	private RelativeLayout mSunLayout;
	
	private ImageView mMonImg;
	private ImageView mTuesImg;
	private ImageView mWedImg;
	private ImageView mThurImg;
	private ImageView mFriImg;
	private ImageView mSatuImg;
	private ImageView mSunImg;
	
	private int[] mWeekdayStatus = {0,0,0,0,0,0,0};
	private String[] mStatus = {"false","false","false","false","false","false","false"};
	private LinearLayout mBackLayout;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weekday_set);

		mBackLayout = (LinearLayout)findViewById(R.id.layout_weekday_back);
		mBackLayout.setOnClickListener(this);
		mMonLayout = (RelativeLayout)findViewById(R.id.monday);
		mTuesLayout = (RelativeLayout)findViewById(R.id.tuesday);
		mWedLayout = (RelativeLayout)findViewById(R.id.wednesday);
		mThurLayout = (RelativeLayout)findViewById(R.id.thursday);
		mFriLayout = (RelativeLayout)findViewById(R.id.friday);
		mSatuLayout = (RelativeLayout)findViewById(R.id.saturday);
		mSunLayout = (RelativeLayout)findViewById(R.id.sunday);
		
		mMonLayout.setOnClickListener(this);
		mTuesLayout.setOnClickListener(this);
		mWedLayout.setOnClickListener(this);
		mThurLayout.setOnClickListener(this);
		mFriLayout.setOnClickListener(this);
		mSatuLayout.setOnClickListener(this);
		mSunLayout.setOnClickListener(this);
		
		mMonImg = (ImageView)findViewById(R.id.monday_img);
		mTuesImg = (ImageView)findViewById(R.id.tuesday_img);
		mWedImg = (ImageView)findViewById(R.id.wednesday_img);
		mThurImg = (ImageView)findViewById(R.id.thursday_img);
		mFriImg = (ImageView)findViewById(R.id.friday_img);
		mSatuImg = (ImageView)findViewById(R.id.saturday_img);
		mSunImg = (ImageView)findViewById(R.id.sunday_img);
		Intent it = getIntent();
		mWeekdayStatus = it.getIntArrayExtra("status");
		for(int i=0;i<7;i++){
			if(mWeekdayStatus[i] == 1){
				mStatus[i] = "true";
			}
			else{
				mStatus[i] = "false";
			}
		}
				
		setStatus(mMonImg,mStatus[0]);
		setStatus(mTuesImg,mStatus[1]);
		setStatus(mWedImg,mStatus[2]);
		setStatus(mThurImg,mStatus[3]);
		setStatus(mFriImg,mStatus[4]);
		setStatus(mSatuImg,mStatus[5]);
		setStatus(mSunImg,mStatus[6]);
		Log.i(LOG, "mMonImgStatus : " + mStatus[0] + " mTuesImgStatus: " + mStatus[1] + " mWedImgStatus: " + mStatus[2]
				+ " mThurImgStatus : " + mStatus[3] + " mFriImgStatus: " + mStatus[4]+ " mSatuImgStatus : " + mStatus[5]
				+ " mSunImgStatus : " + mStatus[6]
				);
				
	}
	
	private void setStatus(ImageView img,String status){
		if(status.equals("true")){
			img.setSelected(true);
		}
		else{
			img.setSelected(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.layout_weekday_back:
			Intent data = new Intent();
			data.putExtra("status", mWeekdayStatus);
			setResult(202, data);
			finish();
			break;
		case R.id.monday:
			if(mMonImg.isSelected()){
				mMonImg.setSelected(false);
				mWeekdayStatus[0] = 0;
			}
			else{
				mMonImg.setSelected(true);
				mWeekdayStatus[0] = 1;
			}
			break;
		case R.id.tuesday:
			if(mTuesImg.isSelected()){
				mTuesImg.setSelected(false);
				mWeekdayStatus[1] = 0;
			}
			else{
				mTuesImg.setSelected(true);
				mWeekdayStatus[1] = 1;
			}
			break;
		case R.id.wednesday:
			if(mWedImg.isSelected()){
				mWedImg.setSelected(false);
				mWeekdayStatus[2] = 0;
			}
			else{
				mWedImg.setSelected(true);
				mWeekdayStatus[2] = 1;
			}
			break;
		case R.id.thursday:
			if(mThurImg.isSelected()){
				mThurImg.setSelected(false);
				mWeekdayStatus[3] = 0;
			}
			else{
				mThurImg.setSelected(true);
				mWeekdayStatus[3] = 1;
			}
			break;
		case R.id.friday:
			if(mFriImg.isSelected()){
				mFriImg.setSelected(false);
				mWeekdayStatus[4] = 0;
			}
			else{
				mFriImg.setSelected(true);
				mWeekdayStatus[4] = 1;
			}
			break;
		case R.id.saturday:
			if(mSatuImg.isSelected()){
				mSatuImg.setSelected(false);
				mWeekdayStatus[5] = 0;
			}
			else{
				mSatuImg.setSelected(true);
				mWeekdayStatus[5] = 1;
			}
			break;
		case R.id.sunday:
			if(mSunImg.isSelected()){
				mSunImg.setSelected(false);
				mWeekdayStatus[6] = 0;
			}
			else{
				mSunImg.setSelected(true);
				mWeekdayStatus[6] = 1;
			}
			break;
		}
	}

	
	@Override
	 public boolean dispatchKeyEvent(KeyEvent event) { 
		
		if ((event.getAction() == KeyEvent.ACTION_DOWN &&  event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
			Intent data = new Intent();
			data.putExtra("status", mWeekdayStatus);
			setResult(202, data);
			finish();
		}
		return super.dispatchKeyEvent(event);  
	 }
	
	

}
