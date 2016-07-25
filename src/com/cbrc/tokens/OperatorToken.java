package com.cbrc.tokens;

import com.cbrc.base.Token;

public abstract class OperatorToken extends Token {

	public OperatorToken(String token) {
		super(token);
	}

	@Override
	protected abstract String getAdditionalDetails();

}
