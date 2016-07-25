package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class CaseKeywordToken extends KeywordToken {

	public CaseKeywordToken() {
		super("case");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Case statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("case")) {
			Token tokenObject = new CaseKeywordToken();
			currentMarker.push("case");
			return tokenObject;
		} else return null;
	}

}
