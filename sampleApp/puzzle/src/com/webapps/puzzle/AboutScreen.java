package com.webapps.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutScreen extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        TextView about = (TextView) findViewById(R.id.aboutText);
        about.setText("This application has been developped for"+
        		" the Webapps group project by second year Computing students"+
        	    " at Imperial College London.\n\n"+ 
        		"Developpers:\n"+
        		"Corina Niculae (cn1612) \n"+ 
        		"Claudia Mihai (cam212)\n" +
        		"Khoa Nguyen (dkn12) \n" +
        		"Christian Nkinsi (cn1312)\n\n"+
        		"version: 0.9.0 (Alpha)");
        
	}
}
