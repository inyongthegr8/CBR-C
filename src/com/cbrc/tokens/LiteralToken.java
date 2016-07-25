package com.cbrc.tokens;

import com.cbrc.base.Token;
import com.cbrc.tokens.interfaces.OperandToken;

public abstract class LiteralToken extends Token implements OperandToken {

	public LiteralToken(String token) {
		super(token);
	}

	protected abstract String getAdditionalDetails();

}
