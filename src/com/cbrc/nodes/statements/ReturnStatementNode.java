package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.keywords.ReturnKeywordToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class ReturnStatementNode extends StatementNode {

	public ReturnStatementNode() {
		super();
	}
	
	public ReturnStatementNode(ReturnKeywordToken returnKeyword, ExpressionNode expression, SemicolonToken semicolon) {
		super();
		
		returnKeyword.setParentNode(this);
		expression.setParentNode(this);
		semicolon.setParentNode(this);
		
		returnKeyword.setIndex(0);
		expression.setIndex(1);
		semicolon.setIndex(2);
		
		this.getChildNodes().add(returnKeyword);
		this.getChildNodes().add(expression);
		this.getChildNodes().add(semicolon);
	}

	public ReturnStatementNode(Node parentNode) {
		super(parentNode);
	}

	public ReturnStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public ReturnStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public ReturnKeywordToken getReturnKeyword() {
		return (ReturnKeywordToken) this.getChildNodes().get(0);
	}
	
	public void setReturnKeyword(ReturnKeywordToken returnKeyword) {
		returnKeyword.setParentNode(this);
		this.getChildNodes().set(0, returnKeyword);
	}
	
	public ExpressionNode getExpression() {
		return (ExpressionNode) this.getChildNodes().get(1);
	}
	
	public void setExpression(ExpressionNode expression) {
		expression.setParentNode(this);
		this.getChildNodes().set(1, expression);
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
		return this.getReturnKeyword().getToken() + " Expression: " + this.getExpression().toString();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		ReturnStatementNode returnStatement = new ReturnStatementNode();
		returnStatement.setParentNode(parent);
		
		returnStatement.addChild((ReturnKeywordToken) tokens.consume());
		returnStatement.addChild((ExpressionNode) new ExpressionNode().generateThisNode(tokens, returnStatement));
		returnStatement.addChild((SemicolonToken) tokens.consume());
		
		return returnStatement;
	}

}
