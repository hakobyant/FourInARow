package com.example.fourinarow;

import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameProcessActivity extends Activity implements OnClickListener {

	int screenWidth, screenHeight, cellSize, place;
	RelativeLayout GameLayout;
	TableLayout Info;
	TableRow firstPlayer, secondPlayer;
	TextView firstName, secondName;
	Button backButton;
	static String turnIndicatorText;

	boolean isTouchAllowed = false;
	Rect[] rects; // will keep the places of the clickable
					// rectangles
	ImageView image;
	Bitmap icon;
	InputStream inputs = null;
	static boolean turn;
	int[][] board = new int[7][6];
	static TextView turnIndicator;
	String playerColor = GameManager.getInstance().mainActivity.getPlayer()
			.getPlayerColor();

	String opponentColor = GameManager.getInstance().mainActivity.getPlayer()
			.getOpponentColor();
	boolean soundIsOn = GameManager.getInstance().mainActivity.getPlayer().getPlayerIsSoundOn();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
				
		GameManager.getInstance().gameProcessActivity = this;

		turnIndicator = new TextView(this);

		rects = new Rect[8];
		
		if (GameManager.getInstance().mainActivity.isFistTime) {
			goToMainActivity();
		} else {

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
			cellSize = screenWidth / 9;
			place = screenHeight / 3;
			GameLayout = new RelativeLayout(this);
			GameLayout.setBackgroundResource(R.drawable.background);// Sets the
																	// background
																	// of
																	// the UI

			try {
				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						cellSize, cellSize);
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						cellSize, cellSize);

				params1.setMargins(cellSize, cellSize, 0, 0);
				inputs = this.getResources().getAssets()
						.open(playerColor + ".png");
				icon = BitmapFactory.decodeStream(inputs);
				ImageView image1 = new ImageView(this);
				image1.setImageBitmap(icon);
				GameLayout.addView(image1, params1);

				params2.setMargins(cellSize, 5 * cellSize / 2, 0, 0);
				inputs = this.getResources().getAssets()
						.open(opponentColor + ".png");
				icon = BitmapFactory.decodeStream(inputs);

				ImageView image2 = new ImageView(this);
				image2.setImageBitmap(icon);
				GameLayout.addView(image2, params2);

			} catch (IOException e) {
				e.printStackTrace();
			}
			Info = new TableLayout(this);

			firstPlayer = new TableRow(this);
			secondPlayer = new TableRow(this);
			// buttonsRow = new TableRow(this);

			firstName = new TextView(this);
			firstName.setTextSize(22);
			firstName.setText(GameManager.getInstance().mainActivity
					.getPlayer().getPlayerUsername() + "(You)");
			firstName.setPadding(3 * cellSize, cellSize, 0, 0);
			firstPlayer.addView(firstName);

			secondName = new TextView(this);
			secondName.setTextSize(22);
			if (GameManager.getInstance().mainActivity.getPlayer()
					.getCurrentOpponentPlayer() != null)
				secondName.setText(GameManager.getInstance().mainActivity
						.getPlayer().getCurrentOpponentPlayer()
						.getPlayerUsername());
			secondName.setPadding(3 * cellSize, 3 * cellSize / 4, 0, 0);
			secondPlayer.addView(secondName);

			Info.addView(firstPlayer);
			Info.addView(secondPlayer);

			RelativeLayout.LayoutParams indicatorParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			indicatorParams.setMargins(cellSize, 5 * cellSize, 0, 0);
			turnIndicator.setText(turnIndicatorText);
			turnIndicator.setTextSize(20);
			GameLayout.addView(turnIndicator, indicatorParams);

			MyView grid = new MyView(this);
			GameLayout.addView(Info);
			GameLayout.addView(grid);
			this.setContentView(GameLayout);
			isTouchAllowed = true;

		}

	}

	private void goToMainActivity() {
		MainActivity.isFistTime = false;
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}

	Paint BorderLinePaint = new Paint();

	public class MyView extends View {
		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub

			super.onDraw(canvas);

			BorderLinePaint.setStrokeWidth(3);
			for (int i = 0; i < 8; i++) {
				rects[i] = new Rect(cellSize * (i + 1), place + 3 * cellSize,
						cellSize * (i + 2), place + cellSize * 9);
				canvas.drawLine(cellSize * (i + 1), place + cellSize, cellSize
						* (i + 1), place + cellSize * 7, BorderLinePaint);

			}
			canvas.drawLine(cellSize, place + cellSize, cellSize * 8, place
					+ cellSize, BorderLinePaint);
			canvas.drawLine(cellSize, place + cellSize * 7, cellSize * 8, place
					+ cellSize * 7, BorderLinePaint);

		}

	}

	public boolean onTouchEvent(MotionEvent event) {

		if (!turn)
			return true;
		if (!isTouchAllowed)
			return true;
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			for (int i = 0; i < 7; i++) {
				if (rects[i].contains(x, y)) {
					dropDown(i);// Log.i("RECT #", (i+1) + " ");
					ServerConnection.sendMove(
							GameManager.getInstance().mainActivity.getPlayer(),
							GameManager.getInstance().mainActivity.getPlayer()
									.getCurrentOpponentPlayer(), Integer
									.toString(i));
				}

			}
		}

		return true;
	}

	private boolean isGameDraw() {

		for (int i = 0; i < 7; i++) {
			if (board[i][5] == 0)
				return false;
		}
		return true;
	}

	private boolean checkIfFinished() {
		// Checking if game has finished horizontally

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				if (sumToRight(i, j) == 4 || sumToRight(i, j) == -4)
					return true;
			}
		}
		// Checking if game has finished diagonally
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (sumToUpRight(i, j) == 4 || sumToUpRight(i, j) == -4)
					return true;
			}
		}
		for (int i = 3; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				if (sumToUpLeft(i, j) == 4 || sumToUpLeft(i, j) == -4)
					return true;
			}
		}
		// Checking if game has finished vertically
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 3; j++) {
				if (sumToUp(i, j) == 4 || sumToUp(i, j) == -4)
					return true;
			}
		}
		if (isGameDraw())
			return true;
		return false;

	}

	public int sumToRight(int i, int j) {
		int S = 0;
		for (int k = 0; k < 4; k++) {
			S += board[i + k][j];
		}
		return S;
	}

	public int sumToUpLeft(int i, int j) {
		int S = 0;
		for (int k = 0; k < 4; k++) {
			S += board[i - k][j + k];
		}
		return S;
	}

	public int sumToUp(int i, int j) {
		int S = 0;
		for (int k = 0; k < 4; k++) {
			S += board[i][j + k];
		}
		return S;
	}

	public int sumToUpRight(int i, int j) {
		int S = 0;
		for (int k = 0; k < 4; k++) {
			S += board[i + k][j + k];
		}
		return S;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent1 = new Intent(this, MainActivity.class);
		startActivity(intent1);
	}

	public void dropDown(int i) {
		try {
			for (int j = 0; j < 6; j++) {
				if (board[i][j] == 0) {

					RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
							cellSize, cellSize);
					iconParams.setMargins(cellSize * (i + 1), place + (6 - j)
							* cellSize, 0, 0);

					// iconParams.setMargins(0,0,0,0);
					inputs = this
							.getResources()
							.getAssets()
							.open(turn ? playerColor + ".png" : opponentColor
									+ ".png");
					icon = BitmapFactory.decodeStream(inputs);
					image = new ImageView(this);
					image.setImageBitmap(icon);
					GameLayout.addView(image, iconParams);
					turn = !turn;
					turnIndicator.setText(turn ? "Your turn"
							: "Opponent's turn");
					board[i][j] = turn ? 1 : -1;
					MediaPlayer mp = MediaPlayer.create(
							getApplicationContext(), R.raw.tap);
					if(soundIsOn)
						mp.start();
					Log.i("Done", "easily");
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		if (checkIfFinished()) {
			Dialog dialog = new Dialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			if (isGameDraw()) {
				MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
						R.raw.draw);
				if(soundIsOn)
					mp.start();
				dialog.setTitle("Draw");

			} else {
				MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
						!turn ? R.raw.victory : R.raw.losing);
				if(soundIsOn)
					mp.start();
				dialog.setTitle(!turn ? "You Won" : "You Lost");
			}
			dialog.show();
		}
	}

	/*
	 * -----------startGameWith--------------- Starts a game with the opponent
	 * 'opponentID':'opponentUsername':'opponentScore'
	 */
	public void startGameWith(final Player player1, final Player player2) {
		// TODO Auto-generated method stub

		turn = (player1.getPlayerID() == GameManager.getInstance().mainActivity
				.getPlayer().getPlayerID()) ? true : false;

		if (turn) {
			GameManager.getInstance().mainActivity.getPlayer()
					.setCurrentOpponentPlayer(player2);
			turnIndicatorText = "Your turn";
		} else {
			GameManager.getInstance().mainActivity.getPlayer()
					.setCurrentOpponentPlayer(player1);

			turnIndicatorText = "Opponent's turn";

			ServerConnection.getMove(player1);
		}

		if (secondName != null) {
			secondName
					.setText(GameManager.getInstance().mainActivity.getPlayer()
							.getCurrentOpponentPlayer().getPlayerUsername());
		}
	}
}
