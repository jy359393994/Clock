package com.gcl.myclock;

import java.util.ArrayList;
import java.util.List;

import com.gcl.myclock.tools.LocalMusicMgrByProvider;
import com.gcl.myclock.tools.LocalMusicMgrByProvider.OnLoadOverObserver;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivitySelectMusic extends Activity implements OnItemClickListener{
	
	private ListView mListView;
	private List<String> mAllMusic = new ArrayList<String>();
	private LocalMusicMgrByProvider mMusicMgr;
	private MusicAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_music);
		mListView = (ListView)findViewById(R.id.select_music_list);
		mAdapter = new MusicAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mMusicMgr = new LocalMusicMgrByProvider(this);
		mMusicMgr.loadMusicsAsync(new OnLoadOverObserver() {
			
			@Override
			public void onLoadOver() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mAllMusic = mMusicMgr.getAllMusics();
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		});
	}
	
	private class MusicAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAllMusic.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mAllMusic.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			if(view == null){
				view = new TextView(ActivitySelectMusic.this);				
			}
			int start = mAllMusic.get(arg0).lastIndexOf("/") + 1;
			((TextView)view).setText(mAllMusic.get(arg0).substring(start, mAllMusic.get(arg0).length()));
			((TextView)view).setPadding(15, 20, 10, 20);
			
			return view;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		if(position < 0 || position >= mAllMusic.size()){
			return;
		}
		Intent data = new Intent();
		data.putExtra("name", (mAllMusic.get(position)).substring(mAllMusic.get(position).lastIndexOf("/") + 1, mAllMusic.get(position).length()));
		data.putExtra("path", mAllMusic.get(position));
		setResult(300, data);
		finish();
	}
	

}
