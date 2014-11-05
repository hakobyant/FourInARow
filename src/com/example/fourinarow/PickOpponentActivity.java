package com.example.fourinarow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class PickOpponentActivity extends Activity implements OnClickListener {

	final static public int NUMBER_OF_ONLINE_PLAYERS = 20;
	final static public int CHANGE_TO_OFFLINE = 0;
	final static public int CHANGE_TO_ONLINE = 1;

	RelativeLayout PickOpponentLayout;
	TableLayout players;
	ArrayList<TableRow> playerRows;
	TextView[] newTextView;
	ProgressBar[] newProgressBar;
	List<Player> RetrievedPlayers;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		GameManager.getInstance().pickOpponentActivity = this;
		GameManager.getInstance().mainActivity.getPlayer().setIsPlayerOnline(
				true);

		if (GameManager.getInstance().mainActivity.isFistTime) {
			goToGameProcessActivity();
		} else {

			ServerConnection
					.removePreviousMessages(GameManager.getInstance().mainActivity
							.getPlayer());
		}
	}

	private void goToGameProcessActivity() {
		Intent intent = new Intent(this, GameProcessActivity.class);
		startActivity(intent);
	}

	private void createPickOpponentLayout() {

		newTextView = new TextView[NUMBER_OF_ONLINE_PLAYERS];
		newProgressBar = new ProgressBar[NUMBER_OF_ONLINE_PLAYERS];
		for (int i = 0; i < 20; i++){
			newTextView[i] = new TextView(this);
			newProgressBar[i] = new ProgressBar(this);
		}
		PickOpponentLayout = new RelativeLayout(this);
		PickOpponentLayout.setBackgroundResource(R.drawable.background);// Sets
																		// the
																		// background
																		// of
																		// the
																		// UI

		players = new TableLayout(this);

		// TextView Rules = new TextView(this);
		// Rules.setTextSize(30);
		// Rules.setText("Player 1\nPlayer 2\nPlayer 3");
		// Rules.setPadding(10, 80, 10, 0);
		/*
		 * Rules.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent PickOpponentIntent = new
		 * Intent(PickOpponentActivity.this, GameProcessActivity.class);
		 * startActivity(PickOpponentIntent); } });
		 * 
		 * Rules.setGravity(Gravity.CENTER); Rules.setMovementMethod(new
		 * ScrollingMovementMethod()); PickOpponentLayout.addView(Rules);
		 */
	}

	@Override
	public void onClick(View v) {

		for (int i = 0; i < NUMBER_OF_ONLINE_PLAYERS; i++) {
			if (v.getId() == newTextView[i].getId()) {

				Log.i("Vasa", Integer.toString(v.getId()));
				Log.i("Vasa", Integer.toString(i));

				ServerConnection.requestGameWith(
						GameManager.getInstance().mainActivity.getPlayer(),
						RetrievedPlayers.get(i));
				startGameWith(
						GameManager.getInstance().mainActivity.getPlayer(),
						RetrievedPlayers.get(i));
				return;
			}
		}
	}

	public Player opponent;
	public String name;
	public int score;

	public void updatePlayerTable(final List<Player> retrievedResponse) {
		// TODO Auto-generated method stub

		RetrievedPlayers = retrievedResponse;
		createPickOpponentLayout();

		for (int i = 0; i < retrievedResponse.size() - 1; i++) {
			final int j = i;
			opponent = retrievedResponse.get(i);
			name = retrievedResponse.get(i).getPlayerUsername();
			score = retrievedResponse.get(i).getPlayerScore();
			TableRow newPlayerRow = new TableRow(this);
			newTextView[i].setTextSize(20);

			newTextView[i].setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					ServerConnection.requestGameWith(
							GameManager.getInstance().mainActivity.getPlayer(),
							retrievedResponse.get(j));
					GameManager.getInstance().gameProcessActivity.turnIndicatorText = GameProcessActivity.YOUR_TURN;

					Intent PickOpponentIntent = new Intent(
							PickOpponentActivity.this,
							GameProcessActivity.class);
					startActivity(PickOpponentIntent);
					PickOpponentIntent.putExtra("Name", name);
					PickOpponentIntent.putExtra("Score", score);

					return true;
				}
			});

			// newTextView[i].setOnClickListener(new OnClickListener() {
			// public void onClick(View v) { // TODO Auto-generated
			// Intent PickOpponentIntent = new Intent(
			// PickOpponentActivity.this,
			// GameProcessActivity.class);
			// startActivity(PickOpponentIntent);
			// PickOpponentIntent.putExtra("Name", name);
			// PickOpponentIntent.putExtra("Score", score);
			// ServerConnection.requestGameWith(
			// GameManager.getInstance().mainActivity.getPlayer(),
			// retrievedResponse.get(j));
			// }
			// });

			newTextView[i].setText(Integer.toString(i + 1) + ". "
					+ retrievedResponse.get(i).getPlayerUsername() + "("
					+ score + ")");
			newPlayerRow.addView(newTextView[i]);

			players.addView(newPlayerRow);

		}

		PickOpponentLayout.addView(players);

		this.setContentView(PickOpponentLayout);

		// PickOpponentLayout.setMovementMethod(new ScrollingMovementMethod());
	}

	public void startGameWith(final Player player1, final Player player2) {

		GameManager.getInstance().gameProcessActivity.startGameWith(player1,
				player2);

		Intent intent = new Intent(this, GameProcessActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		ServerConnection.updateIsOnline(CHANGE_TO_OFFLINE);
		GameManager.getInstance().mainActivity.getPlayer().setIsPlayerOnline(
				false);
		super.onBackPressed();
	}

	@Override
	public void onPause() {
		ServerConnection.updateIsOnline(CHANGE_TO_OFFLINE);
		GameManager.getInstance().mainActivity.getPlayer().setIsPlayerOnline(
				false);
		Log.i("Test", "Paused");
		super.onPause();
	}

	@Override
	public void onResume() {
		GameManager.getInstance().mainActivity.getPlayer().setIsPlayerOnline(
				true);
		ServerConnection.updateIsOnline(CHANGE_TO_ONLINE);

		ServerConnection
				.removePreviousMessages(GameManager.getInstance().mainActivity
						.getPlayer());

		super.onResume();
	}

}
