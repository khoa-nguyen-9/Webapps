package com.webapps.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HintsInfo extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hints);
        
        
        //if(q.gethint1().isChecked()){
        TextView hint1 = (TextView) findViewById(R.id.showhint1);
        //hint1.setText("Hint 1:\n" + question.getHints().get(0));
        
        hint1.setText("Hint 1:\n" +
        				"Lorem ipsum dolor sit amet, consectetur "
        				+ "adipiscing elit. Ut posuere dui et dictum cursus. "
        				+ "Integer ac dui sem. Morbi non semper nulla.");
        //}
        
      //if(q.gethint2().isChecked()){
        TextView hint2 = (TextView) findViewById(R.id.showhint2);
      //hint2.setText("Hint 1:\n" + question.getHints().get(1));
        hint2.setText("Hint 2:\n" +
        				"Lorem ipsum dolor sit amet, consectetur "
        				+ "adipiscing elit. Ut posuere dui et dictum cursus. "
        				+ "Integer ac dui sem. Morbi non semper nulla.");
        //}
        
      //if(q.gethint3().isChecked()){
        TextView hint3 = (TextView) findViewById(R.id.showhint3);
      //hint3.setText("Hint 1:\n" + question.getHints().get(2));
        hint3.setText("Hint 3:\n" +
        				"Lorem ipsum dolor sit amet, consectetur "
        				+ "adipiscing elit. Ut posuere dui et dictum cursus. "
        				+ "Integer ac dui sem. Morbi non semper nulla.");
        //}
	}
}