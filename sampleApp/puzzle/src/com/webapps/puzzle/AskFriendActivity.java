package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import library.DatabaseHandler;
import library.UserFunctions;
import library.UserInfoFunctions;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AskFriendActivity extends Activity{
	
	ListView friendList;
    Button submit;
 
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_USERNAME = "username";
	private static String KEY_CREDITS = "credits";
	private final int askCredits = 5;
	
	User[] friends;
	User currentUser;

	
	protected DatabaseHandler dbHandler;
	private HashMap<String, String> user;
	
    String[] listContent;
 
    /** Called when the activity is first created. */
 
    @Override
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        dbHandler = new DatabaseHandler(getApplicationContext());
        user = dbHandler.getUserDetails();
        ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting user info ...");
		GetUserFriendsTask g = new GetUserFriendsTask(Integer.parseInt(user.get("uid")), progressDialog);
        g.execute();
        
        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId())
	    {
	    case R.id.logout:
	    	new UserFunctions().logoutUser(this);
	        Intent login = new Intent(this, LoginActivity.class);
	        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(login);
	        finish();
	    	return true;
	    case R.id.how_to_play:
	    	Intent howTo = new Intent(getApplicationContext(), HowToPlayActivity.class);
	        startActivity(howTo);
            return true;
	    case R.id.action_settings:
	    	Intent settings = new Intent(getApplicationContext(), Settings.class);
	        startActivity(settings);
            return true;
	    case R.id.about:
	    	Intent about = new Intent(getApplicationContext(), AboutScreen.class);
	        startActivity(about);
            return true;
        default:
        	break;
	    }
	    return super.onOptionsItemSelected(item);
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
						currentUser = new User(Integer.parseInt(uid), friendIDs, null, null, null, null, null, null, null, Integer.parseInt(json_userinfo.getString(KEY_CREDITS)), null);
						friends = new User[friendIDs.size()];
						int count = 0;
						listContent = new String[friendIDs.size()];
						for (Integer i : friendIDs) {
							JSONObject json2 = userinfoFunction.getUserInfoByID(i.toString());
							JSONObject json_userinfo2 = json2.getJSONObject("userinfo");
							
							User f = new User(i, null, null, null, null, null, null, null, null, Integer.parseInt(json_userinfo2.getString(KEY_CREDITS)), json_userinfo2.getString(KEY_USERNAME));
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
			friendList = (ListView)findViewById(R.id.friendlist);
			 
	        submit = (Button)findViewById(R.id.challengesubmit);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
					 
	                android.R.layout.simple_list_item_multiple_choice, listContent);
		
	        friendList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        friendList.setAdapter(adapter);
	 
	        submit.setOnClickListener(new Button.OnClickListener(){
	 
	            @Override
	            public void onClick(View v) {
	 
	                // TODO Auto-generated method stub
	                String selected = "";
	                int cntChoice = friendList.getCount();
	                SparseBooleanArray sparseBooleanArray = 
	                		                  friendList.getCheckedItemPositions();
	 
	                String ask = "ask";
	                
	                switch(getIntent().getIntExtra("number", 0)){
	                case 0:
	                	break;
	                case 1:
	                	ask += "1";
	                	break;
	                case 2:
	                	ask += "2";
	                	break;
	                case 3:
	                	ask += "3";
	                	default:
	                		break;
	                }
	                
	                int hintId = getIntent().getIntExtra(ask, 0);
	                
	                
	                for(int i = 0; i < cntChoice; i++){
	                    if(sparseBooleanArray.get(i)) {
	                         selected += 
	                              friendList.getItemAtPosition(i).toString() + "\n";
	                         
	                         friends[i].updateUserLrequest(hintId);
	                         friends[i].updateUserRequester(currentUser.getUserID());
	                         currentUser.minusCredits(askCredits);
	                         currentUser.updateUserCredits(currentUser.getCredits());
	                    }
	                 }
	                Toast.makeText(AskFriendActivity.this,selected,Toast.LENGTH_LONG).show();
	                
	                
	                
	                
	                
	                finish();
	                
	                
	            }});
			progressDialog.dismiss();
		}
	}
	
}