package com.example.fourinarow;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HighScoresActivity extends Activity {

	RelativeLayout SettingsLayout;

	Button backButton;
	TextView Rules;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		int N = 1000;
		File myDir = getFilesDir();
		try {
			File scoresFile = new File(myDir + "/scores/", "Scores Progress");
			if (scoresFile.getParentFile().mkdirs()) {
				scoresFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(scoresFile);

				fos.write(Integer.toString(N).getBytes());
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File scoresFile = new File(myDir + "/scores/", "Scores Progress");
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(scoresFile));
			BufferedReader r = new BufferedReader(new InputStreamReader(
					inputStream));
			StringBuilder total = new StringBuilder();
			String line;

			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			r.close();
			inputStream.close();
			Log.i("****Big Hopes****", "file content is: " + total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("my Dir", myDir + "/scores/");

		SettingsLayout = new RelativeLayout(this);
		SettingsLayout.setBackgroundResource(R.drawable.background);// Sets the
																	// background
																	// of the UI

		this.setContentView(SettingsLayout);

	}

}
