package com.msdpe.tictactoe_leaderboard;

public class PlayerRecord {
	@com.google.gson.annotations.SerializedName("ties")
	private int mTies;

	@com.google.gson.annotations.SerializedName("losses")
	private int mLosses;
	
	@com.google.gson.annotations.SerializedName("wins")
	private int mWins;

	@com.google.gson.annotations.SerializedName("id")
	private int mId;
	
	@com.google.gson.annotations.SerializedName("playerName")
	private String mPlayerName;
	
	@com.google.gson.annotations.SerializedName("status")
	private String mStatus;

	public PlayerRecord() {

	}
	
	public PlayerRecord(String playerName, String status) {
		mPlayerName = playerName;
		mStatus = status;
	}

	@Override
	public String toString() {
		return getPlayerName();
	}

	public PlayerRecord(String playerName, int id, int wins, int losses, int ties, String status) {
		this.setPlayerName(playerName);
		this.setId(id);
		this.setWins(wins);
		this.setLosses(losses);
		this.setTies(ties);
		this.setStatus(status);
	}

	public String getPlayerName() {
		return mPlayerName;
	}
	
	public final void setPlayerName(String playerName) {
		mPlayerName = playerName;
	}
	
	public String getStatus() { return mStatus; }
	public void setStatus(String status) { mStatus = status; }
	
	public int getWins() { return mWins; }
	public void setWins(int wins) { mWins = wins; }
	
	public int getLosses() { return mLosses; }
	public void setLosses(int losses) { mLosses = losses; }
	
	public int getTies() { return mTies; }
	public void setTies(int ties) { mTies = ties; }

	public int getId() {
		return mId;
	}

	public final void setId(int id) {
		mId = id;
	}


	@Override
	public boolean equals(Object o) {
		return o instanceof PlayerRecord && ((PlayerRecord) o).mId == mId;
	}
}
