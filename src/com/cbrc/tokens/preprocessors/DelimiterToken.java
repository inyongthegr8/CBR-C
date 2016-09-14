package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.literals.NonNumericLiteralToken;

public class DelimiterToken extends NonNumericLiteralToken {

	public DelimiterToken() {
		super("");
	}
	
	public DelimiterToken(String token) {
		super(token);
	}
	
	/**
	 * Token value will include delimiters, which necessitates
	 * the substring function to get the true string (filename) value of the
	 * literal.
	 * @return The true string value of the literal
	 */
	public String getValue() {
		return this.getToken().substring(1, this.getToken().length() - 1);
	}

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	
	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.startsWith("<") && token.endsWith(">")) {
			return new DelimiterToken(token);
		}
		return null;
	}

}
