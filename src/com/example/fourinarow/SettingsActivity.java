package com.example.fourinarow;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SettingsActivity  extends Activity implements OnClickListener{
	RelativeLayout settingsLayout;
	TableLayout form;
	TableRow nameRow, colorRow, opponentColorRow, soundRow;
	EditText nameEdit;
	Spinner prefColorSpinner, opponentColorSpinner, soundSpinner;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		createSettingsLayout();
		
	
	}
	
	private void createSettingsLayout() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		settingsLayout = new RelativeLayout(this);
		settingsLayout.setPadding(dm.widthPixels/8, dm.heightPixels/4, dm.widthPixels/8, 0);
		settingsLayout.setBackgroundResource(R.drawable.background);
		form = new TableLayout(this);
		nameRow = new TableRow(this);
		TextView nameTag = new TextView(this);
		nameEdit = new EditText(this);
		//nameEdit.setText(MainActivity.Player.playerUsername);
		nameTag.setText("Edit Your Name");
		nameRow.addView(nameTag);
		nameRow.addView(nameEdit);
		form.addView(nameRow);
		colorRow = new TableRow(this);
		opponentColorRow = new TableRow(this);
		TextView colorTag = new TextView(this);
		TextView opponentColorTag = new TextView(this);
		prefColorSpinner= new Spinner(this);
		opponentColorSpinner= new Spinner(this);
			List<String> colorList = new ArrayList<String>();
			
			colorList.add("Red");
			colorList.add("Yellow");
			colorList.add("Blue");
	
		    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorList);
		    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		    prefColorSpinner.setAdapter(spinnerArrayAdapter);
		    opponentColorSpinner.setAdapter(spinnerArrayAdapter);
		    int spinnerPosition = spinnerArrayAdapter.getPosition("Yellow");
		    opponentColorSpinner.setSelection(spinnerPosition);
		colorTag.setText("Your Color");
		opponentColorTag.setText("Opponent's Color");
		colorRow.addView(colorTag);
		colorRow.addView(prefColorSpinner);
		opponentColorRow.addView(opponentColorTag);
		opponentColorRow.addView(opponentColorSpinner);
		form.addView(colorRow);
		form.addView(opponentColorRow);
		Spinner soundSpinner= new Spinner(this);
		TextView soundTag = new TextView(this);
		List<String> onOffList = new ArrayList<String>();
		onOffList.add("On");
		onOffList.add("Off");
		
		 ArrayAdapter<String> SoundAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, onOffList);
		soundTag.setText("Sound");
		soundRow = new TableRow(this);
		SoundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	    soundSpinner.setAdapter(SoundAdapter);
		soundRow.addView(soundTag);
		soundRow.addView(soundSpinner);
		form.addView(soundRow);
		settingsLayout.addView(form);
		Button submitButton = new Button(this);
		submitButton.setText("Submit changes");
		submitButton.setOnClickListener(this);
		form.addView(submitButton);
		setContentView(settingsLayout);
		
		
	}
	@Override
	public void onClick(View v) {
		if(nameEdit.getText().toString().compareTo("") != 0 && prefColorSpinner.getSelectedItem() != opponentColorSpinner.getSelectedItem()){
			ServerConnection.updateName(nameEdit.getText().toString());
		}
		else {
			if(prefColorSpinner.getSelectedItem() == opponentColorSpinner.getSelectedItem()){
				Dialog dialog1 = new Dialog(this);
				dialog1.setTitle("Choose Different Colors");
				dialog1.show();
			}else {
				Dialog dialog = new Dialog(this);
				dialog.setTitle("Fill The Name");
				dialog.show();
			}
		}
	}
	
	public void goToMainActivity(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
}


