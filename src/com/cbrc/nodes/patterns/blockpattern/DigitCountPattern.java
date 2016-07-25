package com.cbrc.nodes.patterns.blockpattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.patterns.expressionpattern.SentinelValuePattern;
import com.cbrc.nodes.patterns.formulapattern.CounterPatternNode;
import com.cbrc.nodes.patterns.formulapattern.DigitRemovalPattern;
import com.cbrc.nodes.statements.DoWhileStatementNode;
import com.cbrc.nodes.statements.StatementBodyNode;
import com.cbrc.nodes.statements.WhileStatementNode;
import com.cbrc.tokens.identifiers.IntegerVariableIdentifierToken;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;

public class DigitCountPattern extends BlockPattern {

	@Override
	public boolean isPattern(Node node) {
		if (node instanceof WhileStatementNode || node instanceof DoWhileStatementNode) {
			SentinelValuePattern svp = getSentinelValuePattern(node);
			if (svp != null) {
				Node statementBody = getStatementBodyNode(node);
				CounterPatternNode cpn = getCounterPattern(statementBody);
				DigitRemovalPattern drp = getDigitRemovalPattern(statementBody);
				
				if (cpn != null && drp != null) {
					if (checkValidity(svp, drp, cpn)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean checkValidity(SentinelValuePattern svp, DigitRemovalPattern drp, CounterPatternNode cpn) {
		int sentinelValue = this.getSentinelValue(svp);
		
		if (sentinelValue == 0) {
			int drValue = this.getDigitRemovalValue(drp);
			if (drValue == 10) {
				int counterValue = this.getCounterValue(cpn);
				if (counterValue == 1) {
					String sentinelToken = this.getSentinelToken(svp);
					String drpToken = this.getDRPToken(drp);
					
					if (sentinelToken.equals(drpToken)) {
						return true;
					}
				}
			}
			
			
		}
		
		return false;
	}
	
	private String getDRPToken(DigitRemovalPattern drp) {
		Node variable = drp.getChildNodes().get(0);
		if (variable instanceof VariableIdentifierToken){
			return ((VariableIdentifierToken) variable).getToken();
		}
		return null;
	}

	private int getCounterValue(CounterPatternNode cpn) {
		Node child = cpn.getChildNodes().get(2);
		if (child instanceof ExpressionNode) {
			for (Node child2:child.getChildNodes()) {
				if (child2 instanceof IntegerLiteralToken) {
					return ((IntegerLiteralToken) child2).getValueAsInt();
				}
			}
		}
		return -1;
	}

	private int getDigitRemovalValue(DigitRemovalPattern drp) {
		Node child = drp.getChildNodes().get(2);
		if (child instanceof ExpressionNode) {
			for (Node child2:child.getChildNodes()) {
				if (child2 instanceof IntegerLiteralToken) {
					return ((IntegerLiteralToken) child2).getValueAsInt();
				}
			}
		}
		return -1;
	}

	private int getSentinelValue(SentinelValuePattern svp) {
		for (Node child:svp.getChildNodes()) {
			if (child instanceof IntegerLiteralToken) {
				return ((IntegerLiteralToken) child).getValueAsInt();
			}
		}
		return -1;
	}

	private String getSentinelToken (SentinelValuePattern svp) {
		for (Node child:svp.getChildNodes()) {
			if (child instanceof IntegerVariableIdentifierToken) {
				return ((IntegerVariableIdentifierToken) child).getToken();
			}
		}
		return "";
	}

	private DigitRemovalPattern getDigitRemovalPattern(Node statementBody) {
		for (Node child:statementBody.getChildNodes()) {
			if (child instanceof DigitRemovalPattern) {
				return (DigitRemovalPattern) child;
			}
		}
		return null;
	}

	private CounterPatternNode getCounterPattern(Node statementBody) {
		for (Node child:statementBody.getChildNodes()) {
			if (child instanceof CounterPatternNode) {
				return (CounterPatternNode) child;
			}
		}
		return null;
	}

	private SentinelValuePattern getSentinelValuePattern(Node node) {
		for (Node child:node.getChildNodes()) {
			if (child instanceof SentinelValuePattern) {
				return (SentinelValuePattern) child;
			}
		}
		return null;
	}

	private Node getStatementBodyNode(Node node) {
		for (Node child:node.getChildNodes()) {
			if (child instanceof StatementBodyNode) {
				return child;
			}
		}
		return null;
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		// TODO Auto-generated method stub
		return null;
	}

	private final String ERROR_0 = "Code is not a while statement.";
	private final String ERROR_1 = "Code does not match digit count pattern. Your digit removal, sentinel value or counter pattern might be invalid. Please refer to the specific section in the document for more details.";
	private final String ERROR_2 = "Code does not match digit count patter. Digit count makes use of sentinel pattern as its terminating condition.";
	private final String ERROR_3 = "Code does not match digit count patter. Digit count makes use of digit removal and counter pattern to count the number of digits.";
	
	@Override
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();
		if (node instanceof WhileStatementNode || node instanceof DoWhileStatementNode) {
			SentinelValuePattern svp = getSentinelValuePattern(node);
			if (svp != null) {
				Node statementBody = getStatementBodyNode(node);
				CounterPatternNode cpn = getCounterPattern(statementBody);
				DigitRemovalPattern drp = getDigitRemovalPattern(statementBody);
				
				if (cpn != null && drp != null) {
					if (!checkValidity(svp, drp, cpn)) {
						messages.add(ERROR_1);
					}
				} else messages.add(ERROR_3);
			} else messages.add(ERROR_2);
		} else messages.add(ERROR_0);
		return messages;
	}

}
