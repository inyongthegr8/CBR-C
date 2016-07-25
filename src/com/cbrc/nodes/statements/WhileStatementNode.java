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
import com.cbrc.tokens.operators.enums.GroupingType;

public class WhileStatementNode extends StatementWithExpressionNode {

	public WhileStatementNode() {
		super();
	}
	
	public WhileStatementNode(KeywordToken mainToken,
			OpenGroupingToken openParenthesis, ExpressionNode expression,
			CloseGroupingToken closeParenthesis) {
		super(mainToken, openParenthesis, expression, closeParenthesis);
		this.getOpenParenthesis().setGroupingType(GroupingType.LOOP_EXPRESSION);
		this.getCloseParenthesis().setGroupingType(GroupingType.LOOP_EXPRESSION);
	}

	public WhileStatementNode(Node parentNode) {
		super(parentNode);
	}

	public WhileStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public WhileStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		WhileStatementNode statementNode = new WhileStatementNode();
		statementNode.setParentNode(parent);
		
		this.handleTokenConsumption(tokens, statementNode);

		if (tokens.lookahead() instanceof OpenBlockToken) {
			//consumes the openblock token
			tokens.consume();
			
			statementNode.addChild(new StatementBodyNode().generateThisNode(tokens, statementNode));
			
			//consumes the closeblock token
			tokens.consume();
		} else {
			statementNode.addChild(new StatementBodyNode().generateThisNode(tokens, statementNode));
			
			// in case the last semicolon of the statement is not consumed, it would be consumed here
			if (tokens.lookahead() instanceof SemicolonToken) tokens.consume();
		}
		
		return statementNode;
	}

}
