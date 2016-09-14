package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.PreprocessorToken;

public class DefineKeywordToken extends PreprocessorToken {

	public DefineKeywordToken() {
		super("#define");
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "#define statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("#define")) {
			Token tokenObject = new DefineKeywordToken();
			currentMarker.push("#define");
			return tokenObject;
		}
		return null;
	}
}
