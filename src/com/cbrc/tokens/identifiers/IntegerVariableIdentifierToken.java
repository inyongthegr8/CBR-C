package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.keywords.IntKeywordToken;

public class IntegerVariableIdentifierToken extends VariableIdentifierToken {

	public IntegerVariableIdentifierToken() {
		super("");
	}
	
	public IntegerVariableIdentifierToken(String token) {
		super(token);
	}

	@Override
	public DataType getDataType() {
		return DataType.INT;
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (this.isVariableIdentifier(token, succeedingToken)) {
			VariableIdentifierToken tokenObject = new IntegerVariableIdentifierToken(token);
			
			if (tokens.isVariableRegistered(tokenObject)) {
				if (tokens.getVariableDataType(tokenObject).equals(this.getDataType())) {
					return tokenObject;
				} else return null;
			} else {
				
				if (tokens.peek() instanceof IntKeywordToken) {
					this.handleAddingToSymbolTable(token, currentMarker, tokens, tokenObject, this.getDataType());
					return tokenObject;
				} else return null;
			}
			
		}
		return null;
	}

}
