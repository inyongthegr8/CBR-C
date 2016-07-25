package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.keywords.FloatKeywordToken;

public class FloatVariableIdentifierToken extends VariableIdentifierToken {

	public FloatVariableIdentifierToken() {
		super("");
	}
	
	public FloatVariableIdentifierToken(String token) {
		super(token);
	}

	@Override
	public DataType getDataType() {
		return DataType.FLOAT;
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (this.isVariableIdentifier(token, succeedingToken)) {
			VariableIdentifierToken tokenObject = new FloatVariableIdentifierToken(token);
			
			if (tokens.isVariableRegistered(tokenObject)) {
				if (tokens.getVariableDataType(tokenObject).equals(this.getDataType())) {
					return tokenObject;
				} else return null;
			} else {
				if (tokens.peek() instanceof FloatKeywordToken) {
					this.handleAddingToSymbolTable(token, currentMarker, tokens, tokenObject,this.getDataType());
					return tokenObject;
				} else return null;
			}
			
		}
		return null;
	}

}
