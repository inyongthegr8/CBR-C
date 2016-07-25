package com.cbrc.nodes.functions.body;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class LongVariableDeclarationNode extends VariableDeclarationNode {
	
	public LongVariableDeclarationNode() {
		super();
	}

	public LongVariableDeclarationNode(TypeSpecKeywordToken dataType,
			VariableIdentifierToken variableIdentifier, SemicolonToken semicolon) {
		super(dataType, variableIdentifier, semicolon);
	}

	public LongVariableDeclarationNode(Node parentNode) {
		super(parentNode);
	}

	public LongVariableDeclarationNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public LongVariableDeclarationNode(Node parentNode,
			ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		TypeSpecKeywordToken dataType = (TypeSpecKeywordToken) tokens.consume();
		VariableIdentifierToken variableIdentifier = (VariableIdentifierToken) tokens.consume();
		SemicolonToken semicolon = (SemicolonToken) tokens.consume();
		
		VariableDeclarationNode variableDeclarationNode = new LongVariableDeclarationNode(dataType, variableIdentifier, semicolon);
		variableDeclarationNode.setParentNode(parent);
		
		return variableDeclarationNode;
	}

}
