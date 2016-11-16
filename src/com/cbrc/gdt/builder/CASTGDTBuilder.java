package com.cbrc.gdt.builder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.cbrc.ast.walker.DFSGenericTreeWalker;
import com.cbrc.base.Node;
import com.cbrc.db.utils.DerbyUtils;
import com.cbrc.feedback.builders.CASTGDTLevel1FeedbackBuilder;
import com.cbrc.feedback.builders.CASTGDTLevel2FeedbackBuilder;
import com.cbrc.feedback.builders.CASTGDTLevel3FeedbackBuilder;
import com.cbrc.feedback.builders.CASTGDTLevel3FeedbackRater;
import com.cbrc.feedback.builders.CASTGDTLevel4FeedbackBuilder;
import com.cbrc.feedback.builders.Level3Feedback;
import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.gdt.GDTNode;
import com.cbrc.nodes.gdt.GoalGDTNode;
import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;
import com.cbrc.nodes.patterns.PatternNode;
import com.cbrc.nodes.statements.StatementNode;

public class CASTGDTBuilder {
	
	private static final double MISCONCEPTION_DRIFT_TRESHOLD = 0.5;

	private HeadGDTNode headGDTNode;
	
	private boolean debug;
	
	private int matchedAgainstID;
	private String finalFeedback;
	private CASTGDTIndexer indexer;

	public CASTGDTBuilder () {
		buildTreePrelims("");
	}
	
	public CASTGDTBuilder (String descriptor) {
		buildTreePrelims(descriptor);
	}
	
	public CASTGDTBuilder (int GDTGoalDBID, String descriptor) {
		buildTreePrelims(GDTGoalDBID, descriptor);
	}
	
	private void buildTreePrelims(String descriptor)
	{
		this.headGDTNode = new HeadGDTNode();
		GoalGDTNode goalNode = new GoalGDTNode(descriptor);
		this.getHeadGDTNode().addChild(goalNode);
		indexer = new CASTGDTIndexer(this.getSuperGoal());
	}
	
	private void buildTreePrelims(int GDTGoalDBID, String descriptor)
	{
		this.headGDTNode = new HeadGDTNode();
		GoalGDTNode goalNode = new GoalGDTNode(descriptor);
		goalNode.setDBID(GDTGoalDBID);
		this.getHeadGDTNode().addChild(goalNode);
		indexer = new CASTGDTIndexer(this.getSuperGoal());
	}
	
	public void appendPlanNodeToGDT(PlanGDTNode source, GDTNode destination)
	{
		source.setParentNode(destination);
		destination.addChild(source);
	}
	
	public void appendPlanNodeWithASTToGDT(PlanGDTNode source, GDTNode destination, Node astNode)
	{
		source.setASTNode(astNode);
		source.setDescriptor(astNode.toString());
		appendPlanNodeToGDT(source, destination);
	}
	
	public void processFirstCode(TranslationUnitNode transUnitNode, String professorFeedback) throws Exception {
		PlanGDTNode planNode = this.createNewPlan(transUnitNode);
		planNode.setFaulty(false);
		
		addPlanNodeToGDT(planNode);
		
		planNode.setDBID(DerbyUtils.addFirstSolutionToDB(transUnitNode.getSource().getAbsolutePath(), professorFeedback, "<p>" + professorFeedback + "</p>", this.getSuperGoal().getDBID()));
		
		if (this.isDebug() == true) {
			System.out.println("GDT empty, appending " + planNode.toString() + " to GDT\n");
		}
	}
	
	public void addNewFaultyCase(TranslationUnitNode transUnitNode, String level3Feedback) throws Exception, IndexOutOfBoundsException {
		PlanGDTNode planNode = this.createNewPlan(transUnitNode);
		planNode.setFaulty(true);
		
		int planID = DerbyUtils.insertNewPlanWithID(-1, (short) 1, transUnitNode.getSource().getAbsolutePath(), this.getSuperGoal().getDBID());
		planNode.setDBID(planID);
		
		DerbyUtils.addFeedbackInstance(planID, 3, level3Feedback, "<p>" + level3Feedback + "</p>");
		
		addPlanNodeToGDT(planNode);
	}
	
	public int processNewCodeAbvr(TranslationUnitNode transUnitNode, boolean isFaulty, int studentID) throws Exception {
		// TODO Same as processNewCode but only level 3 and 1. No 4. See notepad for more details.
		PlanGDTNode planNode = this.createNewPlan(transUnitNode);
		planNode.setFaulty(isFaulty);
		
		int planID = DerbyUtils.insertNewPlanWithID(studentID, (short) (isFaulty==true?1:0), transUnitNode.getSource().getAbsolutePath(), this.getSuperGoal().getDBID());
		planNode.setDBID(planID);
		
		int feedbackLevel = 3;
		DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), feedbackLevel);
		
		handleLevel3FeedbackAbvr(planNode, studentID);
		
		DerbyUtils.log(this.getSuperGoal().getDBID(), studentID, planID, this.matchedAgainstID, this.finalFeedback, feedbackLevel, (short) (isFaulty==true?1:0), (short) 0);
		
		
		return planID;
	}
	
    public void scaleTest(TranslationUnitNode transUnitNode, boolean isFaulty, int studentID, int tCount) throws IndexOutOfBoundsException, Exception {
    	PlanGDTNode planNode = this.createNewPlan(transUnitNode);
		planNode.setFaulty(isFaulty);
		int planID = DerbyUtils.insertNewPlanWithID(studentID, (short) (isFaulty==true?1:0), transUnitNode.getSource().getAbsolutePath(), this.getSuperGoal().getDBID());
		planNode.setDBID(planID);
		int feedbackLevel = 3;
		DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), feedbackLevel);
		
		for (int i = 0; i < tCount; i++) {
			System.out.println("============================= TEST " + (i + 1) + " =============================");
			long startTime = System.nanoTime();
			System.out.println("Start time: " + startTime);
			//CASTGDTBuilderImperfectMatcher.getImperfectBuggyIndexedMatchWithPrint(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			CASTGDTBuilderImperfectMatcher.getImperfectBuggyNonIndexedMatchWithPrint(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			
			long endTime = System.nanoTime();
			System.out.println("End time: " + endTime);
			System.out.println("Elapsed time: " + (endTime - startTime));
			System.out.println();
		}
		
	}
	
	private void handleLevel3FeedbackAbvr(PlanGDTNode planNode, int studentID) throws Exception {
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			
			//Removed this code so newly added plan will not interfere in the testing of the timer
			addPlanNodeToGDT(planNode);
			
			
			//TODO: modify handleCodeTrail to only retrive faulty cases
			//handleCodeTrail(studentID);
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			
			//handleCodeTrail(studentID);
		} 
		else {
			
			long startTime = System.nanoTime();
			System.out.println("Start time: " + startTime);
			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			long endTime = System.nanoTime();
			System.out.println("End time: " + endTime);
			System.out.println("Elapsed time: " + (endTime - startTime));
			
			ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
			
			PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
			
			addPlanNodeToGDT(planNode);
			
			if (feedbacks.isEmpty()) {
				
				this.finalFeedback = "Something is wrong with your code, can you figure it out? Check the test cases that your code failed for more details.";
				this.matchedAgainstID = -1;
				System.out.println(this.finalFeedback);
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
			} else {
				giveLevel3Feedback(studentID, feedbacks, lvl3);
			}
			
			
			
		}
	}

	public void processNewCode(TranslationUnitNode transUnitNode, Boolean isFaulty, int studentID) throws Exception {
		PlanGDTNode planNode = this.createNewPlan(transUnitNode);
		// TODO: This should be checked later via test cases.
		planNode.setFaulty(isFaulty);
		
		int planID = DerbyUtils.insertNewPlanWithID(studentID, (short) (isFaulty==true?1:0), transUnitNode.getSource().getAbsolutePath(), this.getSuperGoal().getDBID());
		planNode.setDBID(planID);
		
		int feedbackLevel = DerbyUtils.getStudentFeedbackLevel(studentID, this.getSuperGoal().getDBID());
		
		if (this.debug == true) System.out.println("\nStudent " + studentID + " is now in Level " + feedbackLevel + "\n");
		
		if (feedbackLevel == 1) {
			handleLevel1Feeedback(planNode, studentID);
		} 
		else if (feedbackLevel == 2) {
			handleLevel2Feedback(planNode, studentID);
		} 
		else if (feedbackLevel == 3) {
			handleLevel3Feedback(planNode, studentID);
		} 
		else if (feedbackLevel == 4) {
			handleLevel4Feedback(planNode, studentID);
		}
		
		if (this.debug == true) {
			int newFeedbackLevel = DerbyUtils.getStudentFeedbackLevel(studentID, this.getSuperGoal().getDBID());
			System.out.println("\nStudent " + studentID + " is now in Level " + newFeedbackLevel + "\n");
		}
		
		DerbyUtils.log(this.getSuperGoal().getDBID(), studentID, planID, this.matchedAgainstID, this.finalFeedback, feedbackLevel, (short) (isFaulty==true?1:0), (short) 0);
	}
	
	// GUI VER.
	public String processNewCodeGUI(TranslationUnitNode transUnitNode, Boolean isFaulty, int studentID) throws Exception {
		String result = "";
		PlanGDTNode planNode = this.createNewPlan(transUnitNode);
		// TODO: This should be checked later via test cases.
		planNode.setFaulty(isFaulty);
		
		int planID = DerbyUtils.insertNewPlanWithID(studentID, (short) (isFaulty==true?1:0), transUnitNode.getSource().getAbsolutePath(), this.getSuperGoal().getDBID());
		planNode.setDBID(planID);
		
		int feedbackLevel = DerbyUtils.getStudentFeedbackLevel(studentID, this.getSuperGoal().getDBID());
		
		if (this.debug == true) System.out.println("\nStudent " + studentID + " is now in Level " + feedbackLevel + "\n");
		
		if (feedbackLevel == 1) {
			result = handleLevel1FeeedbackGUI(planNode, studentID);
		} 
		else if (feedbackLevel == 2) {
			result = handleLevel2FeedbackGUI(planNode, studentID);
		} 
		else if (feedbackLevel == 3) {
			result = handleLevel3FeedbackGUI(planNode, studentID);
		} 
		else if (feedbackLevel == 4) {
			result = handleLevel4FeedbackGUI(planNode, studentID);
		}
		
		if (this.debug == true) {
			int newFeedbackLevel = DerbyUtils.getStudentFeedbackLevel(studentID, this.getSuperGoal().getDBID());
			System.out.println("\nStudent " + studentID + " is now in Level " + newFeedbackLevel + "\n");
		}
		
		DerbyUtils.log(this.getSuperGoal().getDBID(), studentID, planID, this.matchedAgainstID, this.finalFeedback, feedbackLevel, (short) (isFaulty==true?1:0), (short) 0);
		return result;
	}
	

	public void askForHelpAbvr(int studentID) throws Exception {
		int feedbackLevel = 3;
		int lastSubmittedPlanID = DerbyUtils.getLastSubmittedPlanID(this.getSuperGoal().getDBID(), studentID);
		PlanGDTNode planNode = this.getPlanNodeByID(lastSubmittedPlanID);
		System.out.println("Student " + studentID + " last submitted plan " + lastSubmittedPlanID);
		
		PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
		ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
		CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
		
		PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
		ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
		
		addPlanNodeToGDT(planNode);
		
		if (feedbacks.isEmpty()) {
			
			this.finalFeedback = "Something is wrong with your code, can you figure it out? Check the test cases that your code failed for more details.";
			this.matchedAgainstID = -1;
			System.out.println(this.finalFeedback);
			DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
		} else {
			giveLevel3Feedback(studentID, feedbacks, lvl3);
		}
		
		DerbyUtils.log(this.getSuperGoal().getDBID(), studentID, lastSubmittedPlanID, this.matchedAgainstID, this.finalFeedback, feedbackLevel, (short) 1, (short) 1);
		
	}
	
	public void askForHelp(int studentID) throws Exception {
		int feedbackLevel = DerbyUtils.getStudentFeedbackLevel(studentID, this.getSuperGoal().getDBID());
		int lastSubmittedPlanID = DerbyUtils.getLastSubmittedPlanID(this.getSuperGoal().getDBID(), studentID);
		PlanGDTNode planNode = this.getPlanNodeByID(lastSubmittedPlanID);
		
		System.out.println("Student " + studentID + " last submitted plan " + lastSubmittedPlanID);
		
		// level 1 is not included because cannot ask for help when feedback to be given is level 1

		if (feedbackLevel == 2) {
			
			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
			
			PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
			
			if (feedbacks.isEmpty()) {
				//PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				CASTGDTLevel2FeedbackBuilder lvl2 = new CASTGDTLevel2FeedbackBuilder(this.headGDTNode);
				
				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
				this.finalFeedback = lvl2.getFeedbackText(planNode, bestNonBuggyMatch);
				
				System.out.println(this.finalFeedback);
				
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
			} else {
				giveLevel3Feedback(studentID, feedbacks, lvl3);
			}
			
//			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
//			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode);
//			String level3feedback = lvl3.getFeedbackText(planNode, bestMatch);
//			if (level3feedback == null) {
//				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode);
//				CASTGDTLevel2FeedbackBuilder lvl2 = new CASTGDTLevel2FeedbackBuilder(this.headGDTNode);
//				
//				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
//				this.finalFeedback = lvl2.getFeedbackText(planNode, bestNonBuggyMatch);
//				
//				System.out.println(this.finalFeedback);
//				
//				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
//			}
//			else {
//				giveLevel3Feedback(studentID, level3feedback, lvl3);
//			}
		}
		if (feedbackLevel == 3) {
			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
			
			PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
			
			if (feedbacks.isEmpty()) {
				//PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
				
				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
				this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
				
				System.out.println(this.finalFeedback);
				
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
				handleCodeTrail(studentID);
			} else {
				giveLevel3Feedback(studentID, feedbacks, lvl3);
			}
			
//			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
//			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode);
//			String level3feedback = lvl3.getFeedbackText(planNode, bestMatch);
//			if (level3feedback == null) {
//				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode);
//				CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
//				
//				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
//				this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
//				
//				System.out.println(this.finalFeedback);
//				
//				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
//				handleCodeTrail(studentID);
//			}
//			else {
//				giveLevel3Feedback(studentID, level3feedback, lvl3);
//			}
		} else if (feedbackLevel == 4) {
			PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
			
			this.matchedAgainstID = bestNonBuggyMatch.getDBID();
			this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
			
			System.out.println(this.finalFeedback);
			
			DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
			handleCodeTrail(studentID);
		}
		
		DerbyUtils.log(this.getSuperGoal().getDBID(), studentID, lastSubmittedPlanID, this.matchedAgainstID, this.finalFeedback, feedbackLevel, (short) 1, (short) 1);
	}
	
	private PlanGDTNode getPlanNodeByID(int planID) {
		int i = 0;
		while (i < this.getSuperGoal().getChildNodes().size()) {
			if (((PlanGDTNode) this.getSuperGoal().getChildNodes().get(i)).getDBID() == planID) {
				return (PlanGDTNode) this.getSuperGoal().getChildNodes().get(i);
			}
			i++;
		}
		return null;
	}

	private void giveLevel3Feedback(int studentID,  ArrayList<Level3Feedback> feedbacks, CASTGDTLevel3FeedbackBuilder lvl3)
			throws Exception {

		
		String moreFeedback = "Y";
		Boolean valid = false;
		int i = 0;
		while (i < feedbacks.size() && moreFeedback.equals("Y")) {
			this.finalFeedback = feedbacks.get(i).getFeedbackString();
			this.matchedAgainstID = feedbacks.get(i).getFeedbackID();
			lvl3.setFeedbackID(feedbacks.get(i).getFeedbackID());
			
			System.out.println(this.finalFeedback);
			CASTGDTLevel3FeedbackRater rater = new CASTGDTLevel3FeedbackRater(lvl3);
			rater.rateFeedback();
			
			if (rater.getResponse().equals("N")) {
				while (valid == false) {
					System.out.println("\nWould you like to see another feedback? (Y/N)");
					moreFeedback = getInput(new BufferedReader(new InputStreamReader(System.in)));
					if (moreFeedback.equals("Y") || moreFeedback.equals("N")) valid = true;
					else System.out.println("Invalid Input!");
				}
			} else {
				moreFeedback = "N";
				
				/* DN: This was not used during testing. This part of the code checks if the student's code improved from his or her
				 * last submission, and adjusts the feedback score accordingly 
				 */
				
//				int lastPlanID = DerbyUtils.getLastSubmittedPlanIDAfterInsert(this.getSuperGoal().getDBID(), studentID);
//				if (lastPlanID != -1)
//				{
//					PlanGDTNode lastPlan = this.getPlanNodeByID(lastPlanID);
//					PlanGDTNode planNode = lvl3.getPlanNode();
//					
//					PlanGDTNode lastPlanMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), lastPlan, this.indexer);
//					PlanGDTNode bestWorkingMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
//					
//					if (lastPlanMatch.equals(bestWorkingMatch)){
//						double planNodePQGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(planNode, bestWorkingMatch);
//						double lastPlanPQGram = CASTGDTBuilderImperfectMatcher.getPQGramDistance(lastPlan, lastPlanMatch);
//						
//						if (planNodePQGram < lastPlanPQGram) DerbyUtils.upVoteFeedback(feedbacks.get(i).getFeedbackID());
//						else if (planNodePQGram > lastPlanPQGram) DerbyUtils.downVoteFeedback(feedbacks.get(i).getFeedbackID());
//					}
//				}
			}
			i++;
		}
		
		if (moreFeedback.equals("Y")) {
			this.finalFeedback = "\nNo more feedback could be given.\n";
			this.matchedAgainstID = -1;
			
			System.out.println(this.finalFeedback);
		}

		DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
		
//		String moreFeedback = "Y";
//		Boolean more = true;
//		Boolean valid = false;
//		
//		this.finalFeedback = level3feedback;
//		this.matchedAgainstID = lvl3.getBestPlan().getDBID();
//		
//		
//		System.out.println(level3feedback);
//		CASTGDTLevel3FeedbackRater rater = new CASTGDTLevel3FeedbackRater(lvl3);
//		rater.rateFeedback();
//		
//		
//		ArrayList<PlanGDTNode> exceptions= new ArrayList<PlanGDTNode>();
//		while (rater.getResponse().equals("N") && moreFeedback.equals("Y") && more == true) {
//			PlanGDTNode planNode = lvl3.getPlanNode();
//			PlanGDTNode bestPlan = lvl3.getBestPlan();
//			
//			while (valid == false) {
//				System.out.println("\nWould you like to see other feedback? (Y/N)");
//				moreFeedback = getInput(new BufferedReader(new InputStreamReader(System.in)));
//				if (moreFeedback.equals("Y") || moreFeedback.equals("N")) valid = true;
//				else System.out.println("Invalid Input!");
//			}
//			
//			
//			lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
//			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatchWithExceptions(this.getSuperGoal().getChildNodes(), planNode, exceptions);
//			if (bestMatch == null) more = false;
//			
//			if (more == true) {
//				
//				this.finalFeedback = lvl3.getFeedbackText(planNode, bestMatch);
//				this.matchedAgainstID = bestMatch.getDBID();
//				
//				System.out.println(this.finalFeedback);
//				rater = new CASTGDTLevel3FeedbackRater(lvl3);
//				rater.rateFeedback();
//				
//				exceptions.add(bestPlan);
//			} else {
//				this.finalFeedback = "\nNo more feedback could be given.\n";
//				this.matchedAgainstID = -1;
//				
//				System.out.println(this.finalFeedback);
//			}
//			
//		}
		
//		DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
	}
	
	private void handleLevel2Feedback(PlanGDTNode planNode, int studentID) throws Exception {
		
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			addPlanNodeToGDT(planNode);
			
			handleCodeTrail(studentID);
			
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			
			handleCodeTrail(studentID);
		} 
		else {
			double pqGramScore = getPQGramScoreOfLastMatchedPlan(planNode, studentID);
			if (pqGramScore > MISCONCEPTION_DRIFT_TRESHOLD) {
				handleLevel1Feeedback(planNode, studentID);
			} else {
				addPlanNodeToGDT(planNode);
				
				PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
				CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
				
				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
				
				if (feedbacks.isEmpty()) {
					//PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
					CASTGDTLevel2FeedbackBuilder lvl2 = new CASTGDTLevel2FeedbackBuilder(this.headGDTNode);
					
					this.finalFeedback = lvl2.getFeedbackText(planNode, bestNonBuggyMatch);
					this.matchedAgainstID = bestNonBuggyMatch.getDBID();
					
					System.out.println(this.finalFeedback);
					
					DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
				} else {
					giveLevel3Feedback(studentID, feedbacks, lvl3);
				}
				
	//			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
	//			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode);
	//			
	//			
	//			String level3feedback = lvl3.getFeedbackText(planNode, bestMatch);
	//			if (level3feedback == null) {
	//				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode);
	//				CASTGDTLevel2FeedbackBuilder lvl2 = new CASTGDTLevel2FeedbackBuilder(this.headGDTNode);
	//				
	//				this.finalFeedback = lvl2.getFeedbackText(planNode, bestNonBuggyMatch);
	//				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
	//				
	//				System.out.println(this.finalFeedback);
	//				
	//				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
	//			}
	//			else {
	//				giveLevel3Feedback(studentID, level3feedback, lvl3);
	//			}
			}
		}
	}
	
	// GUI VER.
	private String handleLevel2FeedbackGUI(PlanGDTNode planNode, int studentID) throws Exception {
		String result = "";
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			addPlanNodeToGDT(planNode);
			
			handleCodeTrail(studentID);
			
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			
			handleCodeTrail(studentID);
		} 
		else {
			double pqGramScore = getPQGramScoreOfLastMatchedPlan(planNode, studentID);
			if (pqGramScore > MISCONCEPTION_DRIFT_TRESHOLD) {
				handleLevel1FeeedbackGUI(planNode, studentID);
			} else {
				addPlanNodeToGDT(planNode);
				
				PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
				CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
				
				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
				
				if (feedbacks.isEmpty()) {
					//PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
					CASTGDTLevel2FeedbackBuilder lvl2 = new CASTGDTLevel2FeedbackBuilder(this.headGDTNode);
					
					this.finalFeedback = lvl2.getFeedbackText(planNode, bestNonBuggyMatch);
					this.matchedAgainstID = bestNonBuggyMatch.getDBID();
					
					result = this.finalFeedback;
					
					DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
				} else {
					giveLevel3Feedback(studentID, feedbacks, lvl3);
				}
				
	//			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
	//			PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode);
	//			
	//			
	//			String level3feedback = lvl3.getFeedbackText(planNode, bestMatch);
	//			if (level3feedback == null) {
	//				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode);
	//				CASTGDTLevel2FeedbackBuilder lvl2 = new CASTGDTLevel2FeedbackBuilder(this.headGDTNode);
	//				
	//				this.finalFeedback = lvl2.getFeedbackText(planNode, bestNonBuggyMatch);
	//				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
	//				
	//				System.out.println(this.finalFeedback);
	//				
	//				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 3);
	//			}
	//			else {
	//				giveLevel3Feedback(studentID, level3feedback, lvl3);
	//			}
			}
		}
		return result;
	}

	private void handleLevel4Feedback(PlanGDTNode planNode, int studentID) throws Exception, SQLException, FileNotFoundException, IOException {
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			addPlanNodeToGDT(planNode);
			
			handleCodeTrail(studentID);
			
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			
			handleCodeTrail(studentID);
		} 
		else {
			
			double pqGramScore = getPQGramScoreOfLastMatchedPlan(planNode, studentID);
			if (pqGramScore > MISCONCEPTION_DRIFT_TRESHOLD) {
				handleLevel1Feeedback(planNode, studentID);
			} else {
				addPlanNodeToGDT(planNode);
				
				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
				
				this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
				this.matchedAgainstID = bestNonBuggyMatch.getDBID();
				
				System.out.println(this.finalFeedback);
				
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
				handleCodeTrail(studentID);
			}
		}
	}
	
	// GUI VER.
	private String handleLevel4FeedbackGUI(PlanGDTNode planNode, int studentID) throws Exception, SQLException, FileNotFoundException, IOException {
		String result = "";
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			addPlanNodeToGDT(planNode);
			
			handleCodeTrail(studentID);
			
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			
			handleCodeTrail(studentID);
		} 
		else {
			
			double pqGramScore = getPQGramScoreOfLastMatchedPlan(planNode, studentID);
			if (pqGramScore > MISCONCEPTION_DRIFT_TRESHOLD) {
				handleLevel1FeeedbackGUI(planNode, studentID);
			} else {
				addPlanNodeToGDT(planNode);
				
				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
				
				this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
				this.matchedAgainstID = bestNonBuggyMatch.getDBID();

				result = this.finalFeedback;
				
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
				handleCodeTrail(studentID);
			}
		}
		return result;
	}

	private void handleLevel3Feedback(PlanGDTNode planNode, int studentID) throws Exception {
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			addPlanNodeToGDT(planNode);
			
			handleCodeTrail(studentID);
			
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			
			handleCodeTrail(studentID);
		} 
		else {
			
			double pqGramScore = getPQGramScoreOfLastMatchedPlan(planNode, studentID);
			if (pqGramScore > MISCONCEPTION_DRIFT_TRESHOLD) {
				handleLevel1Feeedback(planNode, studentID);
			} else {
				addPlanNodeToGDT(planNode);
				
				PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
				CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
				
				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
				
				if (feedbacks.isEmpty()) {
					//PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
					CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
					
					this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
					this.matchedAgainstID = bestNonBuggyMatch.getDBID();
					
					System.out.println(this.finalFeedback);
					
					DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
					handleCodeTrail(studentID);
				} else {
					giveLevel3Feedback(studentID, feedbacks, lvl3);
				}
//				
//				CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
//				PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode);
//				String level3feedback = lvl3.getFeedbackText(planNode, bestMatch);
//				if (level3feedback == null) {
//					PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode);
//					CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
//					
//					this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
//					this.matchedAgainstID = bestNonBuggyMatch.getDBID();
//					
//					System.out.println(this.finalFeedback);
//					
//					DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
//					handleCodeTrail(studentID);
//				}
//				else {
//					giveLevel3Feedback(studentID, level3feedback, lvl3);
//				}
			}

		}
	}
	
	// GUI VER.
	private String handleLevel3FeedbackGUI(PlanGDTNode planNode, int studentID) throws Exception {
		String result = "";
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			addPlanNodeToGDT(planNode);
			
			handleCodeTrail(studentID);
			
		} 
		else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			
			handleCodeTrail(studentID);
		} 
		else {
			
			double pqGramScore = getPQGramScoreOfLastMatchedPlan(planNode, studentID);
			if (pqGramScore > MISCONCEPTION_DRIFT_TRESHOLD) {
				handleLevel1FeeedbackGUI(planNode, studentID);
			} else {
				addPlanNodeToGDT(planNode);
				
				PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), bestMatch, false, this.indexer);
				CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
				
				PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
				ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
				
				if (feedbacks.isEmpty()) {
					//PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
					CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
					
					this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
					this.matchedAgainstID = bestNonBuggyMatch.getDBID();
					
					result = this.finalFeedback;
					
					DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
					handleCodeTrail(studentID);
				} else {
					giveLevel3Feedback(studentID, feedbacks, lvl3);
				}
//				
//				CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
//				PlanGDTNode bestMatch = CASTGDTBuilderImperfectMatcher.getImperfectBuggyMatch(this.getSuperGoal().getChildNodes(), planNode);
//				String level3feedback = lvl3.getFeedbackText(planNode, bestMatch);
//				if (level3feedback == null) {
//					PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode);
//					CASTGDTLevel4FeedbackBuilder lvl4 = new CASTGDTLevel4FeedbackBuilder(this.headGDTNode);
//					
//					this.finalFeedback = lvl4.getFeedbackText(planNode, bestNonBuggyMatch);
//					this.matchedAgainstID = bestNonBuggyMatch.getDBID();
//					
//					System.out.println(this.finalFeedback);
//					
//					DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 4);
//					handleCodeTrail(studentID);
//				}
//				else {
//					giveLevel3Feedback(studentID, level3feedback, lvl3);
//				}
			}

		}
		return result;
	}

	private double getPQGramScoreOfLastMatchedPlan(PlanGDTNode planNode,
			int studentID) throws SQLException, Exception {
		boolean found = false;
		PlanGDTNode lastMatchedPlan = null;
		int i = 0;
		int lastMatchedPlanID = DerbyUtils.getLastLoggedMatchedPlan(this.getSuperGoal().getDBID(), studentID);
		while (found == false && i < this.getSuperGoal().getChildNodes().size()) {
			if (lastMatchedPlanID == ((PlanGDTNode) this.getSuperGoal().getChildNodes().get(i)).getDBID()) {
				found = true;
				lastMatchedPlan = (PlanGDTNode) this.getSuperGoal().getChildNodes().get(i);
			}
			i++;
		}
		
		double pqGramScore = -1;
		if (lastMatchedPlan != null) pqGramScore = CASTGDTBuilderImperfectMatcher.getPQGramDistancePlanNodes(planNode, lastMatchedPlan);
		return pqGramScore;
	}

	private void handleLevel1Feeedback(PlanGDTNode planNode, int studentID) throws Exception {
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			addPlanNodeToGDT(planNode);
			
		} else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				System.out.println(this.finalFeedback);
			}
			
			HashMap<Integer, String> map = DerbyUtils.getCodeTrail(studentID, this.getSuperGoal().getDBID());
			
			if (!map.isEmpty())
				handleCodeTrail(studentID);
		} else if (perfectMatch != null && perfectMatch.isFaulty()) {
			addPlanNodeToGDT(planNode);

			ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), planNode, false, this.indexer);
			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
			
			PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
			
			if (feedbacks.isEmpty()) {
				CASTGDTLevel1FeedbackBuilder lvl1 = new CASTGDTLevel1FeedbackBuilder(this.headGDTNode);
				
				this.finalFeedback = lvl1.getFeedbackText(planNode, null);
				this.matchedAgainstID = -1;
				
				System.out.println(this.finalFeedback);
				
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 2);
			} else {
				giveLevel3Feedback(studentID, feedbacks, lvl3);
			}
			
//			while (i < perfectMatches.size() && level3feedback == null) {
//				level3feedback = lvl3.getFeedbackText(planNode, perfectMatches.get(i));
//				i++;
//			}
//			
//			if (level3feedback == null) {
//				CASTGDTLevel1FeedbackBuilder lvl1 = new CASTGDTLevel1FeedbackBuilder(this.headGDTNode);
//				
//				this.finalFeedback = lvl1.getFeedbackText(planNode, null);
//				this.matchedAgainstID = -1;
//				
//				System.out.println(this.finalFeedback);
//				
//				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 2);
//			}
//			else {
//				giveLevel3Feedback(studentID, level3feedback, lvl3);
//			}
		} else {
			addPlanNodeToGDT(planNode);
			
			CASTGDTLevel1FeedbackBuilder lvl1 = new CASTGDTLevel1FeedbackBuilder(this.headGDTNode);
			
			this.finalFeedback = lvl1.getFeedbackText(planNode, null);
			this.matchedAgainstID = -1;
			
			System.out.println(this.finalFeedback);
			
			DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 2);
		}
	}
	
	// GUI VER.
	private String handleLevel1FeeedbackGUI(PlanGDTNode planNode, int studentID) throws Exception {
		String result = "";
		PlanGDTNode perfectMatch = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanIfExists(this.getSuperGoal().getChildNodes(), planNode, this.debug, this.indexer);
		
		if (!planNode.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = -1;
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			addPlanNodeToGDT(planNode);
			
		} else if (perfectMatch != null && !perfectMatch.isFaulty()) {
			this.finalFeedback = "Congratulations! Your code is correct!";
			this.matchedAgainstID = perfectMatch.getDBID();
			
			if (this.debug == true) {
				result = this.finalFeedback;
			}
			
			HashMap<Integer, String> map = DerbyUtils.getCodeTrail(studentID, this.getSuperGoal().getDBID());
			
			if (!map.isEmpty())
				handleCodeTrail(studentID);
		} else if (perfectMatch != null && perfectMatch.isFaulty()) {
			addPlanNodeToGDT(planNode);

			ArrayList<PlanGDTNode> perfectMatches = CASTGDTBuilderPerfectMatcher.getPerfectMatchPlanArray(this.getSuperGoal().getChildNodes(), planNode, false, this.indexer);
			CASTGDTLevel3FeedbackBuilder lvl3 = new CASTGDTLevel3FeedbackBuilder(this.headGDTNode);
			
			PlanGDTNode bestNonBuggyMatch = CASTGDTBuilderImperfectMatcher.getImperfectWorkingMatch(this.getSuperGoal().getChildNodes(), planNode, this.indexer);
			ArrayList<Level3Feedback> feedbacks = lvl3.getFeedbackArray(planNode, perfectMatches, bestNonBuggyMatch);
			
			if (feedbacks.isEmpty()) {
				CASTGDTLevel1FeedbackBuilder lvl1 = new CASTGDTLevel1FeedbackBuilder(this.headGDTNode);
				
				this.finalFeedback = lvl1.getFeedbackText(planNode, null);
				this.matchedAgainstID = -1;
				
				result = this.finalFeedback;
				
				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 2);
			} else {
				giveLevel3Feedback(studentID, feedbacks, lvl3);
			}
			
//			while (i < perfectMatches.size() && level3feedback == null) {
//				level3feedback = lvl3.getFeedbackText(planNode, perfectMatches.get(i));
//				i++;
//			}
//			
//			if (level3feedback == null) {
//				CASTGDTLevel1FeedbackBuilder lvl1 = new CASTGDTLevel1FeedbackBuilder(this.headGDTNode);
//				
//				this.finalFeedback = lvl1.getFeedbackText(planNode, null);
//				this.matchedAgainstID = -1;
//				
//				System.out.println(this.finalFeedback);
//				
//				DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 2);
//			}
//			else {
//				giveLevel3Feedback(studentID, level3feedback, lvl3);
//			}
		} else {
			addPlanNodeToGDT(planNode);
			
			CASTGDTLevel1FeedbackBuilder lvl1 = new CASTGDTLevel1FeedbackBuilder(this.headGDTNode);
			
			this.finalFeedback = lvl1.getFeedbackText(planNode, null);
			this.matchedAgainstID = -1;
			
			result = this.finalFeedback;
			
			DerbyUtils.setFeedbackLevel(studentID, this.getSuperGoal().getDBID(), 2);
		}
		return result;
	}

	private void handleCodeTrail(int studentID) throws SQLException, FileNotFoundException, IOException {
		HashMap<Integer, String> map = DerbyUtils.getCodeTrail(studentID, this.getSuperGoal().getDBID());
		int codeChoice = 0;
		while (!map.isEmpty() && codeChoice >= 0) {
			System.out.println("You have submitted the following code(s) before. Please try to identify your error and in which submitted code is the error manifested the best.\n");

			int i = 0;
			ArrayList <Integer> planIDs = new ArrayList<Integer>();
			for(Integer key:map.keySet()) {
				System.out.println("CODE " + i + ":\n");
				FileInputStream inputStream = new FileInputStream(map.get(key));
				planIDs.add(key);
				try {
					DataInputStream dataStream = new DataInputStream(inputStream);
					try {
						Scanner sc = new Scanner(dataStream);
						try {
							while (sc.hasNext()) {
								System.out.println(sc.nextLine());
							}
						} finally {
							sc.close();
						}
					} finally {
						dataStream.close();
					}
				} finally {
					inputStream.close();
				}
				System.out.println();
				i++;
			}
			
			System.out.println("In which code is the error found? (Input -1 if you don't want to input anymore) ");
			String input = getInput(new BufferedReader(new InputStreamReader(System.in)));
			codeChoice = Integer.parseInt(input);
			
			if (codeChoice != -1) {
				System.out.println("Describe how to fix the error as if you are describing it to a fellow student: ");
				String description = getInput(new BufferedReader(new InputStreamReader(System.in)));
				
				DerbyUtils.addFeedbackInstance(planIDs.get(codeChoice), 3, description, "<p>" + description + "</p>");
				
				map.remove(planIDs.get(codeChoice));
			}
		}
	}
	
	/* Assumes new code is not faulty */
	public void processNewCode(TranslationUnitNode transUnitNode) throws Exception {
		this.processNewCode(transUnitNode, true, -1);
	}
	
	public PlanGDTNode createPlanFromTransUnitNode(TranslationUnitNode transUnitNode) throws IndexOutOfBoundsException, Exception {
		PlanGDTNode planNode = new PlanGDTNode(transUnitNode.getSource().getName());
		planNode.setASTNode(transUnitNode);
		//this.getSuperGoal().addChild(planNode);
		
		DFSGenericTreeWalker dfsTreeWalker = new DFSGenericTreeWalker(transUnitNode);
		ArrayList<PlanGDTNode> addedPlans = new ArrayList<PlanGDTNode>(); // for optimization
		addedPlans.add(planNode);
		
		while (!dfsTreeWalker.isFinished()) {
			Node currentNode = dfsTreeWalker.getCurrentNode();
			if (isPlan(currentNode)) {
				if (currentNode.getPatternForm() != null) planNode.addToPatterns(currentNode.getPatternForm().getClass().getSimpleName());
				PlanGDTNode subPlanNode = new PlanGDTNode();
				PlanGDTNode parentNode = getClosestAncestorInArrayList(addedPlans, currentNode);
				
				if (parentNode != null) {
					appendPlanNodeWithASTToGDT(subPlanNode, parentNode, currentNode);
				} else {
					appendPlanNodeWithASTToGDT(subPlanNode, planNode, currentNode);
				}
				addedPlans.add(subPlanNode);
			}
			dfsTreeWalker.nextNode();
		}
		
		return planNode;
	}
	
	public PlanGDTNode createNewPlan(TranslationUnitNode transUnitNode) throws Exception, IndexOutOfBoundsException
	{
		PlanGDTNode planNode = new PlanGDTNode(transUnitNode.getSource().getName());
		planNode.setASTNode(transUnitNode);
		//this.getSuperGoal().addChild(planNode);
		
		DFSGenericTreeWalker dfsTreeWalker = new DFSGenericTreeWalker(transUnitNode);
		ArrayList<PlanGDTNode> addedPlans = new ArrayList<PlanGDTNode>(); // for optimization
		addedPlans.add(planNode);
		
		while (!dfsTreeWalker.isFinished()) {
			Node currentNode = dfsTreeWalker.getCurrentNode();
			if (isPlan(currentNode)) {
				if (currentNode.getPatternForm() != null) planNode.addToPatterns(currentNode.getPatternForm().getClass().getSimpleName());
				
				PlanGDTNode subPlanNode = new PlanGDTNode();
				PlanGDTNode parentNode = getClosestAncestorInArrayList(addedPlans, currentNode);
				
				if (parentNode != null) {
					appendPlanNodeWithASTToGDT(subPlanNode, parentNode, currentNode);
				} else {
					appendPlanNodeWithASTToGDT(subPlanNode, planNode, currentNode);
				}
				addedPlans.add(subPlanNode);
			}
			dfsTreeWalker.nextNode();
		}
		
		return planNode;
		
	}
	
	private PlanGDTNode getClosestAncestorInArrayList(ArrayList<PlanGDTNode> addedPlans, Node currentNode)
	{
		Boolean isFound = false;
		Node currentAncestorToCheck;
		PlanGDTNode potentialAncestor;
		int i;
		currentAncestorToCheck = currentNode.getParentNode();
		
		while (isFound == false)
		{
			for (i = 0; i < addedPlans.size(); i++) {
				potentialAncestor = addedPlans.get(i);
				if (potentialAncestor.getASTNode().equals(currentAncestorToCheck)) {
					return potentialAncestor;
				}
			}
			currentAncestorToCheck = currentAncestorToCheck.getParentNode();
		}
		
		return null;
		
	}
	
	public static String getInput(BufferedReader br) throws IOException {
		String input = "";
		while (input.length() == 0 ) {
			input = br.readLine();
		    if (input.length() == 0 ) 
		       System.out.println("Nothing entered: ");
		}
		return input;
	}
	
	private boolean isPlan(Node node) {
		if (node instanceof PatternNode) return true;
		else if (node instanceof ExpressionNode) return true;
		else if (node instanceof StatementNode) return true;
		/* Determine later if CaseStatement and DefaultStatement and ReturnStatement should be a part of this */
		return false;
	}
	
	public GoalGDTNode getSuperGoal() {
		return (GoalGDTNode) this.getHeadGDTNode().getChildNodes().get(0);
	}

	public HeadGDTNode getHeadGDTNode() {
		return this.headGDTNode;
	}

	public boolean isDebug() {
		return this.debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	private void addPlanNodeToGDT(PlanGDTNode planNode) {
		//TODO: Modify by not just adding but indexing planNode
		// ALSO FOR TESTING TOMORROW, MAKE SURE TO DIFFERENTIATE INDEXED ADDING AND NON INDEXED ADDING
		indexer.nonIndex(planNode);
		//indexer.index(planNode);
		this.getSuperGoal().addChild(planNode);
		planNode.setParentNode(this.getSuperGoal());
	}

}
