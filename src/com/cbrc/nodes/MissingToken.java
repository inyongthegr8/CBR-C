package com.cbrc.nodes;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;

public class MissingToken extends Token {

	public MissingToken(String token) {
		super(token);
	}
	
	public MissingToken() {
		super("Missing lines of code from your code:");
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAdditionalDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
