package com.cbrc.ast.preprocessor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CASTPreprocessor {

	private File source;

	public CASTPreprocessor(File source) {
		this.source = source;
	}

	public void preprocess() throws IOException, PostDeclarationException {
		String tempFileName = this.source.getPath().substring(0, this.source.getPath().length() - 2) + "-temp.c";
		// TODO: change ++ and -- to non shorthand form, change +=, -=, /= and
		// *= to non shorthand form
		// TODO: change constants to actual values

		File processed = new File(tempFileName);
		FileInputStream inputStream = new FileInputStream(this.source);

		try {
			ArrayList<String> cvars = new ArrayList<String>(); // constant variables
			ArrayList<String> cvals = new ArrayList<String>(); // constant values
			ArrayList<String> types = new ArrayList<String>(); // datatype
			ArrayList<String> vars = new ArrayList<String>(); // variables
			ArrayList<String> vals = new ArrayList<String>(); // values
			ArrayList<String> sourceCode = new ArrayList<String>(); // source code
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
						int lineCount = 1;
						int decLineStart = 0, decLineEnd = 0;
						boolean preprocess = true;
						boolean variableImplStart = false;
						boolean variableImplFinish = false;
						String methodName = "";
 						while(preprocess)
 						{
 							// get #define
 							prevCodeLine = codeLine;
 							codeLine = reader.readLine();
 							if (codeLine == null) preprocess = !preprocess;
 							else 
 							{

 	 							if(codeLine.contains("//") || 
 	 								codeLine.contains("/*") || 
 	 								codeLine.contains("*/") || 
 	 								codeLine.startsWith("#include"))
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
 	 							if(methodName.isEmpty())
 	 							{
 	 								if(isAMethodDeclaration(prevCodeLine + codeLine) && !prevCodeLine.trim().isEmpty())
 									{
 										methodName = getMethodName(prevCodeLine);
 									}
 									// no datatype + datatype method( ... ) {  
 									else if(isAMethodDeclaration(codeLine))
 									{
 										methodName = getMethodName(codeLine);
 									}
 	 							}
 	 							else
 	 							{
 	 								if(variableImplStart == false)
 	 								{
 	 									if(typeCheck(codeLine.trim()).equals("int") ||
 										   typeCheck(codeLine.trim()).equals("long") ||
 										   typeCheck(codeLine.trim()).equals("float") ||
 										   typeCheck(codeLine.trim()).equals("double") ||
 										   typeCheck(codeLine.trim()).equals("char"))
 	 									{
 		 									decLineStart = lineCount;
 		 									// System.out.println("Variable declaration starts at : " + decLineStart);
 		 									variableImplStart = true;
 	 									}
 	 								}
 	 								else
 	 								{
 	 	 								if(!(typeCheck(codeLine.trim()).equals("int") ||
 	 									   typeCheck(codeLine.trim()).equals("long") ||
 	 									   typeCheck(codeLine.trim()).equals("float") ||
 	 									   typeCheck(codeLine.trim()).equals("double") ||
 	 									   typeCheck(codeLine.trim()).equals("char")) && variableImplFinish == false)
 	 									{
 	 										decLineEnd = lineCount - 1;
 	 	 									// System.out.println("Variable declaration ends at : " + decLineEnd);
 	 										variableImplFinish = true;
 	 									}
 	 								}
 	 							}
 	 							lineCount++;
 							}
 						}
						inputStream.getChannel().position(0); // reposition to line 1, column 1.
						// reset!
						variableImplFinish = false;
						methodName = "";
						// make persistent changes!
						decLineStart -= cvars.size();
						decLineEnd -= cvars.size();
						// System.out.println("After removing defines... ");
						// System.out.println("Variable declaration starts at : " + decLineStart);
						// System.out.println("Variable declaration ends at : " + decLineEnd);
						codeLine = "";
						prevCodeLine = "";
						boolean multiLineComment = false;
						boolean isDefine = false;
						Stack<String> codez = new Stack<String>();
 						while(sc.hasNext()){
 							// reset all flags
 							isDefine = false;
							prevCodeLine = codeLine; // get previous code line
							codeLine = sc.nextLine(); // gets current code line
							String cCodeLine = codeLine;
							// remove all comments first
							if(cCodeLine.indexOf("/*") >= 0)
							{
								// MLC = on
								multiLineComment = true;
								// Case 1: Multi Line comment ends with closing MLC
								if(cCodeLine.indexOf("*/") >= 0)
								{
									multiLineComment = !multiLineComment;
									cCodeLine = commentRemover(cCodeLine);
								}
								else
								{
									cCodeLine = commentRemover(cCodeLine);
								}
							}
							else if(multiLineComment)
							{
								// Case 2: Multi Line comment is really a multi line comment
								// Case 2.1: Multi Line comment ends with closing MLC, with or without the methods.
								if(cCodeLine.indexOf("*/") >= 0)
								{
									multiLineComment = !multiLineComment;
									// Case 2.2: Multi Line comment ends with additional single line comments
									// Case 2.3: Multi Line comment ends with additional multi line comments (after Case 2.2)
									if(cCodeLine.lastIndexOf("/*") >= 0)
				                    {
				                        if(cCodeLine.lastIndexOf("/*") > cCodeLine.lastIndexOf("*/"))
				                        {
				                            // */ ... /* ...
				                            multiLineComment = !multiLineComment;
				                        }
				                        cCodeLine = commentRemover(cCodeLine);
				                    }
				                    else if(codeLine.indexOf("//") >= 0)
										cCodeLine = commentRemover(cCodeLine);
									else
										cCodeLine = commentRemover(cCodeLine);
								}
								else cCodeLine = "";
							}
							else if(cCodeLine.startsWith("//"))
							{
								cCodeLine = commentRemover(cCodeLine);
							}
							else if(cCodeLine.indexOf("//") >= 0)
							{
								// will work with the ff. examples: 
								// (0, 0) - // example
								// (0, n - 1) - [code] // example
								cCodeLine = commentRemover(cCodeLine);
							}
							// already removed.
							codeLine = cCodeLine;
							// replace constants with the actual variables
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
							}
							// print all includes. exclude all #defines
							if(codeLine.startsWith("#define"))
							{
								codeLine = "";
								isDefine = true;
							}
							else if(codeLine.trim().startsWith("#include"))
							{
								sourceCode.add(codeLine);
							}
							else if(codeLine.isEmpty())
							{
								// whitespace only, move on
							}
							// check if a method is already declared.
							if(methodName.isEmpty())
							{
								String modifiedCodeLine = "";
								// not yet inside the method
								// check if the codeline contains the following:
								// no datatype + datatype method( ... ) \n{
								if(isAMethodDeclaration(prevCodeLine + codeLine) && !prevCodeLine.trim().isEmpty())
								{
									methodName = getMethodName(prevCodeLine);
									codez.push(methodName);
									// has remaining statements after {?
									modifiedCodeLine = prevCodeLine + codeLine;
									sourceCode.add(codeLine);	
								}
								// no datatype + datatype method( ... ) {  
								else if(isAMethodDeclaration(codeLine))
								{
									methodName = getMethodName(codeLine);
									codez.push(methodName);
									// has remaining statements after {?
									modifiedCodeLine = codeLine;
									sourceCode.add(codeLine);
								}
								String multipleStatements = getRemainingStatements(modifiedCodeLine);
								if(!multipleStatements.isEmpty())
								{
									do
							        {
							            String currentStatement = getCurrentStatement(multipleStatements);
							            // check for conditional statements first
							            if(currentStatement.isEmpty())
							            {
											// whitespace only, move on
							            }
							            else if(currentStatement.startsWith("if"))
							            {
							            	codez.push(SensitiveKeywords.IF.toString());
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
							            else if(currentStatement.startsWith("else if"))
							            {
							            	codez.push(SensitiveKeywords.ELSEIF.toString());
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
							            else if(currentStatement.startsWith("else"))
							            {
							            	codez.push(SensitiveKeywords.ELSE.toString());
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
							            // check for loops first
							            else if(currentStatement.startsWith("while"))
							            {
							            	codez.push(SensitiveKeywords.WHILE.toString());
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
							            else if(currentStatement.startsWith("do"))
							            {
							            	codez.push(SensitiveKeywords.DOWHILE.toString());
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
							            else if(currentStatement.startsWith("for"))
							            {
							            	codez.push(SensitiveKeywords.FOR.toString());
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
							            // check for printf/scanf first
							            else if(currentStatement.startsWith("printf(") || 
							            		currentStatement.startsWith("scanf("))
							            {
							            	if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
										// check for assignments
							            if(currentStatement.contains("+=")||
							               currentStatement.contains("-=")||
							               currentStatement.contains("*=")||
							               currentStatement.contains("-=")||
							               currentStatement.contains("++")||
							               currentStatement.contains("--"))
							            {
							                String converted = "";
							                converted = shorthandChanger(currentStatement);
							                if(currentStatement.length() == multipleStatements.length())
							                    sourceCode.add(converted);
							                else sourceCode.add(converted + " ");
							            }
							            else
							            {
							                if(currentStatement.length() == multipleStatements.length())
							                	sourceCode.add(currentStatement);
							                else sourceCode.add(currentStatement + " ");
							            }
						                multipleStatements = nextStatement(multipleStatements);
							        }
							        while(!multipleStatements.isEmpty());
								}
							}
							else
							{
								// already inside the method
								// initialise all variables first, if there are.
								if(typeCheck(codeLine.trim()).equals("int") ||
								   typeCheck(codeLine.trim()).equals("long") ||
								   typeCheck(codeLine.trim()).equals("float") ||
								   typeCheck(codeLine.trim()).equals("double") ||
								   typeCheck(codeLine.trim()).equals("char"))
								{
									codeLine = convertVarDecs(codeLine.trim());
								}
								// finish implementing variables but datatype happens
								if(!methodName.isEmpty())
								{
									if((typeCheck(codeLine.trim()).equals("int") ||
									   typeCheck(codeLine.trim()).equals("long") ||
									   typeCheck(codeLine.trim()).equals("float") ||
									   typeCheck(codeLine.trim()).equals("double") ||
									   typeCheck(codeLine.trim()).equals("char")) && variableImplFinish == true)
									{
										throw new PostDeclarationException("Post Variable Declaration Done, CBR-C does not support parsing post variable declarations");
									}
									else if(!(typeCheck(codeLine.trim()).equals("int") ||
											   typeCheck(codeLine.trim()).equals("long") ||
											   typeCheck(codeLine.trim()).equals("float") ||
											   typeCheck(codeLine.trim()).equals("double") ||
											   typeCheck(codeLine.trim()).equals("char")) && variableImplFinish == false)
									{
										if(codeLine.charAt(0) != '{' && isAMethodDeclaration(prevCodeLine))
											variableImplFinish = true;
										else if(codeLine.charAt(0) != '}')
											variableImplFinish = false;
									}
								}
								if(codeLine.trim().startsWith("do"))
								{
									sourceCode.add(codeLine);
									if(codeLine.startsWith("do"))
						            {
						            	codez.push(SensitiveKeywords.DOWHILE.toString());
						            }
								}
								else if((codeLine.trim().startsWith("while") && !codez.peek().equals("do"))||
										codeLine.trim().startsWith("for"))
								{

									sourceCode.add(codeLine);
						            if(codeLine.startsWith("while"))
						            {
						            	codez.push(SensitiveKeywords.WHILE.toString());
						            }
						            else if(codeLine.startsWith("for"))
						            {
						            	codez.push(SensitiveKeywords.FOR.toString());
						            }
								}
								// check for conditional statements
								else if(codeLine.trim().startsWith("if") ||
								   codeLine.trim().startsWith("else if") ||
								   codeLine.trim().startsWith("else") ||
								   codeLine.trim().startsWith("switch"))
								{
									sourceCode.add(codeLine);
						            if(codeLine.trim().startsWith("if"))
						            {
						            	codez.push(SensitiveKeywords.IF.toString());
						            }
						            else if(codeLine.trim().startsWith("else if"))
						            {
						            	codez.push(SensitiveKeywords.ELSEIF.toString());
						            }
						            else if(codeLine.trim().startsWith("else"))
						            {
						            	codez.push(SensitiveKeywords.ELSE.toString());
						            }
						            else if(codeLine.trim().startsWith("switch"))
						            {
						            	codez.push(SensitiveKeywords.ELSE.toString());
						            }
								}
								else if(codeLine.contains("{"))
								{
									sourceCode.add(codeLine);
								}
								// prioritise every closing braces LAST
								else if(codeLine.contains("}") && codez.peek().equals(methodName))
								{
									methodName = "";
									codez.pop(); // empty stack na
									sourceCode.add(codeLine);
								}
								else if(codeLine.contains("}"))
								{
									codez.pop(); // remove a block of conditional/loop statement
									sourceCode.add(codeLine);
								}
								else
								{
									String currentStatement = codeLine;
						            // check for conditional statements first
						            if(currentStatement.startsWith("if"))
						            {
						            	codez.push(SensitiveKeywords.IF.toString());
						            }
						            else if(currentStatement.startsWith("else if"))
						            {
						            	codez.push(SensitiveKeywords.ELSEIF.toString());
						            }
						            else if(currentStatement.startsWith("else"))
						            {
						            	codez.push(SensitiveKeywords.ELSE.toString());
						            }
						            else if(currentStatement.startsWith("switch"))
						            {
						            	codez.push(SensitiveKeywords.ELSE.toString());
						            }
						            // check for loops first
						            else if(currentStatement.startsWith("do"))
						            {
						            	codez.push(SensitiveKeywords.DOWHILE.toString());
						            }
						            else if(currentStatement.startsWith("while") && !codez.peek().equals("do"))
						            {
						            	codez.push(SensitiveKeywords.WHILE.toString());
						            }
						            else if(currentStatement.startsWith("for"))
						            {
						            	codez.push(SensitiveKeywords.FOR.toString());
						            }
									// check for assignments
						            if(currentStatement.contains("+=")||
						               currentStatement.contains("-=")||
						               currentStatement.contains("*=")||
						               currentStatement.contains("-=")||
						               currentStatement.contains("++")||
						               currentStatement.contains("--"))
						            {
						                String converted = "";
						                converted = shorthandChanger(currentStatement);
					                    currentStatement = converted;
						            }
						            currentStatement = currentStatement.trim();
						            if(!currentStatement.isEmpty())
						            	for(int i = 0; i < codez.size(); i++)
						            		currentStatement = "\t".concat(currentStatement); // Proper Tabbing method
				                	sourceCode.add(currentStatement);
								}
								if(!codeLine.trim().equals("}") && !codeLine.trim().equals("{") && codeLine.trim().length() == 1)
								{
									String multipleStatements = nextStatement(codeLine); // assume there is another statement
							        do
							        {
							        	if(!multipleStatements.isEmpty())
							        	{
								            String currentStatement = getCurrentStatement(multipleStatements);
								            // check for conditional statements first
								            if(currentStatement.startsWith("if"))
								            {
								            	codez.push(SensitiveKeywords.IF.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            else if(currentStatement.startsWith("else if"))
								            {
								            	codez.push(SensitiveKeywords.ELSEIF.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            else if(currentStatement.startsWith("else"))
								            {
								            	codez.push(SensitiveKeywords.ELSE.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            else if(currentStatement.startsWith("switch"))
								            {
								            	codez.push(SensitiveKeywords.ELSE.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            // check for loops first
								            else if(currentStatement.startsWith("do"))
								            {
								            	codez.push(SensitiveKeywords.DOWHILE.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            else if(currentStatement.startsWith("while") && !codez.peek().equals("do"))
								            {
								            	codez.push(SensitiveKeywords.WHILE.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            else if(currentStatement.startsWith("for"))
								            {
								            	codez.push(SensitiveKeywords.FOR.toString());
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
								            // check for printf/scanf first
								            else if(currentStatement.startsWith("printf(") || 
								            		currentStatement.startsWith("scanf("))
								            {
								            	if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
											// check for assignments
								            if(currentStatement.contains("+=")||
								               currentStatement.contains("-=")||
								               currentStatement.contains("*=")||
								               currentStatement.contains("-=")||
								               currentStatement.contains("++")||
								               currentStatement.contains("--"))
								            {
								                String converted = "";
								                converted = shorthandChanger(currentStatement);
								                if(currentStatement.length() == multipleStatements.length())
								                    sourceCode.add(converted);
								                else sourceCode.add(converted + " ");
								            }
								            else
								            {
								                if(currentStatement.length() == multipleStatements.length())
								                	sourceCode.add(currentStatement);
								                else sourceCode.add(currentStatement + " ");
								            }
							                multipleStatements = nextStatement(multipleStatements);
							        	}
							        }
							        while(!multipleStatements.isEmpty());
								}
								
							}
 						}
						sc.close();
						Iterator<String> iterator = sourceCode.iterator();
						while(iterator.hasNext())
						{
							writer.println(iterator.next());
						}
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
	
	/**
	 * Checks the <pre> trimmedCodeLine </pre> for variables. Cases considered are the following:
	 * 
	 * 1. A statement that contains a single variable.
	 * 2. A statement that contains a single variable with corresponding value.
	 * 3. A statement that contains multiple variables.
	 * 4. A statement that contains multiple variables with or without corresponding value.
	 * 5. A statement that contains multiple statements that may and will have the cases above.
	 *
	 * @param  trimmedCodeLine	the line to check
	 * @param  type	the type being used
	 * @param  types	the data types being stored for the whole C source code.
	 * @param  vars	the variables being stored for the whole C source code.
	 * @param  vals	the values being stored for the whole C source code.
	 */
	
	public void getAllVariables(String trimmedCodeLine, String type, ArrayList<String> types, ArrayList<String> vars, ArrayList<String> vals) {
		// single declarations only
		int indexStart = 0;
		// multiple cases
		if(type.equals("int"))
			indexStart = "int ".length();
		else if (type.equals("long"))
			indexStart = "long ".length();
		else if (type.equals("float"))
			indexStart = "float ".length();
		else if (type.equals("double"))
			indexStart = "double ".length();
		else if (type.equals("char"))
			indexStart = "char ".length();

  		// beforehand, capture all character ';' and ',' to prevent anomalies.
		trimmedCodeLine.replaceAll("\',\'", "\'comma\'");
		trimmedCodeLine.replaceAll("\';\'", "\'sc\'");
	 	String currentDeclaration = trimmedCodeLine.substring(indexStart, trimmedCodeLine.indexOf(";"));
      	do
      	{
      	    if(currentDeclaration.indexOf(",") > 0)
      	    {
		      	/*
				*do multiple cases*
				Case 3.	get multiple variable declarations
						capture every after comma, follow single case methods.
				*/
      	    	types.add(type);
      	        if(currentDeclaration.indexOf("=") > 0)
      	        {
		      		// beforehand, restore the first occurrence of 'sc' and 'comma' to prevent anomalies, whenever applicable
      	        	if (type.equals("char"))
					{
			      		currentDeclaration.replaceFirst("\'comma\'", "\',\'");
			      		currentDeclaration.replaceFirst("\'sc\'", "\';\'");
					}
	      	        vars.add(currentDeclaration.substring(0, currentDeclaration.indexOf("=")).trim());
      	        	vals.add(currentDeclaration.substring(currentDeclaration.indexOf("=") + 1, currentDeclaration.indexOf(",")).trim()); // consider value declarations too
      	        }
      	        else
      	        {
	      	        vars.add(currentDeclaration.substring(0, currentDeclaration.indexOf(",")).trim());
      	        	vals.add("NOTINIT");
      	        }
      	        currentDeclaration = currentDeclaration.substring(currentDeclaration.indexOf(",") + 1).trim();
      	    }
      	    else
      	    {
				/*
				*do single case*
				Case 1.	get undeclared variables
						capture after whitespace until indexOf(";") - 1. Trim the variable name if necessary.
				Case 2. get declared variables
						Follow case 1 method.
						if the datatype is char, capture the open and closing single quotes through startsWith("\'") and endsWith("\'"). Trim unnecessary whitespaces.
				*/
      	    	types.add(type);
      	        if(currentDeclaration.indexOf("=") > 0)
      	        {
		      		// beforehand, restore all character 'sc' and 'comma' to prevent anomalies, whenever applicable, if the type is char ONLY!
      	        	if (type.equals("char"))
					{
			      		currentDeclaration.replaceAll("\'comma\'", "\',\'");
			      		currentDeclaration.replaceAll("\'sc\'", "\';\'");
					}
	      	        vars.add(currentDeclaration.substring(0, currentDeclaration.indexOf("=")).trim());
      	        	vals.add(currentDeclaration.substring(currentDeclaration.indexOf("=") + 1).trim()); // consider value declarations too
      	        }
      	        else
      	        {
      	        	vals.add(currentDeclaration.trim());
      	        	vals.add("NOTINIT");
        		}
      	        currentDeclaration = "";
      	    }
      	}
      	while (!currentDeclaration.isEmpty());
	}
	
	/**
	 * Checks the datatype of the first statement.
	 *
	 * @param  dec	the declaration for the said statement
	 */
	
	public String typeCheck(String dec)
	{
		String res = "";
		if(dec.startsWith("int "))
			res = "int";
		else if (dec.startsWith("long "))
			res = "long";
		else if (dec.startsWith("float "))
			res = "float";
		else if (dec.startsWith("double "))
			res = "double";
		else if (dec.startsWith("char "))
			res = "char";
		return res;
	}
	
	/**
	 * Gets the statement that will be examined for the current line.
	 *
	 * @param  l	the current line
	 * @return the first statement of the line
	 */
	
	public String getCurrentStatement(String l)
    {
        return l.substring(0, l.indexOf(";") + 1).trim();
    }
	
	/**
	 * Removes the current statement in the line and proceeds with the next.
	 * If there are no statements afterwards, then it is considered empty statement.
	 *
	 * @param  l	the current line 
	 * @return the next statements of the line
	 */
    
    public String nextStatement(String l)
    {
        return l.substring(l.indexOf(";") + 1).trim();
    }
    
    /**
	 *
	 * <ul>
	 * 	<li> Addition - <pre>+=</pre> </li>
	 * 	<li> Subtraction - <pre>-=</pre> </li>
	 * 	<li> Multiplication - <pre>*=</pre> </li>
	 * 	<li> Division - <pre>/=</pre> </li>
	 * 	<li> Modulo - <pre>%=</pre> </li>
	 * </ul>
	 *
	 * @param  l	the shorthand statement, where applicable 
	 * @return the longhand form.
	 */
    public String shorthandChanger(String l)
    {
    	String res = "";
        Pattern operationConversion = Pattern.compile("(\\+=|-=|\\*=|\\/=|%=|\\+\\+|--)");
        Matcher matchaC = operationConversion.matcher(l);
        while(matchaC.find())
        {
        	String var = "";
            if(l.contains("+="))
            {
                var = l.trim().substring(0, l.indexOf("+=") - 1).trim();
                l = matchaC.replaceFirst("= " + var + " +");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("-="))
            {
                var = l.trim().substring(0, l.indexOf("-=") - 1).trim();
                l = matchaC.replaceFirst("= " + var + " -");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("*="))
            {
                var = l.trim().substring(0, l.indexOf("*=") - 1).trim();
                l = matchaC.replaceFirst("= " + var + " *");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("/="))
            {
                var = l.trim().substring(0, l.indexOf("/=") - 1).trim();
                l = matchaC.replaceFirst("= " + var + " /");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("%="))
            {
                var = l.trim().substring(0, l.indexOf("%=") - 1).trim();
                l = matchaC.replaceFirst("= " + var + " %");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("++"))
            {
                Pattern plus2VarCap = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?=(\\+\\+))");
                Matcher matchaV = plus2VarCap.matcher(l);
                matchaV.find();
                if(l.indexOf("=") >= 0)
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaC.replaceFirst(" + 1");
                }
                else if(l.contains("printf") || l.contains(","))
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaC.replaceFirst(" + 1");
                }
                else
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaC.replaceFirst(" = " + var + " + 1");
                }
                res = l;
            }
            else if(l.contains("--"))
            {
                Pattern minus2VarCap = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?=(--))");
                Matcher matchaV = minus2VarCap.matcher(l);
                matchaV.find();
                if(l.indexOf("=") >= 0)
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaC.replaceFirst(" - 1");
                }
                else if(l.contains("printf") || l.contains(","))
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaC.replaceFirst(" - 1");
                }
                else
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaC.replaceFirst(" = " + var + " - 1");
                }
                res = l;
            }
            matchaC = operationConversion.matcher(l);
        }
        return res;
    }
    
    /**
	 * Changes the assignment given the current line, if it's in shorthand form, it will be converted to longhand form.
	 * Example: <pre>i += 5;</pre> will become <pre>i = i + 5;</pre>
	 * These covers the following shorthand forms:
	 * 
	 * <ul>
	 * 	<li> Addition - <pre>+=</pre> </li>
	 * 	<li> Subtraction - <pre>-=</pre> </li>
	 * 	<li> Multiplication - <pre>*=</pre> </li>
	 * 	<li> Division - <pre>/=</pre> </li>
	 * 	<li> Modulo - <pre>%=</pre> </li>
	 * </ul>
	 *
	 * @param  l	the shorthand statement, where applicable 
	 * @return the longhand form.
	 */
    
    public String assignmentChanger(String l)
    {
        String res = "";
        Pattern longhandConversion = Pattern.compile("(\\+=|-=|\\*=|\\/=|%=)");
        Matcher matchaLHC = longhandConversion.matcher(l);
        while(matchaLHC.find())
        {
        	String var = "";
            if(l.contains("+="))
            {
                var = l.trim().substring(0, l.indexOf("+=") - 1).trim();
                l = matchaLHC.replaceFirst("= " + var + " +");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("-="))
            {
                var = l.trim().substring(0, l.indexOf("-=") - 1).trim();
                l = matchaLHC.replaceFirst("= " + var + " -");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("*="))
            {
                var = l.trim().substring(0, l.indexOf("*=") - 1).trim();
                l = matchaLHC.replaceFirst("= " + var + " *");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("/="))
            {
                var = l.trim().substring(0, l.indexOf("/=") - 1).trim();
                l = matchaLHC.replaceFirst("= " + var + " /");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("%="))
            {
                var = l.trim().substring(0, l.indexOf("%=") - 1).trim();
                l = matchaLHC.replaceFirst("= " + var + " %");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
        }
        return res;
    }
    
    /**
	 * Changes the unary statement given the current line, if it's in shorthand form, it will be converted to longhand form.
	 * Example: <pre>i++;</pre> will become <pre>i = i + 1;</pre>
	 * These covers the following shorthand forms:
	 * 
	 * <ul>
	 * 	<li> Positive Increment - <pre>++</pre> </li>
	 * 	<li> Negative Increment- <pre>--</pre> </li>
	 * </ul>
	 *
	 * @param  l	the unary statement, where applicable 
	 * @return the longhand form.
	 */
    
    public String unaryChanger(String l)
    {
        String res = "";
        Pattern unaryConversion = Pattern.compile("(\\+\\+|--)");
        Matcher matchaU = unaryConversion.matcher(l);
        while(matchaU.find())
        {
            String var = "";
            Pattern unaryVarCap = Pattern.compile("\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?=(\\+\\+)|(--))");
            Matcher matchaV = unaryVarCap.matcher(l);
            matchaV.find();
            if(l.contains("++"))
            {
                if(l.indexOf("=") < 0)
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaU.replaceFirst(" = " + var + " + 1");
                }
                else
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaU.replaceFirst(" + 1");
                }
                matchaU = unaryConversion.matcher(l);
                res = l;
            }
            else if(l.contains("--"))
            {
                if(l.indexOf("=") < 0)
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaU.replaceFirst(" = " + var + " - 1");
                }
                else
                {
                    var = l.trim().substring(matchaV.start(), matchaV.end()).trim();
                    l = matchaU.replaceFirst(" - 1");
                }
                matchaU = unaryConversion.matcher(l);
                res = l;
            }
        }
        return res;
    }
	
    /**
	 * Changes the unary statement given the current line, if it's in shorthand form, it will be converted to longhand form.
	 * Example: <pre>i++;</pre> will become <pre>i = i + 1;</pre>
	 * These covers the following shorthand forms:
	 * 
	 * <ul>
	 * 	<li> Positive Increment - <pre>++</pre> </li>
	 * 	<li> Negative Increment- <pre>--</pre> </li>
	 * </ul>
	 *
	 * @param  l	the unary statement, where applicable 
	 * @return the longhand form.
	 */
    
    public String commentRemover(String lineContainingComment) {
        String res = lineContainingComment;
        if (lineContainingComment.trim().startsWith("/*")) {
            // MLC = on
            // Case 1: Multi Line comment ends with closing MLC
            // Case 1.1: Multiple code statements with multi line comments
            // Examples:
            // <code>; /* MLC */ <code>; /* MLC */ ... /* MLC */ <code>;
            if (lineContainingComment.indexOf("*/") >= 0) {
                if (lineContainingComment.indexOf("/*", lineContainingComment.indexOf("/*")) >= 0)
                {
                    res = multiCommentRemover(lineContainingComment);
                }
                else
                    res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*")) + lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            } else {
                res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*"));
            }
        } else if (lineContainingComment.trim().startsWith("*/")) {
            String another = lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            // Case 2.2: Multi Line comment ends with additional single line comments
            // Case 2.3: Multi Line comment ends with additional multi line comments (after Case 2.2)
            if (another.indexOf("/*") >= 0) {
                res = multiCommentRemover(another);
            } else if (another.indexOf("//") >= 0) {
                res = another.substring(0, another.indexOf("//") - 1);
            } else {
                res = another;
            }
        } else if (lineContainingComment.indexOf("/*") >= 0) {
            // MLC = on
            // Case 1: Multi Line comment ends with closing MLC
            // Case 1.1: Multiple code statements with multi line comments
            // Examples:
            // <code>; /* MLC */ <code>; /* MLC */ ... /* MLC */ <code>;
            if (lineContainingComment.indexOf("*/") < lineContainingComment.indexOf("/*"))
            {
                // example: */ code /* ...
                lineContainingComment = lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
                res = multiCommentRemover(lineContainingComment);
            }
            else if (lineContainingComment.indexOf("*/") >= 0) {
                if (lineContainingComment.indexOf("/*", lineContainingComment.indexOf("/*")) >= 0)
                {
                    res = multiCommentRemover(lineContainingComment);
                }
                else
                    res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*")) + lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            } else {
                res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*"));
            }
        } else if (lineContainingComment.indexOf("*/") >= 0) {
            String another = lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            // Case 2.2: Multi Line comment ends with additional single line comments
            // Case 2.3: Multi Line comment ends with additional multi line comments (after Case 2.2)
            if (another.indexOf("/*") >= 0) {
                res = multiCommentRemover(another);
            } else if (another.indexOf("//") >= 0) {
                res = another.substring(0, another.indexOf("//") - 1);
            } else {
                res = another;
            }
        } else if (lineContainingComment.startsWith("//")) {
            res = "";
        } else if (lineContainingComment.indexOf("//") >= 0) {
            // will work with the ff. examples: 
            // (0, 0) - // example
            // (0, n - 1) - [code] // example
            res = lineContainingComment.substring(0, lineContainingComment.indexOf("//") - 1);
        }
        return res;
    }
    
    public String multiCommentRemover(String lineContainingComment)
    {
        String another = lineContainingComment;
        do
        {
            if(another.contains("/*") && another.contains("*/"))
                another = another.substring(0, another.indexOf("/*")) + 
                        another.substring(another.indexOf("*/") + 2);
            else if(another.lastIndexOf("//") >= 0)
                another = another.substring(0, another.lastIndexOf("//"));
            else if(another.lastIndexOf("/*") >= 0)
                another = another.substring(0, another.lastIndexOf("/*"));
        } while (another.contains("/*") || another.contains("*/") || another.contains("//"));
        return another;
    }
    
    public String capturePrintFPrefix(String printfs)
    {
        return printfs.substring(0, printfs.indexOf("\""));
    }
    
    public String capturePrintFStrings(String printfs)
    {
        String modified = printfs;
        // replace just in case anomalies
        modified = modified.replaceAll("\'\"\'", "\'DQ\'");
        return modified.substring(modified.indexOf("\""), modified.lastIndexOf("\"") + 1).replaceAll("\'DQ\'", "\'\"\'");
    }
    
    public String capturePrintFSuffix(String printfs)
    {
        String modified = printfs;
        // replace just in case anomalies
        modified = modified.replaceAll("\'\"\'", "\'DQ\'");
        return modified.substring(modified.lastIndexOf("\"") + 1).replaceAll("\'DQ\'", "\'\"\'");
    }
    
    public String captureScanFPrefix(String scanfs)
    {
        return scanfs.substring(0, scanfs.indexOf("\""));
    }
    
    public String captureScanFParams(String scanfs)
    {
        return scanfs.substring(scanfs.indexOf("\""), scanfs.lastIndexOf(")"));
    }
    
    public String captureScanFSuffix(String scanfs)
    {
        return scanfs.substring(scanfs.lastIndexOf(")"));
    }
    
    
    public String getMethodName(String l)
    {
        String trimmed = l.trim();
        String res = "";
        int openParen = trimmed.indexOf("(");
        if((trimmed.startsWith("int ") || trimmed.startsWith("long ") || 
            trimmed.startsWith("float ") || trimmed.startsWith("double ") || 
            trimmed.startsWith("char ") || trimmed.startsWith("void ")))
        { 
            res = trimmed.substring(trimmed.indexOf(" "), openParen).trim();
        }
        else if(l.indexOf("(") >= 0 && l.indexOf(")") >= 0)
        {
            res = trimmed.substring(0, openParen).trim();
        }
        return res;
    }

    public String getRemainingStatements(String l)
    {
        String res = "";
        if((l.startsWith("int ") || l.startsWith("long ") || 
            l.startsWith("float ") || l.startsWith("double ") || 
            l.startsWith("char ") || l.startsWith("void ")))
        {
            int openParen = l.indexOf("(");
            int closeParen = l.indexOf(")");
            int whitespace = l.indexOf(" "); // length of the return datatype
            String methodName = l.substring(l.indexOf(" ") + 1, openParen);
            String parameters = l.substring(openParen, closeParen + 1);
            String remains = l.substring(whitespace + 1 + methodName.length() + parameters.length()).trim();
            if(remains.startsWith("{"))
            {
                res = remains.substring(remains.indexOf("{") + 1).trim();
            }
            else if (remains.startsWith("}"))
            {
            	res = remains.substring(remains.indexOf("}") + 1).trim();
            }
        }
        else if(l.indexOf("(") >= 0 && l.indexOf(")") >= 0)
        {
            int openParen = l.indexOf("(");
            int closeParen = l.indexOf(")");
            String methodName = l.substring(0, openParen);
            String parameters = l.substring(openParen, closeParen + 1);
            String remains = l.substring(methodName.length() + parameters.length());
            if(remains.startsWith("{") )
            {
                res = remains.substring(remains.indexOf("{") + 1).trim();
            }
            else if (remains.startsWith("}"))
            {
            	res = remains.substring(remains.indexOf("}") + 1).trim();
            }
        }
        return res;
    }
    
    public boolean hasRemainingStatements(String l)
    {
    	boolean res = false;
        if((l.startsWith("int ") || l.startsWith("long ") || 
            l.startsWith("float ") || l.startsWith("double ") || 
            l.startsWith("char ") || l.startsWith("void ")))
        {
            int openParen = l.indexOf("(");
            int closeParen = l.indexOf(")");
            int whitespace = l.indexOf(" "); // length of the return datatype
            String methodName = l.substring(l.indexOf(" ") + 1, openParen);
            String parameters = l.substring(openParen, closeParen + 1);
            String remains = l.substring(whitespace + 1 + methodName.length() + parameters.length()).trim();
            if(remains.startsWith("{"))
            {
                remains = remains.substring(remains.indexOf("{") + 1).trim();
                if(remains.isEmpty())
                    res = false; // datatype methodName(){
                else
                    res = true; // datatype methodName(){ statement();
            }
            else
            {
                res = false; // datatype methodName()
            }
        }
        else if(l.indexOf("(") >= 0 && l.indexOf(")") >= 0)
        {
            int openParen = l.indexOf("(");
            int closeParen = l.indexOf(")");
            String methodName = l.substring(0, openParen);
            String parameters = l.substring(openParen, closeParen + 1);
            String remains = l.substring(methodName.length() + parameters.length());
            if(remains.startsWith("{"))
            {
                remains = remains.substring(remains.indexOf("{") + 1).trim();
                if(remains.isEmpty())
                    res = false; // methodName(){
                else
                    res = true; // methodName(){ statement();
            }
            else
            {
                res = false; // methodName()
            }
        }
        return res;
    }
    
    public boolean isAMethodDeclaration(String l)
    {
        boolean res = false;
        if(l.isEmpty())
        {
        	
        }
        else if((l.startsWith("int ") || l.startsWith("long ") || 
            l.startsWith("float ") || l.startsWith("double ") || 
            l.startsWith("char ") || l.startsWith("void ")))
        {
            String methodName = l.substring(l.indexOf(" ") + 1);
            int openParen = methodName.indexOf("(");
            int closeParen = methodName.indexOf(")");
            if(openParen == -1 || closeParen == -1)
            {
            	res = false;
            }
            else
            {
                String parameters = methodName.substring(openParen, closeParen + 1);
                String remains = methodName.substring(parameters.length());
                res = true; // datatype methodName() and { ...
            }
        }
        else if(l.indexOf("(") >= 0 && l.indexOf(")") >= 0)
        {
            int openParen = l.indexOf("(");
            int closeParen = l.indexOf(")");
            if(openParen == -1 || closeParen == -1)
            {
            	res = false;
            }
            else
            {
                String methodName = l.substring(0, openParen);
                String parameters = l.substring(openParen, closeParen + 1);
                String remains = methodName.substring(parameters.length());
                if(remains.startsWith("{") )
                {
                    remains = remains.trim().substring(remains.indexOf("{"));
                    res = true; // methodName(){ ...
                }
                else
                {
                    res = true; // methodName() (provided method name is already set)
                }
            }
        }
        return res;
    }
    
    public String convertVarDecs(String l)
    {
        String res = "";
        String type = typeCheck(l);
        String variables = l.substring(type.length(), l.lastIndexOf(";")).trim();
        if(type.equals("char"))
        {
            l.replaceAll("\',\'", "\'comma\'");
            l.replaceAll("\';\'", "\'sc\'");
        }
        ArrayList<String> vars = new ArrayList<String>();
        ArrayList<String> vals = new ArrayList<String>();
        if(variables.indexOf(",") >= 0)
        {
            do
            {
                String declaration = "";
                if(variables.indexOf(",") >= 0)
                    declaration = variables.substring(0, variables.indexOf(","));
                else
                    declaration = variables;
                String variable = "";
                String value = "Not Initialized";
                if(declaration.contains("="))
                {
                    variable = declaration.substring(0, declaration.indexOf("=")).trim();
                    if(type.equals("char"))
                    {
                        declaration = declaration.replaceAll("\'comma\'", "\',\'");
                        declaration = declaration.replaceAll("\'sc\'", "\';\'");
                    }
                    value = declaration.substring(declaration.indexOf("=") + 1).trim();
                }
                else
                {
                    variable = declaration.substring(0).trim();
                }
                vars.add(variable);
                vals.add(value);
                if(variables.indexOf(",") >= 0)
                    variables = variables.substring(variables.indexOf(",") + 1).trim();
                else
                    variables = "";
            }
            while(!variables.isEmpty());
            int decSize = vars.size();
            // start with variables declaration first
            for(int i = 0; i < decSize; i++)
            {
                String declaration = type.concat(" ").concat(vars.get(i)).concat("; ");
                res = res.concat(declaration);
            }
            // then next is yung value declaration
            for(int i = 0; i < decSize; i++)
            {
                if(!vals.get(i).equals("Not Initialized"))
                {
                    String declaration = vars.get(i).concat(" = ").concat(vals.get(i)).concat("; ");
                    res = res.concat(declaration);
                }
            }
        }
        else
        {
            String declaration = variables;
            String variable = "";
            String value = "Not Initialized";
            if(declaration.contains("="))
            {
                variable = declaration.substring(0, declaration.indexOf("=")).trim();
                if(type.equals("char"))
                {
                    declaration = declaration.replaceAll("\'comma\'", "\',\'");
                    declaration = declaration.replaceAll("\'sc\'", "\';\'");
                }
                value = declaration.substring(declaration.indexOf("=") + 1, declaration.length()).trim();
            }
            else
            {
                variable = declaration.substring(0).trim();
            }
            if(value.equals("Not Initialized"))
            {
                res = type.concat(" ").concat(variable).concat("; ");
            }
            else
            {
                res = type.concat(" ").concat(variable).concat("; ").concat(variable).concat(" = ").concat(value).concat(";");
            }
        }
        return res;
    }
    
}
