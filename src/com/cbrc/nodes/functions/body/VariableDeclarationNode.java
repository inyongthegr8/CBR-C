package com.cbrc.nodes.functions.body;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SemicolonToken;

public abstract class VariableDeclarationNode extends Node {

	public VariableDeclarationNode() {
		super();
	}
	
	public VariableDeclarationNode(TypeSpecKeywordToken dataType, VariableIdentifierToken variableIdentifier, SemicolonToken semicolon) {
		super();
		
		dataType.setParentNode(this);
		variableIdentifier.setParentNode(this);
		semicolon.setParentNode(this);
		
		dataType.setIndex(0);
		variableIdentifier.setIndex(1);
		semicolon.setIndex(2);
		
		this.getChildNodes().add(dataType);
		this.getChildNodes().add(variableIdentifier);
		this.getChildNodes().add(semicolon);
	}

	public VariableDeclarationNode(Node parentNode) {
		super(parentNode);
	}

	public VariableDeclarationNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public VariableDeclarationNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	public TypeSpecKeywordToken getDataType() {
		return (TypeSpecKeywordToken) this.getChildNodes().get(0);
	}

	public void setDataType(TypeSpecKeywordToken dataType) {
		dataType.setParentNode(this);
		this.getChildNodes().set(0, dataType);
	}

	public VariableIdentifierToken getVariableIdentifier() {
		return (VariableIdentifierToken) this.getChildNodes().get(1);
	}

	public void setVariableIdentifier(VariableIdentifierToken variableIdentifier) {
		variableIdentifier.setParentNode(this);
		this.getChildNodes().set(1, variableIdentifier);
	}

	public SemicolonToken getSemicolon() {
		return (SemicolonToken) this.getChildNodes().get(2);
	}

	public void setSemicolon(SemicolonToken semicolon) {
		semicolon.setParentNode(this);
		this.getChildNodes().set(2, semicolon);
	}

	@Override
	protected String getAdditionalDetails() {
		return "DataType: " + this.getDataType().getToken() + ": Variable Identifier: " + this.getVariableIdentifier().getToken();
	}

}
