package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.PreprocessorToken;

public class IncludeKeywordToken extends PreprocessorToken {

	public IncludeKeywordToken() {
		super("#include");
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "#include statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("#include")) {
			Token tokenObject = new IncludeKeywordToken();
			currentMarker.push("#include");
			return tokenObject;
		}
		return null;
	}
}
