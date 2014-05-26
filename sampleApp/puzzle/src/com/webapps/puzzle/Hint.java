package com.webapps.puzzle;

public class Hint {
	private static Location loc;  
	private static String content;
	
	public Hint (Location loc, String con) {
		this.loc = loc;
		this.content = con;
	}
	
	// TODO: write a class to get the location ID from hint table
	public Hint getHint(int hintID) {
		return null;
	}
	
	// TODO: write a class to get the location ID from hint table
	public int getLocID() {
		return 1;
	}
}
