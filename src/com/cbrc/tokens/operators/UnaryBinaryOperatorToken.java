package com.cbrc.tokens.operators;

import com.cbrc.tokens.OperatorToken;
import com.cbrc.tokens.operators.enums.PlusMinusType;

public abstract class UnaryBinaryOperatorToken extends OperatorToken {

	protected PlusMinusType plusMinusType;

	public UnaryBinaryOperatorToken(String token) {
		super(token);
	}
	
	public UnaryBinaryOperatorToken(String token, PlusMinusType plusMinusType) {
		super(token);
		this.plusMinusType = plusMinusType;
	}

	public PlusMinusType getPlusMinusType() {
		return plusMinusType;
	}

	public void setPlusMinusType(PlusMinusType plusMinusType) {
		this.plusMinusType = plusMinusType;
	}

	@Override
	protected String getAdditionalDetails() {
		return this.plusMinusType.toString();
	}

}