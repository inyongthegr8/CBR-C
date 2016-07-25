package com.cbrc.feedback.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.cbrc.db.utils.DerbyUtils;

public class CASTGDTLevel3FeedbackRater {
	
	private CASTGDTLevel3FeedbackBuilder builder;
	private String response;
	
	public CASTGDTLevel3FeedbackRater(CASTGDTLevel3FeedbackBuilder builder) {
		this.builder = builder;
	}
	
	public void rateFeedback() throws IOException, SQLException {
		int feedbackID = builder.getFeedbackID();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Boolean valid = false;
		
		System.out.println();
		while (valid == false) {
			System.out.println("Was this information useful to you? (Y/N) ");
			this.response = this.getInput(br);
			if (this.response.equals("Y") || this.response.equals("N")) valid = true;
			else System.out.println("Invalid Input!");
		}
		
		if (this.response.equals("Y")) DerbyUtils.upVoteFeedback(feedbackID);
		else if (this.response.equals("N")) DerbyUtils.downVoteFeedback(feedbackID);
		
	}
	
	
	/* DN: For the testing, the students were only required to say if the feedback had been helpful to them. This function is
	 * supposed to handle feedback rating from 1 to 5.
	 */
	public void rate1To5Feedback() throws IOException, SQLException {
		int feedbackID = builder.getFeedbackID();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Boolean valid = false;
		
		System.out.println();
		int rating = 0;
		while (valid == false) {
			System.out.println("Was this information useful to you? Please rate the feedback from 1 to 5, 1 being the lowest and 5 being the highest. ");
			this.response = this.getInput(br);
			rating = Integer.parseInt(this.response);
			
			if (rating <= 5 && rating >= 1) valid = true;
			else System.out.println("Invalid Input!");
		}
		
		DerbyUtils.rateFeedback(feedbackID, rating);
	}
	
	private String getInput(BufferedReader br) throws IOException {
		String input = "";
		while (input.length() == 0 ) {
			input = br.readLine();
		    if (input.length() == 0 ) 
		       System.out.println("Nothing entered: ");
		}
		return input;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
