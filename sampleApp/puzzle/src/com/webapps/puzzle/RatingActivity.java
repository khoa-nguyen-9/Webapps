package com.webapps.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RatingActivity extends Activity {
	
	private Question question;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        
        
        question = (Question) getIntent().getSerializableExtra("question");
        
        Button submit = (Button) findViewById(R.id.ok_button);
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RatingBar mBar = (RatingBar) findViewById(R.id.qRating);
				float rating = mBar.getRating();
				Toast.makeText(getApplicationContext(), 
						"You have rated it " + rating + " out of five",
						Toast.LENGTH_SHORT).show();
				finish();
				
			}
		});
	}

}
