package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.PreprocessorToken;

public class OpeningDelimiterToken extends PreprocessorToken {

	public OpeningDelimiterToken() {
		super("<");
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "Opening Delimiter Token for #import";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("<")) {
			Token tokenObject = new OpeningDelimiterToken();
			currentMarker.push("<");
			return tokenObject;
		}
		return null;
	}
}
