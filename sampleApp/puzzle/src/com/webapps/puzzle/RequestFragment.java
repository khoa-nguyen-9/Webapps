package com.webapps.puzzle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RequestFragment extends ListFragment {
	/*public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_requests, container, false);
		return view;
				
	}*/
	
	//List<Request> requests = new ArrayList<Requests>();
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    String[] values = new String[] { "Friend requests", "Hint help",
	    		"Questions by friends", "Messages"};
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  
		  switch(position){
			 case 0:
				  Intent friendRequests = 
				  new Intent(getActivity().getApplicationContext(),
						                               FriendRequestActivity.class);
				  startActivity(friendRequests);
				  break;
			 case 1:
				 Intent hintLocations = 
				  new Intent(getActivity().getApplicationContext(),
						                               HintLocations.class);
				 startActivity(hintLocations);
				 break;
			 case 2:
				 Intent friendQuestions = new Intent(getActivity().getApplicationContext(),
				   QuestionsByFriends.class);
				 startActivity(friendQuestions);
				 break;
			 case 3:
				 Intent message = new Intent(getActivity().getApplicationContext(),
				   MessageListActivity.class);
				 startActivity(message);
				 break;
		  }
		  
	  }
}
