package com.cbrc.nodes.patterns.formulapattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;

public class InitializeToZeroPattern extends FormulaPatternNode {
	
	private VariableIdentifierToken initVarToken;

	public boolean isPattern(Node node) {
		if (node instanceof AssignmentStatementNode) {
			if (node instanceof AssignmentStatementNode) {
				VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
				this.setInitVarToken(var);
				ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
				
				if (expression.getChildNodes().size() == 1) {
					Node nodeFirst = expression.getChildNodes().get(0);
					if (nodeFirst instanceof IntegerLiteralToken) {
						if (((IntegerLiteralToken) nodeFirst).getToken().equals("0")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private final String ERROR_0 = "Code is not an assignment statement.";
	private final String ERROR_1 = "Code does not does not initialize the variable. To initialize a variable, assign a value to it.";
	private final String ERROR_2 = "Code does not does not initialize the variable to 0. To initialize a variable to 0, assign 0 to it.";
	
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();
		
		if (node instanceof AssignmentStatementNode) {
			VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
			this.setInitVarToken(var);
			ExpressionNode expression = ((AssignmentStatementNode) node).getExpression();
			
			if (expression.getChildNodes().size() == 1) {
				Node nodeFirst = expression.getChildNodes().get(0);
				if (nodeFirst instanceof IntegerLiteralToken) {
					if (!((IntegerLiteralToken) nodeFirst).getToken().equals("0")) {
						messages.add(ERROR_2);
					}
				} else messages.add(ERROR_1);
			}
		} else messages.add(ERROR_0);
		
		return messages;
	}

	public VariableIdentifierToken getInitVarToken() {
		return initVarToken;
	}

	public void setInitVarToken(VariableIdentifierToken initVarToken) {
		this.initVarToken = initVarToken;
	}
	
	
}
