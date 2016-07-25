package com.cbrc.nodes.patterns.formulapattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.tokens.OperatorToken;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;

public class AccumulatorPatternNode extends FormulaPatternNode {

	public boolean isPattern(Node node) {
		if (node instanceof AssignmentStatementNode) {
			if (node instanceof AssignmentStatementNode) {
				VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
				ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
				
				Node nodeFirst = expression.getChildNodes().get(0);
				if (nodeFirst instanceof VariableIdentifierToken) {
					if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
						Node secondNode = expression.getChildNodes().get(1);
						if (secondNode instanceof OperatorToken) {
							return true;
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
					
					Node nodeFirst = expression.getChildNodes().get(0);
					if (nodeFirst instanceof VariableIdentifierToken) {
						if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
							Node secondNode = expression.getChildNodes().get(1);
							if (secondNode instanceof OperatorToken) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private final String ERROR_0 = "Code is not an assignment statement.";
	private final String ERROR_1 = "Code does not match accumulator pattern. The syntax for the accumulator pattern is [ACCUMULATOR_VAR] = [ACCUMULATOR_VAR] [OPERATOR] [VALUE]";
	private final String ERROR_2 = "Code does not match accumulator pattern. Accumulator variable must be the first variable in the expression.";
	private final String ERROR_3 = "The accumulator variable did not match the initialized variable.";
	
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();
		
		
		if (node instanceof AssignmentStatementNode) {
			VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
			ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
			
			Node nodeFirst = expression.getChildNodes().get(0);
			if (nodeFirst instanceof VariableIdentifierToken) {
				if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
					Node secondNode = expression.getChildNodes().get(1);
					if (!(secondNode instanceof OperatorToken)) {
						messages.add(ERROR_1);
					}
				} else messages.add(ERROR_2);
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
				
				Node nodeFirst = expression.getChildNodes().get(0);
				if (nodeFirst instanceof VariableIdentifierToken) {
					if (((VariableIdentifierToken) nodeFirst).getToken().equals(var.getToken())) {
						Node secondNode = expression.getChildNodes().get(1);
						if (!(secondNode instanceof OperatorToken)) {
							messages.add(ERROR_1);
						}
					} else messages.add(ERROR_2);
				} else messages.add(ERROR_1);
			} else messages.add(ERROR_3);
		} else messages.add(ERROR_0);
		
		
		return messages;
	}
}
