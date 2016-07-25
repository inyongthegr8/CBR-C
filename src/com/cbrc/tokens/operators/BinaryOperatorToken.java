package com.cbrc.tokens.operators;

import com.cbrc.tokens.OperatorToken;

public abstract class BinaryOperatorToken extends OperatorToken {

	public BinaryOperatorToken(String token) {
		super(token);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

}
