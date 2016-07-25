package com.cbrc.feedback.builders;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.gdt.builder.CASTGDTBuilderImperfectMatcher;
import com.cbrc.nodes.MissingToken;
import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;
import com.cbrc.tokens.operators.SemicolonToken;

public class CASTGDTLevel2FeedbackBuilder extends CASTGDTFeedbackBuilder {

	public CASTGDTLevel2FeedbackBuilder(HeadGDTNode headGDTNode) {
		super(headGDTNode);
	}

	public String getFeedbackText(Node planNode, Node bestPlan) throws Exception {
		StringBuilder builder = new StringBuilder();
		ArrayList<Node> mismatches = new ArrayList<Node>();
		
		//printPQGramTrace(planNode, bestPlan);
		//TODO: PQGRAM, for some reason, doesnt capture mismatches found in other pqGramList
		System.out.print("\n\nScore: ");
		System.out.print(CASTGDTBuilderImperfectMatcher.getPQGramDistanceWithMistmatchNodes(planNode, bestPlan, mismatches));
		
		if (!mismatches.isEmpty()) {
			builder.append("\n\nThese are the parts of your code that are potentially wrong:\n");
			for (Node node:mismatches) {
				if (node instanceof Token) {
					builder.append(((Token) node).getToken());
					if (node instanceof SemicolonToken || node instanceof MissingToken) {
						builder.append("\n");
					}
				}
			}
		}

		
		builder.append("\n\n");
		return builder.toString();
	}

	public void printPQGramTrace(Node planNode, Node bestPlan)
			throws Exception {
		ArrayList<Node[]> pqGrams1 = CASTGDTBuilderImperfectMatcher.create23Grams(planNode);
		ArrayList<Node[]> pqGrams2 = CASTGDTBuilderImperfectMatcher.create23Grams(bestPlan);
		
		System.out.println("\n\nPQGRAM TRACE: ");
		int i = 0;
		for (Node[] pqGram:pqGrams1) {
			i++;
			System.out.print(i + " - {");
			for (Node node:pqGram) {
				System.out.print(node.getClass());
				System.out.print(", ");
			}
			System.out.print("}\n");
		}
		System.out.print("\n");
		i = 0;
		for (Node[] pqGram:pqGrams2) {
			i++;
			System.out.print(i + " - {");
			for (Node node:pqGram) {
				System.out.print(node.getClass());
				System.out.print(", ");
			}
			System.out.print("}\n");
		}
	}
	
	@Override
	public String getFeedbackText(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		return getFeedbackText(planNode.getASTNode(), bestPlan.getASTNode());
	}

	@Override
	public String getFeedbackTextAsHTML(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
