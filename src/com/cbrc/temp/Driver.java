package com.cbrc.temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.cbrc.ast.matcher.factory.RootToLeafPathFactory;
import com.cbrc.ast.matcher.structures.RootToLeafPath;
import com.cbrc.ast.utils.StudentListRecoveryUtility;
import com.cbrc.ast.walker.DFSGenericTreeWalker;
import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.db.utils.DerbyUtils;
import com.cbrc.gdt.builder.CASTCodeAnnotator;
import com.cbrc.gdt.builder.CASTGDTBuilder;
import com.cbrc.gdt.builder.CASTGDTBuilderImperfectMatcher;
import com.cbrc.gdt.builder.CASTGDTStudentTracker;
import com.cbrc.lexers.CASTLexer;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.gdt.PlanGDTNode;
import com.cbrc.parsers.CASTParser;
import com.cbrc.testcase.engines.SourceCodeConverter;
import com.cbrc.testcase.engines.TesterAndScorer;
import com.cbrc.tokens.identifiers.IntegerVariableIdentifierToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;
import com.cbrc.tokens.operators.AssignmentOperatorToken;
import com.cbrc.tokens.operators.PlusOperatorToken;
import com.cbrc.tokens.operators.SemicolonToken;
import com.cbrc.tokens.operators.enums.PlusMinusType;
import com.cbrc.tokens.operators.enums.SemicolonType;

public class Driver {
	
	public final static String MENU_EXIT = "1";
	public final static String MENU_REG_STUDENT = "2";
	public final static String MENU_SUBMIT_CODE = "3";
	public final static String MENU_ASK_HELP = "4";
	public static final String MENU_PRINT_GDT = "5";
	public static final String MENU_ADD_CASE = "6";
	public static final String SCALE_EXP = "7";
	
	public static final String DEFAULT_PATH = "C:\\SampleCodes\\";

	public static void main(String[] args) {
		
		try {
			String path = "";
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				Boolean valid = false;
				String debug = null;
				while (valid != true) {
					System.out.println("Debug mode? Y/N: ");
					debug = getInput(br);
					 if (debug.equals("Y") || debug.equals("N") || debug.equals("A")) valid = true;
					 else System.out.println("Invalid input!\n");
				}
				
				if (debug.equals("A")) {
					System.out.println("CODES CHECKER ACTIVATED!\n\n");
					
					System.out.println("Put all codes in C:\\CodesToCheck\\. What is the base filename? : ");
					String tempPath = getInput(br);
					
					path = "C:\\CodesToCheck\\" + tempPath;
					
					System.out.println("Input filename of correct solution: ");
					String firstSolutionFileName = getInput(br);
					
					File file1 = new File("C:\\CodesToCheck\\" + firstSolutionFileName);
					CASTCodeAnnotator codeAnnotator = new CASTCodeAnnotator(file1);
					codeAnnotator.annotateCode();
					CASTGDTBuilder builder = new CASTGDTBuilder();
					PlanGDTNode correctPlan = builder.createPlanFromTransUnitNode(codeAnnotator.getHeadNode());
					
//					ArrayList<File> tci = new ArrayList<File>();
//					ArrayList<File> tco = new ArrayList<File>();
//					File tcInput, tcOutput;
//						
//					System.out.println("Put all testcases in C:\\CodesToCheck\\TestCases. How many test cases are there?: ");
//					String testcaseCount = getInput(br);
//					int tcCount = Integer.parseInt(testcaseCount);
//					for (int i = 0; i < tcCount; i++) {
//						tcInput = new File("C:\\CodesToCheck\\TestCases\\I" + i + ".txt");
//						tci.add(tcInput);
//						tcOutput = new File("C:\\CodesToCheck\\TestCases\\O" + i + ".txt");
//						tco.add(tcOutput);
//					}
					
					System.out.println("How many solutions are there?: ");
					String solutionCount = getInput(br);
					int sCount = Integer.parseInt(solutionCount);
					for (int i = 1; i < sCount + 1; i++) {
						File file = new File(path + i +".c");
						codeAnnotator = new CASTCodeAnnotator(file);
						codeAnnotator.annotateCode();
						PlanGDTNode tentativePlan = builder.createPlanFromTransUnitNode(codeAnnotator.getHeadNode());
						
						//SourceCodeConverter scc = new SourceCodeConverter(file, path);
						//scc.activate();
						
						//TODO: ArrayList for testcase Inputs and test case outputs
						//TesterAndScorer tas = new TesterAndScorer(scc.getModifiedSource(), tci, tco, path);
						//tas.activate();
						
						double pqGramDistance = CASTGDTBuilderImperfectMatcher.getPQGramDistance(tentativePlan.getASTNode(), correctPlan.getASTNode());
						
						System.out.println("PQGram Distance: " + pqGramDistance);
						
					}
				}
				else if (debug.equals("N")) {
					System.out.println("Please define goal/problem.\n");
					System.out.println("Input problem name: ");
					String goalName = getInput(br);
					
					System.out.println("Input problem description: ");
					String goalDescript = getInput(br);
					
					int newGoalKey = DerbyUtils.addNewGoal(goalName, goalDescript);
					
					CASTGDTStudentTracker students = new CASTGDTStudentTracker();
					CASTGDTBuilder builder = new CASTGDTBuilder(newGoalKey, goalDescript);
					
					builder.setDebug(true);
					System.out.println("Problem defined!\n");
					
					valid = false;
					String recover = null;
					while (valid != true) {
						System.out.println("Would you like to recover student list? Y/N: ");
						recover = getInput(br);
						if (recover.equals("Y") || recover.equals("N")) valid = true;
						else System.out.println("Invalid input!\n");
					}
					
					if (recover.equals("Y")) {
						StudentListRecoveryUtility slru =  new StudentListRecoveryUtility();
						System.out.println("Input goalID: ");
						String goalID = getInput(br);
						students = slru.recoverStudents(Integer.parseInt(goalID), newGoalKey);
					}
					
					
					System.out.println("Input base path (null if default): ");
					String tempPath = getInput(br);
					
					if (tempPath.equals("null")) path = DEFAULT_PATH;
					else path = tempPath;
					
					System.out.println("Input filename of first solution: ");
					String firstSolutionFileName = getInput(br);
					
					File file1 = new File(path + firstSolutionFileName + ".c");
					CASTCodeAnnotator codeAnnotator = new CASTCodeAnnotator(file1);
					codeAnnotator.annotateCode();
					builder.processFirstCode(codeAnnotator.getHeadNode(), "");
					System.out.println("First Solution Added!\n");
					
					ArrayList<File> tci = new ArrayList<File>();
					ArrayList<File> tco = new ArrayList<File>();
					File tc;
					
					String testCaseInputFileName = "";
					while (!testCaseInputFileName.equals("null")) {
						System.out.println("Input filename of test case input: (input null if no more test case inputs) ");
						testCaseInputFileName = getInput(br);

						if (!testCaseInputFileName.equals("null")) {
							tc = new File(path + "TestCases\\" + testCaseInputFileName + ".txt");
							tci.add(tc);
						}
					}
					
					String testCaseOutputFileName = "";
					while (!testCaseOutputFileName.equals("null")) {
						System.out.println("Input filename of test case expected output: (input null if no more test case inputs) ");
						testCaseOutputFileName = getInput(br);

						if (!testCaseOutputFileName.equals("null")) {
							tc = new File(path + "TestCases\\" + testCaseOutputFileName + ".txt");
							tco.add(tc);
						}
					}
					
					
					printGDT(builder, path);
					
					String menuInput = "";
					while (!menuInput.equals(MENU_EXIT)) {
						menuInput = getMenuInput(br);
						
						if (menuInput.equals(MENU_REG_STUDENT)) {
							try {
								registerNewStudent(br, students, builder.getSuperGoal().getDBID());
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
						else if (menuInput.equals(MENU_SUBMIT_CODE)) {
							try {
							    submitNewCode(br, students, builder, path, tci, tco);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (menuInput.equals(MENU_ASK_HELP)) {
							try {
								askForHelp(br, students, builder, path);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (menuInput.equals(MENU_EXIT)) {
							System.out.println("\n\nExiting System.");
						}
						else if (menuInput.equals(MENU_PRINT_GDT)) {
							printGDT(builder, path);
						}
						else if (menuInput.equals(MENU_ADD_CASE)) {
							addCase(br, builder, path);
						}
					}
				} else {
					System.out.println("Please define goal/problem.\n");
					System.out.println("Input problem name: ");
					String goalName = getInput(br);
					
					String goalDescript = "Due to speed concerns, this has been replaced with this text. Refer to text file for actual description.";
					
					int newGoalKey = DerbyUtils.addNewGoal(goalName, goalDescript);
					
					CASTGDTStudentTracker students = new CASTGDTStudentTracker();
					CASTGDTBuilder builder = new CASTGDTBuilder(newGoalKey, goalDescript);
					
					builder.setDebug(true);
					System.out.println("Problem defined!\n");
					
					valid = false;
					String recover = null;
					while (valid != true) {
						System.out.println("Would you like to recover student list? Y/N: ");
						recover = getInput(br);
						if (recover.equals("Y") || recover.equals("N")) valid = true;
						else System.out.println("Invalid input!\n");
					}
					
					if (recover.equals("Y")) {
						StudentListRecoveryUtility slru =  new StudentListRecoveryUtility();
						System.out.println("Input goalID: ");
						String goalID = getInput(br);
						students = slru.recoverStudents(Integer.parseInt(goalID), newGoalKey);
					}
					
					path = DEFAULT_PATH;
					
					System.out.println("Input filename of first solution: ");
					String firstSolutionFileName = getInput(br);
					
					File file1 = new File(path + firstSolutionFileName);
					CASTCodeAnnotator codeAnnotator = new CASTCodeAnnotator(file1);
					codeAnnotator.annotateCode();
					builder.processFirstCode(codeAnnotator.getHeadNode(), "");
					System.out.println("First Solution Added!\n");
					
					ArrayList<File> tci = new ArrayList<File>();
					ArrayList<File> tco = new ArrayList<File>();
					File tcInput, tcOutput;
						
					System.out.println("How many test cases are there?: ");
					String testcaseCount = getInput(br);
					int tcCount = Integer.parseInt(testcaseCount);
					for (int i = 0; i < tcCount; i++) {
						tcInput = new File(path + "TestCases\\I" + i + ".txt");
						tci.add(tcInput);
						tcOutput = new File(path + "TestCases\\O" + i + ".txt");
						tco.add(tcOutput);
					}
						
					System.out.println("How many buggy cases are there?: ");
					String buggyCaseCount = getInput(br);
					int bgCount = Integer.parseInt(buggyCaseCount);
					for (int i = 0; i < bgCount; i++) {
						addAutoCase(i, builder, path);
					}
					
					printGDT(builder, path);
					
					String menuInput = "";
					while (!menuInput.equals(MENU_EXIT)) {
						menuInput = getMenuInputAbvr(br);
						
						if (menuInput.equals(MENU_REG_STUDENT)) {
							try {
								registerNewStudent(br, students, builder.getSuperGoal().getDBID());
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
						else if (menuInput.equals(MENU_SUBMIT_CODE)) {
							try {
								submitNewCodeAbvr(br, students, builder, path, tci, tco);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (menuInput.equals(MENU_ASK_HELP)) {
							try {
								askForHelpAbvr(br, students, builder, path);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else if (menuInput.equals(MENU_EXIT)) {
							System.out.println("\n\nExiting System.");
						}
						else if (menuInput.equals(MENU_PRINT_GDT)) {
							printGDT(builder, path);
						}
						else if (menuInput.equals(SCALE_EXP)) {
							scalabilityExperiment(br, students, builder, path, tci, tco);
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				br.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void scalabilityExperiment(BufferedReader br, CASTGDTStudentTracker students, CASTGDTBuilder builder, String path, ArrayList<File> tci, ArrayList<File> tco) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
		boolean valid = false;
		System.out.println("Submit Code\n");
		
		System.out.println("Input path to code: ");
		String filePath = getInput(br);

		File file = new File(path + filePath);

		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		//builder.processNewCode(annotator.getHeadNode(), faulty.equals("Y")?true:false, students.retrieveStudentID(studentID));
		try
		{
			System.out.println("How many times should the test be performed? ");
			String testCount = getInput(br);
			int tCount = Integer.parseInt(testCount);
			
			builder.scaleTest(annotator.getHeadNode(), true, -1, tCount);
				
//			  planID = builder.processNewCodeAbvr(annotator.getHeadNode(), faulty.equals("Y")?true:false, students.retrieveStudentID(studentID));
//
//			if (faulty.equals("Y")) {
//				System.out.println("Input level 3 feedback for submitted code: ");
//				String feedback = getInput(br);
//				
//				DerbyUtils.addFeedbackInstance(planID, 3, feedback, "<p>" + feedback + "</p>");
//			}
		} catch (Exception e) {
			String feedback = "This code tries to perform an operation on a variable to itself.";
			System.out.println(feedback);
			e.printStackTrace();
		}
		
	}

	public static void addAutoCase(int CaseNo, CASTGDTBuilder builder, String path) throws IndexOutOfBoundsException, ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
		try
		{
			File file = new File(path + "Cases\\C" + CaseNo + ".c");
			//System.out.println("Adding to case base C" + CaseNo);
			CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
			annotator.annotateCode();
			
			//Read feedback from file
			File file2 = new File(path + "Cases\\F" + CaseNo + ".c");
			String feedback = "";
			FileInputStream fis = new FileInputStream(file2);
			try {
				Scanner sc = new Scanner(fis);
				try {
					feedback = sc.nextLine();
				} finally {
					sc.close();
				}
			} finally {
				fis.close();
			}
			builder.addNewFaultyCase(annotator.getHeadNode(), feedback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void addCase(BufferedReader br, CASTGDTBuilder builder, String path) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
		System.out.println("Add faulty case\n");
		
		System.out.println("Input path to code: ");
		String filePath = getInput(br);
		
		System.out.println("Input level 3 feedback: ");
		String feedback = getInput(br);
		
		File file = new File(path + "Cases\\" + filePath);
		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		
		builder.addNewFaultyCase(annotator.getHeadNode(), feedback);
		
	}
	
	// GUI VER.
	public static void addCase(BufferedReader br, CASTGDTBuilder builder, String path, 
			String filePath, String feedback) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
		File file = new File(filePath);
		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		
		builder.addNewFaultyCase(annotator.getHeadNode(), feedback);
	}
	
	//TODO: DO NOT ASK ANYMORE FOR PATH TO CODE, INSTEAD GET LAST SUBMITTED VIA LOG
	public static void askForHelpAbvr(BufferedReader br,
			CASTGDTStudentTracker students, CASTGDTBuilder builder, String path)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		System.out.println("Ask for help\n");
		System.out.println("Input student ID: ");
		String studentID = getInput(br);
		
//		System.out.println("Input path to code: ");
//		String filePath = getInput(br);
//		
//		File file = new File(path + filePath);
//		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
//		annotator.annotateCode();
		builder.askForHelpAbvr(students.retrieveStudentID(studentID));
	}
	

	//TODO: DO NOT ASK ANYMORE FOR PATH TO CODE, INSTEAD GET LAST SUBMITTED VIA LOG
	public static void askForHelp(BufferedReader br,
			CASTGDTStudentTracker students, CASTGDTBuilder builder, String path)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		System.out.println("Ask for help\n");
		System.out.println("Input student ID: ");
		String studentID = getInput(br);
		
//		System.out.println("Input path to code: ");
//		String filePath = getInput(br);
//		
//		File file = new File(path + filePath);
//		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
//		annotator.annotateCode();
		builder.askForHelp(students.retrieveStudentID(studentID));
	}
	
	// GUI VER.
	public static void askForHelp(BufferedReader br,
			CASTGDTStudentTracker students, CASTGDTBuilder builder, String path,
			String studentID)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		
//		System.out.println("Input path to code: ");
//		String filePath = getInput(br);
//		
//		File file = new File(path + filePath);
//		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
//		annotator.annotateCode();
		builder.askForHelp(students.retrieveStudentID(studentID));
	}
	
	public static void submitNewCodeDebug(BufferedReader br, CASTGDTBuilder builder, String path) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
		boolean valid = false;
		
		System.out.println("Input path to code: ");
		String filePath = getInput(br);
		
		String faulty = null;
		while (valid != true) {
			System.out.println("Is the code faulty? Y/N");
			faulty = getInput(br);
			 if (faulty.equals("Y") || faulty.equals("N")) valid = true;
			 else System.out.println("Invalid input!\n");
		}
		
		
		File file = new File(path + filePath);
		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		
		builder.processNewCode(annotator.getHeadNode(), faulty.equals("Y")?true:false, -1);
	}
	
	public static void submitNewCodeAbvr(BufferedReader br,
			CASTGDTStudentTracker students, CASTGDTBuilder builder, String path, ArrayList<File> tci, ArrayList<File> tco)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		boolean valid = false;
		System.out.println("Submit Code\n");
		System.out.println("Input student ID: ");
		String studentID = getInput(br);
		
		System.out.println("Input path to code: ");
		String filePath = getInput(br);

		File file = new File(path + filePath);
		
		SourceCodeConverter scc = new SourceCodeConverter(file, path);
		scc.activate();
		
		//TODO: ArrayList for testcase Inputs and test case outputs
		TesterAndScorer tas = new TesterAndScorer(scc.getModifiedSource(), tci, tco, path);
		tas.activate();
		
		String faulty = null;
		while (valid != true) {
			System.out.println("Is the code faulty? Y/N");
			faulty = getInput(br);
			 if (faulty.equals("Y") || faulty.equals("N")) valid = true;
			 else System.out.println("Invalid input!\n");
		}
		
		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		//builder.processNewCode(annotator.getHeadNode(), faulty.equals("Y")?true:false, students.retrieveStudentID(studentID));
		try
		{
			int planID = builder.processNewCodeAbvr(annotator.getHeadNode(), faulty.equals("Y")?true:false, students.retrieveStudentID(studentID));

			if (faulty.equals("Y")) {
				System.out.println("Input level 3 feedback for submitted code: ");
				String feedback = getInput(br);
				
				DerbyUtils.addFeedbackInstance(planID, 3, feedback, "<p>" + feedback + "</p>");
			}
		} catch (Exception e) {
			String feedback = "This code tries to perform an operation on a variable to itself.";
			System.out.println(feedback);
			e.printStackTrace();
		}
		/* process New Code will print the necessary feedback */
	}
	
	public static void submitNewCode(BufferedReader br,
			CASTGDTStudentTracker students, CASTGDTBuilder builder, String path, ArrayList<File> tci, ArrayList<File> tco)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		boolean valid = false;
		System.out.println("Submit Code\n");
		System.out.println("Input student ID: ");
		String studentID = getInput(br);
		
		System.out.println("Input path to code: ");
		String filePath = getInput(br);

		File file = new File(path + filePath);
		
		SourceCodeConverter scc = new SourceCodeConverter(file, path);
		scc.activate();
		
		//TODO: ArrayList for testcase Inputs and test case outputs
		TesterAndScorer tas = new TesterAndScorer(scc.getModifiedSource(), tci, tco, path);
		tas.activate();
		
		String faulty = null;
		while (valid != true) {
			System.out.println("Is the code faulty? Y/N");
			faulty = getInput(br);
			 if (faulty.equals("Y") || faulty.equals("N")) valid = true;
			 else System.out.println("Invalid input!\n");
		}
		
		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		builder.processNewCode(annotator.getHeadNode(), faulty.equals("Y")?true:false, students.retrieveStudentID(studentID));
		
		/* process New Code will print the necessary feedback */
	}

	// GUI VER.
	public static void submitNewCode(BufferedReader br,
			CASTGDTStudentTracker students, CASTGDTBuilder builder, String path, ArrayList<File> tci, ArrayList<File> tco, 
			String studentID, String filePath, String faulty)
			throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		File file = new File(filePath);
		
		SourceCodeConverter scc = new SourceCodeConverter(file, file.getAbsolutePath());
		scc.activate();
		
		//TODO: ArrayList for testcase Inputs and test case outputs
		TesterAndScorer tas = new TesterAndScorer(scc.getModifiedSource(), tci, tco, file.getAbsolutePath());
		tas.activate();
		
		CASTCodeAnnotator annotator = new CASTCodeAnnotator(file);
		annotator.annotateCode();
		builder.processNewCode(annotator.getHeadNode(), faulty.equals("Y")?true:false, students.retrieveStudentID(studentID));
		
		/* process New Code will print the necessary feedback */
	}
	
	public static void registerNewStudent(BufferedReader br,
			CASTGDTStudentTracker students, int goalID) throws IOException, SQLException {
		System.out.println("Register student\n");
		System.out.println("Input student ID: ");
		String studentID = getInput(br);
		
		System.out.println("Input student Name: ");
		String studentName = getInput(br);
		
		int studentKey = DerbyUtils.addNewStudent(studentID, studentName, goalID);
		students.addStudent(studentKey, studentID, studentName);
	}

	// GUI VER.
	public static void registerNewStudent(BufferedReader br,
			CASTGDTStudentTracker students, int goalID, 
			String studentID, String studentName) throws IOException, SQLException {
		
		int studentKey = DerbyUtils.addNewStudent(studentID, studentName, goalID);
		students.addStudent(studentKey, studentID, studentName);
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

	public static String getMenuInputAbvr(BufferedReader br) throws IOException {
		boolean valid = false;
		String input = "";
		while (valid != true) {
			System.out.println("Please select an action: ");
			System.out.println("1 - Exit");
			System.out.println("2 - Register New Student");
			System.out.println("3 - Submit Code");
			System.out.println("4 - Student Ask For Help");
			System.out.println("5 - Print GDT");
			System.out.println("7 - Scalablity Experiment");
			
			input = getInput(br);
			
			if (input.equals(SCALE_EXP) || input.equals(MENU_EXIT) || input.equals(MENU_ASK_HELP) || input.equals(MENU_REG_STUDENT) || input.equals(MENU_SUBMIT_CODE) || input.equals(MENU_PRINT_GDT))
				valid = true;
			else System.out.println("Invlid Input! Input again\n");
			
		}
		return input;
	}
	
	public static String getMenuInput(BufferedReader br) throws IOException {
		boolean valid = false;
		String input = "";
		while (valid != true) {
			System.out.println("Please select an action: ");
			System.out.println("1 - Exit");
			System.out.println("2 - Register New Student");
			System.out.println("3 - Submit Code");
			System.out.println("4 - Student Ask For Help");
			System.out.println("5 - Print GDT");
			System.out.println("6 - Add Faulty Case");
			
			input = getInput(br);
			
			if (input.equals(MENU_EXIT) || input.equals(MENU_ASK_HELP) || input.equals(MENU_REG_STUDENT) || input.equals(MENU_SUBMIT_CODE) || input.equals(MENU_PRINT_GDT) || input.equals(MENU_ADD_CASE))
				valid = true;
			else System.out.println("Invlid Input! Input again\n");
			
		}
		return input;
	}

//	public static void printGDTBracketNotation(CASTGDTBuilder cgdtBuilder) throws Exception {
//		DFSBracketNotationWalker bracket = new DFSBracketNotationWalker(cgdtBuilder.getHeadGDTNode());
//		System.out.println(bracket.getBracketNotation());
//	}
	
	public static void printGDT(CASTGDTBuilder cgdtBuilder, String path) throws Exception {
		DFSGenericTreeWalker dfsTreeWalker = new DFSGenericTreeWalker(cgdtBuilder.getHeadGDTNode());
		File file = new File(path + "GDT.txt");
		FileOutputStream fos = new FileOutputStream(file);
		try {
			PrintStream ps = new PrintStream(fos);
			try {
				ps.print("GOAL ID: " + cgdtBuilder.getSuperGoal().getDBID());
				ps.println();
				
				System.out.println();
				
				while (!dfsTreeWalker.isFinished()) {
					System.out.println(dfsTreeWalker.toStringCurrentNode());
					ps.print(dfsTreeWalker.toStringCurrentNode());
					ps.println();
					dfsTreeWalker.nextNode();
				}
				
				System.out.println();
			} finally {
				ps.close();
			}
		} finally {
			fos.close();
		}
		
	}
	
	// GUI VER.
	public static void printGDT(CASTGDTBuilder cgdtBuilder, String path, String filePath) throws Exception {
		DFSGenericTreeWalker dfsTreeWalker = new DFSGenericTreeWalker(cgdtBuilder.getHeadGDTNode());
		File file = new File(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		try {
			PrintStream ps = new PrintStream(fos);
			try {
				ps.print("GOAL ID: " + cgdtBuilder.getSuperGoal().getDBID());
				ps.println();
				
				System.out.println();
				
				while (!dfsTreeWalker.isFinished()) {
					System.out.println(dfsTreeWalker.toStringCurrentNode());
					ps.print(dfsTreeWalker.toStringCurrentNode());
					ps.println();
					dfsTreeWalker.nextNode();
				}
				
				System.out.println();
			} finally {
				ps.close();
			}
		} finally {
			fos.close();
		}
		
	}

	@SuppressWarnings("unused")
	public static TranslationUnitNode createParentNode(File file) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, Exception {
		CASTLexer lexer = new CASTLexer(file);
		TokenizedCode tokens = lexer.getTokens();
		CASTParser parser = new CASTParser(tokens, file);
		TranslationUnitNode parentNode = parser.getParentNode();
		DFSGenericTreeWalker dfsTreeWalker = new DFSGenericTreeWalker(parentNode);

		System.out.println();
		
		while (!dfsTreeWalker.isFinished()) {
			System.out.println(dfsTreeWalker.toStringCurrentNode());
			dfsTreeWalker.nextNode();
		}
		
		System.out.println();
		
		RootToLeafPathFactory rtlPathFactory = new RootToLeafPathFactory(parentNode);
		ArrayList<RootToLeafPath> paths = rtlPathFactory.generateRootToLeafPaths();
		
		for (RootToLeafPath rtl: paths) {
			for (Node node:rtl) {
				System.out.println(node.toString());
			}
			System.out.println();
		}
		
		System.out.println();
		return parentNode;
	}

	@SuppressWarnings("unused")
	public static void testTokens() {
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new IntegerVariableIdentifierToken("x"));
		tokens.add(new AssignmentOperatorToken());
		tokens.add(new IntegerVariableIdentifierToken("x"));
		tokens.add(new PlusOperatorToken(PlusMinusType.BINARY_OPERATOR));
		tokens.add(new IntegerLiteralToken("1"));
		tokens.add(new SemicolonToken(SemicolonType.END_OF_STATEMENT));
		
		for(Token t:tokens) {
			System.out.println(t.toString());
		}
	}

}
