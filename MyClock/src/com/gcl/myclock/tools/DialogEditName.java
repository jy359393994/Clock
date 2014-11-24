package com.gcl.myclock.tools;

import java.util.ArrayList;
import java.util.List;

import com.gcl.myclock.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogEditName extends Dialog{
	private View.OnClickListener mOnClickListenerYes;
	private View.OnClickListener mOnClickListenerNo;
	private String mTitle;
	private Activity mActivity;
	private boolean mDisableNoButton;
	private EditText mEditText;
	
	public DialogEditName(Activity context, int theme) {
	    super(context, theme);
	    mActivity = context;
	}

	public DialogEditName(Activity context) {
	    super(context);
	    mActivity = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.v2_d_edit_name);
	    // 鐐瑰嚮鍏朵粬鍖哄煙涓嶆秷澶�
	    setCanceledOnTouchOutside(false);
	    // 鎸夎繑鍥為敭涓嶆秷澶�
	    setCancelable(false);
	    mEditText = (EditText)findViewById(R.id.edittext);
	}
	
	public void setParameters(View.OnClickListener yesListener, View.OnClickListener noListener, String titleStr){
		mOnClickListenerYes = yesListener;
		mOnClickListenerNo = noListener;
		mTitle = titleStr;
		
	}
	
	public String getInputString()
	{
		String s = mEditText.getText().toString();
		return s;
	}

	public void disableNoButton(){
		mDisableNoButton = true;
	}
	
	public void showDialog(){
		show();
		
		WindowManager windowManager = mActivity.getWindowManager();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		Display display = windowManager.getDefaultDisplay();
		lp.width = (int)(display.getWidth())- dip2px(mActivity, 20);
		getWindow().setAttributes(lp);
		
		View rootView = getWindow().getDecorView();
		TextView yesView = (TextView)rootView.findViewById(R.id.yes);
		yesView.setOnClickListener(mOnClickListenerYes);

		TextView noView = (TextView)rootView.findViewById(R.id.no);
		if(mDisableNoButton == true){
			noView.setVisibility(View.GONE);
		}else
			noView.setOnClickListener(mOnClickListenerNo);
		
		TextView titleView = (TextView)rootView.findViewById(R.id.title);
		if(mTitle != null)
			titleView.setText(mTitle);
		
	}
	
	private int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	}

}
