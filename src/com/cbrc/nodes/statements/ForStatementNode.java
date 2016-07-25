package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenBlockToken;
import com.cbrc.tokens.operators.OpenGroupingToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class ForStatementNode extends StatementNode {
	
	public ForStatementNode() {
		super();
	}

	public ForStatementNode(KeywordToken mainToken, OpenGroupingToken openParenthesis, AssignmentStatementNode intialization, ExpressionNode expression, AssignmentStatementNode incerement, CloseGroupingToken closeParenthesis) {
		super();
		
		mainToken.setParentNode(this);
		openParenthesis.setParentNode(this);
		intialization.setParentNode(this);
		expression.setParentNode(this);
		incerement.setParentNode(this);
		closeParenthesis.setParentNode(this);
		
		mainToken.setIndex(0);
		openParenthesis.setIndex(1);
		intialization.setIndex(2);
		expression.setIndex(3);
		incerement.setIndex(4);
		closeParenthesis.setIndex(5);
		
		this.getChildNodes().add(mainToken);
		this.getChildNodes().add(openParenthesis);
		this.getChildNodes().add(intialization);
		this.getChildNodes().add(expression);
		this.getChildNodes().add(incerement);
		this.getChildNodes().add(closeParenthesis);
	}

	public ForStatementNode(Node parentNode) {
		super(parentNode);
	}

	public ForStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public ForStatementNode(Node parentNode, ArrayList<Node> childNodes) {
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
	
	public AssignmentStatementNode getInitialization() {
		return (AssignmentStatementNode) this.getChildNodes().get(2);
	}
	
	public void setInitialization(AssignmentStatementNode initialization) {
		initialization.setParentNode(this);
		this.getChildNodes().set(2, initialization);
	}

	public ExpressionNode getExpression() {
		return (ExpressionNode) this.getChildNodes().get(3);
	}

	public void setExression(ExpressionNode expression) {
		expression.setParentNode(this);
		this.getChildNodes().set(3, expression);
	}
	
	public AssignmentStatementNode getIncrement() {
		return (AssignmentStatementNode) this.getChildNodes().get(4);
	}
	
	public void setIncrement(AssignmentStatementNode increment) {
		increment.setParentNode(this);
		this.getChildNodes().set(4, increment);
	}

	public CloseGroupingToken getCloseParenthesis() {
		return (CloseGroupingToken) this.getChildNodes().get(5);
	}

	public void setCloseParenthesis(CloseGroupingToken closeParenthesis) {
		closeParenthesis.setParentNode(this);
		this.getChildNodes().set(5, closeParenthesis);
	}

	@Override
	protected String getAdditionalDetails() {
		return this.getMainToken().getToken() + this.getOpenParenthesis() + "Initialization: " + this.getInitialization().toString() + "; Expression: " + this.getExpression().toString() + "; Increment: " + this.getIncrement().toString() + this.getCloseParenthesis().getToken();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		ForStatementNode forStatementNode = new ForStatementNode();
		forStatementNode.setParentNode(parent);
		
		forStatementNode.addChild((KeywordToken) tokens.consume());
		forStatementNode.addChild((OpenGroupingToken) tokens.consume());
		
		if (!(tokens.lookahead() instanceof SemicolonToken)) {
			AssignmentStatementNode initializationNode = new AssignmentStatementNode();
			forStatementNode.addChild((AssignmentStatementNode) initializationNode.generateThisNode(tokens, forStatementNode));
		}
		// Consumes the first semicolon
		tokens.consume();
		
		if (!(tokens.lookahead() instanceof SemicolonToken)) {
			ExpressionNode expressionNode = new ExpressionNode();
			forStatementNode.addChild((ExpressionNode) expressionNode.generateThisNode(tokens, forStatementNode));
		}
		// Consumes the second semicolon
		tokens.consume();
		
		if (!(tokens.lookahead() instanceof CloseGroupingToken)) {
			AssignmentStatementNode incrementNode = new AssignmentStatementNode();
			forStatementNode.addChild((AssignmentStatementNode) incrementNode.generateThisNode(tokens, forStatementNode));
		}
		
		forStatementNode.addChild((CloseGroupingToken) tokens.consume());
		
		if (tokens.lookahead() instanceof OpenBlockToken) {
			//consumes the openblock token
			tokens.consume();
			
			forStatementNode.addChild(new StatementBodyNode().generateThisNode(tokens, forStatementNode));
			
			//consumes the closeblock token
			tokens.consume();
		} else {
			forStatementNode.addChild(new StatementBodyNode().generateThisNode(tokens, forStatementNode));
			
			// in case the last semicolon of the statement is not consumed, it would be consumed here
			if (tokens.lookahead() instanceof SemicolonToken) tokens.consume();
		}
		
		return forStatementNode;
	}

}
