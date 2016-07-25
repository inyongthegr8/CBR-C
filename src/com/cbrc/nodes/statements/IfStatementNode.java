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

/** 
 * Models an if statement. From here, the only constant
 * inclusion are the IfKeywordToken, an OpenGroupingToken,
 * an ExpressionNode and a CloseGroupingToken and a 
 * StatementBodyNode. Whether the StatementBodyNode is
 * preceded and followed by a BlockTokens or if it
 * ends with a SemicolonToken depends on the If statement.
 * 
 * @author Alice
 *
 */
public class IfStatementNode extends StatementWithExpressionNode {

	public IfStatementNode() {
		super();
	}
	
	public IfStatementNode(KeywordToken mainToken, OpenGroupingToken openParenthesis, ExpressionNode expression, CloseGroupingToken closeParenthesis) {
		super(mainToken, openParenthesis, expression, closeParenthesis);
		this.getOpenParenthesis().setGroupingType(GroupingType.IF_EXPRESSION);
		this.getCloseParenthesis().setGroupingType(GroupingType.IF_EXPRESSION);
	}

	public IfStatementNode(Node parentNode) {
		super(parentNode);
	}

	public IfStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public IfStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		IfStatementNode statementNode = new IfStatementNode();
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
