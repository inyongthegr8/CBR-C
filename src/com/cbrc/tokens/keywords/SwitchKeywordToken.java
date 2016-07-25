package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

public class SwitchKeywordToken extends KeywordToken {

	public SwitchKeywordToken() {
		super("switch");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Switch statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("switch")) {
			Token tokenObject = new SwitchKeywordToken();
			currentMarker.push("switch");
			return tokenObject;
		}
		return null;
	}

}
