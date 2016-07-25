package com.cbrc.tokens;

import com.cbrc.base.Token;

public abstract class IdentifierToken extends Token {

	public IdentifierToken(String token) {
		super(token);
	}

	protected abstract String getAdditionalDetails();

}
