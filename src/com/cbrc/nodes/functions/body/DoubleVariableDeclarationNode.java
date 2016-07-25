package com.cbrc.nodes.functions.body;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class DoubleVariableDeclarationNode extends VariableDeclarationNode {

	public DoubleVariableDeclarationNode() {
		super();
	}
	
	public DoubleVariableDeclarationNode(TypeSpecKeywordToken dataType,
			VariableIdentifierToken variableIdentifier, SemicolonToken semicolon) {
		super(dataType, variableIdentifier, semicolon);
	}

	public DoubleVariableDeclarationNode(Node parentNode) {
		super(parentNode);
	}

	public DoubleVariableDeclarationNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public DoubleVariableDeclarationNode(Node parentNode,
			ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		TypeSpecKeywordToken dataType = (TypeSpecKeywordToken) tokens.consume();
		VariableIdentifierToken variableIdentifier = (VariableIdentifierToken) tokens.consume();
		SemicolonToken semicolon = (SemicolonToken) tokens.consume();
		
		VariableDeclarationNode variableDeclarationNode = new DoubleVariableDeclarationNode(dataType, variableIdentifier, semicolon);
		variableDeclarationNode.setParentNode(parent);
		
		return variableDeclarationNode;
	}

}
