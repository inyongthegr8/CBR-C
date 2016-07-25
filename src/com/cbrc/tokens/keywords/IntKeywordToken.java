package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.enums.TypeSpecType;


public class IntKeywordToken extends TypeSpecKeywordToken {

	public IntKeywordToken() {
		super("int");
	}
	
	public IntKeywordToken(TypeSpecType typeSpecType) {
		super("int", typeSpecType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("int")) {
			Token tokenObject = new IntKeywordToken();
			beforeGenerateCondition(tokenObject, currentMarker);
			return tokenObject;
		}
		return null;
	}

}
