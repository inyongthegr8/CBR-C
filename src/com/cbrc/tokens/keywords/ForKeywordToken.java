package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class ForKeywordToken extends KeywordToken {

	public ForKeywordToken() {
		super("for");
	}

	@Override
	protected String getAdditionalDetails() {
		return "For statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("for")) {
			Token tokenObject = new ForKeywordToken();
			currentMarker.push("for");
			return tokenObject;
		}
		return null;
	}

}
