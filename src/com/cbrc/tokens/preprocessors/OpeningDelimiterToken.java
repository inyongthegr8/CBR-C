package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.PreprocessorToken;
import com.cbrc.tokens.keywords.BreakKeywordToken;
import com.cbrc.tokens.keywords.enums.BreakType;

public class OpeningDelimiterToken extends PreprocessorToken {

	public OpeningDelimiterToken() {
		super("<");
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "Opening Delimiter Token for #include";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("<") && currentMarker.peek().equals("#include")) {
			Token tokenObject = new OpeningDelimiterToken();
			return tokenObject;
		}
		return null;
	}
}
