package com.webapps.puzzle;

import java.util.ArrayList;

public class User {
	private int userID;
	private String userName;
	private ArrayList<User> friendlist;
	private ArrayList<Question> currentQuestion;
	private ArrayList<Question> answeredQuestion;
	private ArrayList<Location> achieve;
	private int credits;
	
	// TODO: Look up the table and create new question that has the provided ID
	public User (int userID) {
		this.userID = userID;
		this.friendlist = new ArrayList<User>();
		this.currentQuestion = new ArrayList<Question>();
		this.answeredQuestion = new ArrayList<Question>();
		this.achieve = new ArrayList<Location>();
		// int[] questionIDs;
		// for (int i=0; i < questionIDs.length; i++ ) {
		//	currentQuestion.add(new Question (questionIDs[i]));
		// }
		
		// int[] questionIDs;
		// for (int i=0; i < questionIDs.length; i++ ) {
		//	answeredQuestion.add(new Question (questionIDs[i]));
		// }
		
		// int[] locIDs;
		// for (int i=0; i < locIDs.length; i++ ) {
		//	achieve.add(new Location (locIDs[i]));
		// }
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
