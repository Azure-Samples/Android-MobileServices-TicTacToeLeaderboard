package com.msdpe.tictactoe_leaderboard;

import android.app.Application;

public class TicTacToeApplication extends Application {
	
	private TicTacToeService mTicTacToeService;
	
	public TicTacToeApplication() {
		
	}
	
	public TicTacToeService getTicTacToeService() {
		if (mTicTacToeService == null) {
			mTicTacToeService = new TicTacToeService(this);
		}
		return mTicTacToeService;
	}
}
