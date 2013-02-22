package com.msdpe.tictactoe_leaderboard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class PlayerRecordAdapter extends ArrayAdapter<PlayerRecord> {

	private Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;

	public PlayerRecordAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}

	/**
	 * Returns the view for a specific item on the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		final PlayerRecord currentItem = getItem(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}

		row.setTag(currentItem);
		final TextView lblPlayerName = (TextView) row.findViewById(R.id.lblPlayerName);
		final TextView lblWins = (TextView) row.findViewById(R.id.lblWins);
		final TextView lblLosses = (TextView) row.findViewById(R.id.lblLosses);
		final TextView lblTies = (TextView) row.findViewById(R.id.lblTies);
		
		lblPlayerName.setText(currentItem.getPlayerName());
		lblWins.setText(String.valueOf(currentItem.getWins()));
		lblLosses.setText(String.valueOf(currentItem.getLosses()));
		lblTies.setText(String.valueOf(currentItem.getTies()));
		
		return row;
	}
}
