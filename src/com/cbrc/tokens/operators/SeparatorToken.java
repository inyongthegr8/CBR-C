package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.OperatorToken;

public class SeparatorToken extends OperatorToken {

	public SeparatorToken() {
		super(",");
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals(",")) {
			Token separator = new SeparatorToken();
			return separator;
		}
		return null;
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

}
