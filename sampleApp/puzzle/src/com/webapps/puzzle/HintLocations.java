package com.webapps.puzzle;

import java.util.HashMap;

import library.DatabaseHandler;
import library.HintFunctions;
import library.LocationFunctions;
import library.UserFunctions;
import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

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

public class HintLocations extends Activity{
	
	
	ListView locationList;
    Button submit;
    
	private static String KEY_SUCCESS = "success";
	private static String KEY_LREQUEST = "lrequest";
	private static String KEY_REQUESTER = "requester";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";
	private static String KEY_HID = "hid";
	private static String KEY_LID = "lid";
	private static String KEY_HCONTENT = "hcontent";
	private static String KEY_X = "x";
	private static String KEY_Y = "y";
	private static String KEY_LNAME = "lname";
 
	protected User currentUser;
	private User[] requesters;
	private Hint[] hints;
	private Place[] places;
	
	
	protected DatabaseHandler dbHandler;
    private HashMap<String, String> user;
    
    String[] listContent;

 
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        
        dbHandler = new DatabaseHandler(getApplicationContext());
        user = dbHandler.getUserDetails();
        ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting question info ...");
        GetLocationRequestTask task = new GetLocationRequestTask(user.get("uid"), progressDialog);
		task.execute();
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
	
	// AsyncTask to get a location from database 
	private class GetLocationRequestTask extends AsyncTask<String, Void, Integer> {
			
		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;
	
		public GetLocationRequestTask(String uid, ProgressDialog progressDialog)
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
						String[] tokens = json_userinfo.getString(KEY_LREQUEST).split("\\s");
						int [] lrequest = new int[tokens.length - 1];
						hints = new Hint[tokens.length - 1];
						places = new Place[tokens.length - 1];
						for (int i = 0; i < lrequest.length; i++) {
							lrequest[i] = Integer.parseInt(tokens[i+1]);
							HintFunctions hintFunction = new HintFunctions();
							JSONObject json2 = hintFunction.getHint(tokens[i+1]);
							JSONObject json_hint = json2.getJSONObject("hint");
							int lid = Integer.parseInt(json_hint.getString(KEY_LID));
							String content = json_hint.getString(KEY_HCONTENT);
							hints[i] = new Hint(lrequest[i], lid, content);
							LocationFunctions locationFunction = new LocationFunctions();
							JSONObject json3 = locationFunction.getLocation(json_hint.getString(KEY_LID));
							JSONObject json_location = json3.getJSONObject("location");
							float X = Float.parseFloat(json_location.getString(KEY_X));
							float Y = Float.parseFloat(json_location.getString(KEY_Y));
							String name = json_location.getString(KEY_LNAME);
							places[i] = new Place(lid, X, Y, name);
						}
						tokens = json_userinfo.getString(KEY_REQUESTER).split("\\s");
						int [] requester = new int[tokens.length - 1];
						requesters = new User[tokens.length - 1];
						for (int i = 0; i < requester.length; i++) {
							requester[i] = Integer.parseInt(tokens[i+1]);
							JSONObject json2 = userinfoFunction.getUserInfoByID(Integer.toString(requester[i]));
							JSONObject json_userinfo2 = json2.getJSONObject("userinfo");
							requesters[i] = new User(requester[i], null, null, null, null, null, null, null, null,  Integer.parseInt(json_userinfo2.getString(KEY_CREDITS)), json_userinfo2.getString(KEY_USERNAME));
						}
						String username = json_userinfo.getString(KEY_USERNAME);
						int credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
						currentUser = new User(Integer.parseInt(uid), null, null, null, null, null, null, lrequest, requester, credits, username);
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
			
			listContent = new String[places.length];
			
			for(int i = 0; i<places.length; i++){
				listContent[i] = places[i].getName() + ", by " +
			                            requesters[i].getUsername();
			}
			

			locationList = (ListView)findViewById(R.id.friendlist);
			 
	        submit = (Button)findViewById(R.id.challengesubmit);
	        submit.setText("Show on map");
	 
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
	 
	                android.R.layout.simple_list_item_multiple_choice, listContent);
	 
	        locationList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        locationList.setAdapter(adapter);
	 
	        submit.setOnClickListener(new Button.OnClickListener(){
	 
	            @Override
	            public void onClick(View v) {
	                String selected = "";
	                int cntChoice = locationList.getCount();
	                SparseBooleanArray sparseBooleanArray = 
	                		                  locationList.getCheckedItemPositions();
	                Intent map =  //TODO make sure only the current selected location are shown on the map
	                		new Intent(HintLocations.this.getApplicationContext(),
	                				MapActivity.class);
	                map.putExtra("user", currentUser);
                    map.putExtra("mode", "locations");
                    
                    int nc = 0; //number of elements checked
                    
	                for(int i = 0; i < cntChoice; i++){
	                    if(sparseBooleanArray.get(i)) {
	                         selected += 
	                              locationList.getItemAtPosition(i).toString() + "\n";
	                         map.putExtra("requester" + nc, requesters[nc]);
	                         map.putExtra("hint" + nc, hints[nc]);
	                         map.putExtra("place"+nc, places[nc]);
	                         nc++;
	                    }
	                 }
	                map.putExtra("checked", nc);
	                Toast.makeText(HintLocations.this,selected,Toast.LENGTH_LONG).show();
	                
	                startActivity(map);
	            }});
		}
	}
}
