package com.cbrc.nodes.functions.body;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class CharVariableDeclarationNode extends VariableDeclarationNode {
	
	public CharVariableDeclarationNode() {
		super();
	}

	public CharVariableDeclarationNode(TypeSpecKeywordToken dataType,
			VariableIdentifierToken variableIdentifier, SemicolonToken semicolon) {
		super(dataType, variableIdentifier, semicolon);
	}

	public CharVariableDeclarationNode(Node parentNode) {
		super(parentNode);
	}

	public CharVariableDeclarationNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public CharVariableDeclarationNode(Node parentNode,
			ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		TypeSpecKeywordToken dataType = (TypeSpecKeywordToken) tokens.consume();
		VariableIdentifierToken variableIdentifier = (VariableIdentifierToken) tokens.consume();
		SemicolonToken semicolon = (SemicolonToken) tokens.consume();
		
		VariableDeclarationNode variableDeclarationNode = new CharVariableDeclarationNode(dataType, variableIdentifier, semicolon);
		variableDeclarationNode.setParentNode(parent);
		
		return variableDeclarationNode;
	}

}
