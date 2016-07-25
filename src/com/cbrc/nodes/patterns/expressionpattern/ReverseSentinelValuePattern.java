package com.cbrc.nodes.patterns.expressionpattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.tokens.LiteralToken;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.operators.EqualOperatorToken;

public class ReverseSentinelValuePattern extends LogicalExpressionPattern {
	public boolean isPattern(Node node) {
		if (ExpressionPatternStatics.isLogicalExpression(node)){
			if (node.getChildNodes().size() == 3) {
				Node firstNode = node.getChildNodes().get(0);
				if (firstNode instanceof VariableIdentifierToken) {
					Node secondNode = node.getChildNodes().get(1);
					if (secondNode instanceof EqualOperatorToken) {
						Node thirdNode = node.getChildNodes().get(2);
						if(thirdNode instanceof LiteralToken) {
							return true;
						}
					}
				} else if (firstNode instanceof LiteralToken) {
					Node secondNode = node.getChildNodes().get(1);
					if (secondNode instanceof EqualOperatorToken) {
						Node thirdNode = node.getChildNodes().get(2);
						if(thirdNode instanceof VariableIdentifierToken) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private final String ERROR_0 = "Code is not a logical expression.";
	private final String ERROR_1 = "Code does not match sentinel value pattern. The syntax for the sentinel value pattern is [SENTINEL_VAR] == [SENTINEL_VAL_LITERAL].";
	
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();
		
		if (ExpressionPatternStatics.isLogicalExpression(node)){
			if (node.getChildNodes().size() == 3) {
				Node firstNode = node.getChildNodes().get(0);
				if (firstNode instanceof VariableIdentifierToken) {
					Node secondNode = node.getChildNodes().get(1);
					if (secondNode instanceof EqualOperatorToken) {
						Node thirdNode = node.getChildNodes().get(2);
						if(!(thirdNode instanceof LiteralToken)) {
							messages.add(ERROR_0);
						}
					} else messages.add(ERROR_1);
				} else if (firstNode instanceof LiteralToken) {
					Node secondNode = node.getChildNodes().get(1);
					if (secondNode instanceof EqualOperatorToken) {
						Node thirdNode = node.getChildNodes().get(2);
						if(!(thirdNode instanceof VariableIdentifierToken)) {
							messages.add(ERROR_0);
						}
					} else messages.add(ERROR_1);
				} else messages.add(ERROR_1);
			} else messages.add(ERROR_1);
		} else messages.add(ERROR_0);
		
		return messages;
	}
}
