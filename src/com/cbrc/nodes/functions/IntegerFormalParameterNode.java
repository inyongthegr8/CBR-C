package com.cbrc.nodes.functions;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SeparatorToken;

public class IntegerFormalParameterNode extends FormalParameterNode {

	public IntegerFormalParameterNode(){
		super();
	}
	
	public IntegerFormalParameterNode(TypeSpecKeywordToken dataType,
			VariableIdentifierToken formalParamterIdentifier) {
		super(dataType, formalParamterIdentifier);
	}

	public IntegerFormalParameterNode(Node parentNode) {
		super(parentNode);
	}

	public IntegerFormalParameterNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public IntegerFormalParameterNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		
		TypeSpecKeywordToken dataType = (TypeSpecKeywordToken) tokens.consume();
		VariableIdentifierToken parameterIdentifier = (VariableIdentifierToken) tokens.consume();
		
		FormalParameterNode formalParameterNode = new IntegerFormalParameterNode(dataType, parameterIdentifier);
		
		formalParameterNode.setParentNode(parent);
		
		if (tokens.lookahead() instanceof SeparatorToken) tokens.consume();
		
		return formalParameterNode;
	}

}
