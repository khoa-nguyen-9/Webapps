package com.webapps.puzzle;

import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

public class ViewMessageActivity extends Activity {

	private static String KEY_SUCCESS = "success";
	private static String KEY_USERNAME = "username";

	User sender;
	Message message;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		message = (Message) getIntent().getSerializableExtra("message");
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting user info ...");
		GetMessageDetailTask g = new GetMessageDetailTask(message.getSender(), progressDialog);
		g.execute();
	}

	// AsyncTask to get friends from database
	private class GetMessageDetailTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;

		public GetMessageDetailTask(int uid, ProgressDialog progressDialog) {
			this.uid = Integer.toString(uid);
			this.progressDialog = progressDialog;
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		protected Integer doInBackground(String... arg0) {

			UserInfoFunctions userinfoFunction = new UserInfoFunctions();
			JSONObject json = userinfoFunction.getUserInfoByID(uid);

			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if (Integer.parseInt(res) == 1) {
						JSONObject json_userinfo = json
								.getJSONObject("userinfo");
						// Log.v("name", json_user.getString(KEY_NAME));
						sender = new User(Integer.parseInt(uid), null, null, null, null, null,
								null, null, null, 0,
								json_userinfo.getString(KEY_USERNAME));
						responseCode = 1;
					} else {
						responseCode = 0;
						// Error in login
					}
				}

			} catch (NullPointerException e) {
				e.printStackTrace();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return responseCode;
		}

		@Override
		protected void onPostExecute(Integer responseCode) {

			progressDialog.dismiss();
		}
	}

}
