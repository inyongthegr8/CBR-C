package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.OperatorToken;

public class AssignmentOperatorToken extends OperatorToken {

	public AssignmentOperatorToken() {
		super("=");
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("=")) {
			Token assignmentToken = new AssignmentOperatorToken();
			currentMarker.push("assignment");
			return assignmentToken;
		}
		return null;
	}

}
