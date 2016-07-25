package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.KeywordToken;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenGroupingToken;
import com.cbrc.tokens.operators.enums.GroupingType;

public class SwitchStatementNode extends StatementWithExpressionNode {

	public SwitchStatementNode() {
		super();
	}

	public SwitchStatementNode(KeywordToken mainToken,
			OpenGroupingToken openParenthesis, ExpressionNode expression,
			CloseGroupingToken closeParenthesis) {
		super(mainToken, openParenthesis, expression, closeParenthesis);
		this.getOpenParenthesis().setGroupingType(GroupingType.SWITCH_EXPRESSION);
		this.getCloseParenthesis().setGroupingType(GroupingType.SWITCH_EXPRESSION);
	}

	public SwitchStatementNode(Node parentNode) {
		super(parentNode);
	}

	public SwitchStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public SwitchStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		SwitchStatementNode statementNode = new SwitchStatementNode();
		statementNode.setParentNode(parent);
		
		this.handleTokenConsumption(tokens, statementNode);
		
		//consumes the openblock token
		tokens.consume();
		
		statementNode.addChild(new SwitchStatementBodyNode().generateThisNode(tokens, statementNode));
		
		//consumes the closeblock token
		tokens.consume();
		
		return statementNode;
	}

}
