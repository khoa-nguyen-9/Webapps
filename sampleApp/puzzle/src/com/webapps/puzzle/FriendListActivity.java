package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import library.DatabaseHandler;
import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendListActivity extends ListActivity{
	
	
	String[] listContent;
	
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
	
	User[] friends;
	User currentUser;
	
	protected DatabaseHandler dbHandler;
	private HashMap<String, String> user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_ask);
        dbHandler = new DatabaseHandler(getApplicationContext());
        user = dbHandler.getUserDetails();
        ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting user info ...");
		GetUserFriendsTask g = new GetUserFriendsTask(Integer.parseInt(user.get("uid")), progressDialog);
        g.execute();
        
        
	}
	
	@Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  Intent userpage = new Intent(getApplicationContext(), 
				  UserPageActivity.class);
		  User u = friends[position];
		  userpage.putExtra("friend", u);
		  startActivity(userpage);
	 }
	
	// AsyncTask to get friends from database 
	private class GetUserFriendsTask extends AsyncTask<String, Void, Integer> {
			
		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;
	
		public GetUserFriendsTask(int uid, ProgressDialog progressDialog)
		{
			this.uid = Integer.toString(uid);
			this.progressDialog = progressDialog;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}
		
		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userinfoFunction = new UserInfoFunctions();
			JSONObject json = userinfoFunction.getUserInfoByID(uid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_FRIENDS).split("\\s");
						Set<Integer> friendIDs = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							friendIDs.add(Integer.parseInt(tokens[i+1]));
						}
						currentUser = new User(Integer.parseInt(uid), friendIDs, null, null, null, null, null, null, null, 10, null);
						friends = new User[friendIDs.size()];
						int count = 0;
						listContent = new String[friendIDs.size()];
						for (Integer i : friendIDs) {
							JSONObject json2 = userinfoFunction.getUserInfoByID(i.toString());
							JSONObject json_userinfo2 = json2.getJSONObject("userinfo");
							int uid = Integer.parseInt(json_userinfo2.getString(KEY_UID));
							tokens = json_userinfo2.getString(KEY_FRIENDS).split("\\s");
							Set<Integer> friend = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								friend.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo2.getString(KEY_ANSWERED).split("\\s");
							Set<Integer> answered = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								answered.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo2.getString(KEY_CHECKED).split("\\s");
							Set<Integer> checked = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								checked.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo2.getString(KEY_CHINTS).split("\\s");
							Set<Integer> chints = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								chints.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo2.getString(KEY_CHALLENGE).split("\\s");
							Set<Integer> challenge = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								challenge.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo2.getString(KEY_FREQUEST).split("\\s");
							Set<Integer> frequest = new HashSet<Integer>();
							for (int j = 0; j < tokens.length-1; j++) {
								frequest.add(Integer.parseInt(tokens[j+1]));
							}
							tokens = json_userinfo2.getString(KEY_LREQUEST).split("\\s");
							int [] lrequest = new int[tokens.length - 1];
							for (int j = 0; j < lrequest.length; j++) {
								lrequest[j] = Integer.parseInt(tokens[j+1]);
							}
							tokens = json_userinfo2.getString(KEY_REQUESTER).split("\\s");
							int []requester = new int[tokens.length - 1];
							for (int j = 0; j < requester.length; j++) {
								requester[j] = Integer.parseInt(tokens[j+1]);
							}
							
							String username = json_userinfo2.getString(KEY_USERNAME);
							int credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
							User f = new User(uid, friend, answered, checked, chints, challenge, frequest, lrequest, requester, credits, username);
							friends[count] = f;
							listContent[count] = f.getUsername(); 
				            count++;
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
				
			return responseCode;
		}

		@Override
		protected void onPostExecute(Integer responseCode)
		{
			
			progressDialog.dismiss();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
			        android.R.layout.simple_list_item_1, listContent);
			    setListAdapter(adapter);
		}
	}
}
