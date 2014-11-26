package com.gcl.myclock.tools;

import java.util.ArrayList;
import java.util.List;

import com.gcl.myclock.R;
import com.gcl.myclock.tools.Clock.CType;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ClockAdapter extends BaseAdapter implements OnClickListener{
	
	private static final String LOG = "ClockAdapter"; 
	LayoutInflater mInflater;
	Context mContext;
	PositonViewListener mListener;
	List<Clock> mClocks = new ArrayList<Clock>();
	private boolean mIsDelStatus = false;
	public interface PositonViewListener{
		public void clickPositionTB(int position);
		public void clickPositionDelBtn(int position);
	}
	
	public void setClickListener(PositonViewListener listener){
		mListener = listener;
	}
	
	public ClockAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(context);
		
	}
	
	public void addAllClocks(List<Clock> clocks){
		mClocks.clear();
		mClocks.addAll(clocks);
		notifyDataSetChanged();
	}
	
	public void modifyStatus(boolean isDelStatus){
		mIsDelStatus = isDelStatus;
		notifyDataSetChanged();
	}
	
	private void delItem(int position){
		mClocks.remove(position);
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {

		return mClocks.size();
	}

	@Override
	public Object getItem(int position) {

		return mClocks.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		if(position < 0 || position > mClocks.size()){
			return null;
		}
		ViewHolder holder = new ViewHolder();
		if(view == null){
			view = mInflater.inflate(R.layout.clock_list, null);
			holder.mImg = (ImageView) view.findViewById(R.id.main_clock_list_img);
			holder.mText = (TextView) view.findViewById(R.id.main_clock_list_text);
			holder.mToggleBtn = (ToggleButton)view.findViewById(R.id.main_clock_list_togglebtn);
			holder.mDelImg = (ImageView)view.findViewById(R.id.main_clock_list_del_btn);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder)view.getTag();
		}
		
		if(mClocks.get(position).mType.equals(CType.CGetUp)){
			holder.mImg.setBackgroundResource(R.drawable.main_getup_clock_img);
			holder.mText.setText(mClocks.get(position).mTime);
		}
		else if(mClocks.get(position).mType.equals(CType.CBirth)){
			holder.mImg.setBackgroundResource(R.drawable.main_birth_clock_img);
			holder.mText.setText(((BirthClock)mClocks.get(position)).mDay);
		}
		else{
			holder.mImg.setBackgroundResource(R.drawable.main_invert_clock_img);
			holder.mText.setText(((InvertClock)mClocks.get(position)).mTime);
			InvertClock clock = (InvertClock)mClocks.get(position);
//			if(clock.mStatus.equals("false")){
//				holder.mToggleBtn.setVisibility(View.GONE);
//			}
//			else{
//				
//			}
		}
		if(mClocks.get(position).mStatus.equals("true")){
			Log.i("getview", "------------------mStatus: " + mClocks.get(position).mStatus);
			holder.mToggleBtn.setChecked(true);
		}
		else{
			Log.i("getview", "------------------mStatus: " + mClocks.get(position).mStatus);
			holder.mToggleBtn.setChecked(false);
		}
		if(mIsDelStatus){
			holder.mDelImg.setVisibility(View.VISIBLE);
			holder.mToggleBtn.setVisibility(View.GONE);
		}
		else{
			holder.mDelImg.setVisibility(View.GONE);
			holder.mToggleBtn.setVisibility(View.VISIBLE);
		}
			
		holder.mToggleBtn.setTag(position);
		holder.mToggleBtn.setOnClickListener(this);
		holder.mDelImg.setTag(position);
		holder.mDelImg.setOnClickListener(this);
		return view;
	}
	
	static  class ViewHolder{
		
		TextView mText;
		ImageView mImg;
		ToggleButton mToggleBtn;
		ImageView mDelImg;
		
	}

	@Override
	public void onClick(View v) {
		Integer pos = (Integer)v.getTag();
		switch(v.getId()){		
		case R.id.main_clock_list_togglebtn:
			if(mListener != null){
				mListener.clickPositionTB(pos.intValue());
			}
			break;
		case R.id.main_clock_list_del_btn:
			Log.i(LOG, "-----------pos: " + pos.intValue() + "-----------main_clock_list_del_btn");
			if(mListener != null){
				mListener.clickPositionDelBtn(pos.intValue());	
				delItem(pos.intValue());
			}
		}
	}

}
