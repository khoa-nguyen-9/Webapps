package com.webapps.puzzle;

import java.util.HashMap;

import library.DatabaseHandler;
import library.MessageFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewMessageActivity extends Activity {

	private static String KEY_SUCCESS = "success";
	private static String KEY_FRIENDS = "friends";
	private static String KEY_USERNAME = "username";
	private static String KEY_MID = "mid";
	private static String KEY_SENDER = "sender";
	private static String KEY_RECEIVER = "receiver";
	private static String KEY_MCONTENT = "mcontent";

	protected DatabaseHandler dbHandler;
	private HashMap<String, String> user;

	User currentUser;
	int uid;
	User friend;
	Message message;
	Message[] messages;
	String[] listContent = {};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		friend = (User) getIntent().getSerializableExtra("friend");
		
		dbHandler = new DatabaseHandler(getApplicationContext());
        user = dbHandler.getUserDetails();
        ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting user info ...");
		uid = Integer.parseInt(user.get("uid"));
		GetMessageTask gmt = new GetMessageTask(uid,
				friend.getUserID(), progressDialog);
		gmt.execute();
		
	}

	// AsyncTask to get friends from database
	private class GetMessageTask extends AsyncTask<String, Void, Integer> {

		private int uid;
		private int friendid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;

		public GetMessageTask(int uid, int friendid, ProgressDialog progressDialog) {
			this.uid = uid;
			this.friendid = friendid;
			this.progressDialog = progressDialog;
		}

		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		protected Integer doInBackground(String... arg0) {

			MessageFunctions messageFunction = new MessageFunctions();
			JSONObject json = messageFunction.getMessages();

			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						JSONArray json_messages = json.getJSONArray("messages");
						// Log.v("name", json_user.getString(KEY_NAME));
						int count = 0;
						for (int i = 0; i < json_messages.length(); i++) {
							JSONObject json_message = json_messages
									.getJSONObject(i);
							int sender = Integer.parseInt(json_message
									.getString(KEY_SENDER));
							int receiver = Integer.parseInt(json_message
									.getString(KEY_RECEIVER));
							if (((sender == uid) && (receiver == friendid)) || ((sender == friendid) && (receiver == uid))) {
								count++;
							}
						}
						messages = new Message[count];
						listContent = new String[count];
						count=0;
						for (int i = 0; i< json_messages.length(); i++) {
							JSONObject json_message = json_messages
									.getJSONObject(i);
							// Log.v("name", json_user.getString(KEY_NAME));
							int sender = Integer.parseInt(json_message.getString(KEY_SENDER));
							int receiver = Integer.parseInt(json_message.getString(KEY_RECEIVER));
							if (((sender == uid) && (receiver == friendid)) || ((sender == friendid) && (receiver == uid))) {
								int mid = Integer.parseInt(json_message
										.getString(KEY_MID));
								String content = json_message
										.getString(KEY_MCONTENT);
								messages[count] = new Message(mid, sender,receiver, content);
								listContent[count] = messages[count]
										.getContent();
								count++;
							}
							
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
			
			//TODO : display message properly
			setContentView(R.layout.message);
			TextView username = (TextView) findViewById(R.id.userName);
			username.setText(friend.getUsername());
			
			final LinearLayout layout = (LinearLayout) findViewById(R.id.messages);
			TextView messageView;
			final int blue = getResources().getColor(R.color.blue);
			final int red = getResources().getColor(R.color.red);

		    for (int i = 0; i < listContent.length; i++) {
		    
		     
				TextView txt1 = new TextView(NewMessageActivity.this);
				txt1.setText(listContent[i]);
				txt1.setTextSize(16);
	        	
				if(messages[i].getSender() == uid){ //TODO: current user is null currentUser.getUserID()
		        	txt1.setBackgroundColor(blue);
		        	txt1.setGravity(Gravity.LEFT);
		        	
		        	
		        	
		        }else{
		        	txt1.setBackgroundColor(red);
		        	txt1.setGravity(Gravity.RIGHT);
		        }
				layout.addView(txt1);}
		      
			
			
			Button send = (Button) findViewById(R.id.submitMessage);
			send.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					EditText msg = (EditText) findViewById(R.id.messsageContent);
					String mess = msg.getEditableText().toString();
					
					message = new Message(uid, friend.getUserID(), mess);
					message.addMessage();
					
					
					
					
					TextView messageView;
					
					/*View line = new View(NewMessageActivity.this.getApplicationContext());
			        line.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));*/
			        
			        messageView = new TextView(NewMessageActivity.this.getApplicationContext());
			        messageView.setText(mess);
			        messageView.setTextSize(16);
			        messageView.setBackgroundColor(blue);
		        	messageView.setGravity(Gravity.LEFT);
			        layout.addView(messageView);
			       
				}
				
			});
			progressDialog.dismiss();
		}
	}

}
