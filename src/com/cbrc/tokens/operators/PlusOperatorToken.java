package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.interfaces.OperandToken;
import com.cbrc.tokens.operators.enums.PlusMinusType;

public class PlusOperatorToken extends UnaryBinaryOperatorToken{

	public PlusOperatorToken() {
		super("+");
	}
	
	public PlusOperatorToken(PlusMinusType plusMinusType) {
		super("+", plusMinusType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("+")) {
			Token tokenObject = new PlusOperatorToken();
			
			if(tokens.peek() instanceof OperandToken) {
				((PlusOperatorToken) tokenObject).setPlusMinusType(PlusMinusType.BINARY_OPERATOR);
			} else {
				((PlusOperatorToken) tokenObject).setPlusMinusType(PlusMinusType.UNARY_OPERATOR);
			}

			return tokenObject;
		}
		return null;
	}

}
