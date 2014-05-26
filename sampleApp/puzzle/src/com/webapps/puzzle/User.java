package com.webapps.puzzle;

import java.util.ArrayList;

public class User {
	private int userID;
	private String userName;
	private ArrayList<User> friendlist;
	private ArrayList<Question> currentQuestion;
	private ArrayList<Question> answerQuestion;
	private ArrayList<Location> achieve;
	
	
	public int getUserID () {
		return 1;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public ArrayList<User> getFriendlist() {
		return friendlist;
	}
	
	public ArrayList<Question> getcurrentQuestion() {
		return currentQuestion;
	}
	
	public ArrayList<Question> getAnswerQuestion() {
		return answerQuestion;
	}
	
	public ArrayList<Location> getAchieve() {
		return achieve;
	}
}
