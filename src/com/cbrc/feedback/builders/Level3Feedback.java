package com.cbrc.feedback.builders;

public class Level3Feedback {

	private int feedbackID;
	private int rating;
	private String feedbackString;
	
	public Level3Feedback(int feedbackID, int rating, String feedbackString) {
		this.feedbackID = feedbackID;
		this.rating = rating;
		this.feedbackString = feedbackString;
	}

	public int getFeedbackID() {
		return feedbackID;
	}

	public void setFeedbackID(int feedbackID) {
		this.feedbackID = feedbackID;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFeedbackString() {
		return feedbackString;
	}

	public void setFeedbackString(String feedbackString) {
		this.feedbackString = feedbackString;
	}
}
