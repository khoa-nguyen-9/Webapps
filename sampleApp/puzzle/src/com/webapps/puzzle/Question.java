package com.webapps.puzzle;

import java.util.ArrayList;
import java.util.HashMap;

import library.DatabaseHandler;
import library.JSONParser;
import library.QuestionFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class Question {
	private User maker;				// The user who make the question
	private ArrayList<Hint> hints;  // The list of hints for the question
	private String content;			// The content of the question
	private int ranking; 			// The ranking of the question on leaderboard
	private String answer;			// The answer of the question
	private int rewards;			// The rewards of the question
	
	private static String loginURL = "http://khoatestsite.hostoi.com/android_api/";
	private static String registerURL = "http://khoatestsite.hostoi.com/android_api/";
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_QID = "qid";
	private static String KEY_MAKER = "maker";
	private static String KEY_QCONTENT = "qcontent";
	private static String KEY_QRANKING = "qranking";
	private static String KEY_ANSWER = "answer";
	private static String KEY_REWARDS = "rewards";
	HashMap<String, String> map;
	
	private int[] hintIDs;			// The ID of the hints for the question
	
	public Question(User maker, ArrayList<Hint> hints, String content, String answer, int reward) {
		this.maker = maker;
		this.hints = hints;
		this.content = content;
		this.answer = answer;
		this.rewards = reward;
	}
	
	// TODO: Add function to lookup the hintIDs from the current questionID
	public Question(int questID, QuestionActivity q) {
		// creating new HashMap
		map = new HashMap<String, String>();
		
		ProgressDialog progressDialog = new ProgressDialog(q);
		progressDialog.setMessage("Loading ...");
		QuestionTask questionTask = new QuestionTask(progressDialog, q);
		questionTask.execute();
		
		hints = new ArrayList<Hint>();
	}
	
	public Question(int questID) {
		
	}
	
	// Look up the table and update the hints of the question
	public void updateHints() {
		hints = new ArrayList<Hint>();
		for (int i=0; i < hintIDs.length; i++ ) {
			hints.add(new Hint (hintIDs[i]));
		}
	}
	
	// Get the user who made the question
	public User getMaker() {
		return maker;
	}
	
	// Get the content of the question
	public String getContent() {
		return content;
	}
	
	// Get the rank of the question on leader board
	public int getRank() {
		return ranking;
	}
	
	// Get list of hints for the question
	public  ArrayList<Hint> getHints() {
		return hints;
	}
	
	// Get the answer of the question
	public String getAnswer() {
		return answer;
	}
	
	// Get the rewards for the question
	public int getRewards() {
		return rewards;
	}

	private class QuestionTask extends AsyncTask<String, Void, Integer> {

		private ProgressDialog progressDialog;
		private QuestionActivity activity;
		private JSONParser jsonParser;

		private int responseCode = 0;

		public QuestionTask(ProgressDialog progressDialog,QuestionActivity q)
		{
			this.progressDialog = progressDialog;
			this.activity = q;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}

		protected Integer doInBackground(String... arg0) {
			
			String qid = "1";
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.getQuestion(qid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						//user successfully logged in
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
						JSONObject json_question = json.getJSONObject("question");
						//Log.v("name", json_user.getString(KEY_NAME));
						// Clear all previous data in database
						//questionFunction.logoutUser(activity.getApplicationContext());
						map.put(KEY_QID, json_question.getString(KEY_QID));
						
						responseCode = 1;
						// Close Login Screen
						//finish();

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
			
			if (responseCode == 1) {
				progressDialog.dismiss();
				
			}
			else {
				progressDialog.dismiss();
				//activity.showLoginError(responseCode);
		
			}
			
		}
	}
}


