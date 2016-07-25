package com.cbrc.feedback.builders;

import java.util.ArrayList;
import java.util.HashMap;

import com.cbrc.db.utils.DerbyUtils;
import com.cbrc.gdt.builder.CASTGDTBuilderImperfectMatcher;
import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTLevel3FeedbackBuilder extends CASTGDTFeedbackBuilder {

	private int feedbackID;
	private PlanGDTNode bestPlan;
	private PlanGDTNode planNode;
	
	public CASTGDTLevel3FeedbackBuilder(HeadGDTNode headGDTNode) {
		super(headGDTNode);
	}
	
	public ArrayList<Level3Feedback> getFeedbackArray(PlanGDTNode planNode, ArrayList<PlanGDTNode> matches, PlanGDTNode bestNonBuggyMatch) throws Exception {
		ArrayList<Level3Feedback> feedbacks = new ArrayList<Level3Feedback>();
		Level3Feedback feedback;
		for (PlanGDTNode match:matches) {
			feedback = DerbyUtils.getLevel3FeedbackWithRating(match.getDBID());
			feedback.setFeedbackString(feedback.getFeedbackString() + "\nConfidence level: " + ((1 - CASTGDTBuilderImperfectMatcher.getPQGramDistance(planNode.getASTNode(), match.getASTNode())) * 100) + "%");
			
			/* DN: Uncomment this to include pattern feedback. See DN on the function for additional information */
			//handlePatternFeedback(feedback, planNode, bestNonBuggyMatch);
			if (feedback != null) insertAtProper(feedbacks, feedback);
		}
		
		
		return feedbacks;
	}
	
	
	/* DN: This function handles pattern discrepancies. Improving this could help further improve the usefulness of the tool */
	private void handlePatternFeedback(Level3Feedback feedback, PlanGDTNode planNode2, PlanGDTNode bestNonBuggyMatch) {
		StringBuilder sb = new StringBuilder(feedback.getFeedbackString());
		
		ArrayList<String> planPatterns = planNode2.getPatterns();
		ArrayList<String> bestNonBuggyMatchPatterns = bestNonBuggyMatch.getPatterns();
		ArrayList<String> patternDiscrep = new ArrayList<String>();
		
		for (String s:bestNonBuggyMatchPatterns)
		{
			if(!planPatterns.contains(s)) {
				patternDiscrep.add(s);
			}
		}
		
		PatternList pList = new PatternList();
		if (!patternDiscrep.isEmpty()) {
			sb.append("The correct solution has the following pattern but your code does not:\n\n");
			for (String s:patternDiscrep) {
				sb.append(" - " + s + "\n");
			}
			
			sb.append("\n");
			for (String s:patternDiscrep) {
				if (pList.contains(s)) {
					sb.append("The " + s + " follows the syntax: ");
					sb.append(pList.getSyntax(s) + "\n\n");
				}
			}
		}

		feedback.setFeedbackString(sb.toString());
	}

	private void insertAtProper(ArrayList<Level3Feedback> feedbacks, Level3Feedback feedback) {
		boolean found = false;
		int i = 0;
		feedback.setFeedbackString("\n\nThese are the misconceptions manifested in your code: \n" + feedback.getFeedbackString() + "\n\n");
		
		while (found == false && i < feedbacks.size()) {
			if (feedbacks.get(i).getRating() < feedback.getRating()) found = true;
			else i++;
		}
		
		if (found == true) feedbacks.add(i, feedback);
		else feedbacks.add(feedback);
	}

	@Override
	public String getFeedbackText(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		this.bestPlan = bestPlan;
		this.planNode = planNode;
		String feedback = null;
		HashMap<Integer, String> map = DerbyUtils.getLevel3Feedback(bestPlan.getDBID());
		
		if (map.keySet().isEmpty()) return null;
		else {
			this.feedbackID = (int) map.keySet().toArray()[0];
			feedback = map.get(feedbackID);
			StringBuilder builder = new StringBuilder("\n\nThese are the misconceptions manifested in your code: \n");
			builder.append(feedback);
			builder.append("\n\n");
			
			return builder.toString();
		}
	}

	@Override
	public String getFeedbackTextAsHTML(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		String feedback = null;
		HashMap<Integer, String> map = DerbyUtils.getLevel3Feedback(bestPlan.getDBID());
		
		if (map.keySet().isEmpty()) return null;
		else {
			this.feedbackID = (int) map.keySet().toArray()[0];
			feedback = map.get(feedbackID);
			StringBuilder builder = new StringBuilder("<p>These are the misconceptions manifested in your code: </p>");
			builder.append("<p>").append(feedback).append("</p>");
			
			return builder.toString();
		}
	}

	public int getFeedbackID() {
		return feedbackID;
	}

	public void setFeedbackID(int feedbackID) {
		this.feedbackID = feedbackID;
	}

	public PlanGDTNode getBestPlan() {
		return bestPlan;
	}

	public void setBestPlan(PlanGDTNode bestPlan) {
		this.bestPlan = bestPlan;
	}

	public PlanGDTNode getPlanNode() {
		return planNode;
	}

	public void setPlanNode(PlanGDTNode planNode) {
		this.planNode = planNode;
	}

}
