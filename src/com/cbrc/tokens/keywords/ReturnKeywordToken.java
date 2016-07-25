package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class ReturnKeywordToken extends KeywordToken {

	public ReturnKeywordToken() {
		super("return");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Return statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("return")) {
			Token tokenObject = new ReturnKeywordToken();
			currentMarker.push("return");
			return tokenObject;
		}
		return null;
	}

}
