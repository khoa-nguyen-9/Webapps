package com.webapps.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import library.DatabaseHandler;
import library.QuestionFunctions;
import library.UserInfoFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QuestionFragment extends ListFragment {
	/*public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_questions, container, false);
		return view;
				
	}*/
	
	//List<Question> questions = new ArrayList<Question>();
	
	
	private static String KEY_SUCCESS = "success";
	private static String KEY_ANSWERED = "answered";
	private static String KEY_QID = "qid";
	private static String KEY_MAKER = "maker";
	private static String KEY_QCONTENT = "qcontent";
	private static String KEY_QRANKING = "qranking";
	private static String KEY_ANSWER = "answer";
	private static String KEY_REWARDS = "rewards";
	private static String KEY_BASE = "rewards";
	private static String KEY_PUBLISH = "publish";
	private static String KEY_HINTS = "hints";
	
	private Question[] quests;
	String[] values;
	protected DatabaseHandler dbHandler;
    private HashMap<String, String> user;
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    dbHandler = new DatabaseHandler(getActivity());
        user = dbHandler.getUserDetails();
	    ProgressDialog progressDialog = new ProgressDialog(getActivity());
	    progressDialog.setMessage("Loading questions ...");
	    GetQuestionsTask g = new GetQuestionsTask(progressDialog); 
	    g.execute();
	    
	  }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dbHandler = new DatabaseHandler(getActivity());
        user = dbHandler.getUserDetails();
	    ProgressDialog progressDialog = new ProgressDialog(getActivity());
	    progressDialog.setMessage("Loading questions ...");
	    GetQuestionsTask g = new GetQuestionsTask(progressDialog); 
	    g.execute();
	}

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  //Toast.makeText(this.getActivity(), item + " selected", Toast.LENGTH_LONG).show();
		  Intent question = new Intent(getActivity(), QuestionActivity.class);
		  //question.putExtra("question", quests[position]);
		  question.putExtra("question", quests[position]);
	      startActivity(question);
	  }
	  
		// AsyncTask to get a location from database 
		private class GetQuestionsTask extends AsyncTask<String, Void, Integer> {
			
			private int responseCode = 0;
			private ProgressDialog progressDialog;
			
			public GetQuestionsTask(ProgressDialog progressDialog) {
				this.progressDialog = progressDialog;
			}
			
			@Override
			protected void onPreExecute()
			{
				progressDialog.show();
			}

			protected Integer doInBackground(String... arg0) {
				
				QuestionFunctions questionFunction = new QuestionFunctions();
				JSONObject json = questionFunction.getQuestions();
				
				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						String res = json.getString(KEY_SUCCESS);

						if(Integer.parseInt(res) == 1){
							JSONArray json_questions = json.getJSONArray("questions");
							UserInfoFunctions userinfoFunction = new UserInfoFunctions();
							JSONObject json2 = userinfoFunction.getUserInfoByID(user.get("uid"));
							JSONObject json_userinfo = json2.getJSONObject("userinfo");
							String[] tokens = json_userinfo.getString(KEY_ANSWERED).split("\\s");
							Set<Integer> answered = new HashSet<Integer>();
							for (int i = 0; i < tokens.length-1; i++) {
								answered.add(Integer.parseInt(tokens[i+1]));
							}
							int count = json_questions.length();
							for (int i = 0; i < json_questions.length(); i++) {
								JSONObject json_question = json_questions.getJSONObject(i);
								int qid = Integer.parseInt(json_question.getString(KEY_QID));
								boolean publish = json_question.getString(KEY_PUBLISH).equals("1");
								if (answered.contains(qid) || (!publish)) {
									count--;
								}
							}
							quests = new Question[count];
							count = 0;
							for (int i = 0; i < json_questions.length(); i++) {
								JSONObject json_question = json_questions.getJSONObject(i);
								//Log.v("name", json_user.getString(KEY_NAME));
								int qid = Integer.parseInt(json_question.getString(KEY_QID));
								boolean publish = json_question.getString(KEY_PUBLISH).equals("1");
								if (!answered.contains(qid) && publish) {
									tokens = json_question.getString(KEY_HINTS).split("\\s");
									int [] hints = new int[tokens.length - 1];
									for (int j = 0; j < hints.length; j++) {
										hints[j] =Integer.parseInt(tokens[j+1]);
									}
									String content = json_question.getString(KEY_QCONTENT);
									String base = json_question.getString(KEY_BASE);
									String answer = json_question.getString(KEY_ANSWER);
									int rewards = Integer.parseInt(json_question.getString(KEY_REWARDS));
									int maker = Integer.parseInt(json_question.getString(KEY_MAKER));
									int ranking = Integer.parseInt(json_question.getString(KEY_QRANKING));
									quests[count] = new Question(qid, maker, hints, content, ranking, answer, rewards, base, publish);
									count++;
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
				values = new String[quests.length];
			    for(int i = 0; i<values.length; ++i){
			    	values[i] = quests[i].getContent();
			    }
			    

				return responseCode;
			}
			
			@Override
			public void onPostExecute(Integer responseCode){
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				        android.R.layout.simple_list_item_1, values);
				    setListAdapter(adapter);
				progressDialog.dismiss();
			}

		}
		
		
		
}
