package com.gcl.myclock.tools;

public class InvertClock extends Clock{
//	public String mStatus;
//	public String mTime;
//	public String mLabel;
//	public String mMusic;
	
	public InvertClock(String createtime,String status,String time,String label,String music,String path){
		super(CType.CInvert);
		mCreateTime = createtime;
		mStatus = status;
		mTime = time;
		mLabel = label;
		mMusic = music;
		mPath = path;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "addingtime: " + mCreateTime +"status: " + mStatus + " time: " + mTime + " label: " + mLabel + " music : " + 
				mMusic + " type: " + mType + " path:" + mPath;
	}
}
