package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class DefaultKeywordToken extends KeywordToken {

	public DefaultKeywordToken() {
		super("default");
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("default")) {
			Token tokenObject = new DefaultKeywordToken();
			currentMarker.push("case"); // pushing case because expecting behaviour similar to case
			return tokenObject;
		} else return null;
	}

	@Override
	protected String getAdditionalDetails() {
		// TODO Auto-generated method stub
		return "Default statement";
	}

}
