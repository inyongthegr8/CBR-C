package com.cbrc.gdt.builder;

import java.util.ArrayList;

import com.cbrc.ast.walker.DFSGenericTreeWalker;
import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.nodes.MissingToken;
import com.cbrc.nodes.NullPQNode;
import com.cbrc.nodes.gdt.PlanGDTNode;
import com.cbrc.tokens.LiteralToken;

public class CASTGDTBuilderImperfectMatcher {

	public CASTGDTBuilderImperfectMatcher() {
	}
	
	//TODO: Update all Levenshetin distance to pqGram Distance

//	public static PlanGDTNode getImperfectMatch(ArrayList<Node> arrayList, PlanGDTNode planNode) throws Exception {
//		PlanGDTNode currentPlan, bestPlan = null;
//		Node planASTNode = planNode.getASTNode();
//		Node currentPlanASTNode;
//		
//		//double pqGramDist = CASTGDTBuilderImperfectMatcher.getPQGramDistance(node1, node2);
//		
//		
//		int i = 0;// bestScore = Integer.MAX_VALUE, score;
//		double bestPQGram = Double.MAX_VALUE, pqGram;
//		//ArrayList<PlanGDTNode> planNodeAsString = convertTreeToStringDFS(planNode);
//		//ArrayList<PlanGDTNode> currentPlanNodeAsString;
//		
//		while (i < arrayList.size()) {
//			currentPlan = (PlanGDTNode) arrayList.get(i);
//			//currentPlanNodeAsString = convertTreeToStringDFS(currentPlan);
//			//score = applyLevenshteinDistance(planNodeAsString, currentPlanNodeAsString);
//			currentPlanASTNode = currentPlan.getASTNode();
//			pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
//			
//			if (i == 0 || pqGram < bestPQGram) {
//				bestPQGram = pqGram;
//				bestPlan = currentPlan;
//			}
//			
//			i++;
//		}
//		
//		return bestPlan;
//	}
	
//	public static PlanGDTNode getImperfectWorkingMatch(ArrayList<Node> arrayList, PlanGDTNode planNode) throws Exception {
//		PlanGDTNode currentPlan, bestPlan = null;
//		int i = 0, bestScore = Integer.MAX_VALUE, score;
//		ArrayList<PlanGDTNode> planNodeAsString = convertTreeToStringDFS(planNode);
//		ArrayList<PlanGDTNode> currentPlanNodeAsString;
//		
//		while (i < arrayList.size()) {
//			currentPlan = (PlanGDTNode) arrayList.get(i);
//			if (!currentPlan.isFaulty()) {
//				currentPlanNodeAsString = convertTreeToStringDFS(currentPlan);
//				score = applyLevenshteinDistance(planNodeAsString, currentPlanNodeAsString);
//				
//				if (i == 0 || score < bestScore) {
//					bestScore = score;
//					bestPlan = currentPlan;
//				}
//			}
//			i++;
//		}
//		
//		return bestPlan;
//	}
	
	public static PlanGDTNode getImperfectMatch(ArrayList<Node> arrayList, PlanGDTNode planNode, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode currentPlan, bestPlan = null;
		Node planASTNode = planNode.getASTNode();
		Node currentPlanASTNode;

		int i = 0;
		double bestPQGram = Double.MAX_VALUE, pqGram;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size()){
			currentPlan = (PlanGDTNode) indexedList.get(i);
		//while (i < arrayList.size()) {
			//currentPlan = (PlanGDTNode) arrayList.get(i);
			currentPlanASTNode = currentPlan.getASTNode();
			pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
			
			if (i == 0 || pqGram < bestPQGram) {
				bestPQGram = pqGram;
				bestPlan = currentPlan;
			}
			
			i++;
		}
		
		return bestPlan;
	}
	
	//TODO: Work with indexer
	public static PlanGDTNode getImperfectWorkingMatch(ArrayList<Node> arrayList, PlanGDTNode planNode, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode currentPlan, bestPlan = null;
		Node planASTNode = planNode.getASTNode();
		Node currentPlanASTNode;

		int i = 0;
		double bestPQGram = Double.MAX_VALUE, pqGram;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size()){
		//while (i < arrayList.size()) {
			currentPlan = (PlanGDTNode) indexedList.get(i);
			//currentPlan = (PlanGDTNode) arrayList.get(i);
			if (!currentPlan.isFaulty()) {
				currentPlanASTNode = currentPlan.getASTNode();
				pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
				
				if (i == 0 || pqGram < bestPQGram) {
					bestPQGram = pqGram;
					bestPlan = currentPlan;
				}
			}
			i++;
		}
		
		return bestPlan;
	}
	
	//TODO: Work with indexer
	public static PlanGDTNode getImperfectBuggyMatch(ArrayList<Node> arrayList, PlanGDTNode planNode, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode currentPlan, bestPlan = null;
		Node planASTNode = planNode.getASTNode();
		Node currentPlanASTNode;

		int i = 0;
		double bestPQGram = Double.MAX_VALUE, pqGram;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		//System.out.println("Starting comparison against " + indexedList.size() + " cases.");
		
		//System.out.println("Starting comparison against " + arrayList.size() + " cases.");
		
		while (i < indexedList.size()){
			currentPlan = (PlanGDTNode) indexedList.get(i);
		//while (i < arrayList.size()) {
			//currentPlan = (PlanGDTNode) arrayList.get(i);
			if (currentPlan.isFaulty()) {
				currentPlanASTNode = currentPlan.getASTNode();
				pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
				
				if (i == 0 || pqGram < bestPQGram) {
					bestPQGram = pqGram;
					bestPlan = currentPlan;
				}
			}
			i++;
		}
		
		return bestPlan;
	}
	
	public static PlanGDTNode getImperfectBuggyIndexedMatchWithPrint(ArrayList<Node> arrayList, PlanGDTNode planNode, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode currentPlan, bestPlan = null;
		Node planASTNode = planNode.getASTNode();
		Node currentPlanASTNode;

		int i = 0;
		double bestPQGram = Double.MAX_VALUE, pqGram;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		System.out.println("Starting comparison against " + indexedList.size() + " cases.");
		
		while (i < indexedList.size()){
			currentPlan = (PlanGDTNode) indexedList.get(i);
			if (currentPlan.isFaulty()) {
				currentPlanASTNode = currentPlan.getASTNode();
				pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
				
				if (i == 0 || pqGram < bestPQGram) {
					bestPQGram = pqGram;
					bestPlan = currentPlan;
				}
			}
			i++;
		}
		
		return bestPlan;
	}
	
	public static PlanGDTNode getImperfectBuggyNonIndexedMatchWithPrint(ArrayList<Node> arrayList, PlanGDTNode planNode, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode currentPlan, bestPlan = null;
		Node planASTNode = planNode.getASTNode();
		Node currentPlanASTNode;

		int i = 0;
		double bestPQGram = Double.MAX_VALUE, pqGram;
		
		System.out.println("Starting comparison against " + arrayList.size() + " cases.");

		while (i < arrayList.size()) {
			currentPlan = (PlanGDTNode) arrayList.get(i);
			if (currentPlan.isFaulty()) {
				currentPlanASTNode = currentPlan.getASTNode();
				pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
				
				if (i == 0 || pqGram < bestPQGram) {
					bestPQGram = pqGram;
					bestPlan = currentPlan;
				}
			}
			i++;
		}
		
		return bestPlan;
	}
	
	public static PlanGDTNode getImperfectBuggyMatchWithExceptions(ArrayList<Node> arrayList, PlanGDTNode planNode, ArrayList<PlanGDTNode> exceptions, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode currentPlan, bestPlan = null;
		Node planASTNode = planNode.getASTNode();
		Node currentPlanASTNode;
	
		int i = 0;
		double bestPQGram = Double.MAX_VALUE, pqGram;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size()){
			currentPlan = (PlanGDTNode) indexedList.get(i);
		//while (i < arrayList.size()) {
			//currentPlan = (PlanGDTNode) arrayList.get(i);
			if (currentPlan.isFaulty() && !exceptions.contains(currentPlan)) {
				currentPlanASTNode = currentPlan.getASTNode();
				pqGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planASTNode, currentPlanASTNode);
				
				if (i == 0 || pqGram < bestPQGram) {
					bestPQGram = pqGram;
					bestPlan = currentPlan;
				}
			}
			i++;
		}
		
		return bestPlan;
	}

	public static int applyLevenshteinDistance(ArrayList<PlanGDTNode> planNodeAsString, ArrayList<PlanGDTNode> currentPlanNodeAsString) throws Exception {
	    
		int[] v0 = new int[currentPlanNodeAsString.size() + 1] ;
		int[] v1 = new int[currentPlanNodeAsString.size() + 1] ;
	    
	    for (int i = 0; i < v0.length; i++)
	        v0[i] = i;
	    
	    for (int i = 0; i < planNodeAsString.size(); i++)
	    {
	    	v1[0] =  i + 1;
	    	
	    	for (int j = 0; j < currentPlanNodeAsString.size(); j++)
	        {
	    		int cost = 0;
	            if (!CASTGDTBuilderSubTreeMatcher.matchPlanSubTree(planNodeAsString.get(i), currentPlanNodeAsString.get(j)) == true) cost = 1;
	            
	            int value;
	            
	            if (v1[j] + 1 <= v0[j + 1] + 1 && v1[j] + 1 <= v0[j] + cost) {
	            	value = v1[j] + 1;
	            } else if (v0[j + 1] + 1 <= v1[j] + 1 && v0[j + 1] + 1 <=  v0[j] + cost) {
	            	value = v0[j + 1] + 1;
	            } else value = v0[j] + cost;
	            
	            v1[j + 1] = value;
	        }
	    	
	    	for (int j = 0; j < v0.length; j++)
	    		v0[j] = v1[j];
	    }
	    
		return v1[currentPlanNodeAsString.size()];
	}
	
	public static ArrayList<Token> getDiffString(ArrayList<Token> planNodeAsString, ArrayList<Token> currentPlanNodeAsString) throws Exception {
		int[][] C = new int[planNodeAsString.size() + 1][currentPlanNodeAsString.size() + 1] ;
		ArrayList<Token> diffTokens = new ArrayList<Token>();
		for (int i = 0; i <= planNodeAsString.size(); i++) {
			C[i][0] = 0;
		}
		
		for (int i = 0; i <= currentPlanNodeAsString.size(); i++) {
			C[0][i] = 0;
		}
		
		int i = 1, j =1;
		for (i = 1; i <= planNodeAsString.size(); i++) {
			for (j = 1; j <= currentPlanNodeAsString.size(); j++) {
				if (CASTGDTBuilderSubTreeMatcher.matchSubTree(planNodeAsString.get(i - 1), currentPlanNodeAsString.get(j - 1))) {
					C[i][j] = C[i-1][j-1] + 1;
				}
				else {
					C[i][j] = Math.max(C[i][j-1], C[i-1][j]);
				}
			}
		}
		
		printDiff(C, planNodeAsString, currentPlanNodeAsString, i - 1, j - 1, diffTokens);
		
		return diffTokens;
	}
	
	private static void printDiff(int[][] C, ArrayList<Token> planNodeAsString, ArrayList<Token> currentPlanNodeAsString, int i, int j, ArrayList<Token> diffTokens) throws Exception {
		if (i > 0 && j > 0 && CASTGDTBuilderSubTreeMatcher.matchSubTree(planNodeAsString.get(i - 1), currentPlanNodeAsString.get(j - 1))) {
			printDiff(C, planNodeAsString, currentPlanNodeAsString, i-1, j-1, diffTokens);
			diffTokens.add(planNodeAsString.get(i - 1));
		} else if (j > 0 && (i == 0 || C[i][j-1] >= C[i-1][j])) {
			printDiff(C, planNodeAsString, currentPlanNodeAsString, i, j-1, diffTokens);
			diffTokens.add(currentPlanNodeAsString.get(j - 1));
		} else if (i > 0 && (j == 0 || C[i][j-1] < C[i-1][j])) {
			printDiff(C, planNodeAsString, currentPlanNodeAsString, i-1, j, diffTokens);
			diffTokens.add(planNodeAsString.get(i - 1));
		}
			
	}

	public static ArrayList<PlanGDTNode> convertTreeToStringDFS(PlanGDTNode plan) throws Exception {
		DFSGenericTreeWalker planWalker = new DFSGenericTreeWalker(plan);
		ArrayList<PlanGDTNode> string = new ArrayList<PlanGDTNode>();
		while (!planWalker.isFinished()) {
			string.add((PlanGDTNode) planWalker.getCurrentNode());
			planWalker.getNextNode();
		}
		return string;
	}
	
	public static ArrayList<Token> convertTreeToTokenString(PlanGDTNode plan) throws Exception {
		DFSGenericTreeWalker astNodeWalker = new DFSGenericTreeWalker(plan.getASTNode());
		ArrayList<Token> string = new ArrayList<Token>();
		while(!astNodeWalker.isFinished()) {
			if (astNodeWalker.getCurrentNode() instanceof Token) {
				string.add((Token) astNodeWalker.getCurrentNode());
			}
			astNodeWalker.getNextNode();
		}
		return string;
	}
	
	public static ArrayList<Node[]> create23Grams(Node node) throws Exception {
		ArrayList<Node[]> pqGrams = new ArrayList<Node[]>();
		Node[] pqAnc = new Node[2];
		initializePQAnc(pqAnc);
		return pqGramRecurse(pqGrams, node, pqAnc);
	}
	
	private static ArrayList<Node[]> pqGramRecurse(ArrayList<Node[]> pqGrams, Node r, Node[] pqAnc) {
		Node[] pqSib = new Node[3];
		shiftPQAncestor(pqAnc, r);
		initializePQSib(pqSib);
		
		if (r.isLeafNode()) {
			pqGrams.add(unionPQAncSib(pqAnc, pqSib));
		} else {
			for(Node child:r.getChildNodes()) {
				shiftPQSibling(pqSib, child);
				pqGrams.add(unionPQAncSib(pqAnc, pqSib));
				pqGrams = pqGramRecurse(pqGrams, child, pqAnc);
			}
			
			for(int k = 1; k <= 2; k++) {
				shiftPQSibling(pqSib, new NullPQNode());
				pqGrams.add(unionPQAncSib(pqAnc, pqSib));
			}
		}
		
		return pqGrams;
	}

	private static Node[] unionPQAncSib(Node[] pqAnc, Node[] pqSib) {
		int i = 0;
		Node[] pqGram = new Node[5];
		for (i = 0; i < 2; i++) {
			pqGram[i] = pqAnc[i];
		}
		for (; i < 5; i++) {
			pqGram[i] = pqSib[i-2];
		}
		return pqGram;
	}

	private static void initializePQSib(Node[] pqSib) {
		int i = 0;
		for (i = 0; i < 3; i++) {
			pqSib[i] = new NullPQNode();
		}
	}

	private static void initializePQAnc(Node[] pqAnc) {
		int i = 0;
		for (i = 0; i < 2; i++) {
			pqAnc[i] = new NullPQNode();
		}
	}

	private static void shiftPQAncestor(Node[] pqAnc, Node node) {
		pqAnc[0] = pqAnc[1];
		pqAnc[1] = node;	
	}
	
	private static void shiftPQSibling(Node[] pqSib, Node node) {
		pqSib[0] = pqSib[1];
		pqSib[1] = pqSib[2];
		pqSib[2] = node;
	}
	
	public static Node getAnchorNode(Node[] pqGram) {
		return pqGram[1];
	}
	
	private static Boolean matchPQGram(Node[] pqGram1, Node[] pqGram2) {
		Boolean match = true;
		int i = 0;
		while (i < 5 && match == true) {
			if (!pqGram1[i].getClass().equals(pqGram2[i].getClass())) match = false;
			else if (pqGram1[i] instanceof LiteralToken && pqGram2[i] instanceof LiteralToken) {
				if (!((Token) pqGram1[i]).getToken().equals(((Token) pqGram2[i]).getToken())) {
					match = false;
				}
			}
			i++;
		}
		return match;
	}
	
	private static boolean isBlankPQGram(Node[] pqGram) {
		for (int i = 0; i < 5; i++) {
			if (!(pqGram[i] instanceof NullPQNode)) return false;
		}
		return true;
	}
	
	private static int countMatchPQGrams(ArrayList<Node[]> pqGramsPlanRoot, ArrayList<Node[]> pqGramsBestPlanRoot, ArrayList<Node> mismatches) {
		int countMatch = 0;
		int j = 0;
		int i = 0;
		boolean found = false;
		
		//TODO: Also add pqGram Anchors of pqGramsBestPlanRoot to mismatch list that do not match
		
		while (i < pqGramsPlanRoot.size()) {
			found = false;
			j = 0;
			while (j < pqGramsBestPlanRoot.size() && found == false) {
				if (matchPQGram(pqGramsPlanRoot.get(i), pqGramsBestPlanRoot.get(j))) {
					found = true;
					pqGramsBestPlanRoot.set(j, createBlankPQGram());
				}
				j++;
			}
			
			if (found == false) {
				Node node = getAnchorNode(pqGramsPlanRoot.get(i));
				//if (!mismatches.contains(node)) 
					mismatches.add(node);
			} else {
				countMatch++;
			}
			i++;
		}
		
		mismatches.add(new MissingToken());
		
		j = 0;
		while (j < pqGramsBestPlanRoot.size()) {
			if (!isBlankPQGram(pqGramsBestPlanRoot.get(j))) {
				Node node = getAnchorNode(pqGramsBestPlanRoot.get(j));
				//if (!mismatches.contains(node)) 
					mismatches.add(node);
			}
			j++;
		}
		
		return countMatch;
	}

	private static Node[] createBlankPQGram() {
		Node[] blankPQGram = new Node[5];
		for (int k = 0; k < 5; k++) {
			blankPQGram[k] = new NullPQNode();
		}
		return blankPQGram;
	}
	
	public static double getPQGramDistancePlanNodes(PlanGDTNode planRoot, PlanGDTNode bestPlanRoot) throws Exception {
		ArrayList<Node> mismatches = new ArrayList<Node>();
		return getPQGramDistancePlanNodesWithMismatches(planRoot, bestPlanRoot, mismatches);
	}
	
	public static double getPQGramDistancePlanNodesWithMismatches(PlanGDTNode planRoot, PlanGDTNode bestPlanRoot, ArrayList<Node> mismatches) throws Exception {
		return getPQGramDistanceWithMistmatchNodes(planRoot.getASTNode(), bestPlanRoot.getASTNode(), mismatches);
	}
	
	public static double getPQGramDistance(Node planRoot, Node bestPlanRoot) throws Exception {
		ArrayList<Node> mismatches = new ArrayList<Node>();
		return getPQGramDistanceWithMistmatchNodes(planRoot, bestPlanRoot, mismatches);
	}
	
	public static double getPQGramDistanceWithMistmatchNodes(Node planRoot, Node bestPlanRoot, ArrayList<Node> mismatches) throws Exception {
		ArrayList<Node[]> pqGramsPlanRoot = create23Grams(planRoot);
		ArrayList<Node[]> pqGramsBestPlanRoot = create23Grams(bestPlanRoot);
		
		int denom = pqGramsBestPlanRoot.size() + pqGramsPlanRoot.size();
		int numer = countMatchPQGrams(pqGramsPlanRoot, pqGramsBestPlanRoot, mismatches);
		
		//System.out.println("DENOM: " + denom);
		//System.out.println("NUMER: " + numer);
		
		return (1 - (2 * ((numer * 1.0) / (denom * 1.0))));
	}
	
}