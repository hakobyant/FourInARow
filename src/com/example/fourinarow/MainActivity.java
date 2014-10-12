package com.example.fourinarow;


import java.util.List;

import com.example.fourinarow.ServerConnection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
//import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;



public class MainActivity extends Activity {
	
	final static String ID = "ID";
	final static String USERNAME = "USERNAME";
	final static String SCORE = "SCORE";
	final static String SEND_MESSAGE = "send_message";
	final static String GET_MESSAGE = "get_Message";
	final static String SEND_USER = "send_user";
	public static final int DEFAULT_SCORE = 1000;

	private static Player player;
	
	private static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;

	private Handler handler;

	
	boolean firstTime;
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
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		player = new Player();
		
		setPrefs();

//		createHandler();

		Greeting = new TextView(this);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		if(preferences.getInt("ID", -1) == -1) {
			createDialog();
		}
		else {
			player.setPlayer(preferences.getInt("ID", -1), 
							preferences.getString("Name", "Player"), 
							preferences.getInt("Score", DEFAULT_SCORE));

			Greeting.setText("Hello " + player.getPlayerUsername());
		}

		
		createLayout(dm.widthPixels,dm.heightPixels);
	}
	
	private void setPrefs() {
		// TODO Auto-generated method stub
		preferences = getPreferences(0);
		editor = preferences.edit();
		
	}
	
	private void createHandler() {
		handler = new Handler() {
			// Create handleMessage function
			public void handleMessage(Message msg) {
				String getMessageResponse = msg.getData().getString(GET_MESSAGE);
				String sendMessageResponse = msg.getData().getString(SEND_MESSAGE);
				String userIDString = msg.getData().getString(SEND_USER);
				
				if(getMessageResponse == null && userIDString == null && sendMessageResponse == null) {
					Toast.makeText(
							getBaseContext(),
							"Not Got Response From Server.",
							Toast.LENGTH_SHORT).show();
				}
				else if (getMessageResponse != null) {
					
				}
				else if (userIDString != null) {

					Log.i("Test",ServerConnection.doMessageProcess(userIDString));
				}
				else if (sendMessageResponse != null) {
					
				}
			}
		};
		
		ServerConnection.setHandler(handler);
	}


	
	private void createLayout(int screenWidth, int screenHeight) {

		int buttonWidth = screenWidth / 2;
		MainLayout = new RelativeLayout(this);
		MainLayout.setBackgroundResource(R.drawable.background);
		MainLayout.setPadding(screenWidth/4, screenHeight/4, 0, 0);
		
		GreetingRow = new TableRow(this);
		GreetingRow.setPadding(0, 0, 0, 50);
		Greeting = new TextView(this);
		Greeting.setText("Hello " + player.getPlayerUsername());
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
				Intent PickOpponentIntent = new Intent(MainActivity.this, PickOpponentActivity.class);
				startActivity(PickOpponentIntent);
			}
		});
		startGameButton.setText("Start Game");
		StartGameRow.addView(startGameButton);
		
		
		highScoresButton = new Button(this);
		highScoresButton.setWidth(buttonWidth);
//		highScoresButton.setOnClickListener(this);
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
		filterArray[0] = new InputFilter.LengthFilter(20);
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
				ServerConnection.createNewUser(PlayerName);
				Log.i("button ","is submitted");
			}
		});
		dialogLayout.addView(submitNameButton);
		dialog.setContentView(dialogLayout);
		
		dialog.show();
		firstTime = false;
	}

	public static void updateScreen(String response) {
		// TODO Auto-generated method stub
	}


	public static void updateUserInfo(String response) {
		// TODO Auto-generated method stub
		String[] parsedResponse = response.split("[:]");
		editor.putInt(ID, Integer.parseInt(parsedResponse[0]));
		editor.putString("Name", parsedResponse[1]);
		editor.putInt(SCORE, Integer.parseInt(parsedResponse[2]));
		editor.commit();
		
		player.setPlayer(Integer.parseInt(parsedResponse[0]),
						parsedResponse[1],
						Integer.parseInt(parsedResponse[2]));
	}
	
	public static void updateCurrentOpponentInfo(String response) {
		String[] parsedResponse = response.split("[:]");
	
		player.getCurrentOpponentPlayer().setPlayer(Integer.parseInt(parsedResponse[0]),
													parsedResponse[1],
													Integer.parseInt(parsedResponse[2]));

	}
	
	public static void updateUserScore(String response) {
		// TODO Auto-generated method stub
	}


	public static Player getPlayer() {
		return player;
	}

	/*
	 * ---------------UserIsAlreadyPlaying------------
	 * 
	 * Gives a Dialog saying that the user 'player2' is
	 * not available any more. Playing another game.
	 * 
	 */
	public static void UserIsAlreadyPlaying(Player player2) {
		// TODO Auto-generated method stub
		
	}


	/*
	 * Get the online players and update the table
	 */
	
	public static void calculateNewScore(final int score1, final int score2) {
		
	}

	
}