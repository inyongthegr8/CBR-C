/*
 * TesterAndScorer:
 *  Tests the modified source code against
 *  the testcases converted into files.
 * Inputs:
 *  modified source code
 *  inputs for test cases
 *  outputs for test cases
 * Outputs:
 *  arraylist displaying which test cases
 *  failed and which succeeded.
 */

package com.cbrc.testcase.engines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.cbrc.testcase.interfaces.Engine;

public class TesterAndScorer implements Engine{
	
	private File modifiedSource;
	private ArrayList<File> testCaseInputs;
	private ArrayList<File> testCaseOutputs;
	private ArrayList<Boolean> succeeded;
	private String path; 
	
	public TesterAndScorer(File modifiedSource, ArrayList<File> testCaseInputs, ArrayList<File> testCaseOutputs, String path){
		this.modifiedSource = modifiedSource;
		this.testCaseInputs = testCaseInputs;
		this.testCaseOutputs = testCaseOutputs;
		this.succeeded = new ArrayList<Boolean>();
		this.path = path;
	}
	
	public ArrayList<Boolean> getSucceeded(){
		return this.succeeded;
	}

	//TODO: Output test case input, exoutput and output results
	
	public void activate() {
		FileInputStream fis;
		Scanner sc1;
		Scanner sc2;
		String executablepath;
		boolean same = true;
		int i = 0;
		
		for(File tci:testCaseInputs){
			System.out.println("\nTesting Against Test Case " + i + ":\n");
			StringBuilder acoutput = new StringBuilder();
			StringBuilder exoutput = new StringBuilder();
			same = true;
			try {
				File f = new File(this.path + "TestCases\\testcase.txt");
				File tco = testCaseOutputs.get(i);
				if(!f.exists()){
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				this.copyFile(tci, f);
				this.compileCode(this.modifiedSource);
				executablepath = modifiedSource.getPath().substring(0, modifiedSource.getPath().length()-1).concat("exe");
				this.runCode(executablepath, 2500);
				
				File output = new File(this.path + "TestCases\\output.txt");
				fis = new FileInputStream(output);
				sc1 = new Scanner(fis);
				sc2 = new Scanner(tco);
				
				while(sc1.hasNext() && sc2.hasNext()){
//					String sc1str = sc1.next();
//					acoutput.append(sc1str + "\n");
//					
//					String sc2str = sc2.next();
//					exoutput.append(sc2str + "\n");
					
					String sc1str = sc1.nextLine();
					acoutput.append(sc1str + "\n");
					
					String sc2str = sc2.nextLine();
					exoutput.append(sc2str + "\n");
					if(!sc1str.equals(sc2str)){
						same = false;
					}
				}
				if(sc1.hasNext() || sc2.hasNext()){
					while (sc1.hasNext()) acoutput.append(sc1.next() + "\n");
					while (sc2.hasNext()) exoutput.append(sc2.next() + "\n");
					same = false;
				}
				
				System.out.println("Expected output: \n" + exoutput.toString());
				System.out.println("Actual output: \n" + acoutput.toString());
				System.out.println("Match: " + (same == true?"TRUE":"FALSE"));
				this.succeeded.add(same);
				i++;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void copyFile(File source, File destination){
		StringBuilder inputs = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(source);
			try {
				FileOutputStream fos = new FileOutputStream(destination);
				try {
					byte[] buf = new byte[1024];
		            int i = 0;
		            while ((i = fis.read(buf)) != -1) {
		                fos.write(buf, 0, i);
		            }

				} finally {
					fos.close();
				}
				
			} finally {
				fis.close();
			}
			
            
            fis = new FileInputStream(source);
            try {
            	Scanner sc = new Scanner(fis);
            	try {
            		while(sc.hasNext()) {
                		inputs.append(sc.next() + "\n");
                	}
                	System.out.println("Inputs: \n" + inputs.toString());
            	} finally {
            		sc.close();
            	}
            	
            } finally {
            	fis.close();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void compileCode(File sourceCode){
		String command = "cmd /c gcc ";
		String path = sourceCode.getPath();
        String exepath = path.substring(0, path.length()-1).concat("exe");
        
        command = command.concat(path).concat(" -o ").concat(exepath);
        
        System.out.println(command);
		
		Runtime rt = Runtime.getRuntime();
		Process p;
		try {
			p = rt.exec(command);
			@SuppressWarnings("unused")
			int exitVal = p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void runCode(final String commandLine, final long timeout) throws IOException, InterruptedException, TimeoutException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(commandLine);
		/* Set up process I/O. */
		Worker worker = new Worker(process);
		worker.start();
		try {
			worker.join(timeout);
		} catch(InterruptedException ex) {
			worker.interrupt();
			Thread.currentThread().interrupt();
			throw ex;
		} finally {
			process.destroy();
		}
	}
	
//	private void runCode(String path){
//		try {
//			//Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path);
//			Process p = Runtime.getRuntime().exec(path);
//			
//			p.waitFor();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
