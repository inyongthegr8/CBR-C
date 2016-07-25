package com.cbrc.nodes.functions;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SeparatorToken;

public class CharFormalParameterNode extends FormalParameterNode {
	
	public CharFormalParameterNode() {
		super();
	}

	public CharFormalParameterNode(TypeSpecKeywordToken dataType,
			VariableIdentifierToken formalParamterIdentifier) {
		super(dataType, formalParamterIdentifier);
	}

	public CharFormalParameterNode(Node parentNode) {
		super(parentNode);
	}

	public CharFormalParameterNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public CharFormalParameterNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		
		TypeSpecKeywordToken dataType = (TypeSpecKeywordToken) tokens.consume();
		VariableIdentifierToken parameterIdentifier = (VariableIdentifierToken) tokens.consume();
		
		FormalParameterNode formalParameterNode = new CharFormalParameterNode(dataType, parameterIdentifier);
		
		formalParameterNode.setParentNode(parent);
		
		if (tokens.lookahead() instanceof SeparatorToken) tokens.consume();
		
		return formalParameterNode;
	}

}
