package com.example.fourinarow;

import java.io.File;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HighScoresActivity extends Activity{


		RelativeLayout SettingsLayout;
		
		Button backButton;
		TextView Rules;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
			//File myDir = getFilesDir();
			//Log.i("my Dir", myDir + "/scores/");
			
			SettingsLayout = new RelativeLayout(this);
			SettingsLayout.setBackgroundResource(R.drawable.background);// Sets the background of the UI
			
			
			this.setContentView(SettingsLayout);
			
		}
		
	}



