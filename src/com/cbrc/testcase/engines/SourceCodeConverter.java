/*
 * SourceCodeConverter:
 *  Converts a given source code into
 *  a format that readily works with
 *  the test case engine. Basically, it
 *  appends lines to the code that will
 *  enable it to function with the test
 *  case engine.
 * Inputs:
 *  Source code to be converted, name of the program
 * Outputs:
 *  A new version of the code that will be utilized
 *  by the testcase engine.
 */

package com.cbrc.testcase.engines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import com.cbrc.testcase.interfaces.Engine;

public class SourceCodeConverter implements Engine{

	private File sourceCode;
	private File modifiedSource;
	private Scanner sc;
	private String path;
	
	public SourceCodeConverter(File sourceCode, String path){
		this.sourceCode = sourceCode;
		this.path = modifyPath(path);
	}
	
	public SourceCodeConverter(String filePath, String path){
		this.sourceCode = new File(filePath);
		this.path = modifyPath(path);
	}
	
	public File getModifiedSource(){
		return this.modifiedSource;
	}
	
	private String modifyPath(String path) {
		path = path.replace("\\", "\\\\");
		return path;
	}
	
	public void activate(){
		String sourcePath, modPath;
		sourcePath = sourceCode.getPath();
		modPath = sourcePath.substring(0, sourcePath.length()-2);
		modPath = modPath.concat("mod.c");
		this.modifiedSource = new File(modPath);
		
		if(!this.modifiedSource.exists()){
			try {
				this.modifiedSource.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileInputStream fis = new FileInputStream(this.sourceCode);
			FileOutputStream fos = new FileOutputStream(this.modifiedSource);
			PrintStream ps = new PrintStream(fos);
			sc = new Scanner(fis);
			String codeLine = "";
			String prevCodeLine;
			boolean isMain = false;
			while(sc.hasNext()){
				prevCodeLine = codeLine;
				codeLine = sc.nextLine();
				codeLine.trim();
				
				codeLine = codeLine.replace("printf(", "fprintf(testcaseout,");
				codeLine = codeLine.replace("scanf(", "fscanf(testcasein,");
				if(codeLine.contains("int main(") || codeLine.contains("void main(")){
					printUtiltoCode(ps);
					isMain = true;
				}
				if((codeLine.contains("return0;") || codeLine.contains("return 0;")) && isMain == true){
					ps.print("  appendNewLine();");ps.println();
					ps.print("  fclose(testcasein);");ps.println();
					ps.print("  fclose(testcaseout);");ps.println();
				}

				ps.print(codeLine);ps.println();
				if((prevCodeLine.contains("int main(") || prevCodeLine.contains("void main(")) && codeLine.contains("{")){
					ps.print("  openTestCase();");ps.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// TODO: Change hardcoded directory
	
	private void printUtiltoCode(PrintStream ps){
		ps.print("#include <stdio.h>");ps.println();
		ps.print("FILE *testcasein;");ps.println();
		ps.print("FILE *testcaseout;");ps.println();
		ps.print("void openTestCase(){");ps.println();
		ps.print("  if((testcasein = fopen(\"" + this.path + "TestCases\\\\testcase.txt\", \"r\")) == NULL)");ps.println();
		ps.print("  {");ps.println();
		ps.print("     printf(\"No such file\");");ps.println();
		ps.print("     exit(1);");ps.println();
		ps.print("  }");ps.println();
		ps.print("  testcaseout = fopen(\"" + this.path + "TestCases\\\\output.txt\", \"w\");");ps.println();
		ps.print("}");ps.println();
		ps.print("void appendNewLine(){");ps.println();
		ps.print("  fprintf(testcaseout, \"\\n\\n\");");ps.println();
		ps.print("}");ps.println();
	}
}
