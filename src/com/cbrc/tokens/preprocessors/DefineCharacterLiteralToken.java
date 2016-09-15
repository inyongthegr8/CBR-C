package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.literals.NonNumericLiteralToken;

public class DefineCharacterLiteralToken extends NonNumericLiteralToken {

	public DefineCharacterLiteralToken() {
		super("");
	}
	
	public DefineCharacterLiteralToken(String token) {
		super(token);
		// TODO Auto-generated constructor stub
	}
	
	public char getValueAsChar() {
		return this.getValue();
	}
	
	/**
	 * Token value will include the single quotes, hence to get
	 * the true value in Character, get the char at index of 1.
	 * @return The true character value of the literal
	 */
	public Character getValue() {
		return Character.valueOf(this.getToken().charAt(1));
	}

	@Override
	public DataType getDataType() {
		return DataType.CHAR;
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		/* Future implementation.
		if (token.startsWith("'") && token.endsWith("'")) {
			return new DefineCharacterLiteralToken(token);
		}
		*/
		return null;
	}

}
