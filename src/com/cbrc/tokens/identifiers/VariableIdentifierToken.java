package com.cbrc.tokens.identifiers;

import java.util.Stack;

import com.cbrc.base.consts.CastConstants;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.IdentifierToken;
import com.cbrc.tokens.identifiers.enums.DataType;
import com.cbrc.tokens.interfaces.OperandToken;

public abstract class VariableIdentifierToken extends IdentifierToken implements OperandToken{
	
	public VariableIdentifierToken(String token) {
		super(token);
	}

	public abstract DataType getDataType();
	
	@Override
	protected String getAdditionalDetails() {
		return this.getDataType().toString();
	}
	
	protected boolean isVariableIdentifier(String token, String succeedingToken) {
		if (token.matches(CastConstants.IDENTIFIER_REGEX) && !succeedingToken.equals("(")) {
			return true;
		} else return false;
	}
	
	protected void handleAddingToSymbolTable(String token, Stack<String> currentMarker, TokenizedCode tokens, VariableIdentifierToken tokenObject, DataType dataType) {
		tokens.registerVariable(tokenObject, dataType);
	}
	
	public boolean equals(Object o) {
		if (o instanceof VariableIdentifierToken) {
			if (((VariableIdentifierToken) o).getToken() == null) return false;
			else if (((VariableIdentifierToken) o).getToken().equals(this.getToken())) return true;
			else return false;
		} else return false;
	}
	
	public int hashCode() {
		return this.getToken().hashCode();
	}
	
	/*
	 * Algorithm for determining Token action:
	 * 1. Check if token matches the regex for variable (see CastConstants class)
	 * 2. If it is, Check if currentMarker is declaration
	 * 3. If it is, add the token to symbol table, consult the previous token
	 *    in the TokenizedCode for the datatype of the token.
	 * 4. Generate a token object for the token, using the type specified
	 *    in the symbol table (could be a hashmap)
	 *    
	 */

}
