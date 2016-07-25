package com.cbrc.feedback.builders;

import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTLevel1FeedbackBuilder extends CASTGDTFeedbackBuilder {

	public CASTGDTLevel1FeedbackBuilder(HeadGDTNode headGDTNode) {
		super(headGDTNode);
	}

	@Override
	public String getFeedbackText(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
//		ArrayList<PlanGDTNode> planNodeAsString = CASTGDTBuilderImperfectMatcher.convertTreeToStringDFS(planNode);
//		ArrayList<PlanGDTNode> bestPlanAsString = CASTGDTBuilderImperfectMatcher.convertTreeToStringDFS(bestPlan);
//		
//		int errorCount = CASTGDTBuilderImperfectMatcher.applyLevenshteinDistance(planNodeAsString, bestPlanAsString);
		
//		DFSBracketNotationWalker bracket1 = new DFSBracketNotationWalker(planNode.getASTNode());
//		DFSBracketNotationWalker bracket2 = new DFSBracketNotationWalker(bestPlan.getASTNode());
//		int errorCount = (int) RTED.computeDistance(bracket1.getBracketNotation(), bracket2.getBracketNotation());
//		
//		LinkedList<int[]> editMappings = RTED.computeMapping(bracket1.getBracketNotation(), bracket2.getBracketNotation());
//		StringBuilder sb = new StringBuilder();
//		
//		for (int[] editMap:editMappings) {
//			sb.append("[");
//			sb.append(editMap[0]);
//			sb.append(" -> ");
//			sb.append(editMap[1]);
//			sb.append("]\n");
//		}
		
		return "\n\nWrong code. Can you identify the error?\n\n";
	}

	@Override
	public String getFeedbackTextAsHTML(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
//		ArrayList<PlanGDTNode> planNodeAsString = CASTGDTBuilderImperfectMatcher.convertTreeToStringDFS(planNode);
//		ArrayList<PlanGDTNode> bestPlanAsString = CASTGDTBuilderImperfectMatcher.convertTreeToStringDFS(bestPlan);
//		
//		int errorCount = CASTGDTBuilderImperfectMatcher.applyLevenshteinDistance(planNodeAsString, bestPlanAsString);
		
		return "<p>Wrong code. Can you identify the error?</p>";
	}

}
