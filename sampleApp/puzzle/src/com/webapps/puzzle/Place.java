package com.webapps.puzzle;

import library.LocationFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class Place {
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_LID = "lid";
	private static String KEY_X = "x";
	private static String KEY_Y = "y";
	private static String KEY_LNAME = "lname";
	
	private int lid;
	private float X;
    private float Y;
    private String name;
    
    // Create new location with X and Y coordinate only
	public Place(float x, float y) {
        this.X = x;
        this.Y = y;
    }
	
	// Create new location with name only
	public Place(String name) {
		this.name = name;
	}
	
	// Create new location with X,Y coordinates and name
	public Place(float x, float y, String name) {
		this.X = x;
        this.Y = y;
        this.name = name;
	}
	
	// Look up the table and create new location that has the provided ID
	public Place(int lid, ProgressDialog progressDialog) {
		GetLocationTask l = new GetLocationTask(progressDialog, lid);
		l.execute();
		this.lid = lid;
	}
	
	// Add new location to the database, update lid
	public void addLocation(MapActivity act, ProgressDialog progressDialog) {
		AddLocationTask l = new AddLocationTask(progressDialog, X, Y, name);
		l.execute();
	}
	
	// Get X coordinate
	public float getX() {
		return X;
	}
	
	// Get Y coordinate
	public float getY() {
		return Y;
	}
	
	// Get the name of the location
	public String getName() {
		return name;
	}
	
	// Get the name of the location
	public int getID() {
		return lid;
	}
	
	// AsyncTask to get a location from database 
	private class GetLocationTask extends AsyncTask<String, Void, Integer> {
		
		private ProgressDialog progressDialog;
		private String lid;
		
		private int responseCode = 0;

		public GetLocationTask(ProgressDialog progressDialog, int lid)
		{
			this.progressDialog = progressDialog;
			this.lid = Integer.toString(lid);
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}

		protected Integer doInBackground(String... arg0) {
			
			LocationFunctions locationFunction = new LocationFunctions();
			JSONObject json = locationFunction.getLocation(lid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_location = json.getJSONObject("location");
						//Log.v("name", json_user.getString(KEY_NAME));
						X = Float.parseFloat(json_location.getString(KEY_X));
						Y = Float.parseFloat(json_location.getString(KEY_Y));
						name = json_location.getString(KEY_LNAME);
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
	
	// Async task to add a new location to database
	private class AddLocationTask extends AsyncTask<String, Void, Integer> {
		
		private ProgressDialog progressDialog;
		
		private int responseCode = 0;
		private float X;
		private float Y;
		private String name;

		public AddLocationTask(ProgressDialog progressDialog, float X, float Y, String name)
		{
			this.progressDialog = progressDialog;
			this.X = X;
			this.Y = Y;
			this.name = name;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}

		protected Integer doInBackground(String... arg0) {
			
			LocationFunctions locationFunction = new LocationFunctions();
			JSONObject json = locationFunction.addQuestion(Float.toString(X),Float.toString(Y),name);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_location = json.getJSONObject("location");
						//Log.v("name", json_user.getString(KEY_NAME));
						lid = Integer.parseInt(json_location.getString(KEY_LID));
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
