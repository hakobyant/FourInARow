package com.example.fourinarow;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PickOpponentActivity extends Activity implements OnClickListener{
	RelativeLayout PickOpponentLayout;
	LinearLayout BackButtonLayout;
	TableLayout players;
	ArrayList<TableRow> formats;
	Button backButton;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		GameManager.getInstance().pickOpponentActivity = this;
		
		ServerConnection.activePlayersConnection(GameManager.getInstance().mainActivity.getPlayer());
		createPickOpponentLayout();
	}
	
	private void createPickOpponentLayout() {
		players = new TableLayout(this);
		formats = new ArrayList<TableRow>();
		PickOpponentLayout = new RelativeLayout(this);
		PickOpponentLayout.setBackgroundResource(R.drawable.background);// Sets the background of the UI
		
		backButton = new Button(this);
		backButton.setText("Back To Main Menu");
		backButton.setOnClickListener(this);
		
		BackButtonLayout = new LinearLayout(this);
		BackButtonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		BackButtonLayout.addView(backButton);
		
		TextView Rules = new TextView(this);
		Rules.setTextSize(30);
		Rules.setText("Player 1\nPlayer 2\nPlayer 3");
		Rules.setPadding(10, 80, 10, 0);
		Rules.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent PickOpponentIntent = new Intent(PickOpponentActivity.this, GameProcessActivity.class);
				startActivity(PickOpponentIntent);
			}
		});
		
		Rules.setGravity(Gravity.CENTER);
		Rules.setMovementMethod(new ScrollingMovementMethod());
		PickOpponentLayout.addView(Rules);
		PickOpponentLayout.addView(BackButtonLayout);
		
		this.setContentView(PickOpponentLayout);
	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent4 = new Intent(this, MainActivity.class);
		startActivity(intent4);
		
	}
	
	public void updatePlayerTable(List<String[]> retrievedResponse) {
		// TODO Auto-generated method stub
		
	}


}
