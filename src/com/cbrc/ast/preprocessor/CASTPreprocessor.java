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
						Scanner sc = new Scanner(inputStream);
						// NEW
						String codeLine = "";
						String prevCodeLine;
						boolean preprocess = true;
 						while(preprocess)
 						{
 							// get #define
 							prevCodeLine = codeLine;
 							codeLine = reader.readLine();
 							if(codeLine.startsWith("//") || codeLine.startsWith("/*") || codeLine.startsWith("*/") || codeLine.startsWith("#include"))
 							{
 								// DO NOTHING
 							}
 							else if(codeLine.startsWith("#define"))
 							{
 								String variable = codeLine.substring("#define".length() + 1, codeLine.lastIndexOf(" "));
 								String value = codeLine.substring(codeLine.lastIndexOf(" ") + 1);
 								cvars.add(variable);
 								cvals.add(value);
 							}
 							else
 							{
 								preprocess = !preprocess;
 							}
 						}
						// inputStream.getChannel().position(0); // reposition to line 1, column 1.
						// Preprocessor Directives
						/* OLD
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

							// future implementation.
							// reader.mark(3);
							//char c1 = (char) reader.read();
							//char c2 = (char) reader.read();
							
							//if ((c1 == '+' && c2 == '+') || (c1 == '-' && c2 == '-')) {

							}
							

							reader.mark(1);
						}
						*/
						inputStream.getChannel().position(0); // reposition to line 1, column 1.
						codeLine = "";
						prevCodeLine = "";
 						while(sc.hasNext()){
							prevCodeLine = codeLine; // get previous code line
							codeLine = sc.nextLine(); // gets current code line
							for(int i = 0; i < cvars.size(); i++)
							{
								if(!codeLine.startsWith("#define") && !codeLine.startsWith("#include")) // preprocess directives are out of the question.
								{
									Pattern constVarReplacer = Pattern.compile("(?!\')(?!\")\\b" + cvars.get(i) + "\\b(?!\")(?!\')(?!>)");
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
							else
							{
								if(sc.hasNext())
								{
									String datatype = "";
									//single variable declaration for multiple lines (Capture datatypes first and maybe try to capture the FIRST variable as well)
									Pattern declarationConversion = Pattern.compile("\\b(?:(?:long\\s*|char\\s*|int\\s*|float\\s*|double\\s*)+)" // get data type
											+ "(?:\\s+\\s*)"
											+ "(([a-zA-Z_][a-zA-Z0-9_]*)" // get the variables
											+ "\\s*[;,=]\\s*"
											+ "(([a-zA-Z_][a-zA-Z0-9_]*)|" // get variable as value
											+ "'(.|\\\\n|\\\\t|\\\\r)'|" // get char
											+ "\\d*l|" // get long value
											+ "\\d*|" // get integer value
											+ "\\d*\\.\\d*f|" // get float value
											+ "\\d*\\.\\d*d|" // get double value
											+ "\\d*\\.\\d)" // get decimal value 
											+ "(;|,)" // either comma or separator
											+ "\\s*)");
									Matcher matcha = declarationConversion.matcher(codeLine);
									ArrayList<Integer> starts = new ArrayList<Integer>();
									ArrayList<Integer> ends = new ArrayList<Integer>();
									// populate all the starts and ends
									while (matcha.find())
									{
										starts.add(matcha.start());
										ends.add(matcha.end());
									}
									matcha.reset(); 
									if (matcha.find())
									{
										String found = matcha.group();
										// DataTypeChecker
										if(found.startsWith("int"))
										{
											datatype = "int";
										}
										else if(found.startsWith("long"))
										{
											datatype = "long";
										}
										else if(found.startsWith("float"))
										{
											datatype = "float";
										}
										else if(found.startsWith("double"))
										{
											datatype = "double";
										}
										else if(found.startsWith("char"))
										{
											datatype = "char";
										}
										//multiple variable declaration for multiple lines (Capture datatypes first and maybe try to capture the FIRST variable as well)
										Pattern declarationConversion2 = Pattern.compile("\\b(?!for\\s*\\(|if\\s*\\(|else\\s*\\{|do\\s*\\{|while\\s*\\(|" // loops and conditional variables prefixes
												+ "switch\\s*\\(|default\\s*:|case\\s+(([a-zA-Z_][a-zA-Z0-9_]*)|'(.|\\\\n|\\\\t|\\\\r)')|" // switch cases prefixes
												+ "int\\s+|long\\s+|double\\s+|float\\s+|char\\s+|printf\\s*\\(|scanf\\s*\\(|getch\\s*\\(|getchar\\s*\\()" // datatype prefixes and printf/scanf/getch/getchar prefixes
												+ "(([a-zA-Z_][a-zA-Z0-9_]*)[,;]|" // get the uninitialised variables;
												+ "([a-zA-Z_][a-zA-Z0-9_]*)" // get the variables with initialised variables
												+ "(\\s*=\\s*" // equals
												+ "(([a-zA-Z_][a-zA-Z0-9_]*)|" // get variable as value
												+ "'(.|\\\\n|\\\\t|\\\\r)'|" // get char
												+ "\\d*\\.\\d*f|" // get float value
												+ "\\d*\\.\\d*d|" // get double value
												+ "\\d*l|" // get long value
												+ "\\d*\\.\\d*|" // get decimal value
												+ "\\d*)))");  // get integer value
										Matcher matcha2 = declarationConversion2.matcher(codeLine);
										// populate from the first start to the second start
										for(int i = 0; i < starts.size() - 1; i++)
										{
											matcha2.region(starts.get(i), starts.get(i+1));
										}
											
										while(matcha2.find())
										{
											String declaration = matcha2.group();
											// get the vars
											Pattern equalChecker = Pattern.compile("\\b\\s*=\\s*");
											Matcher matcha3 = equalChecker.matcher(declaration);
											if(matcha3.find())
											{
												String variable = declaration.substring(0, declaration.indexOf("=")).trim();
												String value = declaration.substring(declaration.indexOf("=") + 1, declaration.length()).trim();
												if(value.startsWith("\'"))
												{
													datatype = "char";
													if(value.equals("\'\'"))
													{
														value = "\' \'"; // whitespace issue with \u0020
													}
												}
												else if(value.indexOf(".") >= 0)
												{
													int entry = 0;
													if(codeLine.indexOf("float") >= entry)
													{
														datatype = "float";
													}
													else
													{
														datatype = "double";
													}
												}
												System.out.println(datatype + " " + variable + ";");
												System.out.println(variable + " = " + value + ";");
											}
											else
											{
												// not found, variable declaration only
												System.out.println(datatype + " " + declaration);
											}
										}
									}
									writer.println(codeLine);
								}
								else
								{
									writer.print(codeLine); // EOF!
								}
							}
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
