package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.DefaultKeywordToken;
import com.cbrc.tokens.operators.ColonToken;

public class DefaultStatementNode extends StatementNode {

	public DefaultStatementNode() {
		super();
	}
	
	public DefaultStatementNode(DefaultKeywordToken defaultKeyword, ColonToken colon) {
		defaultKeyword.setParentNode(this);
		colon.setParentNode(this);
		
		defaultKeyword.setIndex(0);
		colon.setIndex(1);
		
		this.addChild(defaultKeyword);
		this.addChild(colon);
	}

	public DefaultStatementNode(Node parentNode) {
		super(parentNode);
	}

	public DefaultStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public DefaultStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public DefaultKeywordToken getDefaultKeyword() {
		return (DefaultKeywordToken) this.getChildNodes().get(0);
	}
	
	public void setDefaultKeyword(DefaultKeywordToken defaultKeyword) {
		defaultKeyword.setParentNode(this);
		this.getChildNodes().set(0, defaultKeyword);
	}
	
	public ColonToken getColon() {
		return (ColonToken) this.getChildNodes().get(1);
	}
	
	public void setColon(ColonToken colon) {
		colon.setParentNode(this);
		this.getChildNodes().set(1, colon);
	}

	@Override
	protected String getAdditionalDetails() {
		return "Default statement";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		DefaultStatementNode statementNode = new DefaultStatementNode();
		statementNode.setParentNode(parent);
		
		statementNode.addChild((DefaultKeywordToken) tokens.consume());
		statementNode.addChild((ColonToken) tokens.consume());
		
		//reusing case statement body node because of similar functionality
		statementNode.addChild(new CaseStatementBodyNode().generateThisNode(tokens, statementNode));
		
		return statementNode;
	}

}
