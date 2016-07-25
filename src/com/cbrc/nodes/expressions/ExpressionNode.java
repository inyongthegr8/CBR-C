package com.cbrc.nodes.expressions;

import java.util.ArrayList;
import java.util.Stack;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenGroupingToken;
import com.cbrc.tokens.operators.SemicolonToken;
import com.cbrc.tokens.operators.SeparatorToken;

public class ExpressionNode extends Node {

	public ExpressionNode() {
		super();
	}

	public ExpressionNode(Node parentNode) {
		super(parentNode);
	}

	public ExpressionNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public ExpressionNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected String getAdditionalDetails() {
		StringBuilder expression = new StringBuilder();
		
		for(Node node: this.getChildNodes()) {
			expression.append(node.toString());
			expression.append(" ");
		}
		
		return expression.toString();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		ExpressionNode expression = new ExpressionNode();
		expression.setParentNode(parent);
		
		Stack<OpenGroupingToken> groupings = new Stack<OpenGroupingToken>();
		while (!isTerminatingToken(tokens.lookahead(), groupings)) {
			Token tokenObject = tokens.consume();
			expression.addChild(tokenObject);
			
			if (tokenObject instanceof OpenGroupingToken) {
				groupings.push((OpenGroupingToken) tokenObject);
			} else if (tokenObject instanceof CloseGroupingToken) {
				groupings.pop();
			}
		}
		return expression;
	}
	
	private boolean isTerminatingToken(Token token, Stack<OpenGroupingToken> groupings) {
		if (token instanceof SemicolonToken) return true;
		else if (token instanceof SeparatorToken) return true;
		else if (token instanceof CloseGroupingToken && groupings.empty()) return true;
		else return false;
	}

	public Object toStringAsC() {
		StringBuilder sb = new StringBuilder();
		for(Node token:this.getChildNodes()) {
			sb.append(((Token) token).getToken());
			sb.append(" ");
		}
		return sb.toString();
	}

}
