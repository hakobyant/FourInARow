package com.example.fourinarow;

import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	Rect [] rects = new Rect[8];					//will keep the places of the clickable rectangles
	ImageView image;
	Bitmap icon;
	InputStream inputs = null;
	boolean turn = true;
	int[][] board = new int[7][6];
	TextView turnIndicator;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
    	DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		cellSize = screenWidth/9;
		place = screenHeight/3;
		GameLayout = new RelativeLayout(this);
		GameLayout.setBackgroundResource(R.drawable.background);// Sets the background of the UI
		
		
		try{
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(cellSize, cellSize);
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(cellSize, cellSize);
			
			params1.setMargins(cellSize, cellSize, 0, 0);
			inputs = this.getResources().getAssets().open("red.png");
			icon = BitmapFactory.decodeStream(inputs);
			ImageView image1 = new ImageView(this);
			image1.setImageBitmap(icon);
			GameLayout.addView(image1,params1);
			
			params2.setMargins(cellSize, 5 * cellSize/2, 0, 0);
			inputs = this.getResources().getAssets().open("yellow.png");
			icon = BitmapFactory.decodeStream(inputs);
			ImageView image2 = new ImageView(this);
			image2.setImageBitmap(icon);
			GameLayout.addView(image2,params2);
			
		}catch(IOException e){
			e.printStackTrace();
		}
		Info = new TableLayout(this);
		
		firstPlayer = new TableRow(this);
		secondPlayer = new TableRow(this);
		//buttonsRow = new TableRow(this);
	
		
		firstName = new TextView(this);
		firstName.setTextSize(22);
		firstName.setText("You");
		firstName.setPadding(3 * cellSize, cellSize, 0, 0);
		firstPlayer.addView(firstName);
		
		
		secondName = new TextView(this);
		secondName.setTextSize(22);
		secondName.setText("Your Opponent");
		secondName.setPadding(3 * cellSize, 3 * cellSize/4, 0, 0);
		secondPlayer.addView(secondName);
		
		
		Info.addView(firstPlayer);
		Info.addView(secondPlayer);
		
		turnIndicator = new TextView(this);
		RelativeLayout.LayoutParams indicatorParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		indicatorParams.setMargins(cellSize, 5 * cellSize, 0, 0);
		turnIndicator.setText("Your turn"); 
		turnIndicator.setTextSize(20);
		GameLayout.addView(turnIndicator,indicatorParams);
		
		backButton = new Button(this);
		backButton.setText("Back To Main Menu");
		backButton.setOnClickListener(this);
		
		//LinearLayout BackButtonLayout = new LinearLayout(this);
		//BackButtonLayout.addView(backButton);
		
		//GameLayout.addView(BackButtonLayout);
		MyView grid = new MyView(this);
		GameLayout.addView(Info);
		GameLayout.addView(grid);
		GameLayout.addView(backButton);
		this.setContentView(GameLayout);
		
	}
	Paint firstPlayerPaint = new Paint();
	Paint secondPlayerPaint = new Paint();
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
        	for(int i=0; i<8; i++){
        		rects[i] = new Rect(cellSize*(i+1), place + 3*cellSize, cellSize*(i+2), place + cellSize*9);
        		//rects[i].set(cellSize*(i+1), place, cellSize*(i+2), place + cellSize*6);
        		canvas.drawLine(cellSize*(i+1), place + cellSize, cellSize*(i+1), place + cellSize*7, BorderLinePaint);
        		
        	}
        	canvas.drawLine(cellSize, place + cellSize, cellSize*8, place + cellSize, BorderLinePaint);
        	canvas.drawLine(cellSize, place + cellSize*7, cellSize*8, place + cellSize*7, BorderLinePaint);
        	
        	
        	
        	firstPlayerPaint.setColor(Color.RED);
        	secondPlayerPaint.setColor(Color.YELLOW);
        	//canvas.drawCircle(cellSize, cellSize, cellSize/3, firstPlayerPaint);
        	//canvas.drawCircle(cellSize, cellSize * 2, cellSize/3, secondPlayerPaint);
          
        }
	

	}
public boolean onTouchEvent(MotionEvent event) {
	    
	    if(event.getAction() == MotionEvent.ACTION_UP){
		    	int x = (int)event.getX();
			    int y = (int)event.getY();
			    for(int i = 0; i<7; i++)
			    	{
			    		if (rects[i].contains(x, y)) dropDown(i);//Log.i("RECT #", (i+1) + " ");
			    		
			    	}
		}
	    
	    return false;
}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent1 = new Intent(this, MainActivity.class);
		startActivity(intent1);
	}
	public void dropDown(int i){
		try {
	   			//image.setScaleX(8/10);
	   			//while(board[i][j] != 0 ){j++;}
   				for(int j = 0; j<6; j++){
		   			if(board[i][j] == 0)
		   			{
	   					
			   			RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(cellSize, cellSize);
			   			iconParams.setMargins(cellSize*(i+1),place + (6-j)*cellSize,0,0);
			   			//iconParams.setMargins(0,0,0,0);
			        	inputs = this.getResources().getAssets().open(turn?"red.png":"yellow.png");
			        	icon = BitmapFactory.decodeStream(inputs);
			   			image = new ImageView(this);
			   			image.setImageBitmap(icon);
			   			GameLayout.addView(image, iconParams);
			   			turn = !turn;
			   			turnIndicator.setText(turn?"Your turn":"Opponent's turn");
			   			board[i][j] = turn?1:2;
			   			Log.i("Done","easily");
			   			break;
		   			}
		   			
	   			}				
		}
	        catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				}
	}
}
