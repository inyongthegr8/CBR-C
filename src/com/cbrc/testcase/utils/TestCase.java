package com.cbrc.testcase.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class TestCase {
	
	//Fields
	private ArrayList<String> inputs;
	private ArrayList<String> outputs;
	private int caseNumber;
	private String homeDirectory;
	private String programName;
	
	private String path;
	
	//Constructors
	public TestCase(ArrayList<String> inputs, ArrayList<String> outputs, int caseNumber, String homepath, String programName){
		this.inputs = inputs;
		this.outputs = outputs;
		this.caseNumber = caseNumber;

		if (homepath != null) {
			this.path = homepath + "Test Cases\\";
		} else this.path = TestCaseStatics.DEFAULT_PATH;
		
		this.homeDirectory = this.path + programName + "\\";
	}
	
	//Public Methods
	public void setHomeDirectory(String homeDirectory){
		this.homeDirectory = homeDirectory + programName + "\\";
	}
	
	public File toFile(int type){
		File testCaseFile = null;
		if(type == TestCaseStatics.INPUT) testCaseFile = new File(homeDirectory + "Input " + this.caseNumber + ".txt");
		else if(type == TestCaseStatics.OUTPUT) testCaseFile = new File(homeDirectory + "Output " + this.caseNumber + ".txt");
		if(!testCaseFile.exists()){
			try {
				testCaseFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(testCaseFile);
			PrintStream prt = new PrintStream(fos);
			try {
				if(type == TestCaseStatics.INPUT){
					for(String input:this.inputs){
						prt.println(input);
					}
				}
				else if(type == TestCaseStatics.OUTPUT){
					for(String output:this.outputs){
						prt.println(output);
					}		
				}
			} finally {
				prt.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return testCaseFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
