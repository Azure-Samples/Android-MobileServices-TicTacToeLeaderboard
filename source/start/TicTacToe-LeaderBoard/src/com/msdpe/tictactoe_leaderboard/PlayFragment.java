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
	
	private final String TAG = "PlayFragment";
	private TextView mLblInfo;
	private int mWidth = 0;
	private int mHeight = 0;
	private int mPass = 0;
	private int mXPos = 0;
	private int mYPos = 0;
	private Button[][] mButtonMatrix = new Button[3][3];
	private boolean mPlayersTurn = false;
	private boolean mComputersTurn = false;
	private boolean mGameOver = false;
	private String mPlayersCharacter = "";
	private String mWinningCharacter = "";
	private String mCurrentCharacter = "X";
	private int mSpotsRemaining = 9;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.w(TAG, "onCreateView");
		
		if (savedInstanceState == null) {
			Log.e(TAG, "New play");
			Random whoseFirst = new Random();
			if (whoseFirst.nextInt(2) == 0) {
				mPlayersTurn = false;
				mComputersTurn = true;
				mPlayersCharacter = "O";
			} else {
				mPlayersTurn = true;
				mComputersTurn = false;
				mPlayersCharacter = "X";
			}
		} else {
			mPlayersTurn = savedInstanceState.getBoolean("playersTurn");
			mComputersTurn = savedInstanceState.getBoolean("computersTurn");
			mPlayersCharacter = savedInstanceState.getString("playersCharacter");
			mGameOver = savedInstanceState.getBoolean("gameOver");
			mCurrentCharacter = savedInstanceState.getString("currentCharacter");
			mSpotsRemaining = savedInstanceState.getInt("spotsRemaining");
			mWinningCharacter = savedInstanceState.getString("winningCharacter");
		}
				
		View view =  inflater.inflate(R.layout.fragment_play, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "SavedInstanceState is " + (savedInstanceState == null ? "null" : "not null"));
		
		mLblInfo = (TextView) this.getView().findViewById(R.id.lblInfo);				
		LinearLayout layoutRoot = (LinearLayout) this.getView().findViewById(R.id.layoutRoot);
		RelativeLayout relLayout = new RelativeLayout(getActivity());
		relLayout.addView(new BoardView(getActivity()));
		layoutRoot.addView(relLayout);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth();
        mHeight = display.getHeight();
      
        mXPos = mWidth / 3;
        mYPos = mHeight/3 - (mHeight / 12);

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
				params = new RelativeLayout.LayoutParams(mXPos - buttonMin, mYPos - buttonMin);
				params.leftMargin = marginMin + mXPos * i;
				params.topMargin = marginMin + mYPos * j;
				if (savedInstanceState != null) {
					btn.setText(savedInstanceState.getString("" + (i * 3 + j)));
				}
				relLayout.addView(btn, params);
				
				mButtonMatrix[i][j] = btn;				
			}
		}
			
		if (mComputersTurn) {
			mLblInfo.setText(this.getActivity().getString(R.string.computers_turn));
			this.playComputersTurn();
		} else {
			mLblInfo.setText(this.getActivity().getString(R.string.your_turn));
		}
	}	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "onSaveInstanceState");	
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				outState.putString("" + (i * 3 + j), mButtonMatrix[i][j].getText().toString());
			}
		}
		super.onSaveInstanceState(outState);
		PlayScoreActivity psa = (PlayScoreActivity) getActivity();
		psa.setCancelReload(true);
		
		outState.putBoolean("playersTurn", mPlayersTurn);
		outState.putBoolean("computersTurn", mComputersTurn);
		outState.putString("playersCharacter", mPlayersCharacter);
		outState.putBoolean("gameOver", mGameOver);
		outState.putString("currentCharacter", mCurrentCharacter);
		outState.putInt("spotsRemaining", mSpotsRemaining);
		outState.putString("winningCharacter", mWinningCharacter);
	}
	
	private void playComputersTurn() {
		for (int i = 2; i >= 0; i--) {
			for (int j = 2; j >= 0; j--) {
				if (mButtonMatrix[i][j].getText().toString().equals("")) {
					this.computerPicksSpot(mButtonMatrix[i][j]);
					return;//
				}
			}
		}
	}
	
	private void computerPicksSpot(Button pickedButton) {
		pickedButton.setText(mCurrentCharacter);		
		mSpotsRemaining--;
		
		if (isGameOver() || mSpotsRemaining == 0) {
			handleGameOver();
			return;
		}		
		switchCurrentCharacterAndTurn();		
		mLblInfo.setText(getActivity().getString(R.string.your_turn));
	}

	private void switchCurrentCharacterAndTurn() {
		if (mCurrentCharacter.equals("X")) {
			mCurrentCharacter = "O";
		} else {
			mCurrentCharacter = "X";
		}
		mPlayersTurn = !mPlayersTurn;
		mComputersTurn = !mComputersTurn;
	}
	
	private void printBoard() {
		String board = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (mButtonMatrix[j][i].getText().toString().equals(""))
					board += "$";
				else
					board += mButtonMatrix[j][i].getText().toString();
			}
		}
		Log.e(TAG, "Board----" + board);
	}
	
	private void processPlayerButtonTap(Button tappedButton) {
		Log.e(TAG, "Player tapped - " + tappedButton.getTag());
		
		if (!mGameOver && mPlayersTurn) {
			if (tappedButton.getText().toString().equals("")) {
				tappedButton.setText(mCurrentCharacter);
				mSpotsRemaining--;
				
				if (isGameOver() || mSpotsRemaining == 0) {
					handleGameOver();
					return;
				}
				
				switchCurrentCharacterAndTurn();
				
				if (mComputersTurn)
					playComputersTurn();
			}
		}
	}
	
	private boolean isGameOver() {
		//At least 5 spots must be filled to win a game
		if (mSpotsRemaining > 4)
			return false;
		
		printBoard();
		for (int i = 0; i < 3; i++) {
			//Check vertical lines, then horizontal lines
			if (mButtonMatrix[i][0].getText().toString().equals(mButtonMatrix[i][1].getText().toString()) &&
					mButtonMatrix[i][0].getText().toString().equals(mButtonMatrix[i][2].getText().toString()) &&
					!mButtonMatrix[i][0].getText().toString().equals("")) {			
				mWinningCharacter = mButtonMatrix[i][0].getText().toString();
				Log.i(TAG, "Win - "+ mWinningCharacter);
				return true;
			} else if (mButtonMatrix[0][i].getText().toString().equals(mButtonMatrix[1][i].getText().toString()) && 
					mButtonMatrix[0][i].getText().toString().equals(mButtonMatrix[2][i].getText().toString()) &&
					!mButtonMatrix[0][i].getText().toString().equals("")) {								
				mWinningCharacter = mButtonMatrix[0][i].getText().toString();
				Log.i(TAG, "Win - "+ mWinningCharacter);
				return true;
			}
		}
		//Check top left to bottom right then top right to bottom left
		if (mButtonMatrix[0][0].getText().toString().equals(mButtonMatrix[1][1].getText().toString()) &&
				mButtonMatrix[0][0].getText().toString().equals(mButtonMatrix[2][2].getText().toString()) &&
				!mButtonMatrix[0][0].getText().toString().equals("")) {			
			mWinningCharacter = mButtonMatrix[0][0].getText().toString();
			Log.i(TAG, "Win - "+ mWinningCharacter);
			return true;
		} else if (mButtonMatrix[2][0].getText().toString().equals(mButtonMatrix[1][1].getText().toString()) &&
				mButtonMatrix[2][0].getText().toString().equals(mButtonMatrix[0][2].getText().toString()) &&
				!mButtonMatrix[2][0].getText().toString().equals("")) {
			mWinningCharacter = mButtonMatrix[2][0].getText().toString();
			Log.i(TAG, "Win - "+ mWinningCharacter);
			return true;
		}		
		return false;
	}
	
	private void handleGameOver() {
		mGameOver = true;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		String username = prefs.getString("Username", "Player 1");
		
		if (mWinningCharacter.equals("")) {			
			//save tie
			mLblInfo.setText("You've tied!");
		} else if (mWinningCharacter.equals(mPlayersCharacter)) {
			//save player win
			mLblInfo.setText("You WIN!");
		} else {
			//save player loss
			mLblInfo.setText("You LOSE!");
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
	               canvas.drawLine(mXPos +(mXPos*i), 0, mXPos +(mXPos*i), mHeight, paint);                           
	           }             
	           //horizontal lines
	            paint.setStyle(Style.STROKE);
	               for (int i = 1; i < 3; i++) {
	            	   mPass++;
	                   paint.setColor(Color.BLACK);
	                   canvas.drawLine(0, (mYPos*mPass)+ 5, mWidth, (mYPos*mPass)+5, paint);      
	               }               
	       }
	}
	
}
