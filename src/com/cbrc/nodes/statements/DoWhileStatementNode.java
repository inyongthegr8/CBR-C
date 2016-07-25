package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.dummy.DummyNode;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.keywords.DoWhileKeywordToken;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenBlockToken;
import com.cbrc.tokens.operators.OpenGroupingToken;
import com.cbrc.tokens.operators.SemicolonToken;
import com.cbrc.tokens.operators.enums.GroupingType;

public class DoWhileStatementNode extends StatementWithExpressionNode {

	public DoWhileStatementNode() {
		super();
	}
	
	public DoWhileStatementNode(KeywordToken mainToken,
			OpenGroupingToken openParenthesis, ExpressionNode expression,
			CloseGroupingToken closeParenthesis) {
		super(mainToken, openParenthesis, expression, closeParenthesis);
		this.getOpenParenthesis().setGroupingType(GroupingType.LOOP_EXPRESSION);
		this.getCloseParenthesis().setGroupingType(GroupingType.LOOP_EXPRESSION);
	}

	public DoWhileStatementNode(Node parentNode) {
		super(parentNode);
	}

	public DoWhileStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public DoWhileStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		DoWhileStatementNode statementNode = new DoWhileStatementNode();
		statementNode.setParentNode(parent);
		
		this.padElements(statementNode);
		
		tokens.consume();
		//statementNode.addChild(tokens.consume());

		Node statementBodyNode;
		if (tokens.lookahead() instanceof OpenBlockToken) {
			//consumes the openblock token
			tokens.consume();
			
			statementBodyNode = new StatementBodyNode().generateThisNode(tokens, statementNode);
			
			//consumes the closeblock token
			tokens.consume();
		} else {
			statementBodyNode = new StatementBodyNode().generateThisNode(tokens, statementNode);
			
			// in case the last semicolon of the statement is not consumed, it would be consumed here
			if (tokens.lookahead() instanceof SemicolonToken) tokens.consume();
		}
		
		this.handleTokenConsumption(tokens, statementNode);
		this.repositionElements(statementNode);
		statementNode.setMainToken(new DoWhileKeywordToken());
		if (tokens.lookahead() instanceof SemicolonToken) tokens.consume();
		
		statementNode.addChild(statementBodyNode);
		
		return statementNode;
	}

	private void repositionElements(DoWhileStatementNode statementNode) {
		Node closeParenthesis = statementNode.getChildNodes().remove(statementNode.getChildNodes().size() - 1);
		Node expression = statementNode.getChildNodes().remove(statementNode.getChildNodes().size() - 1);
		Node openParenthesis = statementNode.getChildNodes().remove(statementNode.getChildNodes().size() - 1);
		Node mainToken = statementNode.getChildNodes().remove(statementNode.getChildNodes().size() - 1);
		
		statementNode.replaceChildAtIndex(mainToken, 0);
		statementNode.replaceChildAtIndex(openParenthesis, 1);
		statementNode.replaceChildAtIndex(expression, 2);
		statementNode.replaceChildAtIndex(closeParenthesis, 3);
		
	}

	private void padElements(DoWhileStatementNode statementNode) {
		statementNode.addChild(new DummyNode(null));
		statementNode.addChild(new DummyNode(null));
		statementNode.addChild(new DummyNode(null));
		statementNode.addChild(new DummyNode(null));
	}

}
