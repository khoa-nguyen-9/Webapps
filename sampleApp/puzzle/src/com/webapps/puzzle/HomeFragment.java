package com.webapps.puzzle;

import java.util.HashMap;

import library.DatabaseHandler;
import library.UserFunctions;
import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {
	
	Button btnLogout;
    DatabaseHandler dbHandler;
    private HashMap<String, String> user;
    
    private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";
    
	User currentUser;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = inflater.inflate(R.layout.dashboard, container, false);
		
		btnLogout = (Button) view.findViewById(R.id.btnLogout);
        dbHandler = new DatabaseHandler(getActivity().getApplicationContext());
        user = dbHandler.getUserDetails();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Getting user info ...");
        GetUserInfoTask task = new GetUserInfoTask(user.get("uid"), progressDialog);
        task.execute();
        TextView emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        emailTextView.setText("Hi, " + user.get("name") + ". Let's play.");
        
        
        
        
        btnLogout.setOnClickListener(this);
        Button discover = (Button) view.findViewById(R.id.discover);
        discover.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent places = new Intent(getActivity(), MapActivity.class);
				places.putExtra("mode", "places");
				startActivity(places);
			}
        	
        });
        
        Button friends = (Button) view.findViewById(R.id.friendsButton);
        friends.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent friendList = 
						new Intent(getActivity(),FriendListActivity.class);
				//friendList.putExtra("user",user);
				startActivity(friendList);
				
			}
        	
        });
        
        
        Button messages = (Button) view.findViewById(R.id.messagesButton);
        messages.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent message = new Intent(getActivity(), MessageListActivity.class);
				//message.putExtra("user",user);
				startActivity(message);
				
			}
        	
        });
		
        
		return view;
	}

	@Override
	public void onClick(View v) {
        new UserFunctions().logoutUser(getActivity());
        Intent login = new Intent(getActivity(), LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        // Closing dashboard screen
        getActivity().finish();
    }
	
	// AsyncTask to get friends from database 
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
						int credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
						String username = json_userinfo.getString(KEY_USERNAME);
						currentUser = new User(Integer.parseInt(uid), null, null, null, null, null, null, null, null, credits, username);
						
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
			TextView credits = (TextView) view.findViewById(R.id.creditsTotal);
			credits.setText("Credits: " + currentUser.getCredits() );
			
			progressDialog.dismiss();
		}
	}
}
