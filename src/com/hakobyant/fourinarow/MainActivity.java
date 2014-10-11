package com.hakobyant.fourinarow;

import java.util.List;
import java.lang.Integer;


import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	
	static TextView txt;
	private String phoneIDString;
	private String phoneName = "Phone";
	
	private static class Player {
		private static int playerID = -1;
		private static String playerUsername = "";
		private static int playerScore = DEFAULT_SCORE;
		
		private static int currentOpponentID = -1;
		private static String currentOpponentUsername = "";
		private static int currentOpponentScore = DEFAULT_SCORE;
	}
	
	private static SharedPreferences preferences;
	private static SharedPreferences.Editor editor;
	

	String response = new String("");
	String responseUserID = new String("");
	
	private Handler handler;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txt = (TextView) findViewById(R.id.text);
//		txt.setText("");
		
		setPrefs();
		
		createHandler();

		Log.i("Test","1Ara");

		if(preferences.getInt("ID", -1) == -1) {
			
			ServerConnection.createNewUser(phoneName);
		}
		
		Log.i("Test","2Ara");

		
		ServerConnection.sendMove(Player.playerID, 2, "Tiko");
//		getMessage();
	}
	

	private void setPrefs() {
		// TODO Auto-generated method stub
		preferences = getPreferences(0);
		editor = preferences.edit();
		
	}


	private int getIntegerID(String str) {
		// TODO Auto-generated method stub
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			return -1;
		}
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
					
					txt.setText(ServerConnection.doMessageProcess(getMessageResponse) + " 1 " + phoneIDString);
				}
				else if (userIDString != null) {

					txt.setText(ServerConnection.doMessageProcess(userIDString));
					Log.i("Test",ServerConnection.doMessageProcess(userIDString));
				}
				else if (sendMessageResponse != null) {
					
				}
			}
		};
		
		ServerConnection.setHandler(handler);
	}

	public static void updateScreen(String response) {
		// TODO Auto-generated method stub
	}


	public static void updateUserInfo(String response) {
		// TODO Auto-generated method stub
		String[] parsedResponse = response.split("[:]");
		editor.putInt(ID, Integer.parseInt(parsedResponse[0]));
		editor.putInt(SCORE, Integer.parseInt(parsedResponse[2]));
		editor.commit();
		
		Player.playerID = Integer.parseInt(parsedResponse[0]);
		Player.playerUsername = parsedResponse[1];
		Player.playerScore = Integer.parseInt(parsedResponse[2]);
		
	}
	
	public static void updateCurrentOpponentInfo(String response) {
		String[] parsedResponse = response.split("[:]");
	
		Player.currentOpponentID = Integer.parseInt(parsedResponse[0]);
		Player.currentOpponentUsername = parsedResponse[1];
		Player.currentOpponentScore = Integer.parseInt(parsedResponse[2]);

	}
	
	public static void updateUserScore(String response) {
		// TODO Auto-generated method stub
	}


	/*
	 * -----------startGameWith---------------
	 * Starts a game with the opponent 
	 * 'opponentID':'opponentUsername':'opponentScore'
	 * 
	 */
	public static void startGameWith(int opponentID, String opponentUsername,
			int opponentScore) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * ---------------UserIsAlreadyPlaying------------
	 * 
	 * Gives a Dialog saying that the user with 'secondUserID' is
	 * not available any more. Playing another game.
	 * 
	 */
	public static void UserIsAlreadyPlaying(int secondUserID) {
		// TODO Auto-generated method stub
		
	}


	public static String getCurrentName() {
		// TODO Auto-generated method stub
		return Player.playerUsername;
	}


	public static int getCurrentScore() {
		// TODO Auto-generated method stub
		return Player.playerScore;
	}


	/*
	 * Get the online players and update the table
	 */
	public static void updatePlayerTable(List<String[]> retrievedResponse) {
		// TODO Auto-generated method stub
		
	}
	
	public static void calculateNewScore(final int score1, final int score2) {
		
	}
	
}
