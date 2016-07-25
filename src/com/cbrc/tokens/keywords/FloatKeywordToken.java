package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.enums.TypeSpecType;

public class FloatKeywordToken extends TypeSpecKeywordToken {

	public FloatKeywordToken() {
		super("float");
	}

	public FloatKeywordToken(TypeSpecType typeSpecType) {
		super("float", typeSpecType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("float")) {
			Token tokenObject = new FloatKeywordToken();
			beforeGenerateCondition(tokenObject, currentMarker);
			return tokenObject;
		}
		return null;
	}

}
