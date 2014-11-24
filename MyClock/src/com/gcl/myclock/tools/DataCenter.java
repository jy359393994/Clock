package com.gcl.myclock.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.gcl.myclock.tools.Clock.CType;

import android.content.Context;

public class DataCenter {
	
	private Context mContext;
	private OpenDBHelper mOpenDBHelper;
	private List<Clock> mClocks = new ArrayList<Clock>();
	
	public DataCenter(Context context){
		mContext = context;
		mOpenDBHelper = new OpenDBHelper(context);
	}
	
	public OpenDBHelper getDBHelper(){
		
		return mOpenDBHelper;
	}
	
	public void LoadAllClocks(){
		mClocks.clear();
		List<Clock> allclocks = new ArrayList<Clock>();
		List<GetUpClock> getupclocks = mOpenDBHelper.loadAllGetUpClocks();
		List<BirthClock> birthclocks = mOpenDBHelper.loadAllBirthClocks();
		List<InvertClock> invertclock = mOpenDBHelper.loadAllInvertClocks();
		allclocks.addAll(getupclocks);
		allclocks.addAll(birthclocks);
		allclocks.addAll(invertclock);
		Collections.sort(allclocks, new Comparator<Clock>(){

			@Override
			public int compare(Clock lhs, Clock rhs) {
				// TODO Auto-generated method stub
				return  lhs.compareTo(rhs);
			}
			
		});
		
		
		mClocks.addAll(allclocks);
	}
	
	public List<Clock> getAllClocks(){
		
		return mClocks;
	}
	
	public void addNewGetUpClock(GetUpClock clock){
		
		mClocks.add(clock);
		mOpenDBHelper.addNewGetUpClock(clock);
		
	}
	
	public void updateGetUpClock(GetUpClock oldclock,GetUpClock newclock){
		mOpenDBHelper.updateGetUpClock(oldclock, newclock);
		for(Clock clock:mClocks){
			if(clock.mCreateTime.equals(oldclock.mCreateTime) && clock.mType.equals(CType.CGetUp)){
				GetUpClock getupclock = (GetUpClock)clock;
				getupclock.mStatus = newclock.mStatus;
				getupclock.mTime = newclock.mTime;
				getupclock.mLabel = newclock.mLabel;
				getupclock.mRepeat = newclock.mRepeat;
				getupclock.mMusic = newclock.mMusic;
				getupclock.mVibrate = newclock.mVibrate;
				getupclock.mSleepTime = newclock.mSleepTime;
				getupclock.mPath = newclock.mPath;
			}
		}
	}
	
	public void deleteGetUpClock(GetUpClock clock){
		mOpenDBHelper.removeGetUpClock(clock);
		Iterator<Clock> it = mClocks.iterator();
//		it.next();
		while(it.hasNext()){
			Clock c = (Clock)it.next();
			if(c.mCreateTime.equals(clock.mCreateTime) && c.mType.equals(CType.CGetUp)){
				it.remove();
			}
		}
//		for(Clock c:mClocks){
//			if(c.mAddingTime.equals(clock.mAddingTime) && c instanceof GetUpClock){
//				mClocks.remove(c);
//			}
//		}
	}
	
	public void addNewBirthClock(BirthClock clock){
		
		mClocks.add(clock);
		mOpenDBHelper.addNewBirthClock(clock);
		
	}
	
	public void updateBirthClock(BirthClock oldclock,BirthClock newclock){
		mOpenDBHelper.updateBirthClock(oldclock, newclock);
		for(Clock clock:mClocks){
			if(clock.mCreateTime.equals(oldclock.mCreateTime) && clock.mType.equals(CType.CBirth)){
				BirthClock birthclock = (BirthClock)clock;
				birthclock.mStatus = newclock.mStatus;
				birthclock.mDay = newclock.mDay;
				birthclock.mTime = newclock.mTime;
				birthclock.mLabel = newclock.mLabel;
				birthclock.mMusic = newclock.mMusic;
				birthclock.mVibrate = newclock.mVibrate;
				birthclock.mPath = newclock.mPath;

			}
		}
	}

	public void deleteClock(Clock clock){
//		mOpenDBHelper.removeBirthClock(clock);
		Iterator<Clock> it = mClocks.iterator();
//		it.next();
		while(it.hasNext()){
			Clock c = (Clock)it.next();
			if(c.mCreateTime.equals(clock.mCreateTime) && c.mType.equals(CType.CGetUp)){
				it.remove();
				mOpenDBHelper.removeGetUpClock((GetUpClock)clock);
			}
			if(c.mCreateTime.equals(clock.mCreateTime) && c.mType.equals(CType.CBirth)){
				it.remove();
				mOpenDBHelper.removeBirthClock((BirthClock)clock);
			}
			if(c.mCreateTime.equals(clock.mCreateTime) && c.mType.equals(CType.CInvert)){
				it.remove();
				mOpenDBHelper.removeInvertClock((InvertClock)clock);
			}
		}
//		for(Clock c:mClocks){
//			if(c.mAddingTime.equals(clock.mAddingTime) && c instanceof BirthClock){
//				mClocks.remove(c);
//			}
//		}
	}
	
	public void addNewInvertClock(InvertClock clock){
		
		mClocks.add(clock);
		mOpenDBHelper.addNewInvertClock(clock);
		
	}
	
	public void updateInvertClock(InvertClock oldclock,InvertClock newclock){
		
		mOpenDBHelper.updateInvertClock(oldclock, newclock);
		for(Clock clock:mClocks){
			if(clock.mCreateTime.equals(oldclock.mCreateTime) && clock.mType.equals(CType.CInvert)){
				InvertClock invertclock = (InvertClock)clock;
				invertclock.mStatus = newclock.mStatus;
				invertclock.mTime = newclock.mTime;
				invertclock.mLabel = newclock.mLabel;
				invertclock.mMusic = newclock.mMusic;
				invertclock.mPath = newclock.mPath;

			}
		}
	}
	
	public void deleteInvertClock(InvertClock clock){
		mOpenDBHelper.removeInvertClock(clock);
		Iterator<Clock> it = mClocks.iterator();
//		it.next();
		while(it.hasNext()){
			Clock c = (Clock)it.next();
			if(c.mCreateTime.equals(clock.mCreateTime) && c.mType.equals(CType.CInvert)){
				it.remove();
			}
		}
//		for(Clock c:mClocks){
//			if(c.mAddingTime.equals(clock.mAddingTime) && c instanceof InvertClock){
//				mClocks.remove(c);
//			}
//		}
	}
	
	public Clock getClockFromCt(String ct){
		Clock c = null;
		for(Clock cl:mClocks){
			if(cl.mCreateTime.equals(ct)){
				c = cl;
				break;
			}
		}
		
		return c;
	}
}
