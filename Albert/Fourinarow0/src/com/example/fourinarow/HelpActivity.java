package com.example.fourinarow;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class HelpActivity extends Activity implements OnClickListener{


		RelativeLayout HelpLayout;
		LinearLayout BackButtonLayout;
		
		Button backButton;
		TextView Rules;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
			HelpLayout = new RelativeLayout(this);
			HelpLayout.setBackgroundResource(R.drawable.background);// Sets the background of the UI
			
			backButton = new Button(this);
			backButton.setText("Back To Main Menu");
			backButton.setOnClickListener(this);
			
			BackButtonLayout = new LinearLayout(this);
			BackButtonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			BackButtonLayout.addView(backButton);
			
			Rules = new TextView(this);
			Rules.setTextSize(30);
			Rules.setText("The goal of the game is to connect four of your tokens in a line. All directions (vertical, horizontal, diagonal) are allowed. Players take turns putting one of their tokens into one of the seven slots. A token falls down as far as possible within a slot. The player with the red tokens begins. The game ends immediately when the one of the players connects four stones.");
			Rules.setPadding(10, 80, 10, 0);
			
			Rules.setGravity(Gravity.CENTER);
			Rules.setMovementMethod(new ScrollingMovementMethod());
			HelpLayout.addView(Rules);
			HelpLayout.addView(BackButtonLayout);
			
			this.setContentView(HelpLayout);
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent4 = new Intent(this, MainActivity.class);
			startActivity(intent4);
			
		}
		
	}


