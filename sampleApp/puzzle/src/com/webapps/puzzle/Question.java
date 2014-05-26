package com.webapps.puzzle;

import java.util.ArrayList;

public class Question {
	private User maker;				// The user who make the question
	private ArrayList<Hint> hints;  // The list of hints for the question
	private String content;			// The content of the question
	private int ranking; 			// The ranking of the question on leaderboard
	private String answer;			// The answer of the question
	private int rewards;			// The rewards of the question
	
	private int[] hintIDs;			// The ID of the hints for the question
	
	public Question(User maker, ArrayList<Hint> hints, String content, String answer, int reward) {
		this.maker = maker;
		this.hints = hints;
		this.content = content;
		this.answer = answer;
		this.rewards = reward;
	}
	
	// TODO: Add function to lookup the hintIDs from the current questionID
	public Question(int questID) {
		hints = new ArrayList<Hint>();
	}
	
	// Look up the table and update the hints of the question
	public void updateHints() {
		hints = new ArrayList<Hint>();
		for (int i=0; i < hintIDs.length; i++ ) {
			hints.add(new Hint (hintIDs[i]));
		}
	}
	
	// Get the user who made the question
	public User getMaker() {
		return maker;
	}
	
	// Get the content of the question
	public String getContent() {
		return content;
	}
	
	// Get the rank of the question on leader board
	public int getRank() {
		return ranking;
	}
	
	// Get list of hints for the question
	public  ArrayList<Hint> getHints() {
		return hints;
	}
	
	// Get the answer of the question
	public String getAnswer() {
		return answer;
	}
	
	// Get the rewards for the question
	public int getRewards() {
		return rewards;
	}

}
