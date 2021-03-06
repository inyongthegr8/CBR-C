package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.PreprocessorToken;

public class ClosingDelimiterToken extends PreprocessorToken {

	public ClosingDelimiterToken() {
		super(">");
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "Closing Delimiter Token for #include";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals(">") && currentMarker.peek().equals("#include")) {
			Token tokenObject = new ClosingDelimiterToken();
			currentMarker.pop();
			return tokenObject;
		}
		return null;
	}
}
