package com.webapps.puzzle;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class HowToPlayActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        TextView about = (TextView) findViewById(R.id.aboutText);
        about.setText("(VERY Rough) Rules:\n"+
        			  "1. Click on a question\n"+
        			  "2. Click on hint button to get hint location\n"+
        			  "3. Go to the location and check in\n"+
        			  "4. Get hints and answer question");
        
	}
}
