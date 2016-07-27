package com.cbrc.lexers.factories;

import java.io.BufferedReader;
import java.io.IOException;

import com.cbrc.base.consts.CastConstants;

public class CASTStringTokenFactory {

	public static String buildToken(BufferedReader reader) throws IOException {
		StringBuilder token = new StringBuilder();
		String tokenToReturn = "";
		boolean hasTokenToReturn = false;
		char currentChar;
		
		// TODO: Add handler here that parses out the end of file newline
		
		while(hasTokenToReturn == false) {
			reader.mark(1);
			int status = reader.read();
			if(status == -1){
				hasTokenToReturn = true;
			}
			else {
				currentChar = (char) status;
				if (currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || String.valueOf(currentChar).equals(CastConstants.NEWLINE)) {
					reader.reset();
					reader.skip(1);
					reader.mark(1);
				}
				
				if (currentChar == '\'') {
					reader.reset();
					
					handleCharLiteralTokenString(reader, token);
				} else if (currentChar == '"') {
					reader.reset();
					
					handleStringLiteralTokenString(reader, token);
				} else if (currentChar == '|') {
					reader.reset();
					
					handleOrOperatorTokenString(reader, token);
				} else if (currentChar == '&') {
					reader.reset();
					
					handleAmpersandTokenString(reader, token);
				} else if (currentChar == '>' || currentChar == '<' || currentChar == '=' || currentChar == '!' || currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
					
					// insert ++, --, +=, -=, *= and /= handling here
					
					reader.reset();
					
					handleOperatorTokenString(reader, token);
				} else if (currentChar == ';' || currentChar == '(' || currentChar == ')' || currentChar == '{' || currentChar == '}' || currentChar == ':' || currentChar == ',') {
					reader.reset();
					
					appendSingleCharacterToTokenString(reader, token);
				}
				else {
					reader.reset();
					
					handleOtherTokenString(reader, token);
				}
				
				tokenToReturn = trimToken(token);
				if(!tokenToReturn.equals("") && !tokenToReturn.contains(CastConstants.NEWLINE)) hasTokenToReturn = true;
			}
			
		}

		return tokenToReturn;
	}
	
	private static void handleOtherTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		reader.mark(1);
		while(isNonTerminating(reader.read())) {
			reader.reset();
			
			appendSingleCharacterToTokenString(reader, token);
			String tokenString = trimToken(token);
			
			if (tokenString.equals("else")) {
				reader.mark(3);
				char[] elseCheck = new char[3];
				reader.read(elseCheck, 0, 3);
				String elseString = String.valueOf(elseCheck);
				if (elseString.equals(" if")) {
					reader.reset();
					
					token.append(elseString);
					reader.skip(3);
				} else reader.reset();
			}
			
			reader.mark(1);
		}
		
		reader.reset();
	}

	private static void appendSingleCharacterToTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		char currentChar;
		currentChar = (char) reader.read();
		token.append(currentChar);
	}

	private static void handleOperatorTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		char currentChar;
		appendSingleCharacterToTokenString(reader, token);
		
		reader.mark(1);
		currentChar = (char) reader.read();
		if (currentChar == '=' || (currentChar == '+' && token.charAt(token.length() - 1) == '+') || (currentChar == '-' && token.charAt(token.length() - 1) == '-')) {
			
			// append something else to the TokenString. Preprocess shorthand ++, --, -=, +=, /= and *= here
			
			reader.reset();
			appendSingleCharacterToTokenString(reader, token);
		} else {
			reader.reset();
		}
	}

	private static void handleAmpersandTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		char currentChar;
		appendSingleCharacterToTokenString(reader, token);
		
		reader.mark(1);
		currentChar = (char) reader.read();
		if (currentChar == '&') {
			reader.reset();
			appendSingleCharacterToTokenString(reader, token);
		} else {
			reader.reset();
		}
	}

	private static void handleOrOperatorTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		appendSingleCharacterToTokenString(reader, token);
		appendSingleCharacterToTokenString(reader, token);
	}

	private static void handleStringLiteralTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		char currentChar;
		boolean terminate = false;
		while (terminate == false) {
			appendSingleCharacterToTokenString(reader, token);
			
			reader.mark(1);
			currentChar = (char) reader.read();
			if (currentChar == '"' && token.charAt(token.length() - 1) != '\\') {
				reader.reset();
				
				appendSingleCharacterToTokenString(reader, token);
				terminate = true;
			} else {
				reader.reset();
			}
		}
	}

	private static void handleCharLiteralTokenString(BufferedReader reader,
			StringBuilder token) throws IOException {
		char currentChar;
		boolean terminate = false;
		while (terminate == false) {
			appendSingleCharacterToTokenString(reader, token);
			
			reader.mark(1);
			currentChar = (char) reader.read();
			if (currentChar == '\'' && token.charAt(token.length() - 1) != '\\') {
				reader.reset();
				
				appendSingleCharacterToTokenString(reader, token);
				terminate = true;
			} else {
				reader.reset();
			}
		}
	}

	private static String trimToken(StringBuilder token) {
		String tokenToReturn;
		tokenToReturn = token.toString();
		tokenToReturn = tokenToReturn.replace('\n', ' ');
		tokenToReturn = tokenToReturn.replace('\r', ' ');
		tokenToReturn = tokenToReturn.trim();
		return tokenToReturn;
	}

	private static boolean isNonTerminating(int current) {
		char currentChar = (char) current;
		
		if (current == -1) return false;
		else if(currentChar == ' ') return false;
		else if(currentChar == ',') return false;
		else if(currentChar == ';') return false;
		else if(currentChar == '\n') return false;
		else if(currentChar == '\r') return false;
		else if(currentChar == '(') return false;
		else if(currentChar == ')') return false;
		else if(currentChar == '{') return false;
		else if(currentChar == '}') return false;
		else if(currentChar == '<') return false;
		else if(currentChar == '>') return false;
		else if(currentChar == '=') return false;
		else if(currentChar == '!') return false;
		else if(currentChar == '+') return false;
		else if(currentChar == '-') return false;
		else if(currentChar == '*') return false;
		else if(currentChar == '/') return false;
		else if(currentChar == '&') return false;
		else if(currentChar == '|') return false;
		else if(currentChar == '"') return false;
		else if(currentChar == '\'') return false;
		else if(currentChar == ':') return false;
		else if(String.valueOf(currentChar).equals(CastConstants.NEWLINE)) return false;
		else return true;
	}
	
}
