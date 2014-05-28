package com.webapps.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Settings extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        TextView about = (TextView) findViewById(R.id.aboutText);
        about.setText("settings go here. light/dark theme, interests, etc");
        
	}
}
