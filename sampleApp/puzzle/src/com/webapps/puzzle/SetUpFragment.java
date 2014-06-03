package com.webapps.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SetUpFragment extends Fragment {
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = inflater.inflate(R.layout.fragment_setup, container, false);
		
		addHintsOnClickListener();
		challengeFriendsOnClickListener();
		
		return view;
	}

	private void addHintsOnClickListener() {
		Button hints = (Button) view.findViewById(R.id.addhints);
		hints.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent hints = 
			new Intent(SetUpFragment.this.getActivity().getApplicationContext(),
					HintSetup.class);
				startActivity(hints);
				
			}});
	}

	private void challengeFriendsOnClickListener() {
		
		Button friends = (Button) view.findViewById(R.id.challengefriends);
		friends.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent friends = 
			new Intent(SetUpFragment.this.getActivity().getApplicationContext()
					,AskFriendActivity.class); //TODO: logic needed in ask friend activity
				startActivity(friends);
			}
			
		});
		
	}

}
