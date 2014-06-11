package com.webapps.puzzle;

import java.util.HashSet;
import java.util.Set;

import library.UserInfoFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LeaderBoardFragment extends ListFragment {
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_ANSWERED = "answered";
	private static String KEY_CHECKED = "checked";
	private static String KEY_CHINTS = "chints";
	private static String KEY_CHALLENGE = "challenge";
	private static String KEY_FREQUEST = "frequest";
	private static String KEY_LREQUEST = "lrequest";
	private static String KEY_CREDITS = "credits";
	private static String KEY_REQUESTER = "requester";
	private static String KEY_USERNAME = "username";
	
	private User[] users;
	String[] values;
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	     
	    ProgressDialog progressDialog = new ProgressDialog(getActivity());
	    progressDialog.setMessage("Loading users ...");
	    GetAllUsersTask g = new GetAllUsersTask(progressDialog); 
	    g.execute();
	    
	  }
	
	/*@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		ProgressDialog progressDialog = new ProgressDialog(getActivity());
	    progressDialog.setMessage("Loading users ...");
	    GetAllUsersTask g = new GetAllUsersTask(progressDialog); 
	    g.execute();
	}*/
	
	
	 @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  Intent userpage = new Intent(getActivity().getApplicationContext(), 
				  UserPageActivity.class);
		  
		  //String item = (String) getListAdapter().getItem(position);
		  User u = users[position];
		  userpage.putExtra("user", u);
		  startActivity(userpage);
	 }
	
	// AsyncTask to get a location from database 
	private class GetAllUsersTask extends AsyncTask<String, Void, Integer> {
		
		private int responseCode = 0;
		private ProgressDialog progressDialog;
		
		public GetAllUsersTask(ProgressDialog progressDialog) {
			this.progressDialog = progressDialog;
		}
		
		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userinfoFunction = new UserInfoFunctions();
			JSONObject json = userinfoFunction.getAllUserInfo();
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONArray json_users = json.getJSONArray("userinfos");
						users = new User[json_users.length()];
						for (int i = 0; i < json_users.length(); i++) {
							JSONObject json_userinfo = json_users.getJSONObject(i);
							//Log.v("name", json_user.getString(KEY_NAME));
							int uid = Integer.parseInt(json_userinfo.getString(KEY_UID));
							String[] tokens = json_userinfo.getString(KEY_FRIENDS).split("\\s");
							Set<Integer> friends = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								friends.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo.getString(KEY_ANSWERED).split("\\s");
							Set<Integer> answered = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								answered.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo.getString(KEY_CHECKED).split("\\s");
							Set<Integer> checked = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								checked.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo.getString(KEY_CHINTS).split("\\s");
							Set<Integer> chints = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								chints.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo.getString(KEY_CHALLENGE).split("\\s");
							Set<Integer> challenge = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								challenge.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo.getString(KEY_FREQUEST).split("\\s");
							Set<Integer> frequest = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								frequest.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo.getString(KEY_LREQUEST).split("\\s");
							int [] lrequest = new int[tokens.length - 1];
							for (int j = 0; j < lrequest.length; j++) {
								lrequest[j] = Integer.parseInt(tokens[j+1]);
							}
							tokens = json_userinfo.getString(KEY_REQUESTER).split("\\s");
							int []requester = new int[tokens.length - 1];
							for (int j = 0; j < requester.length; j++) {
								requester[j] = Integer.parseInt(tokens[j+1]);
							}
							
							String username = json_userinfo.getString(KEY_USERNAME);
							int credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
							users[i] = new User(uid, friends, answered, checked, chints, challenge, frequest, lrequest, requester, credits, username);
						}
						responseCode = 1;
					}else{
						responseCode = 0;
						// Error in login
					}
				}

			} catch (NullPointerException e) {
				e.printStackTrace();

			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			
			sortUsers();
			values = new String[users.length];
		    for(int i = 0; i<values.length; ++i){
		    	values[i] = users[i].getUsername() + "\t" 
		    + users[i].getCredits() + "\t" + users[i].getChecked().size();
		    }
		    

			return responseCode;
		}
		
		@Override
		public void onPostExecute(Integer responseCode){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			        android.R.layout.simple_list_item_1, values);
			    setListAdapter(adapter);
			progressDialog.dismiss();
		}
		
		
		private void sortUsers(){
			for (int i = 0; i<users.length-1; i++) {
				for (int j = i+1; j < users.length; j++) {
					if ((users[i].getCredits() == users[j].getCredits()) && 
							(users[i].getChecked().size() < users[j].getChecked().size()) 
							|| (users[i].getCredits() < users[j].getCredits())){
						User s = users[i];
						users[i] = users[j];
						users[j] = s;
					}
				}
			}
		}
		
	}
}
