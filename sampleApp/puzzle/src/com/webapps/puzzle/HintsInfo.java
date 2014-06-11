package com.webapps.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HintsInfo extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hints);
        
        Hint info1 = (Hint) getIntent().getSerializableExtra("hint0");
        Hint info2 = (Hint) getIntent().getSerializableExtra("hint1");
        Hint info3 = (Hint) getIntent().getSerializableExtra("hint2");
        
        
        //q.gethint1().isChecked()
        TextView hint1 = (TextView) findViewById(R.id.showhint1);
        //hint1.setText("Hint 1:\n" + question.getHints().get(0));
                    
        String h = info1 == null? "Check in at the first hint location "
        		+ "to unlock the hint." : info1.getContent();
        hint1.setText("Hint 1:\n" + h + "\n");
        
        
        //q.gethint2().isChecked())
        TextView hint2 = (TextView) findViewById(R.id.showhint2);
        //hint2.setText("Hint 1:\n" + question.getHints().get(1));
       
        h = info2 == null? "Check in at the second hint location "
        		+ "to unlock the hint." : info2.getContent();
        hint2.setText("Hint 2:\n" + h + "\n");
        
        
        //if(q.gethint3().isChecked()){
        TextView hint3 = (TextView) findViewById(R.id.showhint3);
        //hint3.setText("Hint 1:\n" + question.getHints().get(2));
        
        h = info3 == null? "Check in at the third hint location "
        		+ "to unlock the hint." : info3.getContent();
        hint3.setText("Hint 3:\n" + h);
        
	}
}