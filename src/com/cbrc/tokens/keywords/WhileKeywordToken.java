package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.keywords.enums.WhileType;

public class WhileKeywordToken extends KeywordToken {

	private WhileType whileType;
	
	public WhileKeywordToken() {
		super("while");
	}
	
	public WhileKeywordToken(WhileType whileType) {
		this();
		this.whileType = whileType;
	}

	public WhileType getWhileType() {
		return whileType;
	}

	public void setWhileType(WhileType whileType) {
		this.whileType = whileType;
	}

	@Override
	protected String getAdditionalDetails() {
		return "While Statement: " + this.whileType.toString();
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("while")) {
			Token tokenObject = new WhileKeywordToken();
			
			if (currentMarker.peek().equals("do")) {
				((WhileKeywordToken) tokenObject).setWhileType(WhileType.DO_WHILE_LOOP);
			} else {
				((WhileKeywordToken) tokenObject).setWhileType(WhileType.WHILE_LOOP);
				currentMarker.push("while");
			}
			
			return tokenObject;
		}
		return null;
	}

}
