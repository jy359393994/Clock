package com.gcl.myclock.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

public class LocalMusicMgrByProvider {
	private final String LOG_TAG = "LocalMusicMgrByProvider";

	private Context mContext;
	private Map<String, ArrayList<String>> mContainerAllMusics = new HashMap<String, ArrayList<String>>();

	public LocalMusicMgrByProvider(Context context) {
		mContext = context;
	}

	public interface OnLoadOverObserver
	{
		public void onLoadOver();
	}
	
	public void loadAllMusics() {
		loadAudioFiles();
	}
	
	public void loadMusicsAsync(final OnLoadOverObserver observer)
	{
//		new AsyncTask<Void, Void, Void>() {
//
//			@Override
//			protected Void doInBackground(Void... params) {
//				// TODO Auto-generated method stub
//				loadAudioFiles();
//				return null;
//			}
//			
//			@Override
//			protected void onPostExecute(Void result) {
//				// TODO Auto-generated method stub
//				if (observer != null)
//				{
//					observer.onLoadOver();
//				}
//			}
//			
//		}.execute();
		
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadAudioFiles();
				if (observer != null)
				{
					observer.onLoadOver();
				}
			}
		});
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	public List<String> getAllMusics() {
		List<String> list = new ArrayList<String>();
		Set<String> setAudio = mContainerAllMusics.keySet();
		for (String s : setAudio)
		{
			list.addAll(mContainerAllMusics.get(s));
		}
		return list;
	}

	public Map<String, ArrayList<String>> getAllMusicsByFolder() {
		return mContainerAllMusics;
	}
	
	@Override
	public String toString()
	{
		int count2 = 0;
		Set<String> setAudio = mContainerAllMusics.keySet();
		for (String s : setAudio)
		{
			List<String> list = mContainerAllMusics.get(s);
			Log.i(LOG_TAG, "folder name: (" + s + ")" + "  count: " + list.size());
			count2 += list.size();
			for (String name : list)
			{
				Log.i(LOG_TAG, name);
			}
		}
		
		Log.i(LOG_TAG, "\n*** folder count: " + mContainerAllMusics.size() + "  image count: " + count2);
		
		return "";
	}
	

	private void loadAudioFiles() {
		Log.i(LOG_TAG, "loadAudioFiles");
		Cursor cursor = getCursor();
		if (cursor != null && cursor.getCount() > 0) {
			mContainerAllMusics.clear();
			int count = cursor.getCount();
			while (cursor.moveToNext()) {
				String fileName = cursor.getString(1);
				String filePath = cursor.getString(2);
				// filter the size>100kb files.
				File file = new File(filePath);
/*				if (file.length() < 100*1000)
				{
					continue;
				}*/
				String baseDir = getFilePathBaseDir(filePath);
				if (baseDir != null) {
					if (mContainerAllMusics.containsKey(baseDir) == false) {
						mContainerAllMusics.put(baseDir, new ArrayList<String>());
					}
					mContainerAllMusics.get(baseDir).add(filePath);
				}
			}
		}
	}

	private Cursor getCursor() {

		Cursor ret = null;
		String sortOrder = MediaStore.Audio.Media.TITLE_KEY;
		StringBuilder where = new StringBuilder();
		where.append(MediaStore.Audio.Media.TITLE + " != ''");

		String[] cursorCols = new String[] { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ARTIST_ID,
				MediaStore.Audio.Media.DURATION,
				MediaStore.MediaColumns.DATE_ADDED };

		where.append(" AND " + MediaStore.Audio.Media.IS_MUSIC + "=1");

		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ret = query(mContext, uri, cursorCols, where.toString(), null, sortOrder);

		return ret;
	}


	private Cursor query(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		return query(context, uri, projection, selection, selectionArgs,
				sortOrder, 0);
	}
	
	private Cursor query(Context context, Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder,
			int limit) {
		try {
			ContentResolver resolver = context.getContentResolver();
			if (resolver == null) {
				return null;
			}
			if (limit > 0) {
				uri = uri.buildUpon().appendQueryParameter("limit", "" + limit)
						.build();
			}
			return resolver.query(uri, projection, selection, selectionArgs,
					sortOrder);
		} catch (UnsupportedOperationException ex) {
			return null;
		}

	}
	
	private String getFilePathBaseDir(String filePath){
    	if(filePath == null || filePath.length() <= 0)
    		return null;
    	
		int lastPos = filePath.lastIndexOf("/");
		if(lastPos > 0){
			String dirStr =null;
			try{
			dirStr = filePath.substring(0, lastPos);
			}catch(IndexOutOfBoundsException  e){
				return null;
			}
			return dirStr;
		}	
    	return null;
    }

}
