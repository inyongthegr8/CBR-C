package com.cbrc.lexers.structs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.cbrc.base.Token;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.operators.SemicolonToken;

public class TokenizedCode extends ArrayList<Token>{

	private static final long serialVersionUID = 4444558912006164542L;
	private HashMap<VariableIdentifierToken, DataType> symbolTable;
	private File sourceCode;
	private int currentIndex;
	
	public TokenizedCode(File sourceCode) {
		super();
		this.sourceCode = sourceCode;
		this.symbolTable = new HashMap<VariableIdentifierToken, DataType>();
		this.currentIndex = 0;
	}
	
	public File getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(File sourceCode) {
		this.sourceCode = sourceCode;
	}

	public HashMap<VariableIdentifierToken, DataType> getSymbolTable() {
		return symbolTable;
	}

	public void setSymbolTable(HashMap<VariableIdentifierToken, DataType> symbolTable) {
		this.symbolTable = symbolTable;
	}
	
	public void registerVariable(VariableIdentifierToken variableToken, DataType dataType) {
		this.symbolTable.put(variableToken, dataType);
	}
	
	public DataType getVariableDataType(VariableIdentifierToken variableToken) {
		return this.symbolTable.get(variableToken);
	}
	
	public boolean isVariableRegistered(VariableIdentifierToken variableToken) {
		return this.symbolTable.containsKey(variableToken);
	}

	public String toString() {
		StringBuilder toString = new StringBuilder();
		
		for(Token token:this) {
			toString.append(token.toString());
			toString.append("\n");
			
			if(token instanceof SemicolonToken) toString.append("\n");
		}
		
		return toString.toString();
	}
	
	public Token peek() {
		return this.get(this.size() - 1);
	}
	
	public Token consume() {
		Token tokenObject = this.get(currentIndex);
		if (this.currentIndex < this.size()) currentIndex++;
		return tokenObject;
	}
	
	public Token lookahead() {
		Token result = null;
		if(currentIndex < this.size())
			result = this.get(currentIndex);
		return result;
	}
	
	public Token lookback() {
		if (currentIndex == 0) 
			return this.get(currentIndex);
		else return this.get(currentIndex - 1);
	}
	
	public void reset() {
		this.currentIndex = 0;
	}
	
	public void stepBack() {
		if (this.currentIndex != 0) this.currentIndex--;
	}
	
	public void stepForward() {
		if (this.currentIndex != this.size() - 1) this.currentIndex++;
	}
	
	public boolean hasMoreToConsume() {
		if (this.currentIndex < this.size()) return true;
		else return false;
	}

}
