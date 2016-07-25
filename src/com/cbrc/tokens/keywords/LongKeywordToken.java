package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.enums.TypeSpecType;

public class LongKeywordToken extends TypeSpecKeywordToken {

	public LongKeywordToken() {
		super("long");
	}

	public LongKeywordToken(TypeSpecType typeSpecType) {
		super("long", typeSpecType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("long")) {
			Token tokenObject = new LongKeywordToken();
			beforeGenerateCondition(tokenObject, currentMarker);
			return tokenObject;
		}
		return null;
	}

}
