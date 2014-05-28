package com.webapps.puzzle;

import java.util.ArrayList;

public class User {
	private int userID;								// Unique userID
	private String userName;						// User Name
	private ArrayList<User> friendlist;				// List of friends of the user
	private ArrayList<Question> currentQuestion;	// List of questions for the user
	private ArrayList<Question> answeredQuestion;	// List of questions answered by the user
	private ArrayList<Location> achieve;			// List of location that the user has been to
	private int credits;							// Credits of the user
	
	private int[] friendIDs;						// IDs of friend list
	private int[] currentQuestionIDs;				// IDs of current question list
	private int[] answeredQuestionIDs;				// IDs of answered question list
	private int[] achieveIDs;						// IDs of the place the user has been to
	
	// TODO: Add function to lookup the IDs from the current userID
	public User (int userID) {
		this.userID = userID;
		this.friendlist = new ArrayList<User>();
		this.currentQuestion = new ArrayList<Question>();
		this.answeredQuestion = new ArrayList<Question>();
		this.achieve = new ArrayList<Location>();		
	}
	
	// // TODO: Add function to lookup the user data from the current UserID
	public void updateUser() {
		friendlist = new ArrayList<User>();
		for (int i=0; i < friendIDs.length; i++ ) {
			friendlist.add(new User (friendIDs[i]));
		}
		
		currentQuestion = new ArrayList<Question>();
		for (int i=0; i < currentQuestionIDs.length; i++ ) {
			currentQuestion.add(new Question (currentQuestionIDs[i]));
		}
		
		answeredQuestion = new ArrayList<Question>();
		for (int i=0; i < answeredQuestionIDs.length; i++ ) {
			answeredQuestion.add(new Question (answeredQuestionIDs[i]));
		}
		
		achieve = new ArrayList<Location>();
		for (int i=0; i < achieveIDs.length; i++ ) {
			achieve.add(new Location (achieveIDs[i]));
		}
	}
	
	// Return userID 
	public int getUserID () {
		return userID;
	}
	
	// Return the userName 
	public String getUserName() {
		return userName;
	}
	
	// Return the friend list
	public ArrayList<User> getFriendlist() {
		return friendlist;
	}
	
	// Return the current questions of the user
	public ArrayList<Question> getcurrentQuestion() {
		return currentQuestion;
	}
	
	// Return the list of question that the user answered   
	public ArrayList<Question> getAnsweredQuestion() {
		return answeredQuestion;
	}
	
	// Return a list of location where the user has been to 
	public ArrayList<Location> getAchieve() {
		return achieve;
	}
	
	// Return the credits of the user
	public int getCredits() {
		return credits;
	}
	
	// Add credits
	public void addCredits(int amount){
		this.credits += amount;
	}
	
	// Minus credits
	public void minusCredits(int amount) {
		this.credits  -= amount;
	}
}
