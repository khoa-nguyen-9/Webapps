package com.webapps.puzzle;

public class Hint {
	private Location loc;	// The location associated with the hint  
	private String content;	// The content of the hint
	
	public Hint (Location loc, String con) {
		this.loc = loc;
		this.content = con;
	}
	
	// Return the location associated with the hint
	public Location getLocation() {
		return loc;
	}
	
	// Return the content of the hint
	public String getContent() {
		return content;
	}
	
	// TODO: make a class to populate database to a location 
	// Return an location object that has information
	public Location getLocData(int locID) {
		return null;
	}
	
	// TODO: write a class to get the location ID from hint table
	public int getLocID() {
		return 1;
	}
}
