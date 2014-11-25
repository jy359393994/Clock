package com.gcl.myclock.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenDBHelper extends SQLiteOpenHelper{
	
	private static final String LOG = "OpenHelper";
	private static final String NAME = "database_db";
	public static final int DATABASE_VERSION = 1;
	
	public static String DATABASE_TABLE_GETUP = "get_up";
	public static String DATABASE_TABLE_GETUP_ID = "id";
	public static String DATABASE_TABLE_GETUP_CREATE_TIME = "addingtime";
	public static String DATABASE_TABLE_GETUP_STATUS = "status";
	public static String DATABASE_TABLE_GETUP_TIME = "time";
	public static String DATABASE_TABLE_GETUP_LABLE = "label";
	public static String DATABASE_TABLE_GETUP_REPEAT = "repeat";
	public static String DATABASE_TABLE_GETUP_MUSIC = "music";
	public static String DATABASE_TABLE_GETUP_VIBRATE = "vibrate";
	public static String DATABASE_TABLE_GETUP_SLEEPTIME = "sleeptime";
	public static String DATABASE_TABLE_GETUP_MUSIC_PATH = "path";
	
	public static String DATABASE_TABLE_BIRTH = "birth";
	public static String DATABASE_TABLE_BIRTH_ID = "id";
	public static String DATABASE_TABLE_BIRTH_CREATE_TIME = "addingtime";
	public static String DATABASE_TABLE_BIRTH_STATUS = "status";
	public static String DATABASE_TABLE_BIRTH_DAY = "day";
	public static String DATABASE_TABLE_BIRTH_TIME = "time";
	public static String DATABASE_TABLE_BIRTH_LABLE = "label";
	public static String DATABASE_TABLE_BIRTH_MUSIC = "music";
	public static String DATABASE_TABLE_BIRTH_VIBRATE = "vibrate";
	public static String DATABASE_TABLE_BIRTH_MUSIC_PATH = "path";
	
	
	public static String DATABASE_TABLE_INVERT = "invert";
	public static String DATABASE_TABLE_INVERT_ID = "id";
	public static String DATABASE_TABLE_INVERT_CREATE_TIME = "addingtime";
	public static String DATABASE_TABLE_INVERT_STATUS = "status";
	public static String DATABASE_TABLE_INVERT_TIME = "time";
	public static String DATABASE_TABLE_INVERT_LABLE = "label";
	public static String DATABASE_TABLE_INVERT_MUSIC = "music";
	public static String DATABASE_TABLE_INVERT_MUSIC_PATH = "path";

	
	

	public OpenDBHelper(Context context) {
		super(context, NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i(LOG, "-------------onCreate---------------");
		createGetUpTable(db);
		createBirthTable(db);
		createInvertTable(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		
	}
	
	private void createGetUpTable(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_GETUP + " ("
                + DATABASE_TABLE_GETUP_ID + " INTEGER PRIMARY KEY autoincrement,"
                + DATABASE_TABLE_GETUP_CREATE_TIME +  " TEXT,"
                + DATABASE_TABLE_GETUP_STATUS +  " TEXT,"
                + DATABASE_TABLE_GETUP_TIME + " TEXT,"
                + DATABASE_TABLE_GETUP_LABLE + " TEXT,"
                + DATABASE_TABLE_GETUP_REPEAT + " TEXT,"
                + DATABASE_TABLE_GETUP_MUSIC + " TEXT,"
                + DATABASE_TABLE_GETUP_VIBRATE + " TEXT," 
                + DATABASE_TABLE_GETUP_SLEEPTIME + " TEXT," 
                + DATABASE_TABLE_GETUP_MUSIC_PATH + " TEXT"
                + ");");
	}
	
	public void addNewGetUpClock(GetUpClock clock){
		
		if(clock == null){
			Log.i(LOG, "--------- getupclock is null");
			return;
		}
		SQLiteDatabase writeDB = null;
		writeDB = getWritableDatabase();
		writeDB.execSQL("insert into " + DATABASE_TABLE_GETUP + " (addingtime,status,time,label,repeat,music,vibrate,sleeptime,path) values(?,?,?,?,?,?,?,?,?)",
				new Object[]{clock.mCreateTime,clock.mStatus,clock.mTime,clock.mLabel,clock.mRepeat,clock.mMusic,clock.mVibrate,clock.mSleepTime,clock.mPath});
		Log.i(LOG, "--------- addNewGetUpClock ---------------");
					
	}
	//更改某个闹钟的信息，更改后的组成一个新的闹钟，替换数据库中之前的闹钟(暂时不考虑重复的闹钟，及所有信息都一样)
	public void updateGetUpClock(GetUpClock oldclock,GetUpClock newclock){
		Log.i(LOG, "----------updateGetUpClock------------");
		if(oldclock == null || newclock == null){
			Log.i(LOG, "----------getup oldclock or newclock is null");
		}
		
		SQLiteDatabase readDB = null;
		SQLiteDatabase writeDB = null;
		readDB = getReadableDatabase();
		writeDB = getWritableDatabase();
		String[] selectArgs = {oldclock.mCreateTime};
		Cursor cursor = readDB.query(DATABASE_TABLE_GETUP, null, (DATABASE_TABLE_GETUP_CREATE_TIME + "=?"), selectArgs, null, null, null);
		if(cursor != null){
			String s = "update " + DATABASE_TABLE_GETUP + " set  status='" + newclock.mStatus +"',time='" + newclock.mTime + "',label='" 
					+ newclock.mLabel + "',repeat='" + newclock.mRepeat + "',music='" + newclock.mMusic + "',vibrate='" 
					+ newclock.mVibrate + "',sleeptime='" + newclock.mSleepTime + "',path='" + newclock.mPath + 
					"' where addingtime ='" + oldclock.mCreateTime + "'";
			Log.i(LOG, s);
			writeDB.execSQL(s);
		}		
	}
	
	public List<GetUpClock> loadAllGetUpClocks(){
		Log.i(LOG, "--------- loadAllGetUpClocks------");
		List<GetUpClock>list = new ArrayList<GetUpClock>();
		SQLiteDatabase readDB = null;
		readDB = getReadableDatabase();
		Cursor cursor = readDB.query(DATABASE_TABLE_GETUP, null, null, null, null, null, null);
		if(cursor == null || cursor.getCount() <= 0){
			if(cursor != null){
				cursor.close();
			}
			return list;
		}
		Log.i(LOG, "--------- loadAllGetUpClocks found size = ------" + cursor.getCount());
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			String addingtime = cursor.getString(1);
			String status = cursor.getString(2);
			String time = cursor.getString(3);
			String label = cursor.getString(4);
			String repeat = cursor.getString(5);
			String music = cursor.getString(6);
			String vibrate = cursor.getString(7);
			String sleeptime = cursor.getString(8);
			String path = cursor.getString(9);
			GetUpClock clock = new GetUpClock(addingtime, status, time, label, repeat, music, vibrate, sleeptime,path);
			list.add(clock);
			cursor.moveToNext();
			Log.i(LOG, clock.toString());
		}
		cursor.close();
		return list;
	}
	
	public void removeGetUpClock(GetUpClock clock){
		if(clock == null){
			Log.i(LOG, "--------- getupclock is null");
			return;
		}
		SQLiteDatabase writeDB = null;
		writeDB = getWritableDatabase();
		String[] selectArgs = {clock.mCreateTime};
		int delNum = writeDB.delete(DATABASE_TABLE_GETUP, (DATABASE_TABLE_GETUP_CREATE_TIME +  "=?"), selectArgs);
		if(delNum >= 1){
			Log.i(LOG, "delete getupClock : " + clock);
		}
	}
	
	private void createBirthTable(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_BIRTH + " ("
                + DATABASE_TABLE_BIRTH_ID + " INTEGER PRIMARY KEY autoincrement,"
                + DATABASE_TABLE_BIRTH_CREATE_TIME + " TEXT,"
                + DATABASE_TABLE_BIRTH_STATUS +  " TEXT,"
                + DATABASE_TABLE_BIRTH_DAY + " TEXT,"
                + DATABASE_TABLE_BIRTH_TIME + " TEXT,"
                + DATABASE_TABLE_BIRTH_LABLE + " TEXT,"
                + DATABASE_TABLE_BIRTH_MUSIC + " TEXT,"
                + DATABASE_TABLE_BIRTH_VIBRATE + " TEXT," 
                + DATABASE_TABLE_BIRTH_MUSIC_PATH + " TEXT"
                + ");");
	}
	
	public void addNewBirthClock(BirthClock clock){
		if(clock == null){
			Log.i(LOG, "--------- birthclock is null");
			return;			
		}
		SQLiteDatabase writeDB = null;
		writeDB = getWritableDatabase();
		writeDB.execSQL("insert into " + DATABASE_TABLE_BIRTH + " (addingtime,status,day,time,label,music,vibrate,path) values(?,?,?,?,?,?,?,?)",
				new Object[]{clock.mCreateTime,clock.mStatus,clock.mDay,clock.mTime,clock.mLabel,clock.mMusic,clock.mVibrate,clock.mPath});
		Log.i(LOG, "--------- addNewBirthClock ---------------");
	}
	
	public void updateBirthClock(BirthClock oldclock,BirthClock newclock){
		Log.i(LOG, "----------updateBirthClock----------------");
		if(oldclock == null || newclock == null){
			Log.i(LOG, "----------getup oldclock or newclock is null");
		}
		
		SQLiteDatabase readDB = null;
		SQLiteDatabase writeDB = null;
		readDB = getReadableDatabase();
		writeDB = getWritableDatabase();
		String[] selectArgs = {oldclock.mCreateTime};
		Cursor cursor = readDB.query(DATABASE_TABLE_BIRTH, null, (DATABASE_TABLE_BIRTH_CREATE_TIME +"=?"), selectArgs, null, null, null);
		if(cursor != null){
			String s = "update " + DATABASE_TABLE_BIRTH + " set status='" + newclock.mStatus +"',day='" + newclock.mDay + "',time='" 
					+ newclock.mTime + "',label='" + newclock.mLabel + "',music='" + newclock.mMusic + "',vibrate='" 
					+ newclock.mVibrate + "',path='" + newclock.mPath +
					"' where addingtime='" + oldclock.mCreateTime + "'";
			Log.i(LOG, s);
			writeDB.execSQL(s);
		}		
	}
	
	public List<BirthClock> loadAllBirthClocks(){
		Log.i(LOG, "--------- loadAllGetUpClocks------");
		List<BirthClock>list = new ArrayList<BirthClock>();
		SQLiteDatabase readDB = null;
		readDB = getReadableDatabase();
		Cursor cursor = readDB.query(DATABASE_TABLE_BIRTH, null, null, null, null, null, null);
		if(cursor == null || cursor.getCount() <= 0){
			if(cursor != null){
				cursor.close();
			}
			return list;
		}
		Log.i(LOG, "--------- loadAllBirthClocks found size = ------" + cursor.getCount());
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			String addingtime = cursor.getString(1);
			String status = cursor.getString(2);
			String time = cursor.getString(3);
			String day = cursor.getString(4);
			String label = cursor.getString(5);
			String music = cursor.getString(6);
			String vibrate = cursor.getString(7);
			String path = cursor.getString(8);
			BirthClock clock = new BirthClock(addingtime, status, day, time, label, music, vibrate,path);
			list.add(clock);
			cursor.moveToNext();
			Log.i(LOG, clock.toString());
		}
		cursor.close();
		return list;
	}
	
	public void removeBirthClock(BirthClock clock){
		if(clock == null){
			Log.i(LOG, "--------- birthclock is null");
			return;
		}
		SQLiteDatabase writeDB = null;
		writeDB = getWritableDatabase();
		String[] selectArgs = {clock.mCreateTime};
		int delNum = writeDB.delete(DATABASE_TABLE_BIRTH, (DATABASE_TABLE_BIRTH_CREATE_TIME + "=?"), selectArgs);
		if(delNum >= 1){
			Log.i(LOG, "delete birthClock : " + clock);
		}
	}
	
	private void createInvertTable(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_INVERT + " ("
                + DATABASE_TABLE_INVERT_ID + " INTEGER PRIMARY KEY autoincrement,"
                + DATABASE_TABLE_INVERT_CREATE_TIME +  " TEXT,"
                + DATABASE_TABLE_INVERT_STATUS +  " TEXT,"
                + DATABASE_TABLE_INVERT_TIME + " TEXT,"
                + DATABASE_TABLE_INVERT_LABLE + " TEXT,"
                + DATABASE_TABLE_INVERT_MUSIC + " TEXT," 
                + DATABASE_TABLE_INVERT_MUSIC_PATH + " TEXT"
                + ");");
	}
	
	public void addNewInvertClock(InvertClock clock){
		if(clock == null){
			Log.i(LOG, "--------- invertclock is null");
			return;
		}
		SQLiteDatabase writeDB = null;
		writeDB = getWritableDatabase();
		writeDB.execSQL("insert into " + DATABASE_TABLE_INVERT + " (addingtime,status,time,label,music,path) values(?,?,?,?,?,?)",
				new Object[]{clock.mCreateTime,clock.mStatus,clock.mTime,clock.mLabel,clock.mMusic,clock.mPath});
		Log.i(LOG, "--------- addNewBirthClock ---------------");
	}
	
	public void updateInvertClock(InvertClock oldclock,InvertClock newclock){
		Log.i(LOG, "----------updateInvertClock------------");
		if(oldclock == null || newclock == null){
			Log.i(LOG, "----------getup oldclock or newclock is null");
		}
		
		SQLiteDatabase readDB = null;
		SQLiteDatabase writeDB = null;
		readDB = getReadableDatabase();
		writeDB = getWritableDatabase();
		String[] selectArgs = {oldclock.mCreateTime};
		Cursor cursor = readDB.query(DATABASE_TABLE_INVERT, null, (DATABASE_TABLE_INVERT_CREATE_TIME + "=?"), selectArgs, null, null, null);
		if(cursor != null){
			String s = "update " + DATABASE_TABLE_INVERT + " set  status='" + newclock.mStatus + "',time='" 
					+ newclock.mTime + "',label='" + newclock.mLabel + "',music='" + newclock.mMusic + "',path='" + newclock.mPath 
					+ "' where addingtime='" + oldclock.mCreateTime + "'";
			
			Log.i(LOG, s);
			writeDB.execSQL(s);
		}		
	}
	
	
	public List<InvertClock> loadAllInvertClocks(){
		Log.i(LOG, "--------- loadAllInvertClocks------");
		List<InvertClock>list = new ArrayList<InvertClock>();
		SQLiteDatabase readDB = null;
		readDB = getReadableDatabase();
		Cursor cursor = readDB.query(DATABASE_TABLE_INVERT, null, null, null, null, null, null);
		if(cursor == null || cursor.getCount() <= 0){
			if(cursor != null){
				cursor.close();
			}
			return list;
		}
		Log.i(LOG, "--------- loadAllInvertClocks found size = ------" + cursor.getCount());
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			String addingtime = cursor.getString(1);
			String status = cursor.getString(2);
			String time = cursor.getString(3);
			String label = cursor.getString(4);
			String music = cursor.getString(5);
			String path = cursor.getString(6);
			InvertClock clock = new InvertClock(addingtime, status, time, label, music,path);
			list.add(clock);
			cursor.moveToNext();
			Log.i(LOG, clock.toString());
		}
		cursor.close();
		return list;
	}
	
	
	public void removeInvertClock(InvertClock clock){
		if(clock == null){
			Log.i(LOG, "--------- invertclock is null");
			return;
		}
		SQLiteDatabase writeDB = null;
		writeDB = getWritableDatabase();
		String[] selectArgs = {clock.mCreateTime};
		int delNum = writeDB.delete(DATABASE_TABLE_INVERT, (DATABASE_TABLE_INVERT_CREATE_TIME +  "=?"), selectArgs);
		if(delNum >= 1){
			Log.i(LOG, "delete invertClock : " + clock);
		}
	}

}
