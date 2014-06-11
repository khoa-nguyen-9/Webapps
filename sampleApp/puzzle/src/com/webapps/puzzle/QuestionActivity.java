package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
	private Question question;
	// private String question;
	protected static boolean checkIn1;
	private RatingBar ratingBar;
	private float givenRating;
	protected User currentUser;

	private static String KEY_SUCCESS = "success";
	private static String KEY_CHECKED = "checked";
	private static String KEY_CHINTS = "chints";
	private static String KEY_CREDITS = "credits";
	private static String KEY_USERNAME = "username";
	private static String KEY_LID = "lid";
	private static String KEY_HCONTENT = "hcontent";
	private static String KEY_X = "x";
	private static String KEY_Y = "y";
	private static String KEY_LNAME = "lname";

	private Hint[] hints;
	private Place[] places;

	protected DatabaseHandler dbHandler;
	private HashMap<String, String> user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		Intent intent = getIntent();
		// question = intent.getStringExtra("question");
		question = (Question) intent.getSerializableExtra("question");

		dbHandler = new DatabaseHandler(getApplicationContext());
		user = dbHandler.getUserDetails();
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting question info ...");
		GetUserInfoTask task = new GetUserInfoTask(user.get("uid"),
				progressDialog);
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

	// called when a hint button is clicked
	public void launchHint1(View view) {
		launchHint(1);
	}

	public void launchHint2(View view) {
		launchHint(2);
	}

	public void launchHint3(View view) {
		launchHint(3);
	}

	private void launchHint(int i) {
		Intent maps = new Intent(getApplicationContext(), MapActivity.class);

		maps.putExtra("hint0", hints[0]);
		maps.putExtra("place0", places[0]);

		maps.putExtra("hint1", hints[1]);
		maps.putExtra("place1", places[1]);

		maps.putExtra("hint2", hints[2]);
		maps.putExtra("place2", places[2]);

		maps.putExtra("mode", "hints");

		startActivity(maps);
	}

	public void askFriends1(View view) {
		askFriends(1);
	}

	public void askFriends2(View view) {
		askFriends(2);
	}

	public void askFriends3(View view) {
		askFriends(3);
	}

	public void askFriends(int i) {
		Intent ask = new Intent(this, AskFriendActivity.class);

		switch (i) {
		case 1:
			ask.putExtra("number", 1);
			ask.putExtra("ask1", hints[0].getHintID());

			break;
		case 2:
			ask.putExtra("number", 2);
			ask.putExtra("ask1", hints[1].getHintID());
			break;
		case 3:
			ask.putExtra("number", 3);
			ask.putExtra("ask2", hints[2].getHintID());
			break;
		default:
			break;
		}

		startActivity(ask);
	}

	/*
	 * 
	 * 
	 */
	// AsyncTask to get a location from database
	private class GetUserInfoTask extends AsyncTask<String, Void, Integer> {

		private String uid;
		private ProgressDialog progressDialog;
		private int responseCode = 0;

		public GetUserInfoTask(String uid, ProgressDialog progressDialog) {
			this.uid = uid;
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
						String[] tokens = json_userinfo.getString(KEY_CHECKED)
								.split("\\s");
						Set<Integer> checked = new HashSet<Integer>();
						for (int i = 0; i < tokens.length - 1; i++) {
							checked.add(Integer.parseInt(tokens[i + 1]));
						}
						tokens = json_userinfo.getString(KEY_CHINTS).split(
								"\\s");
						Set<Integer> chints = new HashSet<Integer>();
						for (int i = 0; i < tokens.length - 1; i++) {
							chints.add(Integer.parseInt(tokens[i + 1]));
						}

						String username = json_userinfo.getString(KEY_USERNAME);
						int credits = Integer.parseInt(json_userinfo
								.getString(KEY_CREDITS));
						currentUser = new User(Integer.parseInt(uid), null,
								null, checked, chints, null, null, null, null,
								credits, username);
						hints = new Hint[question.getHints().length];
						places = new Place[question.getHints().length];
						for (int i = 0; i < question.getHints().length; i++) {
							HintFunctions hintFunction = new HintFunctions();
							JSONObject json2 = hintFunction.getHint(Integer
									.toString(question.getHints()[i]));
							JSONObject json_hint = json2.getJSONObject("hint");
							int lid = Integer.parseInt(json_hint
									.getString(KEY_LID));
							String content = json_hint.getString(KEY_HCONTENT);
							hints[i] = new Hint(question.getHints()[i], lid,
									content);
							LocationFunctions locationFunction = new LocationFunctions();
							JSONObject json3 = locationFunction
									.getLocation(json_hint.getString(KEY_LID));
							JSONObject json_location = json3
									.getJSONObject("location");
							float X = Float.parseFloat(json_location
									.getString(KEY_X));
							float Y = Float.parseFloat(json_location
									.getString(KEY_Y));
							String name = json_location.getString(KEY_LNAME);
							places[i] = new Place(lid, X, Y, name);
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
			TextView emailTextView = (TextView) findViewById(R.id.question);
			emailTextView.setText(question.getContent());

			final Intent hint = new Intent(
					QuestionActivity.this.getApplicationContext(),
					HintsInfo.class);
			int[] hintIDs = question.getHints();
			for (int i = 0; i < hintIDs.length; i++) {
				if (currentUser.getChint().contains(hintIDs[i])) {

					hint.putExtra("hint" + i, hints[i]);
				}
			}
			for (int i = 0; i < hintIDs.length; i++) {
				if (currentUser.getChint().contains(hintIDs[i])) {
					// Show the tick -> move the the location info

					int hintButton;
					if (i == 0) {
						hintButton = R.id.hint1;
					} else if (i == 1) {
						hintButton = R.id.hint2;
					} else {
						hintButton = R.id.hint3;
					}

					final Button btn = (Button) QuestionActivity.this
							.findViewById(hintButton);
					btn.setText("Show hint");
					btn.setId(i);
					btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							startActivity(hint);

						}

					});
				}
			}
			
			
			
			Button submitButton = (Button) QuestionActivity.this
					.findViewById(R.id.submitQuestionB);
			submitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					EditText userAnswer = (EditText) QuestionActivity.this
							.findViewById(R.id.answer);
					String answer = userAnswer.getEditableText()
							.toString();

					if (question.getAnswer().equalsIgnoreCase(answer)) {
						currentUser.updateUserAnswered(question.getID());
						for (int i = 0; i < hints.length; i++) {
							currentUser.updateUserChints(hints[i]
									.getHintID());
						}
						for (int i = 0; i < places.length; i++) {
							currentUser.updateUserChecked(places[i]
									.getID());
						}
						currentUser.addCredits(question.getRewards());
						currentUser.updateUserCredits(currentUser
								.getCredits());

						Intent intent = new Intent(
								getApplicationContext(),
								RatingActivity.class);
						intent.putExtra("question", question);
						startActivity(intent);
						finish();

					}
				}

			});

		}
	}
}