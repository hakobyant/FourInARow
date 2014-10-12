package com.example.fourinarow;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SettingsActivity  extends Activity implements OnClickListener{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		RelativeLayout rel = new RelativeLayout(this);
		rel.setBackgroundResource(R.drawable.background);
		setContentView(rel);
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
