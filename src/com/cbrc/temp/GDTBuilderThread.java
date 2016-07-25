package com.cbrc.temp;

import java.io.File;
import java.io.IOException;

import com.cbrc.gdt.builder.CASTCodeAnnotator;
import com.cbrc.gdt.builder.CASTGDTBuilder;

public class GDTBuilderThread extends Thread {

	private CASTGDTBuilder cgdtBuilder;

	public GDTBuilderThread() {
		this.cgdtBuilder = new CASTGDTBuilder();
		this.cgdtBuilder.setDebug(true);
	}
	
	public synchronized void processFirstCode(String filename, String professorFeedback) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, Exception {
		File file1 = new File(filename);
		CASTCodeAnnotator codeAnnotator = new CASTCodeAnnotator(file1);
		codeAnnotator.annotateCode();
		this.cgdtBuilder.processFirstCode(codeAnnotator.getHeadNode(), professorFeedback);
	}
	
	public synchronized void processCode(String filename) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, Exception {
		File file1 = new File(filename);
		CASTCodeAnnotator codeAnnotator = new CASTCodeAnnotator(file1);
		codeAnnotator.annotateCode();
		this.cgdtBuilder.processNewCode(codeAnnotator.getHeadNode());
	}
	
	public void run() {
		try {
			while (true) {
				//System.out.println("Im still alive!");
				sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public CASTGDTBuilder getCgdtBuilder() {
		return cgdtBuilder;
	}

	public void setCgdtBuilder(CASTGDTBuilder cgdtBuilder) {
		this.cgdtBuilder = cgdtBuilder;
	}
	
}
