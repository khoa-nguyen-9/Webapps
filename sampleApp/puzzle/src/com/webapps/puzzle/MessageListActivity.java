package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import library.MessageFunctions;
import library.UserFunctions;
import library.UserInfoFunctions;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MessageListActivity extends ListActivity {

	ListView userList;
	Button submit;

	private static String KEY_SUCCESS = "success";
	private static String KEY_MID = "mid";
	private static String KEY_SENDER = "sender";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_MCONTENT = "mcontent";

	private static String KEY_USERNAME = "username";  

	User[] friends;
	User currentUser;

	protected DatabaseHandler dbHandler;
	private HashMap<String, String> user;

	String[] listContent = {};

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHandler = new DatabaseHandler(getApplicationContext());
		user = dbHandler.getUserDetails();
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting user info ...");
		GetMessageTask g = new GetMessageTask(
				Integer.parseInt(user.get("uid")), progressDialog);
		g.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent messagePage = new Intent(getApplicationContext(),
				NewMessageActivity.class);
		messagePage.putExtra("friend",friends[position]);
		startActivity(messagePage);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			new UserFunctions().logoutUser(this);
			Intent login = new Intent(this, LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			finish();
			return true;
		case R.id.how_to_play:
			Intent howTo = new Intent(getApplicationContext(),
					HowToPlayActivity.class);
			startActivity(howTo);
			return true;
		case R.id.action_settings:
			Intent settings = new Intent(getApplicationContext(),
					Settings.class);
			startActivity(settings);
			return true;
		case R.id.about:
			Intent about = new Intent(getApplicationContext(),
					AboutScreen.class);
			startActivity(about);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// AsyncTask to get friends from database
	private class GetMessageTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;

		public GetMessageTask(int uid, ProgressDialog progressDialog) {
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
						JSONObject json_userinfo = json.getJSONObject("userinfo");
						String [] tokens = json_userinfo.getString(KEY_FRIENDS).split("\\s");
						Set<Integer> friend = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							friend.add(Integer.parseInt(tokens[i+1]));
						}
						int count = 0;
						friends = new User[friend.size()];
						listContent= new String [friend.size()];
						for (Integer i : friend) {
							json = userinfoFunction.getUserInfoByID(Integer.toString(i));
							json_userinfo = json.getJSONObject("userinfo");
							String name = json_userinfo.getString(KEY_USERNAME);
							User f = new User(i, null, null, null, null, null, null, null, null, 0, name);
							friends[count] = f;
							listContent[count] = f.getUsername(); 
							count++;
						}
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
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_list_item_1, listContent);
			setListAdapter(adapter);
		}
	}
}
