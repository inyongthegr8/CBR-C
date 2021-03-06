package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;

public class DivisionOperatorToken extends BinaryOperatorToken {

	public DivisionOperatorToken() {
		super("/");
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("/")) {
			return new DivisionOperatorToken();
		}
		return null;
	}

}
