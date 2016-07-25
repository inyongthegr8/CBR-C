package com.cbrc.ast.matcher;

import java.util.ArrayList;

import com.cbrc.ast.matcher.consts.NodeWeight;
import com.cbrc.ast.matcher.factory.RootToLeafPathFactory;
import com.cbrc.ast.matcher.structures.RootToLeafPath;
import com.cbrc.base.Node;
import com.cbrc.nodes.FunctionNode;
import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.functions.FunctionBodyNode;
import com.cbrc.nodes.functions.body.BodyProperNode;

public class OldPatternMatcher {
	
	private TranslationUnitNode mainTree;
	private TranslationUnitNode matchTree;
	private RootToLeafPathFactory pathFactory;
	private boolean debug = false;
	private double finalMatchScore;
	private double patternScore;
	
	public OldPatternMatcher() {
		this.pathFactory = new RootToLeafPathFactory();
	}
	
	public OldPatternMatcher(TranslationUnitNode mainTree, TranslationUnitNode matchTree) {
		this();
		this.mainTree = mainTree;
		this.matchTree = matchTree;
	}

	public TranslationUnitNode getMainTree() {
		return mainTree;
	}

	public void setMainTree(TranslationUnitNode mainTree) {
		this.mainTree = mainTree;
	}

	public TranslationUnitNode getMatchTree() {
		return matchTree;
	}

	public void setMatchTree(TranslationUnitNode matchTree) {
		this.matchTree = matchTree;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public double getFinalMatchScore() {
		return finalMatchScore;
	}

	public void setFinalMatchScore(double finalMatchScore) {
		this.finalMatchScore = finalMatchScore;
	}
	
	public double getPatternScore() {
		return patternScore;
	}

	public void setPatternScore(double patternScore) {
		this.patternScore = patternScore;
	}

	public void init() {
		getRecursePatternScore(matchTree);
	}
	
	private void getRecursePatternScore(Node node) {
		this.patternScore += NodeWeight.getWeight(node);
		
		for(Node childNode:node.getChildNodes()) {
			this.getRecursePatternScore(childNode);
		}
	}

	public double dfsMatch() throws Exception {
		
		this.finalMatchScore = 0.0;
		
		ArrayList<Node> bodyProperNodes = getBodyProperNodes();
		if (bodyProperNodes != null) {
			
			for (Node rootToSearchNode:bodyProperNodes) {
				//SCORE IS ONLY A PLACEHOLDER
				//Node matchFoundNode = 
				recurseDFSTraverse(rootToSearchNode, this.mainTree);
			}
			
			//FOR EACH BODY PROPER NODE, LOOK FOR CORRESPONDING NODE
			//IN MAIN TREE. ONCE FOUND, CHECK IF ALL CHILDREN OF
			//MATCH NODE EXISTS IN THE SUBTREE OF MAIN TREE, DFS STYLE
		}
		
		
		return this.finalMatchScore;
		
	}

	private void recurseDFSTraverse(Node rootToSearchNode, Node searchNode) {
		// TODO Auto-generated method stub
		// RECURSIVELY DFS THROUGH ALL THE NODES OF THIS.MAINTREE
		// TO LOOK FOR MATCHED SUBTREES OF EACH ROOTTOSEARCHNODE
		
		if (this.debug == true) {
			System.out.println(rootToSearchNode.toString());
			System.out.println("MATCHING WITH: ");
			System.out.println(searchNode.toString());
		}
		
		//TODO: PERHAPS AT THIS POINT, ACCUMULATE A SCORE, THEN MAYBE
		//      CALL A FUNCITON TO HANDLE SUBTREE MATCHING
		if (searchNode.getClass().equals(rootToSearchNode.getClass())) {
			NodeWeight.getWeight(searchNode);
			if (this.debug == true) {
				System.out.println("MATCH FOUND!\n");
			}
			//return searchNode;
		}
		else {
			if (this.debug == true) {
				System.out.println("NO MATCH FOUND!\n");
			}
			
			if(searchNode.hasChildren()) {
				ArrayList <Node> childNodes = searchNode.getChildNodes();
				for (Node childNode:childNodes) {
						recurseDFSTraverse(rootToSearchNode, childNode);
				}
			}
			//return null;
		}
	}

	private ArrayList<Node> getBodyProperNodes() {
		ArrayList <Node> translationChildren = this.matchTree.getChildNodes();
		for (Node tc:translationChildren) {
			if (tc instanceof FunctionNode) {
				if (((FunctionNode) tc).getFunctionIdentifier().getToken().equals("main")) {
					ArrayList <Node> mainFuncChildren = tc.getChildNodes();
						for (Node mfc:mainFuncChildren) {
							if (mfc instanceof FunctionBodyNode) {
								ArrayList <Node> fbnChildren = mfc.getChildNodes();
								for (Node fbnc:fbnChildren) {
									if (fbnc instanceof BodyProperNode) {
										return fbnc.getChildNodes();
									}
								}
							}
						}
				}
			}
		}
		return null;
	}
	
	public double match() throws Exception {
		
		double finalMatchScore = 0.0, totalPathMatchScore = 0.0, bestMatchScore = 0.0;
		
		this.pathFactory.setRootNode(mainTree);
		ArrayList<RootToLeafPath> mainTreeRTLPaths = this.pathFactory.generateRootToLeafPaths();
		
		this.pathFactory.setRootNode(matchTree);
		ArrayList<RootToLeafPath> matchTreeRTLPaths = this.pathFactory.generateRootToLeafPaths();
		
		for (RootToLeafPath matchTreePath: matchTreeRTLPaths) {
			
			if (this.debug == true) {
				System.out.println("MATCHING PATH:");
				for (RootToLeafPath rtl: matchTreeRTLPaths) {
					for (Node node:rtl) {
						System.out.println(node.toString());
					}
					System.out.println();
				}
			}
			
			for (RootToLeafPath mainTreePath: mainTreeRTLPaths) {
				
				if (this.debug == true) {
					System.out.println("WITH:");
					for (RootToLeafPath rtl: mainTreeRTLPaths) {
						for (Node node:rtl) {
							System.out.println(node.toString());
						}
					}
				}
				
				double currentMatchScore = getPathMatchScore(matchTreePath, mainTreePath);
				
				if (this.debug == true) {
					System.out.printf("SCORE: %f\n", currentMatchScore);
					System.out.println();
				}
				
				if (bestMatchScore < currentMatchScore) bestMatchScore = currentMatchScore;
			}
			
			totalPathMatchScore += bestMatchScore;
			bestMatchScore = 0;
		}
		
		finalMatchScore = totalPathMatchScore / matchTreeRTLPaths.size();
		
		return finalMatchScore;
	}

	private double getPathMatchScore(RootToLeafPath matchTreePath,
			RootToLeafPath mainTreePath) {
		double pathMatchScore = 0.0, totalPathScore = 0.0;
		
		for (Node matchTreeNode: matchTreePath) {
			for (Node mainTreeNode: mainTreePath) {
				if (matchTreeNode.equals(mainTreeNode)) {
					totalPathScore += NodeWeight.getWeight(matchTreeNode);
				}
			}
		}
		
		pathMatchScore = totalPathScore / matchTreePath.getMaximumScore();
		
		return pathMatchScore;
		
	}


}
