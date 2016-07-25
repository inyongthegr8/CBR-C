package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;

public class PrintfKeywordToken extends FunctionIdentifierToken {

	public PrintfKeywordToken() {
		super("printf");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Printf statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("printf")) {
			Token tokenObject = new PrintfKeywordToken();
			currentMarker.push("printf");
			return tokenObject;
		}
		return null;
	}

}
