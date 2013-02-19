package com.msdpe.tictactoe_leaderboard;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private Button btnPlay;
	private EditText txtName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnPlay = (Button) findViewById(R.id.btnPlay);
		txtName = (EditText) findViewById(R.id.txtName);
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Validate the player's name
				if (txtName.getText().toString().matches("")) {
					txtName.setError("Please enter a name");
					return;
				} else {
					txtName.setError(null);
					
					//Save player's name to preferences
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					SharedPreferences.Editor prefsEditor = prefs.edit();
					prefsEditor.putString("Username", txtName.getText().toString());
					prefsEditor.commit();
					
					Intent intent = new Intent(getApplicationContext(), PlayScoreActivity.class);
			        startActivity(intent);
					
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
