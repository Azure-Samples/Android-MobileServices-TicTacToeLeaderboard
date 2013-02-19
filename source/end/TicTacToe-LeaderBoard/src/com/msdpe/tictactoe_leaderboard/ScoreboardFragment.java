package com.msdpe.tictactoe_leaderboard;

import java.util.List;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.app.AlertDialog;
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
	private PlayerRecordAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.fragment_leaderboard, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		lvLeaderboard = (ListView) this.getView().findViewById(R.id.lvLeaderboard);
		View headerView = this.getActivity().getLayoutInflater().inflate(R.layout.leaderboard_header, null);
		lvLeaderboard.addHeaderView(headerView);
		
		
		mAdapter = new PlayerRecordAdapter(getActivity(), R.layout.row_list_player_record);
		//ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
		//listViewToDo.setAdapter(mAdapter);
		lvLeaderboard.setAdapter(mAdapter);
		
		refreshPlayerRecords();
		
		//Static array of names for now
//		String[] names = new String[] { "Player One", "Player 2", "Player 3", "Player 4",
//		        "Player 5", "Player 6", "Player 7", "Player 8", "Player 9", "Player 10",
//		        "Player 11", "Player 12", "Player 13", "Player 14", "Player 15", "Player 16" };
//		
//		lvLeaderboard.setAdapter(new ArrayAdapter<String>(this.getActivity(),
//		        android.R.layout.simple_list_item_single_choice,
//		        android.R.id.text1, names));
	}
	
	private void refreshPlayerRecords() {
		TicTacToeApplication myApp = (TicTacToeApplication) getActivity().getApplication();
		TicTacToeService tttService = myApp.getTicTacToeService();
		
		tttService.getPlayerRecords(new TableQueryCallback<PlayerRecord>() {
			
			@Override
			public void onCompleted(List<PlayerRecord> results, int count,
					Exception exception, ServiceFilterResponse response) {
				// TODO Auto-generated method stub
				
//				lvLeaderboard.setAdapter(new ArrayAdapter<PlayerRecord>(getActivity(),
//				        android.R.layout.simple_list_item_single_choice,
//				        android.R.id.text1, results));
				
				if (exception == null) {
					mAdapter.clear();

					for (PlayerRecord item : results) {
						mAdapter.add(item);
					}

				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(exception.getMessage());
					builder.setTitle("Error");
					builder.create().show();
				}
			}
		});
	}
}
