package com.msdpe.tictactoe_leaderboard;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayFragment extends Fragment {
	
	private String TAG = "PlayFragment";
	private TextView lblInfo;
	private int width = 0;
	private int height = 0;
	private int pass = 0;
	private int xpos = 0;
	private int ypos = 0;
	private Button[][] buttonMatrix = new Button[3][3];
	private boolean playersTurn = false;
	private boolean computersTurn = false;
	private boolean gameOver = false;
	private String playersCharacter = "";
	private String winningCharacter = "";
	private String currentCharacter = "X";
	private int spotsRemaining = 9;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.w(TAG, "onCreateView");
		
		if (savedInstanceState == null) {
			Log.e(TAG, "New play");
			Random whoseFirst = new Random();
			if (whoseFirst.nextInt(2) == 0) {
				playersTurn = false;
				computersTurn = true;
				playersCharacter = "O";
			} else {
				playersTurn = true;
				computersTurn = false;
				playersCharacter = "X";
			}
		} else {
			playersTurn = savedInstanceState.getBoolean("playersTurn");
			computersTurn = savedInstanceState.getBoolean("computersTurn");
			playersCharacter = savedInstanceState.getString("playersCharacter");
			gameOver = savedInstanceState.getBoolean("gameOver");
			currentCharacter = savedInstanceState.getString("currentCharacter");
			spotsRemaining = savedInstanceState.getInt("spotsRemaining");
			winningCharacter = savedInstanceState.getString("winningCharacter");
		}
				
		View view =  inflater.inflate(R.layout.fragment_play, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "SavedInstanceState is " + (savedInstanceState == null ? "null" : "not null"));
		
		lblInfo = (TextView) this.getView().findViewById(R.id.lblInfo);				
		LinearLayout layoutRoot = (LinearLayout) this.getView().findViewById(R.id.layoutRoot);
		RelativeLayout relLayout = new RelativeLayout(getActivity());
		relLayout.addView(new BoardView(getActivity()));
		layoutRoot.addView(relLayout);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
      
        xpos = width / 3;
        ypos = height/3 - (height / 12);

        int buttonMin = 40;
		int marginMin = 20;		
		
		Button btn;
		RelativeLayout.LayoutParams params;
		
		//Crate and add buttons
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3 ; j ++) {
				btn = new Button(getActivity());			
				btn.setTag("" + i + "-" + j);
				//Using the same click listener for each button
				btn.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						processPlayerButtonTap((Button) v);
					}
				});
				params = new RelativeLayout.LayoutParams(xpos - buttonMin, ypos - buttonMin);
				params.leftMargin = marginMin + xpos * i;
				params.topMargin = marginMin + ypos * j;
				if (savedInstanceState != null) {
					btn.setText(savedInstanceState.getString("" + (i * 3 + j)));
				}
				relLayout.addView(btn, params);
				
				buttonMatrix[i][j] = btn;				
			}
		}
			
		if (computersTurn) {
			lblInfo.setText(this.getActivity().getString(R.string.computers_turn));
			this.playComputersTurn();
		} else {
			lblInfo.setText(this.getActivity().getString(R.string.your_turn));
		}
	}	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "onSaveInstanceState");	
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				outState.putString("" + (i * 3 + j), buttonMatrix[i][j].getText().toString());
			}
		}
		super.onSaveInstanceState(outState);
		PlayScoreActivity psa = (PlayScoreActivity) getActivity();
		psa.setCancelReload(true);
		
		outState.putBoolean("playersTurn", playersTurn);
		outState.putBoolean("computersTurn", computersTurn);
		outState.putString("playersCharacter", playersCharacter);
		outState.putBoolean("gameOver", gameOver);
		outState.putString("currentCharacter", currentCharacter);
		outState.putInt("spotsRemaining", spotsRemaining);
		outState.putString("winningCharacter", winningCharacter);
	}
	
	private void playComputersTurn() {
		for (int i = 2; i >= 0; i--) {
			for (int j = 2; j >= 0; j--) {
				if (buttonMatrix[i][j].getText().toString().equals("")) {
					this.computerPicksSpot(buttonMatrix[i][j]);
					return;//
				}
			}
		}
	}
	
	private void computerPicksSpot(Button pickedButton) {
		pickedButton.setText(currentCharacter);		
		spotsRemaining--;
		
		if (isGameOver() || spotsRemaining == 0) {
			handleGameOver();
			return;
		}
		
		switchCurrentCharacterAndTurn();		
		lblInfo.setText(getActivity().getString(R.string.your_turn));
	}

	private void switchCurrentCharacterAndTurn() {
		if (currentCharacter.equals("X")) {
			currentCharacter = "O";
		} else {
			currentCharacter = "X";
		}
		playersTurn = !playersTurn;
		computersTurn = !computersTurn;
	}
	
	private void printBoard() {
		String board = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttonMatrix[j][i].getText().toString().equals(""))
					board += "$";
				else
					board += buttonMatrix[j][i].getText().toString();
			}
		}
		Log.e(TAG, "Board----" + board);
	}
	
	private void processPlayerButtonTap(Button tappedButton) {
		Log.e(TAG, "Player tapped - " + tappedButton.getTag());
		
		if (!gameOver && playersTurn) {
			if (tappedButton.getText().toString().equals("")) {
				tappedButton.setText(currentCharacter);
				spotsRemaining--;
				
				if (isGameOver() || spotsRemaining == 0) {
					handleGameOver();
					return;
				}
				
				switchCurrentCharacterAndTurn();
				
				if (computersTurn)
					playComputersTurn();
			}
		}
	}
	
	private boolean isGameOver() {
		//At least 5 spots must be filled to win a game
		if (spotsRemaining > 4)
			return false;
		
		printBoard();
		for (int i = 0; i < 3; i++) {
			//Check vertical lines, then horizontal lines
			if (buttonMatrix[i][0].getText().toString().equals(buttonMatrix[i][1].getText().toString()) &&
					buttonMatrix[i][0].getText().toString().equals(buttonMatrix[i][2].getText().toString()) &&
					!buttonMatrix[i][0].getText().toString().equals("")) {			
				winningCharacter = buttonMatrix[i][0].getText().toString();
				Log.i(TAG, "Win - "+ winningCharacter);
				return true;
			} else if (buttonMatrix[0][i].getText().toString().equals(buttonMatrix[1][i].getText().toString()) && 
					buttonMatrix[0][i].getText().toString().equals(buttonMatrix[2][i].getText().toString()) &&
					!buttonMatrix[0][i].getText().toString().equals("")) {								
				winningCharacter = buttonMatrix[0][i].getText().toString();
				Log.i(TAG, "Win - "+ winningCharacter);
				return true;
			}
		}
		//Check top left to bottom right then top right to bottom left
		if (buttonMatrix[0][0].getText().toString().equals(buttonMatrix[1][1].getText().toString()) &&
				buttonMatrix[0][0].getText().toString().equals(buttonMatrix[2][2].getText().toString()) &&
				!buttonMatrix[0][0].getText().toString().equals("")) {			
			winningCharacter = buttonMatrix[0][0].getText().toString();
			Log.i(TAG, "Win - "+ winningCharacter);
			return true;
		} else if (buttonMatrix[2][0].getText().toString().equals(buttonMatrix[1][1].getText().toString()) &&
				buttonMatrix[2][0].getText().toString().equals(buttonMatrix[0][2].getText().toString()) &&
				!buttonMatrix[2][0].getText().toString().equals("")) {
			winningCharacter = buttonMatrix[2][0].getText().toString();
			Log.i(TAG, "Win - "+ winningCharacter);
			return true;
		}		
		return false;
	}
	
	private void handleGameOver() {
		gameOver = true;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		String username = prefs.getString("Username", "Player 1");
		
		TicTacToeApplication myApp = (TicTacToeApplication) getActivity().getApplication();
		
		if (winningCharacter.equals("")) {			
			//save tie
			lblInfo.setText("You've tied!");
		} else if (winningCharacter.equals(playersCharacter)) {
			//save player win
			lblInfo.setText("You WIN!");
		} else {
			//save player loss
			lblInfo.setText("You LOSE!");
		}
	}
	
	private class BoardView extends View {
	       public BoardView(Context context) {
	           super(context);	           	           
	       }
	     
	       @Override
	       protected void onDraw(Canvas canvas) {
	           super.onDraw(canvas);
	           Log.w("PlayFragment", "onDraw");
	           // custom drawing code here
	           // remember: y increases from top to bottom
	           // x increases from left to right
	         
	           Paint paint = new Paint();
	           paint.setStyle(Paint.Style.FILL);

	           // make the entire canvas white
	           paint.setColor(Color.TRANSPARENT);
	           canvas.drawPaint(paint);
	           
	           //vertical lines
	           for (int i = 0; i < 2; i++) {                  	                      
	               paint.setColor(Color.BLACK);	               
	               canvas.drawLine(xpos +(xpos*i), 0, xpos +(xpos*i), height, paint);                           
	           }             
	           //horizontal lines
	            paint.setStyle(Style.STROKE);
	               for (int i = 1; i < 3; i++) {
	            	   pass++;
	                   paint.setColor(Color.BLACK);
	                   canvas.drawLine(0, (ypos*pass)+ 5, width, (ypos*pass)+5, paint);      
	               }               
	       }
	}
	
}
