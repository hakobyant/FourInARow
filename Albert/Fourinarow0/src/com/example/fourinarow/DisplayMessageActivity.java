package com.example.fourinarow;

//import android.app.ActionBar.LayoutParams;
//import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity implements OnClickListener{
	
	String received;
	RelativeLayout r,r1;
	GridLayout m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_display_message);
		
		r = new RelativeLayout(this);

		r1 = new RelativeLayout(this);
		//String received;
		received = getIntent().getStringExtra("tatik");
		TextView displayText = new TextView(this);

		displayText.setText(received);
		displayText.setTextSize(20);
		r.addView(displayText);
		
		Button click = new Button(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int w = dm.widthPixels;
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		params.leftMargin = w/2; //Your X coordinate
		params.topMargin = w/2; //Your Y coordinate
		click.setLayoutParams(params);
		//click.setY(w/2);
		//click.setX(w/2);
		click.setText("back");
		click.setOnClickListener(this);
		r1.addView(click);
		r.addView(r1);
		r.setBackgroundResource(R.drawable.background);
		this.setContentView(r);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent1 = new Intent(this, MainActivity.class);
		intent1.putExtra("message", received);
		
		startActivity(intent1);
		
	}
	
}
