package com.webapps.puzzle;

public class Location { 
	private float X;
    private float Y;
    private String name;
    
    // Create new location with X and Y coordinate only
	public Location(float x, float y) {
        this.X = x;
        this.Y = y;
    }
	
	// Create new location with name only
	public Location(String name) {
		this.name = name;
	}
	
	// Create new location with X,Y coordinates and name
	public Location(float x, float y, String name) {
		this.X = x;
        this.Y = y;
        this.name = name;
	}
	
	// Get X coordinate
	public float getX() {
		return X;
	}
	
	// Get Y coordinate
	public float getY() {
		return Y;
	}
	
	// Get the name of the location
	public String getName() {
		return name;
	}

}
