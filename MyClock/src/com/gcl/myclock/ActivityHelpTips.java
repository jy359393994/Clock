package com.gcl.myclock;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivityHelpTips extends Activity implements OnPageChangeListener{
	
	private final String TAG = "ActivityHelpTips";
	private ViewPager mPager;
	private ViewPagerAdapter mAdapter;
	private List<View> mViews;
	private ImageView mDots[];
	private int mCurrentIndex = 0;
	
	private boolean mViewMeasureFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "---------- onCreate-------");
		setContentView(R.layout.help_tips);
		initViews();
		initDots();
	}
	
	
	private void initViews(){
		LayoutInflater inflater = LayoutInflater.from(this);
		mViews = new ArrayList<View>();
		mViews.add(inflater.inflate(R.layout.tip_one, null));
		mViews.add(inflater.inflate(R.layout.tip_two, null));
		mViews.add(inflater.inflate(R.layout.tip_three, null));
		
		mPager = (ViewPager)findViewById(R.id.viewpager);
		mAdapter = new ViewPagerAdapter(mViews, this);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);
		
		View v = mViews.get(0).findViewById(R.id.tip);
    	ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            public boolean onPreDraw()
            {
                if (mViewMeasureFlag == false)
                {
            		animate(mViews.get(0).findViewById(R.id.tip));
                	mViewMeasureFlag = true;
                }
                return true;
            }
        });

	}
	
	
	private void initDots(){
		LinearLayout layout_dots = (LinearLayout)findViewById(R.id.layout_dots);
		mDots = new ImageView[mViews.size()];
		for(int i = 0; i < mViews.size(); i++){
			mDots[i] = (ImageView)layout_dots.getChildAt(i);
			mDots[i].setSelected(false);
		}
		
		mDots[mCurrentIndex].setSelected(true);		
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "--------------------------------");
		setCurrentDot(arg0);

		animate(mViews.get(arg0).findViewById(R.id.tip));

	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	private void animate(View target)
	{
        Animation a = new TranslateAnimation(0.0f,
                0.0f, -target.getY() + 150, 0.0f);
        a.setDuration(1000);
        a.setStartOffset(300);
        a.setRepeatCount(0);
        a.setFillAfter(true);
        a.setInterpolator(AnimationUtils.loadInterpolator(this,
                android.R.anim.bounce_interpolator));
        
        target.startAnimation(a);
	}
	
	private void setCurrentDot(int position){
		if(position < 0 || position > mViews.size() || position == mCurrentIndex){
			return;
		}
		mDots[mCurrentIndex].setSelected(false);
		mCurrentIndex = position;
		mDots[mCurrentIndex].setSelected(false);
	}
}
