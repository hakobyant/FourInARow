package com.example.fourinarow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {
	RelativeLayout settingsLayout;
	TableLayout form;
	TableRow nameRow, colorRow, opponentColorRow, soundRow;
	EditText nameEdit;
	Spinner prefColorSpinner, opponentColorSpinner, soundSpinner;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GameManager.getInstance().settingsActivity = this;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		createSettingsLayout();

	}

	private void createSettingsLayout() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		settingsLayout = new RelativeLayout(this);
		settingsLayout.setPadding(dm.widthPixels / 8, dm.heightPixels / 5,
				dm.widthPixels / 8, 0);
		settingsLayout.setBackgroundResource(R.drawable.background);
		form = new TableLayout(this);
		nameRow = new TableRow(this);
		TextView nameTag = new TextView(this);
		nameEdit = new EditText(this);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(15);
		nameEdit.setFilters(filterArray);
		nameEdit.setHint("Type Here");
		//nameEdit.setFocusable(false);
		nameEdit.setSelection(nameEdit.getText().length());
		nameEdit.setOnKeyListener(new EditText.OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {

	            if(keyCode == KeyEvent.KEYCODE_ENTER){
		    	//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	            	Log.i("hello", "******axper*******");
	            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(nameEdit.getWindowToken(), 0);
	            }
		    	return (keyCode == KeyEvent.KEYCODE_ENTER);
	        }
	    });
		nameEdit.setText(GameManager.getInstance().mainActivity.getPlayer().getPlayerUsername());
		nameTag.setText("Edit Your Name");
		nameRow.addView(nameTag);
		nameRow.addView(nameEdit);
		form.addView(nameRow);
		colorRow = new TableRow(this);
		opponentColorRow = new TableRow(this);
		TextView colorTag = new TextView(this);
		TextView opponentColorTag = new TextView(this);
		
		prefColorSpinner = new Spinner(this);
		opponentColorSpinner = new Spinner(this);
		soundSpinner = new Spinner(this);

		List<String> colorList = new ArrayList<String>();

		colorList.add("red");
		colorList.add("yellow");
		colorList.add("blue");
		colorList.add("green");
		colorList.add("black");

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, colorList);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prefColorSpinner.setAdapter(spinnerArrayAdapter);
		opponentColorSpinner.setAdapter(spinnerArrayAdapter);
		//int spinnerPosition = spinnerArrayAdapter.getPosition("Yellow");
		int opponentSpinnerPosition = spinnerArrayAdapter.getPosition(GameManager.getInstance().mainActivity.getPlayer().getOpponentColor());
		int playerSpinnerPosition = spinnerArrayAdapter.getPosition(GameManager.getInstance().mainActivity.getPlayer().getPlayerColor());
		
		
		Log.i("Test",Integer.toString(opponentSpinnerPosition));
		Log.i("Test",Integer.toString(playerSpinnerPosition));
		
		
		opponentColorSpinner.setSelection(opponentSpinnerPosition);
		prefColorSpinner.setSelection(playerSpinnerPosition);
		
		colorTag.setText("Your Color");
		opponentColorTag.setText("Opponent's Color");
		colorRow.addView(colorTag);
		colorRow.addView(prefColorSpinner);
		opponentColorRow.addView(opponentColorTag);
		opponentColorRow.addView(opponentColorSpinner);
		form.addView(colorRow);
		form.addView(opponentColorRow);
		TextView soundTag = new TextView(this);
		
		List<String> onOffList = new ArrayList<String>();
		
		onOffList.add("On");
		onOffList.add("Off");

		ArrayAdapter<String> SoundAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, onOffList);
		soundTag.setText("Sound");
		soundRow = new TableRow(this);
		SoundAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		soundSpinner.setAdapter(SoundAdapter);
		soundSpinner.setSelection(GameManager.getInstance().mainActivity.getPlayer().getPlayerIsSoundOn()?0:1);
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
		
		String newName = nameEdit.getText().toString();
		
		if (newName.compareTo("") != 0
				&& prefColorSpinner.getSelectedItem() != opponentColorSpinner
						.getSelectedItem()) {
			Player player = GameManager.getInstance().mainActivity.getPlayer();
			
			String response = "";
			response += Integer.toString(player.getPlayerID());
			response += ":";
			response += newName;
			response += ":";
			response += Integer.toString(player.getPlayerScore());
			
			GameManager.getInstance().mainActivity.updateUserInfo(
					response, 
					prefColorSpinner.getSelectedItem().toString(),
					opponentColorSpinner.getSelectedItem().toString(),
					soundSpinner.getSelectedItem().toString(), false
					);
			Log.i("Vasa",response);
			goToMainActivity();
		} else {
			if (prefColorSpinner.getSelectedItem() == opponentColorSpinner
					.getSelectedItem()) {
				Dialog dialog1 = new Dialog(this);
				dialog1.setTitle("Choose Different Colors");
				dialog1.show();
			} else {
				Dialog dialog = new Dialog(this);
				dialog.setTitle("Fill The Name");
				dialog.show();
			}
		}
	}

	public void goToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}

