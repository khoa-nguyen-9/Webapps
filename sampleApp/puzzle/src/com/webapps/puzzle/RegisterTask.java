package com.webapps.puzzle;

import library.DatabaseHandler;
import library.UserFunctions;
import library.UserInfoFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

public class RegisterTask extends AsyncTask<String, Void, Integer> {

	private ProgressDialog progressDialog;
	private RegisterActivity activity;
	private static String KEY_SUCCESS = "success";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	private int responseCode = 0;
	
	/*Constructor that takes parameters passed by LoginFragment and stores them as class- 
	 * wide fields so that all methods can access necessary variables. 
	 * */
	public RegisterTask(RegisterActivity activity, ProgressDialog progressDialog)
	{
		this.activity = activity;
		this.progressDialog = progressDialog;
	}

	/*A necessary but very simple method that launches a ProgressDialog to show the
	 * user that a background task is operating (registration).*/
	@Override
	protected void onPreExecute()
	{
		progressDialog.show();
	}

	/*This method does almost all of the work for the class. It builds a connection to my 
	 * server, collects the details from the UI of the user's information, and then tries
	 * to register the user with the SQL database. All of the actual HTTP connection work
	 * is done in a background library class for security - including the hashing of a
	 * password into a 64bit encryption. */
	@Override
	protected Integer doInBackground(String... arg0) {
		EditText userName = (EditText)activity.findViewById(R.id.registerEmail);
		EditText passwordEdit = (EditText)activity.findViewById(R.id.registerPassword);
		EditText nameEdit = (EditText)activity.findViewById(R.id.registerName);
		String name = nameEdit.getText().toString();
		String email = userName.getText().toString();
		String password = passwordEdit.getText().toString();
		Log.v(email, password);
		UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.registerUser(name, email, password);

        // check for login response
        try {
            if (json.getString(KEY_SUCCESS) != null) {
                //registerErrorMsg.setText("");
                String res = json.getString(KEY_SUCCESS);
                if(Integer.parseInt(res) == 1){
                    // user successfully registred
                    // Store user details in SQLite Database
                    DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
                    JSONObject json_user = json.getJSONObject("user");

                    // Clear all previous data in database
                    userFunction.logoutUser(activity.getApplicationContext());
                    db.addUser(json_user.getString(KEY_NAME), 
                    	json_user.getString(KEY_EMAIL), json.getString(KEY_UID), 
                    	json_user.getString(KEY_CREATED_AT));
                    UserInfoFunctions userinfoFunction = new UserInfoFunctions();
        			JSONObject json2 = userinfoFunction.addUser(json.getString(KEY_UID),json_user.getString(KEY_NAME));
                    //successful registration
                    responseCode = 1;
                }else{
                    // Error in registration
                	responseCode = 0;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		return responseCode;
    }

	/*This final method concludes the background task. Its responseCode variable is sent from
	 * doInBackground, and this method acts based on the code it is sent. If the code is 1, 
	 * registration was successful and the main activity notifies the user of succes - the
	 * inverse occurs if there is a failure and 0 was sent.*/
	@Override
	protected void onPostExecute(Integer responseCode)
	{
		EditText userName = (EditText)activity.findViewById(R.id.registerEmail);
		EditText passwordEdit = (EditText)activity.findViewById(R.id.registerPassword);
		String s = userName.getText().toString();
		
		if (responseCode == 1) {
			progressDialog.dismiss();
			activity.registerReport(responseCode);
			userName.setText("");
			passwordEdit.setText("");	
		}
		if (responseCode == 0) {
			progressDialog.dismiss();
			activity.registerReport(responseCode);	
		}
	}
}