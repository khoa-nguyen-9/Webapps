package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import library.DatabaseHandler;
import library.HintFunctions;
import library.LocationFunctions;
import library.QuestionFunctions;
import library.UserInfoFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class SetUpFragment extends Fragment {
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";
	private static String KEY_HID = "hid";
	private static String KEY_LID = "lid";
	private static String KEY_QID = "qid";
	private static String KEY_X = "x";
	private static String KEY_Y = "y";
	private static String KEY_LNAME = "lname";
	
	private final int questionCredits = 10;
	protected DatabaseHandler dbHandler;
    private HashMap<String, String> user;
    
    private User currentUser;
    private User[] friends; // List of user friends
    private String[] friendNames;
    private Place[] places; // List of all available places in database
    private String[] placeNames;
	View view;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = inflater.inflate(R.layout.fragment_setup, container, false);
		return view;
	}
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	   ProgressDialog progressDialog = new ProgressDialog(getActivity());
     progressDialog.setMessage("Getting user info ...");
     dbHandler = new DatabaseHandler(getActivity());
     user = dbHandler.getUserDetails();
     GetUserInfoTask task = new GetUserInfoTask(user.get("uid"), progressDialog);
     task.execute();
	}
	
	// AsyncTask to get a location from database 
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
						String[] tokens = json_userinfo.getString(KEY_FRIENDS).split("\\s");
						Set<Integer> friendList = new HashSet<Integer>();
						for (int i = 0; i < tokens.length-1; i++) {
							friendList.add(Integer.parseInt(tokens[i+1]));
						}
						friends = new User[friendList.size()+1];
						friendNames = new String[friendList.size() + 1];
						friends[0] = new User(0,null,null,null,null,null,null,null,null,0,"No one");
						friendNames[0] = "No one";
						int count = 0;
						for (Integer i : friendList) {
							json = userinfoFunction.getUserInfoByID(Integer.toString(i));
							json_userinfo = json.getJSONObject("userinfo");
							String name = json_userinfo.getString(KEY_USERNAME);
							friends[count+1] = new User(i,null,null,null,null,null,null,null,null,0,name);
							friendNames[count+1] = name;
							count++;
						}
						
						String username = json_userinfo.getString(KEY_USERNAME);
						int credits = Integer.parseInt(json_userinfo.getString(KEY_CREDITS));
						currentUser = new User(Integer.parseInt(uid), friendList, null, null, null, null, null, null, null, credits, username);
						LocationFunctions locationFunction = new LocationFunctions();
						JSONObject json2 = locationFunction.getLocations();
						JSONArray json_locations = json2.getJSONArray("locations");
						//Log.v("name", json_user.getString(KEY_NAME));
						placeNames = new String[json_locations.length()];
						places = new Place[json_locations.length()];
						
 						for (int i = 0; i < json_locations.length(); i++) {
							JSONObject json_location = json_locations.getJSONObject(i);
							int lid = Integer.parseInt(json_location.getString(KEY_LID));
							float x = Float.parseFloat(json_location.getString(KEY_X));
							float y = Float.parseFloat(json_location.getString(KEY_Y));
							String name = json_location.getString(KEY_LNAME);
							placeNames[i] = i == 0? "Use friend's location to set up": name;
							places[i] = new Place(lid, x, y, name);
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
			progressDialog.dismiss();
			
			
			Spinner dropdown = (Spinner) view.findViewById(R.id.hint1Spin);
			ArrayAdapter<String> adapter = 
					new ArrayAdapter<String>(getActivity().getApplicationContext(), 
							           android.R.layout.simple_spinner_item, placeNames);
			dropdown.setAdapter(adapter);
			
			
			dropdown = (Spinner) view.findViewById(R.id.hint2Spin);
			adapter = 
					new ArrayAdapter<String>(getActivity().getApplicationContext(), 
							           android.R.layout.simple_spinner_item, placeNames);
			dropdown.setAdapter(adapter);
			
			dropdown = (Spinner) view.findViewById(R.id.hint3Spin);
			adapter = 
					new ArrayAdapter<String>(getActivity().getApplicationContext(), 
							           android.R.layout.simple_spinner_item, placeNames);
			dropdown.setAdapter(adapter);
			
			
			dropdown = (Spinner) view.findViewById(R.id.spinner1);
			adapter = 
					new ArrayAdapter<String>(getActivity().getApplicationContext(), 
							           android.R.layout.simple_spinner_item, friendNames);
			dropdown.setAdapter(adapter);
			
			dropdown = (Spinner) view.findViewById(R.id.spinner2);
			adapter = 
					new ArrayAdapter<String>(getActivity().getApplicationContext(), 
							           android.R.layout.simple_spinner_item, friendNames);
			dropdown.setAdapter(adapter);
			
			dropdown = (Spinner) view.findViewById(R.id.spinner3);
			adapter = 
					new ArrayAdapter<String>(getActivity().getApplicationContext(), 
							           android.R.layout.simple_spinner_item, friendNames);
			dropdown.setAdapter(adapter);
			
			
			
			
			Button submit = (Button) getActivity().findViewById(R.id.submitq);
			submit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// Call the SubmitQuestionTask here
					// Provide it with list of X,Y coordinates, location name, hint content, 
					// ranking (can be 0 for now), answer, rewards (have a edit text for this or set at 10) 
					// base (i.e preference put a dropbox for this or just set at 'random' for now)
					// publish, friendsIDs(list of friend ID the user has chosen to challenge, call friends[i].getUserID for this)
					
					
					EditText question = 
							(EditText) view.findViewById(R.id.userQuestion);
					String content = question.getEditableText().toString();
					
					EditText h1 = 
							(EditText) view.findViewById(R.id.newhint1);
					String hint1 = h1.getEditableText().toString();
					
					EditText h2 = 
							(EditText) view.findViewById(R.id.newhint2);
					String hint2 = h2.getEditableText().toString();
					
					EditText h3 = 
							(EditText) view.findViewById(R.id.newhint3);
					String hint3 = h3.getEditableText().toString();
					
					String[] hcontent = {hint1,hint2,hint3};
					
					EditText ans = 
							(EditText) view.findViewById(R.id.userAnswer);
					String answer = h3.getEditableText().toString();
					
					CheckBox cb = (CheckBox) view.findViewById(R.id.radioPublish);
					boolean publish = cb.isChecked();
					
					Spinner hint1Spin = (Spinner) view.findViewById(R.id.hint1Spin);
					int pos1 = hint1Spin.getSelectedItemPosition();
					Place place1 = places[pos1];
					
					Spinner hint2Spin = (Spinner) view.findViewById(R.id.hint2Spin);
					int pos2 = hint2Spin.getSelectedItemPosition();
					Place place2 = places[pos2];
					
					Spinner hint3Spin = (Spinner) view.findViewById(R.id.hint3Spin);
					int pos3 = hint3Spin.getSelectedItemPosition();
					Place place3 = places[pos3];
					
					Place[] ps = {place1,place2,place3};
					
					
					Spinner friend1Spin = (Spinner) view.findViewById(R.id.spinner1);
					pos1 = friend1Spin.getSelectedItemPosition();
					User friend = friends[pos1];
					
					int id1 = pos1 == 0 ? 0 :friend.getUserID();
					
					Spinner friend2Spin = (Spinner) view.findViewById(R.id.spinner2);
					pos1 = friend2Spin.getSelectedItemPosition();
					friend = friends[pos1];
					
					int id2 = pos1 == 0 ? 0 :friend.getUserID();
					
					
					Spinner friend3Spin = (Spinner) view.findViewById(R.id.spinner3);
					pos1 = friend3Spin.getSelectedItemPosition();
					
					friend = friends[pos1];
					
					int id3 = pos1 == 0? 0 : friend.getUserID();
					
					int[]friendIDs = {id1,id2,id3};
					
					//TODO: add a no friend option and sumbit friendID as 0
					
					
					SubmitQuestionTask questionTask = 
							new SubmitQuestionTask(ps, 
									           hcontent,
									           content,
									           0, 
									          answer, 
									          0, 
									          "",
									          publish, 
									          friendIDs, 
									          progressDialog);
					questionTask.execute();		
				}
				
			});
			
			  
		}
	}
	
	// AsyncTask to get a location from database 
	private class SubmitQuestionTask extends AsyncTask<String, Void, Integer> {
			
		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;
		private Place[] places;
		private String[] hcontent;
		private String content;
		private String ranking;
		private String answer;
		private String rewards;
		private String base;
		private String publish;
		private int[] friendsIDs;
		
		public SubmitQuestionTask(Place[] places, String[] hcontent, String content, int ranking, String answer, int rewards, String base, boolean publish, int[] friendIDs, ProgressDialog progressDialog)
		{
			this.progressDialog = progressDialog;
			this.places = places;
			this.hcontent = hcontent;
			this.content= content;
			this.ranking = Integer.toString(ranking);
			this.answer = answer;
			this.rewards = Integer.toString(rewards);
			this.base = base;
			if (publish) {
				this.publish = "1";
			} else {
				this.publish = "0";
			}
			this.friendsIDs = friendIDs;
		}

		@Override
		protected void onPreExecute()
		{
			progressDialog.show();
		}
		
		protected Integer doInBackground(String... arg0) {
			
			HintFunctions hintFunction = new HintFunctions();
			JSONObject json = hintFunction.addHint(Integer.toString(places[0].getID()), hcontent[0]);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_hint = json.getJSONObject("hint");
						
						//Log.v("name", json_user.getString(KEY_NAME));
						String hid = " " + json_hint.getString(KEY_HID);
						for (int i = 1; i<places.length; i++) {
							json = hintFunction.addHint(Integer.toString(places[i].getID()), hcontent[i]);
							json_hint = json.getJSONObject("hint");
							hid += " " + json_hint.getString(KEY_HID);
						}
						QuestionFunctions questionFunctions = new QuestionFunctions();
						json = questionFunctions.addQuestion(Integer.toString(currentUser.getUserID()), content, ranking, answer, rewards, base, publish, hid);
						JSONObject json_question = json.getJSONObject("question");
						if (publish.equals("1")) {
							currentUser.minusCredits(questionCredits);
							currentUser.updateUserCredits(currentUser.getCredits());
						}
						//Log.v("name", json_user.getString(KEY_NAME));
						int qid = Integer.parseInt(json_question.getString(KEY_QID));
						for (int i=0;i < friendsIDs.length; i++) {
							if (friendsIDs[i] != 0) {
								User friend = new User(friendsIDs[i],null,null,null,null,null,null,null,null,0,null);
								friend.updateUserChallenge(qid);
							}
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
			progressDialog.dismiss();
		}
	}
}