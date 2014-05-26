package com.webapps.puzzle;

public class Hint {
	private Location loc;	// The location associated with the hint  
	private String content;	// The content of the hint
	
	private int locID;		// The location ID to lookup in the database
	
	public Hint (Location loc, String con) {
		this.loc = loc;
		this.content = con;
	}
	
	// TODO: Add function to lookup the the locID from the current hintID
	public Hint (int hintID) {
		loc = null;
	}
	
	// Look up the table and update the location of the hint
	public void updateLoc () {
		loc = new Location(locID);
	}
	
	// Return the location associated with the hint
	public Location getLocation() {
		return loc;
	}
	
	// Return the content of the hint
	public String getContent() {
		return content;
	}
	
	// TODO: write a class to get the location ID from hint table
	public int getLocID() {
		return 1;
	}
}
