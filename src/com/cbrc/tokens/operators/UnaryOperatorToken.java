package com.cbrc.tokens.operators;

import com.cbrc.tokens.OperatorToken;

public abstract class UnaryOperatorToken extends OperatorToken {

	public UnaryOperatorToken(String token) {
		super(token);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

}
