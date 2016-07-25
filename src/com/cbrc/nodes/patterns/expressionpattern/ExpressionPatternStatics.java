package com.cbrc.nodes.patterns.expressionpattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.operators.AndOperatorToken;
import com.cbrc.tokens.operators.EqualOperatorToken;
import com.cbrc.tokens.operators.GreaterThanEqualToOperatorToken;
import com.cbrc.tokens.operators.GreaterThanOperatorToken;
import com.cbrc.tokens.operators.LessThanEqualToOperatorToken;
import com.cbrc.tokens.operators.LessThanOperatorToken;
import com.cbrc.tokens.operators.NotEqualOperatorToken;
import com.cbrc.tokens.operators.NotOperatorToken;
import com.cbrc.tokens.operators.OrOperatorToken;

public class ExpressionPatternStatics {

	public static boolean isLogicalExpression (Node node) {
		if (node instanceof ExpressionNode) {
			ArrayList<Node> childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.size(); i++) {
				if (isLogicalOperator(childNodes.get(i))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isLogicalOperator(Node node) {
		if (node instanceof GreaterThanEqualToOperatorToken || node instanceof GreaterThanOperatorToken) {
			return true;
		}
		else if (node instanceof LessThanEqualToOperatorToken || node instanceof LessThanOperatorToken) {
			return true;
		}
		else if (node instanceof NotOperatorToken) {
			return true;
		}
		else if (node instanceof EqualOperatorToken || node instanceof NotEqualOperatorToken) {
			return true;
		}
		else if (node instanceof OrOperatorToken || node instanceof AndOperatorToken) {
			return true;
		}
		else return false;
	}
}
