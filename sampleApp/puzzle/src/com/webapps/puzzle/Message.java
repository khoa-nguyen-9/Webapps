package com.webapps.puzzle;

import java.io.Serializable;

import library.MessageFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class Message implements Serializable {
	
	private int mid;				// ID of the question
	private int sender;				// The user who make the question
	private int receiver;  			// The list of hints for the question
	private String content;			// The content of the question
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_MID = "mid";
	private static String KEY_SENDER = "sender";
	private static String KEY_RECEIVER = "receiver";
	private static String KEY_MCONTENT = "mcontent";
	
	public Message(int mid, int sender, int receiver, String content) {
		this.mid = mid;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}
	
	public Message(int sender, int receiver, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
	}
	
	// Add new message to the database, update lid
	public void addMessage() {
		AddMessageTask task = new AddMessageTask(sender, receiver, content);
		task.execute();
	}
	
	// Get the user who sent the message
	public int getSender() {
		return sender;
	}
	
	// Get the content of the message
	public String getContent() {
		return content;
	}

	
	// Get the receiver of the message
	public int getReceiver() {
		return receiver;
	}	
	
	// Async task to add a new location to database
	private class AddMessageTask extends AsyncTask<String, Void, Integer> {
		
		private int responseCode = 0;
		private String sender;				// The user who make the question
		private String receiver;  			// The list of hints for the question
		private String mcontent;			// The content of the question

		public AddMessageTask(int sender, int receiver, String mcontent)
		{
			this.sender = Integer.toString(sender);
			this.mcontent = mcontent;
			this.receiver = Integer.toString(receiver);
		}

		protected Integer doInBackground(String... arg0) {
			
			MessageFunctions messageFunction = new MessageFunctions();
			JSONObject json = messageFunction.addMessage(sender, receiver, mcontent);
					
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_message = json.getJSONObject("message");
						//Log.v("name", json_user.getString(KEY_NAME));
						mid = Integer.parseInt(json_message.getString(KEY_MID));
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
	}

}