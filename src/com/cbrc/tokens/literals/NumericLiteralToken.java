package com.cbrc.tokens.literals;

import com.cbrc.tokens.LiteralToken;
import com.cbrc.tokens.identifiers.enums.DataType;

public abstract class NumericLiteralToken extends LiteralToken {

	public NumericLiteralToken(String token) {
		super(token);
	}
	
	public abstract DataType getDataType();

	@Override
	protected String getAdditionalDetails() {
		return this.getDataType().toString();
	}

}
