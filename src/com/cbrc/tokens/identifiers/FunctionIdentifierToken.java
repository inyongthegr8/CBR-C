package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.base.consts.CastConstants;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.IdentifierToken;

public class FunctionIdentifierToken extends IdentifierToken {

	public FunctionIdentifierToken() {
		super("");
	}
	
	public FunctionIdentifierToken(String token) {
		super(token);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}
	
	private boolean isFunctionIdentifier(String token, String succeedingToken) {
		if (token.matches(CastConstants.IDENTIFIER_REGEX) && succeedingToken.equals("(")) {
			return true;
		} else return false;
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (succeedingToken != null) {
			if (isFunctionIdentifier(token, succeedingToken)) {
				Token tokenObject = new FunctionIdentifierToken(token);
				return tokenObject;
			}
			return null;
		}
		return null;
		
	}

}
