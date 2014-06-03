package com.webapps.puzzle;

import library.UserFunctions;
import android.app.Activity;
import android.content.Intent;
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
 
    String[] listContent = {"Location 1", "Location 2", "Location 3", "Location 4",
    		                 "Location 5"};
 
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        
     
        locationList = (ListView)findViewById(R.id.friendlist);
 
        submit = (Button)findViewById(R.id.challengesubmit);
        submit.setText("Show on map");
 
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
 
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
 
                for(int i = 0; i < cntChoice; i++){
                    if(sparseBooleanArray.get(i)) {
                         selected += 
                              locationList.getItemAtPosition(i).toString() + "\n";
                    }
                 }
                Toast.makeText(HintLocations.this,selected,Toast.LENGTH_LONG).show();
                Intent map =  //TODO make sure only the current selected location are shown on the map
                		new Intent(HintLocations.this.getApplicationContext(),
                				MapActivity.class);
                startActivity(map);
            }});
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
