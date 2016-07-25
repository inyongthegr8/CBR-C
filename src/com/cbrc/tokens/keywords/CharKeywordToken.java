package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.enums.TypeSpecType;

public class CharKeywordToken extends TypeSpecKeywordToken {

	public CharKeywordToken() {
		super("char");
	}

	public CharKeywordToken(TypeSpecType typeSpecType) {
		super("char", typeSpecType);
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("char")) {
			Token tokenObject = new CharKeywordToken();
			beforeGenerateCondition(tokenObject, currentMarker);
			return tokenObject;
		}
		return null;
	}

}
