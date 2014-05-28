package com.webapps.puzzle;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    String[] values = new String[] { "Question 1", "Question 2",
	    		"Question 3","Question 4","Question 5","Question 6",
	    		"Question 7","Question 8", "Question 9","Question 10",};
	    
	    /*String[] values = new String[questions.size()];
	    for(int i = 0; i<values.length; ++i){
	    	values[i] = questions.get(i).getContent();
	    }*/
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		  String item = (String) getListAdapter().getItem(position);
		  //Toast.makeText(this.getActivity(), item + " selected", Toast.LENGTH_LONG).show();
		  Intent question = new Intent(getActivity(), QuestionActivity.class);
		  //question.putExtra("question", questions.get(position));
		  question.putExtra("question", item);
	      startActivity(question);
	  }
}
