package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import library.DatabaseHandler;
import library.LocationFunctions;
import library.UserInfoFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserPageActivity extends Activity {
	
	private User friend;
	
	
	protected DatabaseHandler dbHandler;
    private HashMap<String, String> user;
    
    private User currentUser;
    private Place[] places;
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_CHECKED = "checked";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";    
	private static String KEY_FREQUEST = "frequest";
	private static String KEY_X = "x";
	private static String KEY_Y = "y";
	private static String KEY_LNAME = "lname";
	private static String KEY_LID = "lid";
	    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);
        friend = (User) getIntent().getSerializableExtra("friend");
        
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting user info ...");
        dbHandler = new DatabaseHandler(getApplicationContext());
        user = dbHandler.getUserDetails();
        GetUserInfoTask task = new GetUserInfoTask(user.get("uid"), progressDialog);
        task.execute();

	}
	
	// AsyncTask to get a location from database 
	private class GetUserInfoTask extends AsyncTask<String, Void, Integer> {
			
		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;
	
		public GetUserInfoTask(String uid, ProgressDialog progressDialog)
		{
			this.uid = uid;
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
						String[] tokens = json_userinfo.getString(KEY_CHECKED).split("\\s");
						Set<Integer> checked = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							checked.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_FRIENDS).split("\\s");
						Set<Integer> friends = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							friends.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_FREQUEST).split("\\s");
						Set<Integer> frequests = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							frequests.add(Integer.parseInt(tokens[i+1]));
						}
						String username = json_userinfo.getString(KEY_USERNAME);
						int credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
						currentUser = new User(Integer.parseInt(uid), friends, null, checked, null, null, frequests, null, null, credits, username);
						LocationFunctions locationFunction = new LocationFunctions();
						JSONObject json2 = locationFunction.getLocations();
						JSONArray json_locations = json2.getJSONArray("locations");
						int count = 0;
						for (int i = 0; i < json_locations.length(); i++) {
							JSONObject json_location = json_locations.getJSONObject(i);
							int lid = Integer.parseInt(json_location.getString(KEY_LID));
							if (friend.getChecked().contains(lid)) {
								count++;
							}
						}
						places = new Place[count];
						count=0;
						for (int i = 0; i < json_locations.length(); i++) {
							JSONObject json_location = json_locations.getJSONObject(i);
							int lid = Integer.parseInt(json_location.getString(KEY_LID));
							if (friend.getChecked().contains(lid)) {
								float x = Float.parseFloat(json_location.getString(KEY_X));
								float y = Float.parseFloat(json_location.getString(KEY_Y));
								String name = json_location.getString(KEY_LNAME);
								places[count] = new Place(lid, x, y, name);
								count++;
							}
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
	        Intent intent = getIntent();
	        
	        
	        
	        TextView tv = (TextView) findViewById(R.id.userText);
	        tv.setText("User name: " + friend.getUsername() );
	        
	        TextView points = (TextView) findViewById(R.id.textView12);
	        points.setText("Number of points: " + friend.getCredits());
	        
	        
	        
	        Button add = (Button) findViewById(R.id.add_as_friend);
	        if(currentUser.getFriends().contains(friend.getUserID()) ||
	        		currentUser.getFriendRequest().contains(friend.getUserID())){
	        	add.setVisibility(View.INVISIBLE);
	        }else{
	        	add.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						friend.updateUserFrequest(currentUser.getUserID());
						Toast.makeText(UserPageActivity.this, 
						"Friend request sent!", Toast.LENGTH_LONG).show();
						Button add = (Button) findViewById(R.id.add_as_friend);
								add.setVisibility(View.INVISIBLE);
					}
		        	
		        });
	        }
	        
	        Button message = (Button) findViewById(R.id.messageUser);
	        message.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent message = new Intent(getApplicationContext(), 
							NewMessageActivity.class);
					message.putExtra("friend", friend);
					startActivity(message);
					
				}
	        	
	        });
	        
	       final int friendPlaces = places.length;
	        final Place[] placesHolder = places;
	        Button places = (Button) findViewById(R.id.placesVisited);
	        places.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(friendPlaces == 0){
						Toast.makeText(getApplicationContext(),
								"No places to show.", 
								Toast.LENGTH_LONG).show();
					}else {
						Intent map = 
						new Intent(UserPageActivity.this.getApplicationContext(),
												MapActivity.class);
						map.putExtra("mode", "friendPlaces");
						map.putExtra("numberPlaces", friendPlaces);
								
						for(int i = 0; i < friendPlaces; i++){
							map.putExtra("place" + i, placesHolder[i]);
						}
						startActivity(map);
					}
				}
			});
	        
		}
	}
}
