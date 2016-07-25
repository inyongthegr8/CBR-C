package com.cbrc.gdt.builder;

public class CASTGDTBuilderOldCode {
//	private static double MATCH_TRESHOLD = 0.7;
//	private PlanGDTNode getImperfectMatch(PlanGDTNode planNode) throws Exception {
//	DFSGenericTreeWalker subPlanWalkerA;
//	DFSGenericTreeWalker subPlanWalkerB;
//	PlanGDTNode plan, bestPlan = null;
//	int i = 0, matchCount, mismatchCount, highMatchCount = 0, lowMismatchCount = Integer.MAX_VALUE, subPlanCount = 0, bestSubPlanCount = 0;
//	
//	while (i < this.getSuperGoal().getChildNodes().size()) {
//		plan = (PlanGDTNode) this.getSuperGoal().getChildNodes().get(i);
//		subPlanWalkerB = new DFSGenericTreeWalker(plan);
//		//subPlanWalkerA = new DFSGenericTreeWalker(planNode);
//		matchCount = 0;
//		mismatchCount = 0;
//		subPlanCount = 0;
//				
//		while (!subPlanWalkerB.isFinished()) {
//			subPlanWalkerA = new DFSGenericTreeWalker(planNode);	
//			while (!subPlanWalkerA.isFinished()) {
//			
//				if (matchPlanSubTree((PlanGDTNode) subPlanWalkerA.getCurrentNode(), (PlanGDTNode) subPlanWalkerB.getCurrentNode())) {
//					matchCount = matchCount + 1;
//				} else {
//					mismatchCount = mismatchCount + 1;
//				}
//				
//				subPlanWalkerA.getNextNode();
//			}
//			subPlanCount++;
//			subPlanWalkerB.getNextNode();
//		}
//		
//		if (i == 0) {
//			highMatchCount =  matchCount;
//			lowMismatchCount = mismatchCount;
//			bestSubPlanCount = subPlanCount;
//			bestPlan = plan;
//		}
//		
//		if (matchCount >= highMatchCount) {
//			highMatchCount = matchCount;
//			bestPlan = plan;
//			bestSubPlanCount = subPlanCount;
//		} else if (matchCount == highMatchCount) {
//			if (mismatchCount < lowMismatchCount) {
//				lowMismatchCount = mismatchCount;
//				bestPlan = plan;
//				bestSubPlanCount = subPlanCount;
//			}
//		}
//		i++;
//		
//	}
//	
//	if (!isPossibleMatch(highMatchCount, lowMismatchCount, bestSubPlanCount)) bestPlan = null;
//	
//	return bestPlan;
//}
	
//	private boolean isPossibleMatch (int matchCount, int mismatchCount, int subPlanCount) {
//		
//		/* temporary formula */
//		
//		if (matchCount / subPlanCount > MATCH_TRESHOLD) return true;
//		return false;
//	}
//
//	public void processCodeOld(TranslationUnitNode transUnitNode) throws Exception {
//		if (this.getSuperGoal().getChildNodes().isEmpty()) {
//			PlanGDTNode planNode = this.createNewPlan(transUnitNode);
//			if (this.debug == true) {
//				System.out.println("GDT empty, appending " + planNode.toString() + " to GDT\n");
//			}
//		}
//		else {
//			/* TODO: Create a utility that gives out planNode ID, then each time a plan Node is generated, assign an ID to it */
//			PlanGDTNode planNode = this.createNewPlan(transUnitNode);
//			
//			/* function call attaches plan to supergoal node */
//			
//			lookForPerfectMatchPlan(planNode);
//			
//			if (planNode.getParentNode() != null) {
//
//				PlanGDTNode bestPlan = getImperfectMatch(planNode);
//				
//				if (bestPlan != null) {
////					this.getSuperGoal().getChildNodes().remove(planNode);
////					planNode.setParentNode(null);
////					
////					processSubplans(planNode, bestPlan);
//					
//					
//				}
//				/* If perfect match not found (parent node is not null), for each plannode in the new plan node, 
//				 * compare it with the subplans of the existing plans in the tree, then perform matching on that 
//				 * (perhaps perfeect match too). The goal is to find the best plan in the tree with the most 
//				 * matching nodes. */
//			}
//			//else {
//				/* exact match of plan on the sub plan level */
//			//}
//		}
//	}
//
//	@SuppressWarnings("unused")
//	private void processSubplans(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
//		/* process the subplans in the planNode, so they attach to the appropriate nodes in the bestPlan, if planNode does not exist yet */
//		DFSGenericTreeWalker subPlanWalkerA = new DFSGenericTreeWalker(planNode);
//		DFSGenericTreeWalker subPlanWalkerB = new DFSGenericTreeWalker(bestPlan);
//		
//		PlanGDTNode planSubPlan, bestPlanSubPlan;
//		while (!subPlanWalkerB.isFinished()) {
//			bestPlanSubPlan = (PlanGDTNode) subPlanWalkerA.getCurrentNode();
//			boolean isFound = false;
//			while (isFound == false && !subPlanWalkerA.isFinished()) {
//				planSubPlan = (PlanGDTNode) subPlanWalkerA.getCurrentNode();
//				
//				if (matchPlanSubTree(bestPlanSubPlan, planSubPlan)) {
//					/* GENERATE FEEDBACK HERE, TEMPORARY OBJECT ONLY */
//					GenericFeedBack gfb = new GenericFeedBack(bestPlanSubPlan);
//					isFound = true;
//				}
//			}
//			
//			if (isFound == false) {
//				// append node to proper location in GDT, i.e. append to sibling plans
//			}
//		}
//		
//	}

}
