package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.operators.OpenBlockToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class ElseStatementNode extends StatementNode {

	public ElseStatementNode() {
		super();
	}
	
	public ElseStatementNode(KeywordToken mainToken) {
		super();
		mainToken.setParentNode(this);
		mainToken.setIndex(0);
		this.getChildNodes().add(mainToken);
	}

	public ElseStatementNode(Node parentNode) {
		super(parentNode);
	}

	public ElseStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public ElseStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public KeywordToken getMainToken() {
		return (KeywordToken) this.getChildNodes().get(0);
	}

	public void setMainToken(KeywordToken mainKeyword) {
		mainKeyword.setParentNode(this);
		this.getChildNodes().set(0, mainKeyword);
	}

	@Override
	protected String getAdditionalDetails() {
		return this.getMainToken().getToken();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		ElseStatementNode elseStatementNode = new ElseStatementNode();
		elseStatementNode.setParentNode(parent);
		
		elseStatementNode.addChild((KeywordToken) tokens.consume());
		
		if (tokens.lookahead() instanceof OpenBlockToken) {
			//consumes the openblock token
			tokens.consume();
			
			elseStatementNode.addChild(new StatementBodyNode().generateThisNode(tokens, elseStatementNode));
			
			//consumes the closeblock token
			tokens.consume();
		} else {
			elseStatementNode.addChild(new StatementBodyNode().generateThisNode(tokens, elseStatementNode));
			
			// in case the last semicolon of the statement is not consumed, it would be consumed here
			if (tokens.lookahead() instanceof SemicolonToken) tokens.consume();
		}
		
		return elseStatementNode;
	}

}
