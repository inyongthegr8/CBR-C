package com.cbrc.tokens.literals;

import java.math.BigDecimal;
import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.base.consts.CastConstants;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.enums.DataType;

public class FloatLiteralToken extends NumericLiteralToken {

	public FloatLiteralToken() {
		super("");
	}
	
	public FloatLiteralToken(String token) {
		super(token);
	}
	
	public float getValueAsFloat() {
		return Float.parseFloat(this.getToken());
	}
	
	public BigDecimal getValue() {
		return new BigDecimal(this.getToken());
	}

	@Override
	public DataType getDataType() {
		return DataType.FLOAT;
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.matches(CastConstants.FLOAT_LITERAL_REGEX) && !token.matches(CastConstants.INT_LITERAL_REGEX)) {
			return new FloatLiteralToken(token);
		}
		return null;
	}

}
