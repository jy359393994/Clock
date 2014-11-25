package com.gcl.myclock.tools;

import android.widget.AnalogClock;

public class Clock implements Comparable<Object>{
	
	public String mStatus;
	public String mTime;
	public String mLabel;
	public String mMusic;
	public String mCreateTime;
	public String mPath;
	public CType mType;
	
	
	public enum CType{
		CGetUp,
		CBirth,
		CInvert
	}
	
	public Clock(CType type){
		mType = type;
	}

	@Override
	public int compareTo(Object another) {

		if(this == another)
			return 0;
		else if(another != null &&another instanceof Clock){
			Clock c = (Clock)another;
			if(Long.parseLong(mCreateTime) <= Long.parseLong(c.mCreateTime)){
				return -1;
			}
			else{
				return 1;
			}
		}
		else{
			return -1;
		}
	}

}
