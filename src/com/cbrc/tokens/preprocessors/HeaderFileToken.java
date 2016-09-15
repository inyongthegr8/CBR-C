package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.PreprocessorToken;

public class HeaderFileToken extends PreprocessorToken {

	public HeaderFileToken() {
		super("");
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "Header File for #include";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (currentMarker.peek().equals("#include")) {
			Token tokenObject = new HeaderFileToken();
			return tokenObject;
		}
		return null;
	}

}
