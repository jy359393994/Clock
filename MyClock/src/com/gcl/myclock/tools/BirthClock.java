package com.gcl.myclock.tools;

public class BirthClock extends Clock{
//	public String mStatus;
//	public String mTime;
	public String mDay;
//	public String mMusic;
	public String mVibrate;
//	public String mLabel;
	
	public BirthClock(String createtime,String status,String time,String day,String label,String music,String vibrate,String path){
		super(CType.CBirth);
		mCreateTime = createtime;
		mStatus = status;
		mTime = time;
		mMusic = music;
		mVibrate = vibrate;
		mDay = day;
		mLabel = label;
		mPath = path;
				
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "addingtime: " + mCreateTime + "status: " + mStatus + " day: " + mDay + " time: " + mTime + " label: " + mLabel + " music : " + mMusic +
				" vibrate: " + mVibrate + " type: " + mType + " path:" + mPath;
	}
	

}
