package com.example.fourinarow;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
//import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class MainActivity extends Activity implements OnClickListener{
	boolean firstTime = true;
	private static DisplayMetrics dm;
	String PlayerName = "New Player";
	RelativeLayout MainLayout;
	Dialog dialog;
	TextView Greeting;
	TableLayout ButtonsLayout;
	TableRow GreetingRow, StartGameRow, HighScoresRow, SettingsRow, HelpRow;
	EditText enterName;
	Button startGameButton, highScoresButton, settingsButton, helpButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		if(firstTime) {
			createDialog();
		}
		
		createLayout(dm.widthPixels,dm.heightPixels);
			
	}
	
	private void createLayout(int screenWidth, int screenHeight) {

		int buttonWidth = screenWidth / 2;
		MainLayout = new RelativeLayout(this);
		MainLayout.setBackgroundResource(R.drawable.background);
		MainLayout.setPadding(screenWidth/4, screenHeight/4, 0, 0);
		
		GreetingRow = new TableRow(this);
		GreetingRow.setPadding(0, 0, 0, 50);
		Greeting = new TextView(this);
		Greeting.setText("Hello " + PlayerName);
		Greeting.setWidth(buttonWidth);
		Greeting.setTypeface(Typeface.MONOSPACE);
		Greeting.setGravity(Gravity.CENTER_HORIZONTAL);
		GreetingRow.addView(Greeting);
		
		
		ButtonsLayout = new TableLayout(this);
		
		ButtonsLayout.addView(GreetingRow);
		StartGameRow = new TableRow(this); ButtonsLayout.addView(StartGameRow);
		HighScoresRow = new TableRow(this); ButtonsLayout.addView(HighScoresRow);
		SettingsRow = new TableRow(this); ButtonsLayout.addView(SettingsRow);
		HelpRow = new TableRow(this); ButtonsLayout.addView(HelpRow);
		
		
		startGameButton = new Button(this);
		startGameButton.setWidth(buttonWidth);
		startGameButton.setOnClickListener(new OnClickListener() {//Goes to GameProcessActivity
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("digig","r");
				Intent PickOpponentIntent = new Intent(MainActivity.this, PickOpponentActivity.class);
				startActivity(PickOpponentIntent);
			}
		});
		startGameButton.setText("Start Game");
		StartGameRow.addView(startGameButton);
		
		
		highScoresButton = new Button(this);
		highScoresButton.setWidth(buttonWidth);
		highScoresButton.setOnClickListener(this);
		highScoresButton.setText("Highscores");
		HighScoresRow.addView(highScoresButton);

		settingsButton = new Button(this);
		settingsButton.setWidth(buttonWidth);
		settingsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent settingsIntent= new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(settingsIntent);
			}
		});
		
		settingsButton.setText("Settings");
		SettingsRow.addView(settingsButton);
		
		helpButton = new Button(this);
		helpButton.setWidth(buttonWidth);
		helpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent HelpIntent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(HelpIntent);
			}
		});
		
		helpButton.setText("Help");
		HelpRow.addView(helpButton);
		
		MainLayout.addView(ButtonsLayout);
		setContentView(MainLayout);
	}

	private void createDialog() {
		dialog = new Dialog(this);
		dialog.setTitle("Please Enter Your Name");
		//dialog.getWindow().setLayout(screenWidth/3, screenHeight/3);
		LinearLayout dialogLayout = new LinearLayout(this);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(8);
		enterName = new EditText(this);
		enterName.setFilters(filterArray);
		enterName.setHint("Type here");
		enterName.setOnKeyListener(new EditText.OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {

	            return (keyCode == KeyEvent.KEYCODE_ENTER);
	        }
	    });
		dialogLayout.addView(enterName);
		Button submitNameButton = new Button(this);
		submitNameButton.setText("Submit");
		submitNameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PlayerName = enterName.getText().toString();
				Greeting.setText("Hello " + PlayerName);
				dialog.dismiss();
				Log.i("button ","is submitted");
			}
		});
		dialogLayout.addView(submitNameButton);
		dialog.setContentView(dialogLayout);
		
		dialog.show();
		firstTime = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
}