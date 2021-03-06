package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class ElseKeywordToken extends KeywordToken {

	public ElseKeywordToken() {
		super("else");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Else statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("else")) {
			Token tokenObject = new ElseKeywordToken();
			currentMarker.push("else");
			return tokenObject;
		}
		return null;
	}

}
