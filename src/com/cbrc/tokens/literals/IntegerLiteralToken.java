package com.cbrc.tokens.literals;

import java.math.BigInteger;
import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.base.consts.CastConstants;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;

public class IntegerLiteralToken extends NumericLiteralToken {

	public IntegerLiteralToken() {
		super("");
	}
	
	public IntegerLiteralToken(String token) {
		super(token);
	}
	
	public int getValueAsInt() {
		return Integer.parseInt(this.getToken());
	}
	
	public BigInteger getValue() {
		return new BigInteger(this.getToken());
	}

	@Override
	public DataType getDataType() {
		return DataType.INT;
	}
	
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.matches(CastConstants.INT_LITERAL_REGEX)) {
			return new IntegerLiteralToken(token);
		}
		return null;
	}

}
