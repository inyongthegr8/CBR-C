package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;

public class ScanfKeywordToken extends FunctionIdentifierToken {

	public ScanfKeywordToken() {
		super("scanf");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Scanf statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("scanf")) {
			Token tokenObject = new ScanfKeywordToken();
			currentMarker.push("scanf");
			return tokenObject;
		}
		return null;
	}

}
