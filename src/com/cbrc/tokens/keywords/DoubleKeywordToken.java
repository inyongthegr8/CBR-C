package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.enums.TypeSpecType;

public class DoubleKeywordToken extends TypeSpecKeywordToken {

	public DoubleKeywordToken() {
		super("double");
	}

	public DoubleKeywordToken(TypeSpecType typeSpecType) {
		super("double", typeSpecType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("double")) {
			Token tokenObject = new DoubleKeywordToken();
			beforeGenerateCondition(tokenObject, currentMarker);
			return tokenObject;
		}
		return null;
	}

}
