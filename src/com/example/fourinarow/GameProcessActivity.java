package com.example.fourinarow;

import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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

	final static String YOUR_TURN = "Your turn";
	final static String OPPONENTS_TURN = "Opponent's turn";
	
	
	int screenWidth, screenHeight;
	static int cellSize;
	int place;
	RelativeLayout GameLayout, backButtonLayout;
	TableLayout Info;
	TableRow firstPlayer, secondPlayer;
	TextView firstName, secondName;
	Button resignButton;
	MediaPlayer mp = new MediaPlayer();
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
	Paint BorderLinePaint = new Paint();
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
			createGameProcessScreen();
		}

	}

	private void createGameProcessScreen() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		cellSize = screenWidth / 9;
		place = screenHeight / 3;
		GameLayout = new RelativeLayout(this);
		GameLayout.setBackgroundResource(R.drawable.background); //Sets the background of UI
		
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(cellSize, cellSize);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(cellSize, cellSize);

		params1.setMargins(cellSize, cellSize, 0, 0);
		params2.setMargins(cellSize, 5 * cellSize / 2, 0, 0);

		try {
			
			//drawing the players icon 
			inputs = this.getResources().getAssets().open(playerColor + ".png");
			icon = BitmapFactory.decodeStream(inputs);
			ImageView image1 = new ImageView(this);
			image1.setImageBitmap(icon);
			GameLayout.addView(image1, params1);
			
			//drawing the opponents icon
			inputs = this.getResources().getAssets().open(opponentColor + ".png");
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

		firstName = new TextView(this);
		firstName.setTextSize(pxToDp(cellSize)/2);
		firstName.setText(GameManager.getInstance().mainActivity
				.getPlayer().getPlayerUsername() + "(You)");
		firstName.setPadding(3 * cellSize, cellSize, 0, 0);
		firstPlayer.addView(firstName);

		secondName = new TextView(this);
		secondName.setTextSize(pxToDp(cellSize)/2);
		if (GameManager.getInstance().mainActivity.getPlayer()
				.getCurrentOpponentPlayer() != null)
			secondName.setText(GameManager.getInstance().mainActivity
					.getPlayer().getCurrentOpponentPlayer()
					.getPlayerUsername());
		secondName.setPadding(3 * cellSize, cellSize, 0, 0);
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
		for (int i = 0; i < 8; i++) {
			rects[i] = new Rect(cellSize * (i + 1), place + cellSize,
					cellSize * (i + 2), place + cellSize * 7);
			}
		GameLayout.addView(Info);
		GameLayout.addView(grid);
		
		backButtonLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams blp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, cellSize);
		resignButton = new Button(this);
		resignButton.setText("Resign");
		resignButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mp.isPlaying())
					popUpDialog(1);
				}
		});
		backButtonLayout.setPadding(cellSize, 4*cellSize, cellSize, 0);
		backButtonLayout.addView(resignButton, blp);
		GameLayout.addView(backButtonLayout);
		this.setContentView(GameLayout);
		isTouchAllowed = true;
	}
	
	@Override
	public void onBackPressed() {

	 }
	
	private void goToMainActivity() {
		MainActivity.isFistTime = false;
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}
	
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
			BorderLinePaint.setStyle(Paint.Style.STROKE);
			for (int i = 0; i < 7; i++) {
				/*canvas.drawLine(cellSize * (i + 1), place + cellSize, cellSize
						* (i + 1), place + cellSize * 7, BorderLinePaint);
				*/
				canvas.drawRect(rects[i],BorderLinePaint);
			}
			
			//canvas.drawLine(cellSize, place + cellSize, cellSize * 8, place
				//	+ cellSize, BorderLinePaint);
			//canvas.drawLine(cellSize, place + cellSize * 7, cellSize * 8, place
			//		+ cellSize * 7, BorderLinePaint);

		}

	}

	public static int pxToDp(int px){
	    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}
	
	public static float dpToPx(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
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
					turnIndicator.setText(turn ? GameProcessActivity.YOUR_TURN
							: GameProcessActivity.OPPONENTS_TURN);
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
			
			if(isGameDraw())
				popUpDialog(2);
			else {
				if(!turn){						
				popUpDialog(0);
				}
				else popUpDialog(1);
			}
			
		}
	}

	/*
	 * status is 0 if the player won,
	 * status is 1 if the opponent won,
	 * status is 2 if draw 		
	 */
	private void popUpDialog(int status) {
		Dialog dialog = new Dialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		TableLayout dialogLayout = new TableLayout(this);
		dialog.setContentView(dialogLayout);
		
		TableRow rematchRow=new TableRow(this);
		rematchRow.setGravity(Gravity.CENTER_HORIZONTAL);
		Button rematchButton = new Button(this);
		rematchButton.setText("Rematch");
		rematchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mp.stop();
			}
		});
		rematchRow.addView(rematchButton);
		
		TableRow mainMenuRow=new TableRow(this);
		mainMenuRow.setGravity(Gravity.CENTER_HORIZONTAL);
		Button mainMenuButton = new Button(this);
		mainMenuButton.setText("Main Menu");	
		mainMenuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mp.stop();
				Intent intent = new Intent(GameProcessActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		mainMenuRow.addView(mainMenuButton);
		
		dialogLayout.addView(rematchRow);
		dialogLayout.addView(mainMenuRow);
	
		switch (status){
			
			case 0:{
				mp = MediaPlayer.create(getApplicationContext(), R.raw.victory);
				if(soundIsOn)
					mp.start();
				dialog.setTitle("You Won");
				break;	
			}		
			case 1:{
				mp = MediaPlayer.create(getApplicationContext(), R.raw.losing);
				if(soundIsOn)
					mp.start();
				dialog.setTitle("You Lost");
				break;	
			}
			case 2:{ 
				mp = MediaPlayer.create(getApplicationContext(), R.raw.draw);
				if(soundIsOn)
					mp.start();
				dialog.setTitle("Draw");
				break;	
			}
		}
		dialog.show();
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
			turnIndicatorText = GameProcessActivity.YOUR_TURN;
		} else {
			GameManager.getInstance().mainActivity.getPlayer()
					.setCurrentOpponentPlayer(player1);

			turnIndicatorText = GameProcessActivity.OPPONENTS_TURN;

			ServerConnection.getMove(player1);
		}

		if (secondName != null) {
			secondName
					.setText(GameManager.getInstance().mainActivity.getPlayer()
							.getCurrentOpponentPlayer().getPlayerUsername());
		}
	}
}

