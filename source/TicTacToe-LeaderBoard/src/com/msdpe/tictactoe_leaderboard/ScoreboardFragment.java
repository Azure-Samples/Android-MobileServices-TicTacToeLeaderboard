package com.msdpe.tictactoe_leaderboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreboardFragment extends Fragment {
	
	private ListView lvLeaderboard;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create a new TextView and set its text to the fragment's section
		// number argument value.
//		TextView textView = new TextView(getActivity());
//		textView.setGravity(Gravity.CENTER);
//		textView.setText("Scoreboard");
//		return textView;
		
		return inflater.inflate(R.layout.fragment_leaderboard, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		lvLeaderboard = (ListView) this.getView().findViewById(R.id.lvLeaderboard);
		View headerView = this.getActivity().getLayoutInflater().inflate(R.layout.leaderboard_header, null);
		lvLeaderboard.addHeaderView(headerView);
		
		String[] names = new String[] { "Player One", "Player 2", "Player 3", "Player 4",
		        "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10",
		        "Player 11", "Player 12", "Player 13", "Player 14", "Player 15", "Player 16" };
		
		lvLeaderboard.setAdapter(new ArrayAdapter<String>(this.getActivity(),
		        android.R.layout.simple_list_item_single_choice,
		        android.R.id.text1, names));
		
	}
}
