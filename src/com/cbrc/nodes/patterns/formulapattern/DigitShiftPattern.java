package com.cbrc.nodes.patterns.formulapattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;
import com.cbrc.tokens.operators.MultiplyOperatorToken;

public class DigitShiftPattern extends FormulaPatternNode {
	
	public boolean isPattern(Node node) {
		if (node instanceof AssignmentStatementNode) {
			if (node instanceof AssignmentStatementNode) {
				VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
				ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
				
				if (expression.getChildNodes().size() == 3) {
					Node nodeFirst = expression.getChildNodes().get(0);
					if (nodeFirst instanceof VariableIdentifierToken) {
						if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
							Node secondNode = expression.getChildNodes().get(1);
							if (secondNode instanceof MultiplyOperatorToken) {
								Node thirdNode = expression.getChildNodes().get(2);
								if (thirdNode instanceof IntegerLiteralToken) {
									int numLiteral = ((IntegerLiteralToken) thirdNode).getValueAsInt();
									double log10 = Math.log10(numLiteral);
									
									if ((log10 == Math.floor(log10)) && !Double.isInfinite(log10)) {
									    return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private final String ERROR_0 = "Code is not an assignment statement.";
	private final String ERROR_1 = "Code does not match digit shift pattern. The syntax for the digit shift pattern is [VARIABLE] = [NUMBER_VAR] * [POWER_OF_TEN_LITERAL].";
	private final String ERROR_2 = "Code does not match digit shift pattern. Number to apply multiplication on must be a power of 10.";
	private final String ERROR_3 = "Code does not match digit shift pattern. Operation must be multiplication (*).";
	
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();
		
		
		if (node instanceof AssignmentStatementNode) {
			VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
			ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
			
			if (expression.getChildNodes().size() == 3) {
				Node nodeFirst = expression.getChildNodes().get(0);
				if (nodeFirst instanceof VariableIdentifierToken) {
					if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
						Node secondNode = expression.getChildNodes().get(1);
						if (secondNode instanceof MultiplyOperatorToken) {
							Node thirdNode = expression.getChildNodes().get(2);
							if (thirdNode instanceof IntegerLiteralToken) {
								int numLiteral = ((IntegerLiteralToken) thirdNode).getValueAsInt();
								double log10 = Math.log10(numLiteral);
								
								if (!((log10 == Math.floor(log10)) && !Double.isInfinite(log10))) {
								    messages.add(ERROR_2);
								}
							} else messages.add(ERROR_2);
						} else messages.add(ERROR_3);
					}  else messages.add(ERROR_1);
				}  else messages.add(ERROR_1);
			}  else messages.add(ERROR_1);
		} else messages.add(ERROR_0);
		
		return messages;
	}
	
}
