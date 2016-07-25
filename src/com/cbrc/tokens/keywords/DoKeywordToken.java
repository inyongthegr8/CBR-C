package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;

/**
 * For simplicity, since only the structure is required
 * and not the actualy running of the code, the do-while
 * loop, whilst appearing on the opposite ends of the
 * do-while loop block, will be considered as one token
 * only.
 * 
 * @author Alice
 *
 */
public class DoKeywordToken extends KeywordToken {

	public DoKeywordToken() {
		super("do");
	}

	@Override
	protected String getAdditionalDetails() {
		return "Do statement";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		if (token.equals("do")) {
			Token tokenObject = new DoKeywordToken();
			currentMarker.push("do");
			return tokenObject;
		}
		return null;
	}

}
