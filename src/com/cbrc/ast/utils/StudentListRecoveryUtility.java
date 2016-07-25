package com.cbrc.ast.utils;

import java.sql.SQLException;
import java.util.ArrayList;

import com.cbrc.db.utils.DerbyUtils;
import com.cbrc.db.utils.Student;
import com.cbrc.gdt.builder.CASTGDTStudentTracker;

public class StudentListRecoveryUtility {
	
	public CASTGDTStudentTracker recoverStudents(int goalID, int newGoalID) throws SQLException {
		CASTGDTStudentTracker students = new CASTGDTStudentTracker();
		ArrayList<Student> studentList = DerbyUtils.getStudentsRegisteredInGoal(goalID);
		for (Student student:studentList) {
			students.addStudent(student.getStudentID(), student.getStudentIdentifier(), student.getStudentName());
			DerbyUtils.addGoalStudent(newGoalID, student.getStudentID());
		}
		return students;
	}

}
