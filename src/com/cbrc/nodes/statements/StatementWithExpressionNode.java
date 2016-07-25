package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.patterns.expressionpattern.LogicalExpressionPattern;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenGroupingToken;

public abstract class StatementWithExpressionNode extends StatementNode {

	public StatementWithExpressionNode() {
		super();
	}
	
	public StatementWithExpressionNode(KeywordToken mainToken, OpenGroupingToken openParenthesis, ExpressionNode expression, CloseGroupingToken closeParenthesis) {
		super();
		
		mainToken.setParentNode(this);
		openParenthesis.setParentNode(this);
		expression.setParentNode(this);
		closeParenthesis.setParentNode(this);
		
		mainToken.setIndex(0);
		openParenthesis.setIndex(1);
		expression.setIndex(2);
		closeParenthesis.setIndex(3);
		
		this.getChildNodes().add(mainToken);
		this.getChildNodes().add(openParenthesis);
		this.getChildNodes().add(expression);
		this.getChildNodes().add(closeParenthesis);
	}

	public StatementWithExpressionNode(Node parentNode) {
		super(parentNode);
	}

	public StatementWithExpressionNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public StatementWithExpressionNode(Node parentNode,
			ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	public KeywordToken getMainToken() {
		return (KeywordToken) this.getChildNodes().get(0);
	}

	public void setMainToken(KeywordToken mainKeyword) {
		mainKeyword.setParentNode(this);
		this.getChildNodes().set(0, mainKeyword);
	}

	public OpenGroupingToken getOpenParenthesis() {
		return (OpenGroupingToken) this.getChildNodes().get(1);
	}

	public void setOpenParenthesis(OpenGroupingToken openParenthesis) {
		openParenthesis.setParentNode(this);
		this.getChildNodes().set(1, openParenthesis);
	}

	public ExpressionNode getExpression() {
		return (ExpressionNode) this.getChildNodes().get(2);
	}
	
	public LogicalExpressionPattern getExpressionPattern() {
		return (LogicalExpressionPattern) this.getChildNodes().get(2);
	}

	public void setExression(ExpressionNode expression) {
		expression.setParentNode(this);
		this.getChildNodes().set(2, expression);
	}

	public CloseGroupingToken getCloseParenthesis() {
		return (CloseGroupingToken) this.getChildNodes().get(3);
	}

	public void setCloseParenthesis(CloseGroupingToken closeParenthesis) {
		closeParenthesis.setParentNode(this);
		this.getChildNodes().set(3, closeParenthesis);
	}

	@Override
	protected String getAdditionalDetails() {
		if (this.getChildNodes().get(2) instanceof ExpressionNode) {
			return this.getMainToken().getToken() + this.getOpenParenthesis().getToken() + this.getExpression().toString() + this.getCloseParenthesis().getToken();
		}
		else {
			return this.getMainToken().getToken() + this.getOpenParenthesis().getToken() + this.getExpressionPattern().toString() + this.getCloseParenthesis().getToken();
		}
		
	}
	
	/**
	 * Should be used to perform proper token consumption for if,
	 * else if, while and the while part of do while (special
	 * handling for do while, since the exression appears after
	 * the statement body isntead of before)
	 * 
	 * Note: do not go overboard with this function, as special
	 * statements like do while loops have their expressions at
	 * the end of the statment, therefore, do not place statementbody
	 * generation here.
	 * 
	 * @param tokens
	 * @param thisNode
	 */
	protected void handleTokenConsumption(TokenizedCode tokens, StatementWithExpressionNode thisNode) {
		Token mainToken = tokens.consume();
		Token openParenthesis = tokens.consume();
		
		ExpressionNode expression = new ExpressionNode();
		expression = (ExpressionNode) expression.generateThisNode(tokens, thisNode);
		
		Token closeParenthesis = tokens.consume();
		
		thisNode.addChild(mainToken);
		thisNode.addChild(openParenthesis);
		thisNode.addChild(expression);
		thisNode.addChild(closeParenthesis);
	}

}