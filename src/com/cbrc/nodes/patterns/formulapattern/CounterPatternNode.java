package com.cbrc.nodes.patterns.formulapattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;
import com.cbrc.tokens.operators.MinusOperatorToken;
import com.cbrc.tokens.operators.PlusOperatorToken;

public class CounterPatternNode extends FormulaPatternNode {
	
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
							if (secondNode instanceof PlusOperatorToken || secondNode instanceof MinusOperatorToken) {
								Node thirdNode = expression.getChildNodes().get(2);
								if (thirdNode instanceof IntegerLiteralToken) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isPatternWithInit(Node node, Token initVar) {
		if (node instanceof AssignmentStatementNode) {
			if (node instanceof AssignmentStatementNode) {
				VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
				if (var.getToken().equals(initVar.getToken())) {
					ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
					
					if (expression.getChildNodes().size() == 3) {
						Node nodeFirst = expression.getChildNodes().get(0);
						if (nodeFirst instanceof VariableIdentifierToken) {
							if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
								Node secondNode = expression.getChildNodes().get(1);
								if (secondNode instanceof PlusOperatorToken || secondNode instanceof MinusOperatorToken) {
									Node thirdNode = expression.getChildNodes().get(2);
									if (thirdNode instanceof IntegerLiteralToken) {
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
	private final String ERROR_1 = "Code does not match a counter pattern. The syntax for the counter pattern is [COUNTER_VAR] = [COUNTER_VAR] [PLUS OR MINUS] [VALUE]";
	private final String ERROR_2 = "The counter variable did not match the initialized variable.";
	
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
						if (secondNode instanceof PlusOperatorToken || secondNode instanceof MinusOperatorToken) {
							Node thirdNode = expression.getChildNodes().get(2);
							if (!(thirdNode instanceof IntegerLiteralToken)) {
								messages.add(ERROR_1);
							}
						} else messages.add(ERROR_1);
					} else messages.add(ERROR_1);
				} else messages.add(ERROR_1);
			} else messages.add(ERROR_1);
		} else messages.add(ERROR_0);
		
		return messages;
	}
	
	public ArrayList<String> isPatternWithFeedbackWithInit(Node node, Token initVar) {
		ArrayList<String> messages = new ArrayList<String>();
		
		if (node instanceof AssignmentStatementNode) {
			VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
			if (var.getToken().equals(initVar.getToken())) {
				ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
				
				if (expression.getChildNodes().size() == 3) {
					Node nodeFirst = expression.getChildNodes().get(0);
					if (nodeFirst instanceof VariableIdentifierToken) {
						if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
							Node secondNode = expression.getChildNodes().get(1);
							if (secondNode instanceof PlusOperatorToken || secondNode instanceof MinusOperatorToken) {
								Node thirdNode = expression.getChildNodes().get(2);
								if (!(thirdNode instanceof IntegerLiteralToken)) {
									messages.add(ERROR_1);
								}
							} else messages.add(ERROR_1);
						} else messages.add(ERROR_1);
					} else messages.add(ERROR_1);
				} else messages.add(ERROR_1);
			} else messages.add(ERROR_2);
			
		} else messages.add(ERROR_0);
		
		return messages;
	}

}
