package com.msdpe.tictactoe_leaderboard;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
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
	private String playersCharacter = "";
	private boolean gameOver = false;
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
		}
		
		
		View view =  inflater.inflate(R.layout.fragment_play, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "SavedInstanceState is " + (savedInstanceState == null ? "null" : "not null"));
		
		lblInfo = (TextView) this.getView().findViewById(R.id.lblInfo);		
		
		
		
		LinearLayout layoutRoot = (LinearLayout) this.getView().findViewById(R.id.layoutRoot);
		RelativeLayout relLayout = new RelativeLayout(getActivity());
		relLayout.addView(new DemoView(getActivity()));
		layoutRoot.addView(relLayout);
		//layoutRoot.addView(new DemoView(getActivity()));
		
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();//start
        height = display.getHeight();//end

      
        xpos = width / 3;
        ypos = height/3 - (height / 12);
//        Log.w("PlayFragment", "Width = " + width);
//        Log.w("PlayFragment", "Height = " + height);
//        Log.w("PlayFragment", "Xpos = " + xpos);
//        Log.w("PlayFragment", "Ypos = " + ypos);
        
//        02-18 14:48:44.125: W/PlayFragment(1445): Width = 320
//		02-18 14:48:44.135: W/PlayFragment(1445): Height = 480
//		02-18 14:48:44.155: W/PlayFragment(1445): Xpos = 106
//		02-18 14:48:44.155: W/PlayFragment(1445): Ypos = 120
        int buttonMin = 40;
		int marginMin = 20;
		
//		Button btn = new Button(getActivity());				
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(xpos - buttonMin, ypos - buttonMin);
//		params.leftMargin = marginMin;
//		params.topMargin = marginMin;
//		relLayout.addView(btn, params);
		
		
		Button btn;
		RelativeLayout.LayoutParams params;
		
//		if (buttonMatrix[0][0] != null) {
//			
//			Log.e(TAG, "BM not null");
//			Log.e(TAG, "Sp1: " + buttonMatrix[0][0].getText());
//		} else {
//			Log.e(TAG, "BM NULL");
//		}
		
		for (int i = 0; i < 3; i ++) {
			for (int j = 0; j < 3 ; j ++) {
				btn = new Button(getActivity());			
				btn.setTag("" + i + "-" + j);
				btn.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						//Log.i(TAG, (String) v.getTag());
						processPlayerButtonTap((Button) v);
					}
				});
				params = new RelativeLayout.LayoutParams(xpos - buttonMin, ypos - buttonMin);
				params.leftMargin = marginMin + xpos * i;
				params.topMargin = marginMin + ypos * j;
				if (savedInstanceState != null) {
					//Log.w(TAG, "Not null -" + (i * 3 + j) + " - " + savedInstanceState.getString("" + (i * 3 + j)));
					btn.setText(savedInstanceState.getString("" + (i * 3 + j)));
				}
				relLayout.addView(btn, params);
				
				buttonMatrix[i][j] = btn;				
			}
		}
		
//		btn = new Button(getActivity());				
//		params = new RelativeLayout.LayoutParams(xpos - buttonMin, ypos - buttonMin);
//		params.leftMargin = marginMin + xpos * 2;
//		params.topMargin = marginMin;
//		relLayout.addView(btn, params);
		
		
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
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				//Log.i(TAG, "" + (i * 3 + j) + "-"+ buttonMatrix[i][j].getText().toString());
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
		
		//check for game over
		
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
	
	private void processPlayerButtonTap(Button tappedButton) {
		if (!gameOver && playersTurn) {
			if (tappedButton.getText().toString().equals("")) {
				tappedButton.setText(currentCharacter);
				spotsRemaining--;
				
				//check for game over
				
				switchCurrentCharacterAndTurn();
				
				if (computersTurn)
					playComputersTurn();
			}
		}
	}
	
	private class DemoView extends View {
	       public DemoView(Context context) {
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
	         
//	           Display display = getActivity().getWindowManager().getDefaultDisplay();
//	           width = display.getWidth();//start
//	           height = display.getHeight();//end
//
//	         
//	           xpos = width / 3;
//	           ypos = height/3 - (height / 12);
	           
	           //vertical
	           for (int i = 0; i < 2; i++) {                  
	                      
	               paint.setColor(Color.BLACK);
	               
	               canvas.drawLine(xpos +(xpos*i), 0, xpos +(xpos*i), height, paint);              
	               //canvas.drawLine(startX, startY, stopX, stopY, paint)              

	           }             
	           //horizontal
	            paint.setStyle(Style.STROKE);
	               for (int i = 1; i < 3; i++) {
	            	   pass++;
	                   paint.setColor(Color.BLACK);
//	                   if (i == 1)
//		            	   paint.setColor(Color.RED);
//		               if (i == 2)
//		            	   paint.setColor(Color.GREEN);
	                   canvas.drawLine(0, (ypos*pass)+ 5, width, (ypos*pass)+5, paint);      
	                   //pass++;
	                                 
	               }               
	       }
	}
	
}
