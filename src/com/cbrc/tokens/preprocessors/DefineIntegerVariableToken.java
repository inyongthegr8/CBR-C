// unused class
package com.cbrc.tokens.preprocessors;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.IdentifierToken;
import com.cbrc.tokens.PreprocessorToken;
import com.cbrc.tokens.identifiers.IntegerVariableIdentifierToken;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.keywords.IntKeywordToken;

public class DefineIntegerVariableToken extends VariableIdentifierToken {
	
	public DefineIntegerVariableToken() {
		super("");
	}
	
	public DefineIntegerVariableToken(String token) {
		super(token);
	}
	
	@Override
	public DataType getDataType() {
		return DataType.INT;
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "constant integer variable";
	}

	@Override
	public Token generateCondition(String token, TokenizedCode tokens,
			Stack<String> currentMarker, String succeedingToken) {
		/* Future implementation.
		
		*/
		return null;
	}
}
