package com.cbrc.nodes.functions;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SeparatorToken;

public class DoubleFormalParameterNode extends FormalParameterNode {

	public DoubleFormalParameterNode() {
		super();
	}
	
	public DoubleFormalParameterNode(TypeSpecKeywordToken dataType,
			VariableIdentifierToken formalParamterIdentifier) {
		super(dataType, formalParamterIdentifier);
	}

	public DoubleFormalParameterNode(Node parentNode) {
		super(parentNode);
	}

	public DoubleFormalParameterNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public DoubleFormalParameterNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		
		TypeSpecKeywordToken dataType = (TypeSpecKeywordToken) tokens.consume();
		VariableIdentifierToken parameterIdentifier = (VariableIdentifierToken) tokens.consume();
		
		FormalParameterNode formalParameterNode = new DoubleFormalParameterNode(dataType, parameterIdentifier);
		
		formalParameterNode.setParentNode(parent);
		
		if (tokens.lookahead() instanceof SeparatorToken) tokens.consume();
		
		return formalParameterNode;
	}

}
