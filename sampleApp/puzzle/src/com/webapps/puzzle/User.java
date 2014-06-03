package com.webapps.puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import library.LocationFunctions;
import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class User {
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_ANSWERED = "answered";
	private static String KEY_CHECKED = "checked";
	private static String KEY_CHINTS = "chints";
	private static String KEY_CHALLENGE = "challenge";
	private static String KEY_FREQUEST = "frequest";
	private static String KEY_LREQUEST = "lrequest";
	private static String KEY_CREDITS = "credits";
	
	private String userName;						// User Name
	
	private int uid;
	private Set<Integer> friends;				// List of ID friends of the user
	private Set<Integer> answered;	            // List of question IDs the user answered
	private Set<Integer> checked;	            // List of location IDs the user has checked in
	private Set<Integer> chints; 			    // List of hint IDs the user has obtained
	private Set<Integer> challenge;			// List of question IDs the user has been challenged
	private Set<Integer> frequest;               // List of user IDs friend request
	private Set<Integer> lrequest;               // List of location requested by other;
	private int credits;
	
	// TODO: Add function to lookup the IDs from the current userID
	public User (int userID, ProgressDialog progressDialog) {
		GetUserInfoTask g = new GetUserInfoTask(userID, progressDialog);
		g.execute();
		this.uid = userID;
	}
	
	// // TODO: Add function to lookup the user data from the current UserID
	public void updateUserChints(int hid) {
		UpdateUserChintsTask u = new UpdateUserChintsTask(uid, hid);
		u.execute();
	}
	
	// Return userID 
	public int getUserID () {
		return uid;
	}
	
	// Return the userName 
	public String getUserName() {
		return userName;
	}
	
	// Return the friend IDs 
	public Set<Integer> getFriends() {
		return friends;
	}
	
	// Return the answered question IDs 
	public Set<Integer> getAnswered() {
		return answered;
	}
	
	// Return the checked location IDs 
	public Set<Integer> getChecked() {
		return checked;
	}
	
	// Return the checked hints IDs 
	public Set<Integer> getChint() {
		return chints;
	}
	
	// Return the challenge question IDs 
	public Set<Integer> getChallenge() {
		return challenge;
	}
	
	// Return the IDs of the user who sent friend request
	public Set<Integer> getFriendRequest() {
		return frequest;
	}
	
	// Return the IDs of the location request
	public Set<Integer> getLocationRequest() {
		return lrequest;
	}
	
	// Return the credits of the user
	public int getCredits() {
		return credits;
	}
	
	// Add credits
	public void addCredits(int amount){
		this.credits += amount;
	}
	
	// Minus credits
	public void minusCredits(int amount) {
		this.credits  -= amount;
	}
	
	// AsyncTask to get a location from database 
	private class GetUserInfoTask extends AsyncTask<String, Void, Integer> {
			
		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;
	
		public GetUserInfoTask(int uid, ProgressDialog progressDialog)
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
						friends = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							friends.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_ANSWERED).split("\\s");
						answered = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							answered.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_ANSWERED).split("\\s");
						answered = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							answered.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_CHECKED).split("\\s");
						checked = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							checked.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_CHINTS).split("\\s");
						chints = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							chints.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_CHALLENGE).split("\\s");
						challenge = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							challenge.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_FREQUEST).split("\\s");
						frequest = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							frequest.add(Integer.parseInt(tokens[i+1]));
						}
						tokens = json_userinfo.getString(KEY_LREQUEST).split("\\s");
						lrequest = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							lrequest.add(Integer.parseInt(tokens[i+1]));
						}
						
						credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
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
		}
	}
	
	// AsyncTask to update friend IDs from database 
	private class UpdateUserFriendsTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String friendid;
		
		private int responseCode = 0;

		public UpdateUserFriendsTask(int uid, int friendid)
		{
			this.uid = Integer.toString(uid);
			this.friendid = Integer.toString(friendid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateFriends(uid, friendid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_FRIENDS).split("\\s");
						friends = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							friends.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update answered question IDs from database 
	private class UpdateUserAnsweredTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String answeredid;
		
		private int responseCode = 0;

		public UpdateUserAnsweredTask(int uid, int answeredid)
		{
			this.uid = Integer.toString(uid);
			this.answeredid = Integer.toString(answeredid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateAnswered(uid, answeredid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_ANSWERED).split("\\s");
						answered = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							answered.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update checked location IDs from database 
	private class UpdateUserCheckedTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String checkedid;
		
		private int responseCode = 0;

		public UpdateUserCheckedTask(int uid, int checkedid)
		{
			this.uid = Integer.toString(uid);
			this.checkedid = Integer.toString(checkedid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChecked(uid, checkedid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_CHECKED).split("\\s");
						checked = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							checked.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update checked hint IDs from database 
	private class UpdateUserChintsTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String chintid;
		
		private int responseCode = 0;

		public UpdateUserChintsTask(int uid, int chintid)
		{
			this.uid = Integer.toString(uid);
			this.chintid = Integer.toString(chintid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChints(uid, chintid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_CHINTS).split("\\s");
						chints = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							chints.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update checked location IDs from database 
	private class UpdateUserChallengeTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String questid;
		
		private int responseCode = 0;

		public UpdateUserChallengeTask(int uid, int questid)
		{
			this.uid = Integer.toString(uid);
			this.questid = Integer.toString(questid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChallenge(uid, questid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_CHALLENGE).split("\\s");
						challenge = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							challenge.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update checked location IDs from database 
	private class UpdateUserFrequestTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String friendid;
		
		private int responseCode = 0;

		public UpdateUserFrequestTask(int uid, int friendid)
		{
			this.uid = Integer.toString(uid);
			this.friendid = Integer.toString(friendid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChallenge(uid, friendid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_FREQUEST).split("\\s");
						frequest = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							frequest.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update checked location IDs from database 
	private class UpdateUserLrequestTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String lid;
		
		private int responseCode = 0;

		public UpdateUserLrequestTask(int uid, int lid)
		{
			this.uid = Integer.toString(uid);
			this.lid = Integer.toString(lid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChallenge(uid, lid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_LREQUEST).split("\\s");
						lrequest = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							lrequest.add(Integer.parseInt(tokens[i+1]));
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
	}
	
	// AsyncTask to update checked location IDs from database 
	private class UpdateUserCreditsTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private String newcredits;
		
		private int responseCode = 0;

		public UpdateUserCreditsTask(int uid, int newcredits)
		{
			this.uid = Integer.toString(uid);
			this.newcredits = Integer.toString(newcredits);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChecked(uid, newcredits);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
						responseCode = 1;
					}else{
							responseCode = 0;
							// Error in login
						}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return responseCode;
		}
	}
	
	// Async task to add a new location to database
	private class AddUserTask extends AsyncTask<String, Void, Integer> {
		private String uid;
		private int responseCode = 0;
		
		public AddUserTask(int uid)
		{
			this.uid = Integer.toString(uid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userinfoFunction = new UserInfoFunctions();
			JSONObject json = userinfoFunction.addUser(uid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
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

	}
	
}
