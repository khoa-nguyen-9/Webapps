package com.webapps.puzzle;

import java.util.ArrayList;

public class Question {
	private User maker;				// The user who make the question
	private ArrayList<Hint> hints;  // The list of hints for the question
	private String content;			// The content of the question 
	private int ranking; 			// The ranking of the question on leaderboard
	
	
	public Question(User maker, ArrayList<Hint> hints, String content) {
		this.maker = maker;
		this.hints = hints;
		this.content = content;
	}
	
	public User getMaker() {
		return maker;
	}
	
	public String getContent() {
		return content;
	}
	
	public int getRank() {
		return ranking;
	}
	
	public  ArrayList<Hint> getHints(int[] hintIDs) {
		return null;
	}
	
	// TODO: write a class to get the hint data
	public Hint getHint(int hintID) {
		return null;
	}
}
