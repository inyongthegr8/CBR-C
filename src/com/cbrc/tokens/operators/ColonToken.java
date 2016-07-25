package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.OperatorToken;

public class ColonToken extends OperatorToken {

	public ColonToken() {
		super(":");
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals(":")) {
			return new ColonToken();
		}
		return null;
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

}
