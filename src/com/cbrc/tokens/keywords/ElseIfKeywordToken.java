package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class ElseIfKeywordToken extends KeywordToken {

	public ElseIfKeywordToken() {
		super("else if");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Else If statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("else if")) {
			Token tokenObject = new ElseIfKeywordToken();
			currentMarker.push("else if");
			return tokenObject;
		} 
		return null;
	}

}
