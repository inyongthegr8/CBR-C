package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.keywords.enums.BreakType;

public class BreakKeywordToken extends KeywordToken {

	private BreakType breakType;
	
	public BreakKeywordToken() {
		super("break");
	}
	
	public BreakKeywordToken(BreakType breakType) {
		this();
		this.breakType = breakType;
	}

	public BreakType getBreakType() {
		return breakType;
	}

	public void setBreakType(BreakType breakType) {
		this.breakType = breakType;
	}

	@Override
	protected String getAdditionalDetails() {
		return "Break statement: " + this.breakType.toString();
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("break")) {
			KeywordToken tokenObject = new BreakKeywordToken();
			
			if (currentMarker.peek().equals("switch")) {
				((BreakKeywordToken) tokenObject).setBreakType(BreakType.SWITCH_CASE_TYPE);
			} else {
				((BreakKeywordToken) tokenObject).setBreakType(BreakType.FUNCTION_TYPE);
			}
			
			return tokenObject;
		} else return null;
	}

}
