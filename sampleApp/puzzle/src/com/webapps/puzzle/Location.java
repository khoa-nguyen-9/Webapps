package com.webapps.puzzle;

public class Location {
	private static float X;
    private static float Y;
    private static String name;
    
	public Location(float x, float y) {
        this.X = x;
        this.Y = y;
    }
	
	public Location(String name) {
		this.name = name;
	}
	
	public Location(float x, float y, String name) {
		this.X = x;
        this.Y = y;
        this.name = name;
	}
	
	public float getX() {
		return X;
	}
	
	public float getY() {
		return Y;
	}
	
	public String getName() {
		return name;
	}
	
	

}
