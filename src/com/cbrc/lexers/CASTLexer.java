package com.cbrc.lexers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.cbrc.base.Token;
import com.cbrc.lexers.factories.CASTStringTokenFactory;
import com.cbrc.lexers.factories.CASTTokenFactory;
import com.cbrc.lexers.structs.TokenizedCode;

//TODO: Fix bug with code ending in newLine not working
public class CASTLexer {
	
	private File sourceCode;
	private TokenizedCode tokens;
	private boolean debug;

	public CASTLexer(File sourceCode) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.sourceCode = sourceCode;
		this.tokens = new TokenizedCode(sourceCode);
		this.debug = false;
		lex();
	}
	
	public File getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(File sourceCode) {
		this.sourceCode = sourceCode;
	}

	public TokenizedCode getTokens() {
		return tokens;
	}

	public void setTokens(TokenizedCode tokens) {
		this.tokens = tokens;
	}

	private void lex() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		ArrayList<String> stringTokenList = generateStringTokenList();
		CASTTokenFactory tokenFactory = new CASTTokenFactory();
		
		for(int index = 0; index < stringTokenList.size(); index++) {
			Token tokenObject = null;
			if (index + 1 < stringTokenList.size()) tokenObject = tokenFactory.createToken(stringTokenList.get(index), stringTokenList.get(index + 1), tokens);
			else tokenObject = tokenFactory.createToken(stringTokenList.get(index), null, tokens);
			
			if (tokenObject != null) {
				this.tokens.add(tokenObject);
			} else if (debug == true) System.out.println(this.getClass().getName() + ": ERROR FOUND: Null Token returned for string " + stringTokenList.get(index));
		}
	}

	private ArrayList<String> generateStringTokenList() throws FileNotFoundException,
			IOException {
		FileInputStream inputStream = new FileInputStream(this.getSourceCode());
		ArrayList<String> stringTokens = new ArrayList<String>();
		try {
			DataInputStream dataStream = new DataInputStream(inputStream);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream));
				try {
					
					reader.mark(1);
					while (reader.read() != -1) {
						reader.reset();
						String token = CASTStringTokenFactory.buildToken(reader);
						if (debug == true) System.out.println(this.getClass().getName() + ": TOKEN FOUND: " + token);
						stringTokens.add(token);
						reader.mark(1);
					}
					
					
				} finally {
					reader.close();
				}
			} finally {
				dataStream.close();
			}
		} finally {
			inputStream.close();
		}
		return stringTokens;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
