package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.interfaces.OperandToken;
import com.cbrc.tokens.operators.enums.GroupingType;

public class OpenGroupingToken extends GroupingToken implements OperandToken{

	public OpenGroupingToken() {
		super("(");
	}
	
	public OpenGroupingToken(GroupingType groupingType) {
		this();
		this.groupingType = groupingType;
	}
	
	/**
	 * In the current implementation, GroupingType is not being used,
	 * as it leads to confusion for little gain. Suggest improving
	 * this in the future, based on the current state of current
	 * Marker plus the tokens in Tokenized Code.
	 */
	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("(")) {
			Token tokenObject = new OpenGroupingToken();
			return tokenObject;
		}
		return null;
	}

}
