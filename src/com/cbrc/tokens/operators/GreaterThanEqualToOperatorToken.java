package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.OperatorToken;

public class GreaterThanEqualToOperatorToken extends OperatorToken {

	public GreaterThanEqualToOperatorToken() {
		super(">=");
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals(">=")) {
			return new GreaterThanEqualToOperatorToken();
		}
		return null;
	}

}
