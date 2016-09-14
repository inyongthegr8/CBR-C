package com.cbrc.tokens;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;

public abstract class PreprocessorToken extends Token {

	public PreprocessorToken(String token) {
		super(token);
	}

	protected abstract String getAdditionalDetails();

}
