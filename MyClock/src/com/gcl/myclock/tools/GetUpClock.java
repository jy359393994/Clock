package com.gcl.myclock.tools;

public class GetUpClock extends Clock{

	public String mRepeat;
	public String mVibrate;
	public String mSleepTime;
	
	public GetUpClock(String createtime,String status,String time,String label,String repeat,String music,String vibrate,String sleeptime,String path){
		super(CType.CGetUp);
		mCreateTime = createtime;
		mStatus = status;
		mTime = time;
		mLabel = label;
		mRepeat = repeat;
		mMusic = music;
		mVibrate = vibrate;
		mSleepTime = sleeptime;
		mPath = path;
		
	}
	
	@Override
	public String toString() {

		return "addingtime: " + mCreateTime + "status: " + mStatus + " time: " + mTime + " label: " + mLabel + " repeat: " + mRepeat + " music : " + 
				mMusic + " vibrate: " + mVibrate + " sleeptime: " + mSleepTime + " type: " + mType + " path:" + mPath;
	}

}
