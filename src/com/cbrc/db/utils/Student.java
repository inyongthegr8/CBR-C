package com.cbrc.db.utils;

public class Student {
	
	private int studentID;
	private String studentIdentifier;
	private String studentName;
	
	public Student(int studentID, String studentIdentifier, String studentName) {
		this.studentID = studentID;
		this.studentIdentifier = studentIdentifier;
		this.studentName = studentName;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getStudentIdentifier() {
		return studentIdentifier;
	}

	public void setStudentIdentifier(String studentIdentifier) {
		this.studentIdentifier = studentIdentifier;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	

}
