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
		lvLeaderboard.setAdapter(mAdapter);		
		refreshPlayerRecords();
	}
	
	private void refreshPlayerRecords() {
		TicTacToeApplication myApp = (TicTacToeApplication) getActivity().getApplication();
		TicTacToeService tttService = myApp.getTicTacToeService();
		
		tttService.getPlayerRecords(new TableQueryCallback<PlayerRecord>() {
			
			@Override
			public void onCompleted(List<PlayerRecord> results, int count,
					Exception exception, ServiceFilterResponse response) {
				
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
