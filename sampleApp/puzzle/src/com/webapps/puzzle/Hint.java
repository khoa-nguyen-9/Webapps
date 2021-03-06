package com.webapps.puzzle;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import library.HintFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class Hint implements Serializable{
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_HID = "hid";
	private static String KEY_LID = "lid";
	private static String KEY_HCONTENT = "hcontent";
	
	private int hid; 		// The id of the hint
	private int lid;		// The location ID to lookup in the database
	private String content;	// The content of the hint
	
	public Hint (int hid, int lid, String con) {
		this.hid = hid;
		this.lid = lid;
		this.content = con;
	}

	// Add new location to the database, update lid
	public void addLocation(ProgressDialog progressDialog) {
		AddHintTask l = new AddHintTask(progressDialog);
		l.execute();
	}
	
	// Return the location associated with the hint
	public int getLocation() {
		return lid;
	}
	
	// Return the content of the hint
	public String getContent() {
		return content;
	}
	
	// Return the id of the hint
	public int getHintID() {
		return hid;
	}
	
	// AsyncTask to get a location from database 
	private class GetHintTask extends AsyncTask<String, Void, Integer> {
		
		private String hid;
		
		private int responseCode = 0;
		private ProgressDialog progressDialog;
		
		public GetHintTask(int hid, ProgressDialog progressDialog)
		{
			this.hid = Integer.toString(hid);
			this.progressDialog = progressDialog;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}
		
		
		protected Integer doInBackground(String... arg0) {
			
			HintFunctions hintFunction = new HintFunctions();
			JSONObject json = hintFunction.getHint(hid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_hint = json.getJSONObject("hint");
						//Log.v("name", json_user.getString(KEY_NAME));
						lid = Integer.parseInt(json_hint.getString(KEY_LID));
						content = json_hint.getString(KEY_HCONTENT);
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
	
	// Async task to add a new hint to database
	private class AddHintTask extends AsyncTask<String, Void, Integer> {
		
		private int responseCode = 0;
		private ProgressDialog progressDialog;

		public AddHintTask(ProgressDialog progressDialog)
		{
			this.progressDialog = progressDialog;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}
		
		protected Integer doInBackground(String... arg0) {
			
			HintFunctions hintFunction = new HintFunctions();
			JSONObject json = hintFunction.addHint(Integer.toString(lid), content);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_hint = json.getJSONObject("hint");
						hid = Integer.parseInt(json_hint.getString(KEY_HID));
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
	
}
