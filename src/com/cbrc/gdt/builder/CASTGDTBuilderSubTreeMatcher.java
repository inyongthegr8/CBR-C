package com.cbrc.gdt.builder;

import com.cbrc.ast.walker.DFSGenericTreeWalker;
import com.cbrc.base.Node;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTBuilderSubTreeMatcher {
	
	public static boolean matchPlanSubTree(PlanGDTNode toBeMatched, PlanGDTNode toBeMatchedAgainst)throws Exception {
		Node node1 = toBeMatched.getASTNode();
		Node node2 = toBeMatchedAgainst.getASTNode();
		
		double pqGramDist = CASTGDTBuilderImperfectMatcher.getPQGramDistance(node1, node2);
		//System.out.println("PQGRAMDIST: " + pqGramDist);
		if (pqGramDist == 0) return true;
		
		return false;
	}

//	public static boolean matchPlanSubTree(PlanGDTNode toBeMatched, PlanGDTNode toBeMatchedAgainst)throws Exception {
//		DFSGenericTreeWalker tbmWalker = new DFSGenericTreeWalker(toBeMatched);
//		DFSGenericTreeWalker tbmaWalker = new DFSGenericTreeWalker(toBeMatchedAgainst);
//		
//		while (!tbmWalker.isFinished() && !tbmaWalker.isFinished()) {
//			PlanGDTNode currentTbmNode = (PlanGDTNode) tbmWalker.getCurrentNode();
//			PlanGDTNode currentTbmaNode = (PlanGDTNode) tbmaWalker.getCurrentNode();
//			// Put condition that checks for "Sameness" of the two plan nodes (nodes themselves, not subtree)
//			if (!currentTbmNode.getASTNode().getClass().equals(currentTbmaNode.getASTNode().getClass())) {
//				return false;
//			}
//			tbmWalker.nextNode();
//			tbmaWalker.nextNode();
//		}
//		
//		if (tbmWalker.isFinished() != true || tbmaWalker.isFinished() != true) return false;
//		
//		return true;
//	}
	
	public static boolean matchSubTree(Node toBeMatched, Node toBeMatchedAgainst)throws Exception {
		DFSGenericTreeWalker tbmWalker = new DFSGenericTreeWalker(toBeMatched);
		DFSGenericTreeWalker tbmaWalker = new DFSGenericTreeWalker(toBeMatchedAgainst);
		
		while (!tbmWalker.isFinished() && !tbmaWalker.isFinished()) {
			Node currentTbmNode = tbmWalker.getCurrentNode();
			Node currentTbmaNode = tbmaWalker.getCurrentNode();
			// Put condition that checks for "Sameness" of the two plan nodes (nodes themselves, not subtree)
			if (!currentTbmNode.getClass().equals(currentTbmaNode.getClass())) {
				return false;
			}
			tbmWalker.nextNode();
			tbmaWalker.nextNode();
		}
		
		if (tbmWalker.isFinished() != true || tbmaWalker.isFinished() != true) return false;
		
		return true;
	}
}