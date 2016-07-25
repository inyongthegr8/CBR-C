package com.cbrc.nodes.patterns.formulapattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;
import com.cbrc.tokens.operators.MultiplyOperatorToken;
import com.cbrc.tokens.operators.PlusOperatorToken;

public class DigitAppendPattern extends FormulaPatternNode {

	public boolean isPattern(Node node) {
		if (node instanceof AssignmentStatementNode) {
			if (node instanceof AssignmentStatementNode) {
				VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
				ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
				
				if (expression.getChildNodes().size() == 5) {
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
									    Node fourthNode = expression.getChildNodes().get(3);
									    if (fourthNode instanceof PlusOperatorToken) {
									    	Node fifthNode = expression.getChildNodes().get(4);
									    	if (fifthNode instanceof IntegerLiteralToken || fifthNode instanceof VariableIdentifierToken) {
									    		return true;
									    	}
									    }
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
	private final String ERROR_1 = "Code does not match digit append pattern. The syntax for the digit append pattern is [NUMBER_VAR] = [NUMBER_VAR] * [POWER_OF_TEN_LITERAL] + [DIGIT].";
	private final String ERROR_2 = "Code does not match digit append pattern. Statement must end in the digit/digits you want to append.";
	private final String ERROR_3 = "Code does not match digit append pattern. Add the digits you want to append after multiplying the original number by a power of 10.";
	private final String ERROR_4 = "Code does not match digit append pattern. Make sure the original number is multiplied by a power of 10 first before adding the digits to append.";
	
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();
		
		
		if (node instanceof AssignmentStatementNode) {
			VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
			ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
			
			if (expression.getChildNodes().size() == 5) {
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
								    Node fourthNode = expression.getChildNodes().get(3);
								    if (fourthNode instanceof PlusOperatorToken) {
								    	Node fifthNode = expression.getChildNodes().get(4);
								    	if (!(fifthNode instanceof IntegerLiteralToken || fifthNode instanceof VariableIdentifierToken)) {
								    		messages.add(ERROR_2);
								    	}
								    } else messages.add(ERROR_3);
								} else messages.add(ERROR_4);
							} else messages.add(ERROR_4);
						} else messages.add(ERROR_4);
					} else messages.add(ERROR_1);
				} else messages.add(ERROR_1);
			} else messages.add(ERROR_1);
		} else messages.add(ERROR_0);
		
		return messages;
	}
	
}
