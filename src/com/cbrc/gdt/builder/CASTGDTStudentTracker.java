package com.cbrc.gdt.builder;

import java.util.ArrayList;

public class CASTGDTStudentTracker {

	private class Student {
		private int studentID;
		private String studentIdentifier;
		private String studentName;
		
		public Student (int studentID, String studentIdentifier, String studentName) {
			this.studentID = studentID;
			this.studentIdentifier = studentIdentifier;
			this.studentName = studentName;
		}
		
		public int getStudentID() {
			return studentID;
		}
		
		public String getStudentIdentifier() {
			return studentIdentifier;
		}
		
		public String getStudentName() {
			return studentName;
		}
	}
	
	private ArrayList<Student> students;
	
	public CASTGDTStudentTracker() {
		students = new ArrayList<Student>();
	}
	
	public void addStudent(int studentID, String studentIdentifier, String studentName) {
		Student student = new Student(studentID, studentIdentifier, studentName);
		students.add(student);
	}
	
	public int retrieveStudentID(String studentIdentifier) {
		for (Student student:students) {
			if (student.getStudentIdentifier().equals(studentIdentifier)) return student.getStudentID();
		}
		return -1;
	}
	
	public String retrieveStudentName(String studentIdentifier) {
		for (Student student:students) {
			if (student.getStudentIdentifier().equals(studentIdentifier)) return student.getStudentName();
		}
		return null;
	}
	
}
