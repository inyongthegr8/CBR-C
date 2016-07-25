package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.keywords.LongKeywordToken;

public class LongVariableIdentifierToken extends VariableIdentifierToken {

	public LongVariableIdentifierToken() {
		super("");
	}
	
	public LongVariableIdentifierToken(String token) {
		super(token);
	}

	@Override
	public DataType getDataType() {
		return DataType.LONG;
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (this.isVariableIdentifier(token, succeedingToken)) {
			VariableIdentifierToken tokenObject = new LongVariableIdentifierToken(token);
			
			if (tokens.isVariableRegistered(tokenObject)) {
				if (tokens.getVariableDataType(tokenObject).equals(this.getDataType())) {
					return tokenObject;
				} else return null;
			} else {
				if (tokens.peek() instanceof LongKeywordToken) {
					this.handleAddingToSymbolTable(token, currentMarker, tokens, tokenObject,this.getDataType());
					return tokenObject;
				} else return null;
			}
			
		}
		return null;
	}

}
