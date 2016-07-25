package com.cbrc.tokens.keywords;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.keywords.enums.TypeSpecType;

public abstract class TypeSpecKeywordToken extends KeywordToken {

	protected TypeSpecType typeSpecType;
	
	public TypeSpecKeywordToken(String token) {
		super(token);
	}
	
	public TypeSpecKeywordToken(String token, TypeSpecType typeSpecType) {
		this(token);
		this.typeSpecType = typeSpecType;
	}
	
	public TypeSpecType getTypeSpecType() {
		return typeSpecType;
	}

	public void setTypeSpecType(TypeSpecType typeSpecType) {
		this.typeSpecType = typeSpecType;
	}

	@Override
	protected String getAdditionalDetails() {
		return this.typeSpecType.toString();
	}
	
	/**
	 * <b>Note:</b> For this modelling, two of the four TypeSpecTypes are not used,
	 * <code>GLOBAL_TYPE_SPEC</code> and <code>PARAMETER_TYPE_SPEC</code>. This is
	 * because parameters would be considered as function variables and Global
	 * variables are not going to be considered as they are not allowed in DLSU
	 * COMPRO1.
	 *  
	 * @param token the TypeSpecKeywordToken object to set the type of.
	 * @param currentMarker The stack containing the current block or marker.
	 */
	protected void beforeGenerateCondition(Token token, Stack<String> currentMarker) {
		if (currentMarker.isEmpty()) {
			currentMarker.push("declaration");
			((TypeSpecKeywordToken) token).setTypeSpecType(TypeSpecType.VARIABLE_TYPE_SPEC);
		} else {
			currentMarker.push("function");
			((TypeSpecKeywordToken) token).setTypeSpecType(TypeSpecType.FUNCTION_TYPE_SPEC);
		}
	}

}
