package com.cbrc.gdt.builder;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTBuilderPerfectMatcher {
	
	//TODO: Work with indexer
	public static ArrayList<PlanGDTNode> getPerfectMatchPlanArray(ArrayList<Node> arrayList, PlanGDTNode planNode, boolean debug, CASTGDTIndexer indexer) throws Exception {
		ArrayList<PlanGDTNode> planNodes = new ArrayList<PlanGDTNode>();
		PlanGDTNode plan;
		int i = 0;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size()){
			plan = (PlanGDTNode) indexedList.get(i);
		//while (i < arrayList.size()) {
			//plan = (PlanGDTNode) arrayList.get(i);
			if (debug == true) {
				//System.out.println("Currently comparing " + planNode.toString() + " with non buggy plan " + plan.toString());
			}
			
			if(CASTGDTBuilderSubTreeMatcher.matchPlanSubTree(planNode, plan)) {
				
				if (debug == true) {
					//System.out.println("MATCH!");
				}
				
				planNodes.add(plan);				
			}
			i++;
			
		}
		return planNodes;
	}
	
	//TODO: Work with indexer
	public static PlanGDTNode getPerfectMatchPlanIfExists(ArrayList<Node> arrayList, PlanGDTNode planNode, boolean debug, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode plan;
		Boolean isFound = false; int i = 0;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size() && isFound == false){
			plan = (PlanGDTNode) indexedList.get(i);
		//while (isFound == false && i < arrayList.size()) {
			//plan = (PlanGDTNode) arrayList.get(i);
			if (debug == true) {
				//System.out.println("Currently comparing " + planNode.toString() + " with non buggy plan " + plan.toString());
			}
			
			if(CASTGDTBuilderSubTreeMatcher.matchPlanSubTree(planNode, plan)) {
				
				if (debug == true) {
					//System.out.println("MATCH!");
				}
				
				return plan;					
			}
			i++;
			
		}
		return null;
	}
	
	public static PlanGDTNode getPerfectMatchNonBuggyPlanIfExists(ArrayList<Node> arrayList, PlanGDTNode planNode, boolean debug, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode plan;
		Boolean isFound = false; int i = 0;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size() && isFound == false){
			plan = (PlanGDTNode) indexedList.get(i);
		//while (isFound == false && i < arrayList.size()) {
			//plan = (PlanGDTNode) arrayList.get(i);
			
			if (!plan.isFaulty()) {
				if (debug == true) {
					//System.out.println("Currently comparing " + planNode.toString() + " with non buggy plan " + plan.toString());
				}
				
				if(CASTGDTBuilderSubTreeMatcher.matchPlanSubTree(planNode, plan)) {
					
					if (debug == true) {
						//System.out.println("MATCH!");
					}
					
					return plan;					
				}
			}
			i++;
			
		}
		return null;
	}
	
	public static boolean lookForPerfectMatchPlan(ArrayList<Node> arrayList, PlanGDTNode planNode, boolean debug, CASTGDTIndexer indexer) throws Exception {
		PlanGDTNode plan;
		Boolean isFound = false; int i = 0;
		
		ArrayList<Node> indexedList = indexer.indexedSearch(planNode);
		
		while (i < indexedList.size() && isFound == false){
			plan = (PlanGDTNode) indexedList.get(i);
		//while (isFound == false && i < arrayList.size()) {
			//plan = (PlanGDTNode) arrayList.get(i);
			
			if (debug == true) {
				//System.out.println("Currently comparing " + planNode.toString() + " to " + plan.toString());
			}
			
			/* if statement performs perfect match */
			if(CASTGDTBuilderSubTreeMatcher.matchPlanSubTree(planNode, plan)) {
				
				if (debug == true) {
					//System.out.println("MATCH!");
				}
				
				isFound = true;
				
			}
			
			if (debug == true) {
				//System.out.println();
			}
			
			i++;
		}
		
		//this.getSuperGoal().getChildNodes().remove(planNode);
		//planNode.setParentNode(null);
		return isFound;
	}
}