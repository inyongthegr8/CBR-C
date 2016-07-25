package com.cbrc.ast.preprocessor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CASTPreprocessor {

	private File source;
	
	public CASTPreprocessor(File source) {
		this.source = source;
	}

	public void preprocess() throws IOException {
		String tempFileName = this.source.getPath().substring(0, this.source.getPath().length() - 2) + "-temp.c";
		// TODO: Remove newline at end of the file, change ++ and -- to non shorthand form, change +=, -=, /= and *= to  non shorthand form
		
		File processed = new File(tempFileName);
		FileInputStream inputStream = new FileInputStream(this.source);
		
		try {
			PrintWriter  writer = new PrintWriter(processed);
			try {
				DataInputStream dataInStream = new DataInputStream(inputStream);
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(dataInStream));
					try {
						reader.mark(1);
						while (reader.read() != -1) {
							reader.reset();
							
							reader.mark(3);
							char c1 = (char) reader.read();
							char c2 = (char) reader.read();
							
							if ((c1 == '+' && c2 == '+') || (c1 == '-' && c2 == '-')) {
								
							}
							
							reader.mark(1);
						}
						
						this.setSource(processed);
					} finally {
						reader.close();
					}
				} finally {
					dataInStream.close();
				}
			} finally {
				writer.close();
			}
		} finally {
			inputStream.close();
		}
		
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

}
