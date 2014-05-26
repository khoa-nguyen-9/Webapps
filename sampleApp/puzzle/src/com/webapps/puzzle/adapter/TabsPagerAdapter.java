package com.webapps.puzzle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.webapps.puzzle.HomeFragment;
import com.webapps.puzzle.LeaderBoardFragment;
import com.webapps.puzzle.MapsFragment;
import com.webapps.puzzle.QuestionFragment;
import com.webapps.puzzle.RequestFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int index) {
		
		switch(index){
			case 0:
				return new HomeFragment();
			case 1:
				return new QuestionFragment();
			case 2:
				return new RequestFragment();
			case 3:
				return new LeaderBoardFragment();
			case 4:
				return new MapsFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		
		return 5;
	}

}
