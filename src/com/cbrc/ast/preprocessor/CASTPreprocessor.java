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
			ArrayList<String> types = new ArrayList<String>(); // datatype
			ArrayList<String> vars = new ArrayList<String>(); // variables
			ArrayList<String> vals = new ArrayList<String>(); // values
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
 							else if(codeLine.trim().startsWith("int ") || 
 									codeLine.trim().startsWith("long ") || 
 									codeLine.trim().startsWith("float ") ||
 									codeLine.trim().startsWith("double ") ||
 									codeLine.trim().startsWith("char "))
 							{
 								String trimmedCodeLine = codeLine.trim();
 								String type = typeCheck(trimmedCodeLine);
 								// multiple declarations in one line check
 								if(trimmedCodeLine.indexOf(";") == trimmedCodeLine.length() - 1)
 								{
 									getAllVariables(trimmedCodeLine, type, types, vars, vals);
 								}
 								else
 								{
 									do
 									{
 										if(trimmedCodeLine.indexOf(";") == trimmedCodeLine.length() - 1)
 		 								{
 											// if type declaration is done, else, make it empty, since it's the last declaration
 											if(!type.isEmpty())
 	 										{
	 											// do the single
	 		 									getAllVariables(trimmedCodeLine, type, types, vars, vals);
 	 										}
 		 									trimmedCodeLine = "";
 		 								}
 										else
 										{
 											// if type declaration is done, else, proceed to the next statement, since there's a next declaration
 											if(!type.isEmpty())
 	 										{
	 											// do the multiple
	 		 									getAllVariables(trimmedCodeLine, type, types, vars, vals);
 	 										}
 		 							        trimmedCodeLine = trimmedCodeLine.substring(trimmedCodeLine.indexOf(";") + 1).trim();
 		 							        type = typeCheck(trimmedCodeLine);
 										}
 									}
 									while(!trimmedCodeLine.isEmpty());
 								}
 							}
 							else if(sc.hasNext())
 							{
 								preprocess = !preprocess;
 							}
 						}
 						preprocess = true;
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
						boolean multiLineComment = false;
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
							else if(codeLine.startsWith("//"))
							{
								writer.print("");
							}
							else if(codeLine.indexOf("//") >= 0)
							{
								// will work with the ff. examples: 
								// (0, 0) - // example
								// (0, n - 1) - [code] // example
								if(sc.hasNext())
									writer.println(commentRemover(codeLine));
								else
									writer.print(commentRemover(codeLine));
							}
							else if(codeLine.indexOf("/*") >= 0)
							{
								// MLC = on
								multiLineComment = true;
								// Case 1: Multi Line comment ends with closing MLC
								if(codeLine.indexOf("*/") >= 0)
								{
									multiLineComment = !multiLineComment;
									if(sc.hasNext())
										writer.println(commentRemover(codeLine));
									else
										writer.print(commentRemover(codeLine));
								}
								else
								{
									if(sc.hasNext())
										writer.println(commentRemover(codeLine));
									else
										writer.print(commentRemover(codeLine));
								}
							}
							else if(multiLineComment)
							{
								// Case 2: Multi Line comment is really a multi line comment
								// Case 2.1: Multi Line comment ends with closing MLC, with or without the methods.
								if(codeLine.indexOf("*/") >= 0)
								{
									multiLineComment = !multiLineComment;
									if(sc.hasNext())
									{
										// Case 2.2: Multi Line comment ends with additional single line comments
										// Case 2.3: Multi Line comment ends with additional multi line comments (after Case 2.2)
										if(codeLine.indexOf("//") >= 0)
											writer.println(commentRemover(codeLine));
										else if(codeLine.indexOf("/*") >= 0)
										{
											writer.println(commentRemover(codeLine));
											multiLineComment = !multiLineComment;
										}
										else
											writer.println(commentRemover(codeLine));
									}
									else
									{
										if(codeLine.indexOf("//") >= 0)
											writer.print(commentRemover(codeLine.substring(0, codeLine.indexOf("//") - 1)));
										else
											writer.print(commentRemover(codeLine));
									}
								}
							}
							else
							{
								String modifiedCodeLine = codeLine;
								if(sc.hasNext())
								{
									// check for assignments
									String assignments = modifiedCodeLine;
							        do
							        {
							            String currentStatement = getCurrentStatement(assignments);
							            if(currentStatement.contains("+=")||
							               currentStatement.contains("-=")||
							               currentStatement.contains("*=")||
							               currentStatement.contains("-=")||
							               currentStatement.contains("++")||
							               currentStatement.contains("--"))
							            {
							                String converted = "";
							                converted = shorthandChanger(currentStatement);
							                if(currentStatement.length() == assignments.length())
							                    System.out.print(converted);
							                else System.out.print(converted + " ");
							                assignments = nextStatement(assignments);
							            }
							            else
							            {
							                if(currentStatement.length() == assignments.length())
							                    System.out.print(currentStatement);
							                else System.out.print(currentStatement + " ");
							                assignments = nextStatement(assignments);
							            }
							        }
							        while(!assignments.isEmpty());
									writer.println();
								}
								else
								{
									// check for assignments
									String assignments = modifiedCodeLine;
							        do
							        {
							            String currentStatement = getCurrentStatement(assignments);
							            if(currentStatement.contains("+=")||
							               currentStatement.contains("-=")||
							               currentStatement.contains("*=")||
							               currentStatement.contains("-=")||
							               currentStatement.contains("++")||
							               currentStatement.contains("--"))
							            {
							                String converted = "";
							                converted = shorthandChanger(currentStatement);
							                if(currentStatement.length() == assignments.length())
							                    System.out.print(converted);
							                else System.out.print(converted + " ");
							                assignments = nextStatement(assignments);
							            }
							            else
							            {
							                if(currentStatement.length() == assignments.length())
							                    System.out.print(currentStatement);
							                else System.out.print(currentStatement + " ");
							                assignments = nextStatement(assignments);
							            }
							        }
							        while(!assignments.isEmpty());
							        // EOF!
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
        return l.substring(0, l.indexOf(";") + 1);
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
                var = l.trim().substring(0, l.indexOf("++")).trim();
                l = matchaC.replaceFirst(" = " + var + " + 1");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("--"))
            {
                var = l.trim().substring(0, l.indexOf("--") - 1).trim();
                l = matchaC.replaceFirst("= " + var + " - 1");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
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
            if(l.contains("++"))
            {
                var = l.trim().substring(0, l.indexOf("++")).trim();
                l = matchaU.replaceFirst(" = " + var + " + 1");
                res = res.concat(l.substring(0, l.indexOf(";") + 1));
            }
            else if(l.contains("--"))
            {
                var = l.trim().substring(0, l.indexOf("--") - 1).trim();
                l = matchaU.replaceFirst("= " + var + " - 1");
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
    
	public String commentRemover(String lineContainingComment) {
        String res = lineContainingComment;
        if (lineContainingComment.trim().startsWith("/*")) 
        {
            // MLC = on
            // Case 1: Multi Line comment ends with closing MLC
            // Case 1.1: Multiple code statements with multi line comments
            // Examples:
            // <code>; /* MLC */ <code>; /* MLC */ ... /* MLC */ <code>;
            if (lineContainingComment.indexOf("*/") >= 0) 
            {
                if (lineContainingComment.indexOf("/*", lineContainingComment.indexOf("/*")) >= 0)
                {
                    res = multiCommentRemover(lineContainingComment);
                }
                else
                    res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*")) + lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            } 
            else 
            {
                res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*"));
            }
        } 
        else if (lineContainingComment.trim().startsWith("*/")) 
        {
            String another = lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            // Case 2.2: Multi Line comment ends with additional single line comments
            // Case 2.3: Multi Line comment ends with additional multi line comments (after Case 2.2)
            if (another.indexOf("/*") >= 0) 
            {
                res = multiCommentRemover(another);
            } 
            else if (another.indexOf("//") >= 0) 
            {
                res = another.substring(0, another.indexOf("//") - 1);
            } 
            else 
            {
                res = another;
            }
        } 
        else if (lineContainingComment.indexOf("/*") >= 0) 
        {
            // MLC = on
            // Case 1: Multi Line comment ends with closing MLC
            // Case 1.1: Multiple code statements with multi line comments
            // Examples:
            // <code>; /* MLC */ <code>; /* MLC */ ... /* MLC */ <code>;
            if (lineContainingComment.indexOf("*/") >= 0) 
            {
                if (lineContainingComment.indexOf("/*", lineContainingComment.indexOf("/*")) >= 0)
                {
                    res = multiCommentRemover(lineContainingComment);
                }
                else
                    res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*")) + lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            } 
            else 
            {
                res = lineContainingComment.substring(0, lineContainingComment.indexOf("/*"));
            }
        } 
        else if (lineContainingComment.indexOf("*/") >= 0) 
        {
            String another = lineContainingComment.substring(lineContainingComment.indexOf("*/") + 2);
            // Case 2.2: Multi Line comment ends with additional single line comments
            // Case 2.3: Multi Line comment ends with additional multi line comments (after Case 2.2)
            if (another.indexOf("/*") >= 0) 
            {
                res = multiCommentRemover(another);
            } 
            else if (another.indexOf("//") >= 0) 
            {
                res = another.substring(0, another.indexOf("//") - 1);
            } 
            else 
            {
                res = another;
            }
        } 
        else if (lineContainingComment.trim().startsWith("//")) 
        {
            res = "";
        } 
        else if (lineContainingComment.indexOf("//") >= 0) 
        {
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
        } while (another.contains("/*") || another.contains("*/") || another.contains("//"));
        return another;
    }
}
