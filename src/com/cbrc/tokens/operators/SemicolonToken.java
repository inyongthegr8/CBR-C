package com.cbrc.tokens.operators;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.OperatorToken;
import com.cbrc.tokens.keywords.ForKeywordToken;
import com.cbrc.tokens.operators.enums.SemicolonType;

public class SemicolonToken extends OperatorToken {

	private SemicolonType semicolonType;
	
	public SemicolonToken() {
		super(";");
	}
	
	public SemicolonToken(SemicolonType semicolonType) {
		this();
		this.semicolonType = semicolonType;
	}

	public SemicolonType getSemicolonType() {
		return semicolonType;
	}

	public void setSemicolonType(SemicolonType semicolonType) {
		this.semicolonType = semicolonType;
	}

	@Override
	protected String getAdditionalDetails() {
		return this.semicolonType.toString();
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals(";")) {
			Token semicolon = new SemicolonToken();
			
			((SemicolonToken) semicolon).setSemicolonType(SemicolonType.END_OF_STATEMENT);
			if (currentMarker.peek().equals("for")) {
				
				int index = tokens.size() - 1, semicolonCount = 0;
				while(!(tokens.get(index) instanceof ForKeywordToken)) {
					index--;
					if(tokens.get(index) instanceof SemicolonToken) {
						semicolonCount++;
					}
				}
				
				if (semicolonCount == 2) {
					currentMarker.pop();
				} else {
					((SemicolonToken) semicolon).setSemicolonType(SemicolonType.FOR_LOOP_PORTION);
				}
			} else currentMarker.pop();
			
			return semicolon;
		}
		return null;
	}

}
