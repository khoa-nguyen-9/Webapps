package com.webapps.puzzle;

import java.io.Serializable;
import java.util.HashSet;

import library.QuestionFunctions;
import library.UserInfoFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class Question implements Serializable {
	
	private Question[] quests; 
	
	private int qid;				// ID of the question
	private int maker;				// The user who make the question
	private int[] hints;  			// The list of hints for the question
	
	private String content;			// The content of the question
	private float ranking; 			// The ranking of the question on leaderboard
	private String answer;			// The answer of the question
	private int rewards;			// The rewards of the question
	private String base;            // The reference of the question (museum, restaurant, etc, ...)
	private boolean publish;        // Is the question publish to leaderboard
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_QID = "qid";
	private static String KEY_MAKER = "maker";
	private static String KEY_QCONTENT = "qcontent";
	private static String KEY_QRANKING = "qranking";
	private static String KEY_ANSWER = "answer";
	private static String KEY_REWARDS = "rewards";
	private static String KEY_BASE = "rewards";
	private static String KEY_PUBLISH = "publish";
	private static String KEY_HINTS = "hints";
	
	public Question(int maker, int[] hints, String content, float ranking, String answer, int rewards, String base, boolean publish) {
		this.maker = maker;
		this.hints = hints;
		this.content = content;
		this.ranking = ranking;
		this.answer = answer;
		this.rewards = rewards;
		this.base = base;
		this.publish = publish;
	}
	
	public Question(int qid, int maker, int[] hints, String content, float ranking, String answer, int rewards, String base, boolean publish) {
		this.qid = qid;
		this.maker = maker;
		this.hints = hints;
		this.content = content;
		this.ranking = ranking;
		this.answer = answer;
		this.rewards = rewards;
		this.base = base;
		this.publish = publish;
	}
	
	// Add new location to the database, update lid
	public void addQuestion() {
		AddQuestionTask l = new AddQuestionTask(maker, hints, content, ranking, answer, rewards, base, publish);
		l.execute();
	}
	
	// Add new location to the database, update lid
	public void updateranking(float ranking) {
		UpdateRankingTask task = new UpdateRankingTask(ranking);
		task.execute();
	}
	
	// Get the user who made the question
	public int getMaker() {
		return maker;
	}
	
	// Get the content of the question
	public String getContent() {
		return content;
	}

	
	// Get the rank of the question on leader board
	public float getRank() {
		return ranking;
	}
	
	// Get list of hints for the question
	public  int[] getHints() {
		return hints;
	}

	
	// Get the answer of the question
	public String getAnswer() {
		return answer;
	}
	
	// Get the rewards for the question
	public int getRewards() {
		return rewards;
	}
	
	// Get the ID of the question
	public int getID() {
		return qid;
	}
	
	// Get the base of the question
	public String getBase() {
		return base;
	}
	
	// Get the rewards for the question
	public boolean getPublish() {
		return publish;
	}

	// AsyncTask to get a question from database 
	private class GetQuestionByIDTask extends AsyncTask<String, Void, Integer> {
		private String qid;
		
		private int responseCode = 0;

		public GetQuestionByIDTask(int qid)
		{
			this.qid = Integer.toString(qid);
		}

		protected Integer doInBackground(String... arg0) {
			
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.getQuestionByID(qid);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_question = json.getJSONObject("question");
						//Log.v("name", json_user.getString(KEY_NAME));
						maker = Integer.parseInt(json_question.getString(KEY_MAKER));
						String[] tokens = json_question.getString(KEY_HINTS).split("\\s");
						hints = new int[tokens.length - 1];
						for (int i = 0; i < hints.length; i++) {
							hints[i] =Integer.parseInt(tokens[i+1]);
						}
						content = json_question.getString(KEY_QCONTENT);
						ranking = Integer.parseInt(json_question.getString(KEY_QRANKING));
						answer = json_question.getString(KEY_ANSWER);
						rewards = Integer.parseInt(json_question.getString(KEY_REWARDS));
						base = json_question.getString(KEY_BASE);
						publish = json_question.getString(KEY_PUBLISH).equals("1");
		
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
	
	// AsyncTask to get a question from database 
	private class GetQuestionByMakerTask extends AsyncTask<String, Void, Integer> {
		private String maker;
		
		private int responseCode = 0;
	
		public GetQuestionByMakerTask(int maker) {
			this.maker = Integer.toString(maker);
		}

		protected Integer doInBackground(String... arg0) {
				
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.getQuestionByMaker(maker);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1){
						JSONArray json_questions = json.getJSONArray("question");
						quests = new Question[json_questions.length()];
						for (int i = 0; i < json_questions.length(); i++) {
							JSONObject json_question = json_questions.getJSONObject(i);
							//Log.v("name", json_user.getString(KEY_NAME));
							int qid = Integer.parseInt(json_question.getString(KEY_QID));
							String[] tokens = json_question.getString(KEY_HINTS).split("\\s");
							int [] hints = new int[tokens.length - 1];
							for (int j = 1; j < tokens.length; j++) {
								hints[j] =Integer.parseInt(tokens[j]);
							}
							String content = json_question.getString(KEY_QCONTENT);
							String base = json_question.getString(KEY_BASE);
							String answer = json_question.getString(KEY_ANSWER);
							int rewards = Integer.parseInt(json_question.getString(KEY_REWARDS));
							int ranking = Integer.parseInt(json_question.getString(KEY_QRANKING));
							boolean publish = json_question.getString(KEY_PUBLISH).equals("1");
							//quests[i] = new Question(qid, Integer.parseInt(maker), hints, content, ranking, answer, rewards, base, publish); 
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
	}	

	// AsyncTask to get a question from database 
	private class GetAllQuestionsTask extends AsyncTask<String, Void, Integer> {
		
		private int responseCode = 0;
	
		public GetAllQuestionsTask() {
			
		}

		protected Integer doInBackground(String... arg0) {
				
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.getQuestions();
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1){
						JSONArray json_questions = json.getJSONArray("question");
						quests = new Question[json_questions.length()];
						for (int i = 0; i < json_questions.length(); i++) {
							JSONObject json_question = json_questions.getJSONObject(i);
							//Log.v("name", json_user.getString(KEY_NAME));
							int qid = Integer.parseInt(json_question.getString(KEY_QID));
							String[] tokens = json_question.getString(KEY_HINTS).split("\\s");
							int [] hints = new int[tokens.length - 1];
							for (int j = 1; j < tokens.length; j++) {
								hints[j] =Integer.parseInt(tokens[j]);
							}
							String content = json_question.getString(KEY_QCONTENT);
							String base = json_question.getString(KEY_BASE);
							String answer = json_question.getString(KEY_ANSWER);
							int rewards = Integer.parseInt(json_question.getString(KEY_REWARDS));
							int maker = Integer.parseInt(json_question.getString(KEY_MAKER));
							int ranking = Integer.parseInt(json_question.getString(KEY_QRANKING));
							boolean publish = json_question.getString(KEY_PUBLISH).equals("1");
							//quests[i] = new Question(qid, maker, hints, content, ranking, answer, rewards, base, publish); 
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
	}	
	
	// AsyncTask to get a question from database 
	private class GetQuestionByRankingTask extends AsyncTask<String, Void, Integer> {
		private String ranking;
		
		private int responseCode = 0;
	
		public GetQuestionByRankingTask(int ranking) {
			this.ranking = Integer.toString(ranking);
		}

		protected Integer doInBackground(String... arg0) {
				
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.getQuestionByRanking(ranking);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1){
						JSONArray json_questions = json.getJSONArray("question");
						quests = new Question[json_questions.length()];
						for (int i = 0; i < json_questions.length(); i++) {
							JSONObject json_question = json_questions.getJSONObject(i);
							//Log.v("name", json_user.getString(KEY_NAME));
							int qid = Integer.parseInt(json_question.getString(KEY_QID));
							String[] tokens = json_question.getString(KEY_HINTS).split("\\s");
							int [] hints = new int[tokens.length - 1];
							for (int j = 1; j < tokens.length; j++) {
								hints[j] =Integer.parseInt(tokens[j]);
							}
							String content = json_question.getString(KEY_QCONTENT);
							String base = json_question.getString(KEY_BASE);
							String answer = json_question.getString(KEY_ANSWER);
							int rewards = Integer.parseInt(json_question.getString(KEY_REWARDS));
							int maker = Integer.parseInt(json_question.getString(KEY_MAKER));
							boolean publish = json_question.getString(KEY_PUBLISH).equals("1");
							//quests[i] = new Question(qid, maker, hints,content, Integer.parseInt(ranking), answer, rewards, base, publish); 
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
	}
	
	// AsyncTask to get a question from database 
	private class GetQuestionByBaseTask extends AsyncTask<String, Void, Integer> {
		private String base;
		
		private int responseCode = 0;
		private ProgressDialog progressDialog;
		
		public GetQuestionByBaseTask(String base, ProgressDialog progressDialog) {
			this.base = base;
		}

		protected Integer doInBackground(String... arg0) {
				
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.getQuestionByBase(base);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);
					if(Integer.parseInt(res) == 1){
						JSONArray json_questions = json.getJSONArray("question");
						quests = new Question[json_questions.length()];
						for (int i = 0; i < json_questions.length(); i++) {
							JSONObject json_question = json_questions.getJSONObject(i);
							//Log.v("name", json_user.getString(KEY_NAME));
							int qid = Integer.parseInt(json_question.getString(KEY_QID));
							String[] tokens = json_question.getString(KEY_HINTS).split("\\s");
							int [] hints = new int[tokens.length - 1];
							for (int j = 1; j < tokens.length; j++) {
								hints[j] =Integer.parseInt(tokens[j]);
							}
							String content = json_question.getString(KEY_QCONTENT);
							int maker = Integer.parseInt(json_question.getString(KEY_MAKER));
							String answer = json_question.getString(KEY_ANSWER);
							int rewards = Integer.parseInt(json_question.getString(KEY_REWARDS));
							int ranking = Integer.parseInt(json_question.getString(KEY_QRANKING));
							boolean publish = json_question.getString(KEY_PUBLISH).equals("1");
							//quests[i] = new Question(qid, maker, hints, content, ranking, answer, rewards, base, publish); 
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
	}
	
	// Async task to add a new location to database
	private class AddQuestionTask extends AsyncTask<String, Void, Integer> {
		
		private int responseCode = 0;
		private String maker;				// The user who make the question
		private String hints;  			// The list of hints for the question
		private String content;			// The content of the question
		private String ranking; 			// The ranking of the question on leaderboard
		private String answer;			// The answer of the question
		private String rewards;			// The rewards of the question
		private String base;            // The reference of the question (museum, restaurant, etc, ...)
		private String publish;        // Is the question publish to leaderboard

		public AddQuestionTask(int maker, int[] hints, String content, float ranking2, String answer, int rewards, String base, boolean publish)
		{
			this.maker = Integer.toString(maker);
			String hintstring = " ";
			for (int i = 0; i < hints.length; i++) {
				hintstring += " " + Integer.toString(hints[i]);
			}
			this.hints = hintstring;
			this.content = content;
			this.ranking = Float.toString(ranking2);
			this.answer = answer;
			this.rewards = Integer.toString(rewards);
			this.base = base;
			if (publish) {
				this.publish = "1";
			} else {
				this.publish = "0";
			}
		}

		protected Integer doInBackground(String... arg0) {
			
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.addQuestion(maker, content, ranking, answer, rewards, base, publish, hints);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
					String res = json.getString(KEY_SUCCESS);

					if(Integer.parseInt(res) == 1){
						JSONObject json_question = json.getJSONObject("question");
						//Log.v("name", json_user.getString(KEY_NAME));
						qid = Integer.parseInt(json_question.getString(KEY_QID));
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
	
	// AsyncTask to update answered question IDs from database 
	private class UpdateRankingTask extends AsyncTask<String, Void, Integer> {

		private String newRanking;
		
		private int responseCode = 0;

		public UpdateRankingTask(float ranking)
		{
			this.newRanking = Float.toString(ranking);
		}

		protected Integer doInBackground(String... arg0) {
			
			QuestionFunctions questionFunction = new QuestionFunctions();
			JSONObject json = questionFunction.updateRanking(Integer.toString(qid), newRanking);
			
			// check for login response
			try {
				if (json.getString(KEY_SUCCESS) != null) {
						responseCode = 1;
				} else{
						responseCode = 0;
						// Error in login
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


