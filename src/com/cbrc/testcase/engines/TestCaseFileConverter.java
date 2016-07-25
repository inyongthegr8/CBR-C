/*
 * TestCaseFileConverter:
 *  Converts test cases into files for use
 *  by the test case engine.
 * Inputs:
 *  ArrayList of TestCases
 * Outputs:
 *  Text files of each test case, divided into
 *  input files and output files, stored into
 *  a folder that is program dependent.
 *  
 */

package com.cbrc.testcase.engines;


import java.io.File;
import java.util.ArrayList;

import com.cbrc.testcase.interfaces.Engine;
import com.cbrc.testcase.utils.TestCase;
import com.cbrc.testcase.utils.TestCaseStatics;

public class TestCaseFileConverter implements Engine{
	
	private ArrayList<TestCase> testCases;
	private ArrayList<File> testCaseInputFiles;
	private ArrayList<File> testCaseOutputFiles;
	
	public TestCaseFileConverter(ArrayList<TestCase> testCases){
		this.testCases = testCases;
		this.testCaseInputFiles = new ArrayList<File>();
		this.testCaseOutputFiles = new ArrayList<File>();
		
	}
	
	public ArrayList<File> getInputFiles(){
		return this.testCaseInputFiles;
	}
	
	public ArrayList<File> getOutputFiles(){
		return this.testCaseOutputFiles;
	}
	
	public void activate(){
		for(TestCase tc:this.testCases){
			File inputFile = tc.toFile(TestCaseStatics.INPUT);
			File outputFile = tc.toFile(TestCaseStatics.OUTPUT);
			this.testCaseInputFiles.add(inputFile);
			this.testCaseOutputFiles.add(outputFile);
		}
	}
	

}
