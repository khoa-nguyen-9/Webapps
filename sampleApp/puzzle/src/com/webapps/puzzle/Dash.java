package com.webapps.puzzle;

import library.UserFunctions;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.webapps.puzzle.adapter.TabsPagerAdapter;

public class Dash extends FragmentActivity implements
	TabListener{
	
		private ViewPager viewpager;
		private TabsPagerAdapter mAdapter;
		private ActionBar actionBar;
		
		private String[] tabs = {"HOME","QUESTIONS","REQUESTS", "LEADERBOARD","MAPS"};
		
	    UserFunctions userFunctions;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        
	        userFunctions = new UserFunctions();
	        if(userFunctions.isUserLoggedIn(getApplicationContext())){
	       // user already logged in show databoard
	            setContentView(R.layout.dashboard);
	            viewpager = (ViewPager) findViewById(R.id.pager);
	    		actionBar = getActionBar();
	    		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
	    		
	    		viewpager.setAdapter(mAdapter);
	            actionBar.setHomeButtonEnabled(false);
	    		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    		
	    		
	    		for(String tab_name : tabs){
	    			actionBar.addTab(actionBar.newTab().setText(tab_name)
	    					.setTabListener(this));
	    		}
	    		
	    		//selects tabs accordingly after swiping
	    		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	    			 
	                @Override
	                public void onPageSelected(int position) {
	                    // on changing the page
	                    // make respected tab selected
	                    actionBar.setSelectedNavigationItem(position);
	                }
	     
	                @Override
	                public void onPageScrolled(int arg0, float arg1, int arg2) {
	                }
	     
	                @Override
	                public void onPageScrollStateChanged(int arg0) {
	                }
	            });
	 
	        }else{
	            // user is not logged in show login screen
	            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
	            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(login);
	            // Closing dashboard screen
	            finish();
	        }
	    }

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
}
