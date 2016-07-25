package com.cbrc.tokens.literals;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;

public class StringLiteralToken extends NonNumericLiteralToken {

	public StringLiteralToken() {
		super("");
	}
	
	public StringLiteralToken(String token) {
		super(token);
	}
	
	/**
	 * Token value will include double quoutes, which necessitates
	 * the substring function to get the true string value of the
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
		if (token.startsWith("\"") && token.endsWith("\"")) {
			return new StringLiteralToken(token);
		}
		return null;
	}

}
