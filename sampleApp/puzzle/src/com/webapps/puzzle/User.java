package com.webapps.puzzle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class User implements Serializable {
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_ANSWERED = "answered";
	private static String KEY_CHECKED = "checked";
	private static String KEY_CHINTS = "chints";
	private static String KEY_CHALLENGE = "challenge";
	private static String KEY_FREQUEST = "frequest";
	private static String KEY_LREQUEST = "lrequest";
	private static String KEY_REQUESTER = "requester";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";
	
	private String username;						// User Name
	private int uid;
	private Set<Integer> friends;				// List of ID friends of the user
	private Set<Integer> answered;	            // List of question IDs the user answered
	private Set<Integer> checked;	            // List of location IDs the user has checked in
	private Set<Integer> chints; 			    // List of hint IDs the user has obtained
	private Set<Integer> challenge;				// List of question IDs the user has been challenged
	private Set<Integer> frequest;              // List of user IDs friend request
	private int[] lrequest;               		// List of location requested by other
	private int[] requester;					// List of user ID who made the request
	private int credits;
	
	public User (int uid, Set<Integer> friends, Set<Integer> answered, Set<Integer> checked, Set<Integer> chints, Set<Integer> challenge, Set<Integer> frequest, int[] lrequest, int[] requester, int credits, String username) {
		this.uid = uid;
		this.friends = friends;
		this.answered = answered;
		this.checked = checked;
		this.chints = chints;
		this.challenge = challenge;
		this.frequest = frequest;
		this.lrequest = lrequest;
		this.requester = requester;
		this.credits = credits;
		this.username = username;
	}

	public void updateUserFriends(int fid) {
		UpdateUserFriendsTask u = new UpdateUserFriendsTask(fid);
		u.execute();
	}
	
	public void updateUserAnswered(int qid) {
		UpdateUserAnsweredTask u = new UpdateUserAnsweredTask(qid);
		u.execute();
	}
	
	public void updateUserChecked(int lid) {
		UpdateUserCheckedTask u = new UpdateUserCheckedTask(lid);
		u.execute();
	}
	
	public void updateUserChints(int hid) {
		UpdateUserChintsTask u = new UpdateUserChintsTask(hid);
		u.execute();
	}
	
	public void updateUserChallenge(int qid) {
		UpdateUserChallengeTask u = new UpdateUserChallengeTask(qid);
		u.execute();
	}
	
	public void updateUserLrequest(int lid) {
		UpdateUserLrequestTask u = new UpdateUserLrequestTask(lid);
		u.execute();
	}
	
	public void updateUserFrequest(int fid) {
		UpdateUserFrequestTask u = new UpdateUserFrequestTask(fid);
		u.execute();
	}
	
	public void updateUserRequester(int rid) {
		UpdateUserRequesterTask u = new UpdateUserRequesterTask(rid);
		u.execute();
	}
	
	public void updateUserCredits(int credits) {
		UpdateUserCreditsTask u = new UpdateUserCreditsTask(credits);
		u.execute();
	}

	// Return userID 
	public int getUserID () {
		return uid;
	}
	
	// Return the userName 
	public String getUserName() {
		return username;
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
	public int[] getLocationRequest() {
		return lrequest;
	}
	

	// Return the IDs of the location request
	public int[] getRequester() {
		return requester;
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
	
	public String getUsername() {
		return username;
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
						lrequest = new int[tokens.length - 1];
						for (int i = 0; i < lrequest.length; i++) {
							lrequest[i] = Integer.parseInt(tokens[i+1]);
						}
						tokens = json_userinfo.getString(KEY_REQUESTER).split("\\s");
						requester = new int[tokens.length - 1];
						for (int i = 0; i < requester.length; i++) {
							requester[i] = Integer.parseInt(tokens[i+1]);
						}
						username = json_userinfo.getString(KEY_USERNAME);
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

		private String friendid;
		
		private int responseCode = 0;

		public UpdateUserFriendsTask(int friendid)
		{
			this.friendid = Integer.toString(friendid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateFriends(Integer.toString(uid), friendid);
			
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

		private String answeredid;
		
		private int responseCode = 0;

		public UpdateUserAnsweredTask(int answeredid)
		{
			this.answeredid = Integer.toString(answeredid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateAnswered(Integer.toString(uid), answeredid);
			
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

		private String checkedid;
		
		private int responseCode = 0;

		public UpdateUserCheckedTask(int checkedid)
		{
			this.checkedid = Integer.toString(checkedid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChecked(Integer.toString(uid), checkedid);
			
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

		private String chintid;
		
		private int responseCode = 0;

		public UpdateUserChintsTask(int chintid)
		{
			this.chintid = Integer.toString(chintid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChints(Integer.toString(uid), chintid);
			
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

		private String questid;
		
		private int responseCode = 0;

		public UpdateUserChallengeTask(int questid)
		{
			this.questid = Integer.toString(questid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateChallenge(Integer.toString(uid), questid);
			
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

		private String friendid;
		
		private int responseCode = 0;

		public UpdateUserFrequestTask(int friendid)
		{
			this.friendid = Integer.toString(friendid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateFrequest(Integer.toString(uid), friendid);
			
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

		private String lid;
		
		private int responseCode = 0;

		public UpdateUserLrequestTask(int lid)
		{
			this.lid = Integer.toString(lid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateLrequest(Integer.toString(uid), lid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_LREQUEST).split("\\s");
						lrequest = new int[tokens.length-1];
						for (int i = 0; i < tokens.length-1; i++) {
							lrequest[i] = Integer.parseInt(tokens[i+1]);
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
	private class UpdateUserRequesterTask extends AsyncTask<String, Void, Integer> {

		private String lid;
		
		private int responseCode = 0;

		public UpdateUserRequesterTask(int lid)
		{
			this.lid = Integer.toString(lid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateRequester(Integer.toString(uid), lid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						//Log.v("name", json_user.getString(KEY_NAME));
						String[] tokens = json_userinfo.getString(KEY_REQUESTER).split("\\s");
						requester = new int[tokens.length-1];
						for (int i = 0; i < tokens.length-1; i++) {
							requester[i] = Integer.parseInt(tokens[i+1]);
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

		private String newcredits;
		
		private int responseCode = 0;

		public UpdateUserCreditsTask(int newcredits)
		{
			this.newcredits = Integer.toString(newcredits);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userInfoFunction = new UserInfoFunctions();
			JSONObject json = userInfoFunction.updateCredits(Integer.toString(uid), newcredits);
			
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
	public class AddUserTask extends AsyncTask<String, Void, Integer> {
		private String uid;
		private int responseCode = 0;
		
		public AddUserTask(int uid)
		{
			this.uid = Integer.toString(uid);
		}

		protected Integer doInBackground(String... arg0) {
			
			UserInfoFunctions userinfoFunction = new UserInfoFunctions();
			JSONObject json = userinfoFunction.addUser(uid,uid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
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
