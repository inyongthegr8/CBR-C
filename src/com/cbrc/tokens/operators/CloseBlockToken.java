package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.operators.enums.BlockType;

public class CloseBlockToken extends BlockToken {

	public CloseBlockToken() {
		super("}");
	}
	
	public CloseBlockToken(BlockType blockType) {
		this();
		this.blockType = blockType;
	}
	
	/**
	 * In the current implementation, BlockType is not being used,
	 * as it leads to confusion for little gain. Suggest improving
	 * this in the future, based on the current state of current
	 * Marker plus the tokens in Tokenized Code.
	 */
	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("}")) {
			Token tokenObject = new CloseBlockToken();
			currentMarker.pop();
			return tokenObject;
		}
		return null;
	}

}
