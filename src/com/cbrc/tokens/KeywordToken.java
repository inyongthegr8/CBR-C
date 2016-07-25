package com.cbrc.tokens;

import com.cbrc.base.Token;

public abstract class KeywordToken extends Token {

	public KeywordToken(String token) {
		super(token);
	}

	protected abstract String getAdditionalDetails();

}
