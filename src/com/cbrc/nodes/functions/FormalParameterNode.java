package com.cbrc.nodes.functions;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;

public abstract class FormalParameterNode extends Node {
	
	public FormalParameterNode() {
		super();
	}
	
	public FormalParameterNode(TypeSpecKeywordToken dataType, VariableIdentifierToken formalParamterIdentifier) {
		super();
		
		dataType.setParentNode(this);
		formalParamterIdentifier.setParentNode(this);
		
		dataType.setIndex(0);
		formalParamterIdentifier.setIndex(1);
		
		this.getChildNodes().add(dataType);
		this.getChildNodes().add(formalParamterIdentifier);
	}

	public FormalParameterNode(Node parentNode) {
		super(parentNode);
	}

	public FormalParameterNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public FormalParameterNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	public TypeSpecKeywordToken getDataType() {
		return (TypeSpecKeywordToken) this.getChildNodes().get(0);
	}

	public void setDataType(TypeSpecKeywordToken dataType) {
		dataType.setParentNode(this);
		this.getChildNodes().set(0, dataType);
	}

	public VariableIdentifierToken getFormalParameterIdentifier() {
		return (VariableIdentifierToken) this.getChildNodes().get(1);
	}

	public void setFormalParameterIdentifier(
			VariableIdentifierToken formalParameterIdentifier) {
		formalParameterIdentifier.setParentNode(this);
		this.getChildNodes().set(1, formalParameterIdentifier);
	}

	@Override
	protected String getAdditionalDetails() {
		return "DataType: " + this.getDataType().getToken() + ": Formal Parameter Identifier: " + this.getFormalParameterIdentifier().getToken(); 
	}

}
