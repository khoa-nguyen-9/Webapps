package com.webapps.puzzle;

import library.UserFunctions;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendRequestActivity extends Activity{
	
	ListView userList;
    Button submit;
 
    String[] listContent = {"User 1", "User 2", "User 3", "User 4",
    		                 "User 5"};
 
    /** Called when the activity is first created. */
 
    @Override
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(listContent.length == 0){
        	setContentView(R.layout.main);
        	LinearLayout ll = (LinearLayout)findViewById(R.id.basiclayout);

        	TextView text = new TextView(this);
        	text.setText("No requests.");
        	text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        	ll.addView(text);
        }else{
        
	        setContentView(R.layout.activity_ask);
	 
	        userList = (ListView)findViewById(R.id.friendlist);
	 
	        submit = (Button)findViewById(R.id.challengesubmit);
	        submit.setText("Add as friend");
	        
	        
	 
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	 
	                android.R.layout.simple_list_item_multiple_choice, listContent);
	 
	        userList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	        userList.setAdapter(adapter);
	 
	        submit.setOnClickListener(new Button.OnClickListener(){
	 
	            @Override
	            public void onClick(View v) {
	
	            	String selected = "";
	                int cntChoice = userList.getCount();
	                SparseBooleanArray sparseBooleanArray = 
	                		                  userList.getCheckedItemPositions();
	 
	                for(int i = 0; i < cntChoice; i++){
	                    if(sparseBooleanArray.get(i)) {
	                         selected += 
	                              userList.getItemAtPosition(i).toString() + "\n";
	                    }
	                 }
	                Toast.makeText(FriendRequestActivity.this,selected + 
	                		"added as friends",Toast.LENGTH_LONG).show();
	                
	                //TODO: add selected user to player's friends
	            }});
        }
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

}
