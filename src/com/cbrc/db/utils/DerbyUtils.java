package com.cbrc.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.cbrc.feedback.builders.Level3Feedback;

public class DerbyUtils {
	
	//TODO: DO NOT FORGET TO SWITCH TO ACTUAL DB
	
	public static final String DB_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	//public static final String DB_NAME = "CASTGDTDB";
	public static final String DB_NAME = "CASTGDTDBDEV";
	public static final String CONNECTION_URL = "jdbc:derby://localhost:1527/" + DB_NAME;
	
	public static ArrayList<Student> getStudentsRegisteredInGoal(int goalID) throws SQLException {
		ArrayList<Student> students = new ArrayList<Student>();
		int studentID;
		String studentIdentifier;
		String studentName;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT S.STUDENTID, S.STUDENTIDENTIFIER, S.STUDENTNAME FROM GOALSTUDENT G, STUDENT S WHERE G.STUDENTID = S.STUDENTID AND G.GOALID = ?");
				try {
					preparedStatement.setInt(1, goalID);
					
					ResultSet result = preparedStatement.executeQuery();
					try {
						while (result.next() == true) {
							studentID = result.getInt(1);
							studentIdentifier = result.getString(2);
							studentName = result.getString(3);
							
							students.add(new Student(studentID, studentIdentifier, studentName));
						}
					} finally {
						result.close();
					}
				} finally {
					conn.close();
				}
			} finally {
				conn.close();
			}
		}
		return students;
	}
	
	public static int getLastSubmittedPlanID(int goalID, int studentID) throws SQLException {
		int lastMatchedPlanID = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT LOGID, PLANID FROM LOG WHERE GOALID = ? AND STUDENTID = ? ORDER BY LOGID DESC");
				try {
					preparedStatement.setInt(1, goalID);
					preparedStatement.setInt(2, studentID);
					
					ResultSet result = preparedStatement.executeQuery();
					try {
						if (result.next() == true) {
							lastMatchedPlanID = result.getInt(2);
						}
					} finally {
						result.close();
					}
					
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return lastMatchedPlanID;
	}
	
	public static int getLastSubmittedPlanIDAfterInsert(int goalID, int studentID) throws SQLException {
		int lastMatchedPlanID = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT LOGID, PLANID FROM LOG WHERE GOALID = ? AND STUDENTID = ? ORDER BY LOGID DESC");
				try {
					preparedStatement.setInt(1, goalID);
					preparedStatement.setInt(2, studentID);
					
					ResultSet result = preparedStatement.executeQuery();
					try {
						if (result.next() == true) {
							if (result.next() == true) {
								lastMatchedPlanID = result.getInt(2);
							}
						}
					} finally {
						result.close();
					}
					
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return lastMatchedPlanID;
	}
	
	public static int getLastLoggedMatchedPlan(int goalID, int studentID) throws SQLException {
		int lastMatchedPlanID = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT LOGID, MATCHEDAGAINSTPLANID FROM LOG WHERE GOALID = ? AND STUDENTID = ? ORDER BY LOGID DESC");
				try {
					preparedStatement.setInt(1, goalID);
					preparedStatement.setInt(2, studentID);
					
					ResultSet result = preparedStatement.executeQuery();
					try {
						if (result.next() == true) {
							lastMatchedPlanID = result.getInt(2);
						}
					} finally {
						result.close();
					}
					
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return lastMatchedPlanID;
	}
	
	public static int log(int goalID, int studentID, int planID, int matchedAgainstPlanID, String feedbackString, int level, short isFaulty, short askForHelp) throws SQLException {
		int logKey = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO LOG (GOALID, STUDENTID, PLANID, MATCHEDAGAINSTPLANID, FEEDBACKSTRING, FEEDBACKLVL, ISFAULTY, ASKFORHELP) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				try {
					synchronized(preparedStatement)
					{
						preparedStatement.setInt(1, goalID);
						preparedStatement.setInt(2, studentID);
						preparedStatement.setInt(3, planID);
						preparedStatement.setInt(4, matchedAgainstPlanID);
						preparedStatement.setString(5, feedbackString);
						preparedStatement.setInt(6, level);
						preparedStatement.setShort(7, isFaulty);
						preparedStatement.setShort(8, askForHelp);
						preparedStatement.executeUpdate();
						
						ResultSet result = preparedStatement.getGeneratedKeys();
					    result.next();
					    logKey = result.getInt(1);
					}
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return logKey;
		
	}
	
	public static void rateFeedback (int feedbackID, int rating) throws SQLException {
		Connection conn = createConnection();
		if (conn != null) {
			try {
				int currRating = 0;
				PreparedStatement ratingQuery = conn.prepareStatement("SELECT RATING FROM FEEDBACKINSTANCE WHERE FEEDBACKINSTANCEID = ?");
				try {
					ratingQuery.setInt(1, feedbackID);
					ResultSet result = ratingQuery.executeQuery();
					
					if (result.next() == true) {
						currRating = result.getInt(1);
					}
					
					// DN: Add curr rating formula here
					
					PreparedStatement ratingUpdate = conn.prepareStatement("UPDATE FEEDBACKINSTANCE SET RATING = ? WHERE FEEDBACKINSTANCEID = ?");
					try {
						ratingUpdate.setInt(1, currRating);
						ratingUpdate.setInt(2, feedbackID);
						
						ratingUpdate.executeUpdate();
						
					} finally {
						ratingUpdate.close();
					}
					
				} finally {
					ratingQuery.close();
				}
			} finally {
				conn.close();
			}
		}
	}
	
	public static void upVoteFeedback (int feedbackID) throws SQLException {
		Connection conn = createConnection();
		if (conn != null) {
			try {
				int currRating = 0;
				PreparedStatement ratingQuery = conn.prepareStatement("SELECT RATING FROM FEEDBACKINSTANCE WHERE FEEDBACKINSTANCEID = ?");
				try {
					ratingQuery.setInt(1, feedbackID);
					ResultSet result = ratingQuery.executeQuery();
					
					if (result.next() == true) {
						currRating = result.getInt(1);
					}
					
					currRating++;
					PreparedStatement ratingUpdate = conn.prepareStatement("UPDATE FEEDBACKINSTANCE SET RATING = ? WHERE FEEDBACKINSTANCEID = ?");
					try {
						ratingUpdate.setInt(1, currRating);
						ratingUpdate.setInt(2, feedbackID);
						
						ratingUpdate.executeUpdate();
						
					} finally {
						ratingUpdate.close();
					}
					
				} finally {
					ratingQuery.close();
				}
			} finally {
				conn.close();
			}
		}
	}
	
	public static void downVoteFeedback (int feedbackID) throws SQLException {
		Connection conn = createConnection();
		if (conn != null) {
			try {
				int currRating = 0;
				PreparedStatement ratingQuery = conn.prepareStatement("SELECT RATING FROM FEEDBACKINSTANCE WHERE FEEDBACKINSTANCEID = ?");
				try {
					ratingQuery.setInt(1, feedbackID);
					ResultSet result = ratingQuery.executeQuery();
					
					if (result.next() == true) {
						currRating = result.getInt(1);
					}
					
					currRating--;
					PreparedStatement ratingUpdate = conn.prepareStatement("UPDATE FEEDBACKINSTANCE SET RATING = ? WHERE FEEDBACKINSTANCEID = ?");
					try {
						ratingUpdate.setInt(1, currRating);
						ratingUpdate.setInt(2, feedbackID);
						
						ratingUpdate.executeUpdate();
						
					} finally {
						ratingUpdate.close();
					}
					
				} finally {
					ratingQuery.close();
				}
			} finally {
				conn.close();
			}
		}
	}
	
	public static void setFeedbackLevel(int studentID, int goalID, int level) throws SQLException {
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("UPDATE GOALSTUDENT SET CURRENTLVL = ? WHERE GOALID = ? AND STUDENTID = ?");
				try {
					preparedStatement.setInt(1, level);
					preparedStatement.setInt(2, goalID);
					preparedStatement.setInt(3, studentID);
					
					preparedStatement.executeUpdate();
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		
	}
	
	public static HashMap<Integer, String> getCodeTrail(int studentID, int goalID) throws SQLException {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT CODE, PLANID FROM PLAN WHERE STUDENTID = ? AND GOALID = ? AND ISFAULTY = 1");
				try {
					preparedStatement.setInt(1, studentID);
					preparedStatement.setInt(2, goalID);
					
					ResultSet result = preparedStatement.executeQuery();
					
					while (result.next() == true) {
						map.put(result.getInt(2), result.getString(1));
					}
					
					return map;
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return null;
	}
	
	public static int getStudentFeedbackLevel(int studentID, int goalID) throws SQLException {
		int feedbacklvl = 1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT CURRENTLVL FROM GOALSTUDENT WHERE STUDENTID = ? AND GOALID = ?");
				try {
					preparedStatement.setInt(1, studentID);
					preparedStatement.setInt(2, goalID);
					ResultSet result = preparedStatement.executeQuery();
					
					if (result.next() == true) {
						feedbacklvl = result.getInt(1);
					}
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return feedbacklvl;
	}
	
	
	public static int addNewStudent(String studentID, String studentName, int goalID) throws SQLException {
		int newStudentKey = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				
				newStudentKey = executeInsertStudentWithID(conn, studentID, studentName);
				executeInsertGoalStudent(conn, newStudentKey, goalID);
			} finally {
				conn.close();
			}
		}
		return newStudentKey;
		
	}
	
	public static void addGoalStudent(int goalID, int studentID) throws SQLException {
		Connection conn = createConnection();
		if (conn != null) {
			try {
				executeInsertGoalStudent(conn, studentID, goalID);
			} finally {
				conn.close();
			}
		}
	}
	
	private static void executeInsertGoalStudent(Connection conn, int newStudentKey, int goalID) throws SQLException {
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement("INSERT INTO GOALSTUDENT (GOALID, STUDENTID, CURRENTLVL) VALUES (?, ?, 1)");
		try {
			synchronized(preparedStatement)
			{
				preparedStatement.setInt(1, goalID);
				preparedStatement.setInt(2, newStudentKey);
				preparedStatement.executeUpdate();
			}
			
		} finally {
			preparedStatement.close();
		}
	}

	private static int executeInsertStudentWithID(Connection conn, String studentID, String studentName) throws SQLException {
		PreparedStatement preparedStatement;
		int newStudentKey;
		preparedStatement = conn.prepareStatement("INSERT INTO STUDENT (STUDENTIDENTIFIER, STUDENTNAME) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		try {
			synchronized(preparedStatement)
			{
				preparedStatement.setString(1, studentID);
				preparedStatement.setString(2, studentName);
				preparedStatement.executeUpdate();
				
				ResultSet result = preparedStatement.getGeneratedKeys();
			    result.next();
			    newStudentKey = result.getInt(1);
			}
			
		} finally {
			preparedStatement.close();
		}
		return newStudentKey;
	}

	public static int addNewGoal(String goalName, String goalDescription) throws SQLException {
		int newGoalKey = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				
				newGoalKey = executeInsertGoalWithID(conn, goalName, goalDescription);
				
			} finally {
				conn.close();
			}
		}
		return newGoalKey;
	}
	
	public static int executeInsertGoalWithID(Connection conn, String goalName, String goalDescription) throws SQLException {
		PreparedStatement preparedStatement;
		int newGoalKey;
		preparedStatement = conn.prepareStatement("INSERT INTO GOAL (PROBLEM, DESCRIPTION) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		try {
			synchronized(preparedStatement)
			{
				preparedStatement.setString(1, goalName);
				preparedStatement.setString(2, goalDescription);
				preparedStatement.executeUpdate();
				
				ResultSet result = preparedStatement.getGeneratedKeys();
			    result.next();
			    newGoalKey = result.getInt(1);
			}
		} finally {
			preparedStatement.close();
		}
		return newGoalKey;
	}
	
//	public static ResultSet checkForNewCode() throws SQLException {
//		Connection conn = createConnection();
//		if (conn != null) {
//			try {
//				PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM NEWCODETABLE");
//				try {
//					ResultSet result = preparedStatement.executeQuery();
//					
//					return result;
//					
//				} finally {
//					preparedStatement.close();
//				}
//			} finally {
//				conn.close();
//			}
//		}
//		return null;
//	}
	
//	public static void markNewCodeAsRead(int codeID) throws SQLException {
//		Connection conn = createConnection();
//		PreparedStatement preparedStatement;
//		try {
//			preparedStatement = conn.prepareStatement("INSERT INTO PLAN (STUDENTID, ISFAULTY, CODE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
//			
//		} finally {
//			conn.close();
//		}
//		
//		
//		try {
//			synchronized(preparedStatement)
//			{
//				preparedStatement.setInt(1, studentID);
//				preparedStatement.setShort(2, isFaulty);
//				preparedStatement.setString(3, codeDirectory);
//				preparedStatement.executeUpdate();
//				
//				ResultSet result = preparedStatement.getGeneratedKeys();
//			    result.next();
//			    newPlanKey = result.getInt(1);
//			}
//			
//		} finally {
//			preparedStatement.close();
//		}
//		return newPlanKey;
//	}
	
	public static Level3Feedback getLevel3FeedbackWithRating(int planID) throws SQLException {
		int feedbackID = -1;
		String feedback;
		int rating = 0;
		Level3Feedback feedbackObj = null;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT FB.FEEDBACKSTRING, FB.FEEDBACKINSTANCEID, FB.RATING FROM PLANFEEDBACK PFB, FEEDBACKINSTANCE FB, PLAN P WHERE PFB.PLANID = P.PLANID AND PFB.FEEDBACKINSTANCEID = FB.FEEDBACKINSTANCEID AND FEEDBACKLVL = 3 AND P.PLANID = ?");
				try {
					preparedStatement.setInt(1, planID);
					ResultSet result = preparedStatement.executeQuery();
					
					if (result.next() == true) {
						if (!result.getString(1).equals("")) {
							feedback = result.getString(1);
							feedbackID = result.getInt(2);
							rating = result.getInt(3);
							feedbackObj = new Level3Feedback(feedbackID, rating, feedback);
						}
					}
					
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return feedbackObj;
	}
	
	public static HashMap<Integer, String> getLevel3Feedback(int planID) throws SQLException {
		int feedbackID = -1;
		String feedback;
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		Connection conn = createConnection();
		if (conn != null) {
			try {
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT FB.FEEDBACKSTRING, FB.FEEDBACKINSTANCEID FROM PLANFEEDBACK PFB, FEEDBACKINSTANCE FB, PLAN P WHERE PFB.PLANID = P.PLANID AND PFB.FEEDBACKINSTANCEID = FB.FEEDBACKINSTANCEID AND FEEDBACKLVL = 3 AND P.PLANID = ?");
				try {
					preparedStatement.setInt(1, planID);
					ResultSet result = preparedStatement.executeQuery();
					
					if (result.next() == true) {
						if (!result.getString(1).equals("")) {
							feedback = result.getString(1);
							feedbackID = result.getInt(2);
							
							map.put(feedbackID, feedback);
						}
					}
					
				} finally {
					preparedStatement.close();
				}
			} finally {
				conn.close();
			}
		}
		return map;
	}
	
	public static int addFirstSolutionToDB(String codeDirectory, String feedbackString, String feedbackHTML, int goalID) throws SQLException {
		int newPlanKey = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
	
				newPlanKey = executeInsertPlanWithID(conn, 0, (short) 0, codeDirectory, goalID);
				//int newFeedbackKey = executeInsertFeedbackInstanceWithID(conn, 4, feedbackString, feedbackHTML);
				//executeInsertPlanFeedBackWithID(conn, newPlanKey, newFeedbackKey);
				
			} finally {
				conn.close();
			}
		}
		return newPlanKey;
	}
	
	public static int addFeedbackInstance(int planID, int level, String feedbackString, String feedbackHTML) throws SQLException {
		int newFeedbackKey = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				
				newFeedbackKey = executeInsertFeedbackInstanceWithID(conn, level, feedbackString, feedbackHTML);
				executeInsertPlanFeedBackWithID(conn, planID, newFeedbackKey);
				
			} finally {
				conn.close();
			}
		}
		return newFeedbackKey;
	}
	
	public static int insertNewPlanWithID(int studentID, short isFaulty, String codeDirectory, int goalID) throws SQLException {
		int newPlanKey = -1;
		Connection conn = createConnection();
		if (conn != null) {
			try {
				newPlanKey = executeInsertPlanWithID(conn, studentID, isFaulty, codeDirectory, goalID);
			} finally {
				conn.close();
			}
		}
		return newPlanKey;
	}

	public static int executeInsertPlanWithID(Connection conn, int studentID, short isFaulty, String codeDirectory, int goalID) throws SQLException {
		PreparedStatement preparedStatement;
		int newPlanKey;
		preparedStatement = conn.prepareStatement("INSERT INTO PLAN (STUDENTID, ISFAULTY, CODE, GOALID) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		try {
			synchronized(preparedStatement)
			{
				preparedStatement.setInt(1, studentID);
				preparedStatement.setShort(2, isFaulty);
				preparedStatement.setString(3, codeDirectory);
				preparedStatement.setInt(4, goalID);
				preparedStatement.executeUpdate();
				
				ResultSet result = preparedStatement.getGeneratedKeys();
			    result.next();
			    newPlanKey = result.getInt(1);
			}
			
		} finally {
			preparedStatement.close();
		}
		return newPlanKey;
	}
	
	public static int executeInsertFeedbackInstanceWithID(Connection conn, int feedbackLevel, String feedbackString, String feedbackHTML) throws SQLException {
		PreparedStatement preparedStatement;
		int newFeedbackInstanceKey;
		preparedStatement = conn.prepareStatement("INSERT INTO FEEDBACKINSTANCE (FEEDBACKLVL, FEEDBACKSTRING, FEEDBACKHTML, RATING) VALUES (?, ?, ?, 0)", Statement.RETURN_GENERATED_KEYS);
		try {
			synchronized(preparedStatement)
			{
				preparedStatement.setInt(1, feedbackLevel);
				preparedStatement.setString(2, feedbackString);
				preparedStatement.setString(3, feedbackHTML);
				preparedStatement.executeUpdate();
				
				ResultSet result = preparedStatement.getGeneratedKeys();
			    result.next();
			    newFeedbackInstanceKey = result.getInt(1);
			}
			
		} finally {
			preparedStatement.close();
		}
		return newFeedbackInstanceKey;
	}
	
	public static int executeInsertPlanFeedBackWithID(Connection conn, int planKey, int feedbackKey) throws SQLException {
		PreparedStatement preparedStatement;
		int newPlanFeedbackID;
		preparedStatement = conn.prepareStatement("INSERT INTO PLANFEEDBACK (PLANID, FEEDBACKINSTANCEID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		try {
			synchronized(preparedStatement)
			{
				preparedStatement.setInt(1, planKey);
				preparedStatement.setInt(2, feedbackKey);
				preparedStatement.executeUpdate();
				
				ResultSet result = preparedStatement.getGeneratedKeys();
			    result.next();
			    newPlanFeedbackID = result.getInt(1);
			}
			
		} finally {
			preparedStatement.close();
		}
		return newPlanFeedbackID;
	}
	
	public static ResultSet runSQLQuery(String SQL) throws SQLException {
		Connection conn = createConnection();
		Statement statement;
		if (conn != null) {
			try {
				statement = conn.createStatement();
				try {
					return statement.executeQuery(SQL);
				} finally {
					statement.close();
				}
			} finally {
				conn.close();
			}
		}
		return null;
	}
	
	public static void runSQLNonQuery(String SQL) throws SQLException {
		Connection conn = createConnection();
		Statement statement;
		if (conn != null) {
			try {
				statement = conn.createStatement();
				try {
					statement.execute(SQL);
				} finally {
					statement.close();
				}
			} finally {
				conn.close();
			}
		}
	}
	
	public static Connection createConnection() {
		 Connection conn = null;
		 
		 try {
	          Class.forName(DB_DRIVER); 
	          //System.out.println(DB_DRIVER + " loaded. ");
	      } catch(java.lang.ClassNotFoundException e)     {
	          System.err.print("ClassNotFoundException: ");
	          System.err.println(e.getMessage());
	          System.out.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
	      }
		 
		 try {
			 conn = DriverManager.getConnection(CONNECTION_URL);		 
	         //System.out.println("Connected to database " + DB_NAME);
		 } catch (Throwable e)  {   
	         System.out.println("Exception thrown: ");
	         e.printStackTrace();
	     }
		 
		 return conn;
	}

}
