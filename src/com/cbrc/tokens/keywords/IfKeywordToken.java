package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class IfKeywordToken extends KeywordToken {

	public IfKeywordToken() {
		super("if");
	}

	@Override
	protected String getAdditionalDetails() {
		return "If statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("if")) {
			Token tokenObject = new IfKeywordToken();
			currentMarker.push("if");
			return tokenObject;
		}
		return null;
	}

}
