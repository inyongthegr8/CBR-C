package com.cbrc.ast.preprocessor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CASTPreprocessor {

	private File source;

	public CASTPreprocessor(File source) {
		this.source = source;
	}

	public void preprocess() throws IOException {
		String tempFileName = this.source.getPath().substring(0, this.source.getPath().length() - 2) + "-temp.c";
		// TODO: change ++ and -- to non shorthand form, change +=, -=, /= and
		// *= to non shorthand form
		// TODO: change constants to actual values

		File processed = new File(tempFileName);
		FileInputStream inputStream = new FileInputStream(this.source);

		try {
			ArrayList<String> cvars = new ArrayList<String>(); // constant variables
			ArrayList<String> cvals = new ArrayList<String>(); // constant values
			PrintWriter writer = new PrintWriter(processed);
			try {
				DataInputStream dataInStream = new DataInputStream(inputStream);
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(dataInStream));
					try {
						// Preprocessor Directives
						reader.mark(1);
						while (reader.read() != -1) {
							reader.reset();
							reader.mark(8);
							// #define or #import read
							char keyword[] = new char[8];
							String res = "";
							for(int i = 0; i < keyword.length; i++)
							{
								char c = (char) reader.read();
								keyword[i] = c;
								res += Character.toString(keyword[i]);
							}
							res = res.trim();
							boolean markInclude = res.equals("#include");
							boolean markDefine = res.equals("#define");
							if(markInclude) reader.read(); // omit spacebar
							while(markInclude)
							{
								reader.mark(64); // read <filename.h>
								Character c = (char) reader.read();
								if(c == '\n' || c == '\r')
								{
									markInclude = !markInclude;
									reader.read();
								}
							}
							while(markDefine)
							{
								reader.mark(64); // read variables
								String variable = "";
								Character c = (char) reader.read(); // read first entry
								while(c != ' ')
								{
									variable += Character.toString(c);
									c = (char) reader.read();
								}
								cvars.add(variable);
								reader.mark(16); // read variables
								String value = "";
								c = (char) reader.read(); // read first entry
								while(c != '\r')
								{
									value += Character.toString(c);
									c = (char) reader.read();
								}
								cvals.add(value);
								reader.mark(1);
								reader.read(); // omit newline
								markDefine = false;
							}
							
							// read variables

							/* future implementation.
							reader.mark(3);
							char c1 = (char) reader.read();
							char c2 = (char) reader.read();
							
							if ((c1 == '+' && c2 == '+') || (c1 == '-' && c2 == '-')) {

							}
							*/

							reader.mark(1);
						}
						inputStream.getChannel().position(0); // reposition to line 1, column 1.
						String codeLine = "";
						String prevCodeLine;
						Scanner sc = new Scanner(inputStream);
 						while(sc.hasNext()){
							prevCodeLine = codeLine; // get previous code line
							codeLine = sc.nextLine(); // gets current code line
							for(int i = 0; i < cvars.size(); i++)
							{
								if(!codeLine.startsWith("#define") && !codeLine.startsWith("#include")) // preprocess directives are out of the question.
								{
									Pattern constVarReplacer = Pattern.compile("\\b" + cvars.get(i) + "\\b");
									Matcher m = constVarReplacer.matcher(codeLine);
									if (m.find())
									{
										codeLine = m.replaceAll(cvals.get(i)); // replace constant variables with the actual constant values
									}
								}
								/* Old Solution. Commented out, deprecated.
								if(!codeLine.startsWith("#define"))
									codeLine = codeLine.replace(cvars.get(i), cvals.get(i)); // replace constant variables with the actual constant values (Find and Replace a la mode)
								*/
							}
							if(codeLine.startsWith("#define"))
							{
								writer.print("");
							}
							else writer.println(codeLine);
						}
						sc.close();
						this.setSource(processed);
					}
					finally 
					{
						reader.close();
					}
				}
				finally 
				{
					dataInStream.close();
				}
			}
			finally 
			{
				writer.close();
			}
		}
		finally 
		{
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
