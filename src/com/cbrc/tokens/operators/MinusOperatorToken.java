package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.interfaces.OperandToken;
import com.cbrc.tokens.operators.enums.PlusMinusType;

public class MinusOperatorToken extends UnaryBinaryOperatorToken {

	public MinusOperatorToken() {
		super("-");
	}

	public MinusOperatorToken(PlusMinusType plusMinusType) {
		super("-", plusMinusType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("-")) {
			Token tokenObject = new MinusOperatorToken();
			
			if(tokens.peek() instanceof OperandToken) {
				((MinusOperatorToken) tokenObject).setPlusMinusType(PlusMinusType.BINARY_OPERATOR);
			} else {
				((MinusOperatorToken) tokenObject).setPlusMinusType(PlusMinusType.UNARY_OPERATOR);
			}

			return tokenObject;	
		}
		return null;
	}

}
