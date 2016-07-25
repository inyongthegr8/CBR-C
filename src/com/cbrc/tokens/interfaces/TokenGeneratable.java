package com.cbrc.tokens.interfaces;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;

public interface TokenGeneratable {
	
	public abstract Token generateCondition(String token, TokenizedCode tokens, Stack<String> currentMarker, String succeedingToken);
	
}
