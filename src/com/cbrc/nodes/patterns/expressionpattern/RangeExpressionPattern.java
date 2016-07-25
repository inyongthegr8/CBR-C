package com.cbrc.nodes.patterns.expressionpattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.tokens.LiteralToken;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.literals.CharacterLiteralToken;
import com.cbrc.tokens.literals.FloatLiteralToken;
import com.cbrc.tokens.literals.IntegerLiteralToken;
import com.cbrc.tokens.operators.AndOperatorToken;
import com.cbrc.tokens.operators.GreaterThanEqualToOperatorToken;
import com.cbrc.tokens.operators.GreaterThanOperatorToken;
import com.cbrc.tokens.operators.LessThanEqualToOperatorToken;
import com.cbrc.tokens.operators.LessThanOperatorToken;

public class RangeExpressionPattern extends LogicalExpressionPattern {
	
	public boolean isPattern(Node node) {
		if (ExpressionPatternStatics.isLogicalExpression(node)){
			if (node.getChildNodes().size() == 7) {
				Node firstNode = node.getChildNodes().get(0);
				Node relational1 = node.getChildNodes().get(1);
				Node thirdNode = node.getChildNodes().get(2);
				Node andNode = node.getChildNodes().get(3);
				Node fifthNode = node.getChildNodes().get(4);
				Node relational2 = node.getChildNodes().get(5);
				Node seventhNode = node.getChildNodes().get(6);
				
				VariableIdentifierToken variableNode = getVariableNode(firstNode, thirdNode, fifthNode, seventhNode);
				LiteralToken upperBound;
				LiteralToken lowerBound;
				
				if (variableNode != null) {
					if (sameType(firstNode, thirdNode, fifthNode, seventhNode, variableNode)) {
						
						upperBound = getUpperBound(firstNode, thirdNode, fifthNode, seventhNode);
						lowerBound = getLowerBound(firstNode, thirdNode, fifthNode, seventhNode);
						
						String firstToken = ((Token) firstNode).getToken();
						String secondToken = ((Token) thirdNode).getToken();
						String thirdToken = ((Token) fifthNode).getToken();
						String fourthToken = ((Token) seventhNode).getToken();
						String variableToken = variableNode.getToken();
						String upperBoundToken = upperBound.getToken();
						String lowerBoundToken = lowerBound.getToken();
						
						if (andNode instanceof AndOperatorToken) {
							if (checkProperUpperBound(relational1, relational2, firstToken, secondToken, thirdToken, fourthToken, upperBoundToken, variableToken) && 
								checkProperLowerBound(relational1, relational2, firstToken, secondToken, thirdToken, fourthToken, lowerBoundToken, variableToken)) {
								return true;
							}
						}
					}
				}
			}
			
		}
		return false;
	}

	private boolean checkProperUpperBound(Node relational1, Node relational2,
			String firstToken, String secondToken, String thirdToken,
			String fourthToken, String upperBoundToken, String variableToken) {
		if ((firstToken.equals(upperBoundToken) && relational1 instanceof GreaterThanOperatorToken ||
			firstToken.equals(upperBoundToken) && relational1 instanceof GreaterThanEqualToOperatorToken) &&
			secondToken.equals(variableToken)) {
			return true;
		}
		else if ((secondToken.equals(upperBoundToken) && relational1 instanceof LessThanOperatorToken ||
				 secondToken.equals(upperBoundToken) && relational1 instanceof LessThanEqualToOperatorToken) &&
				 firstToken.equals(variableToken)) {
			return true;
		}
		else if ((thirdToken.equals(upperBoundToken) && relational2 instanceof GreaterThanOperatorToken ||
				 thirdToken.equals(upperBoundToken) && relational2 instanceof GreaterThanEqualToOperatorToken) &&
				 fourthToken.equals(variableToken)) {
			return true;
		}
		else if ((fourthToken.equals(upperBoundToken) && relational2 instanceof LessThanOperatorToken ||
				 fourthToken.equals(upperBoundToken) && relational2 instanceof LessThanEqualToOperatorToken) &&
				 thirdToken.equals(variableToken)) {
			return true;
		}
		return false;
	}
	
	private boolean checkProperLowerBound(Node relational1, Node relational2,
			String firstToken, String secondToken, String thirdToken,
			String fourthToken, String lowerBoundToken, String variableToken) {
		if ((firstToken.equals(lowerBoundToken) && relational1 instanceof LessThanOperatorToken ||
			firstToken.equals(lowerBoundToken) && relational1 instanceof LessThanEqualToOperatorToken) &&
			secondToken.equals(variableToken)) {
			return true;
		}
		else if ((secondToken.equals(lowerBoundToken) && relational1 instanceof GreaterThanOperatorToken ||
				 secondToken.equals(lowerBoundToken) && relational1 instanceof GreaterThanEqualToOperatorToken) &&
				 firstToken.equals(variableToken)) {
			return true;
		}
		else if ((thirdToken.equals(lowerBoundToken) && relational2 instanceof LessThanOperatorToken ||
				 thirdToken.equals(lowerBoundToken) && relational2 instanceof LessThanEqualToOperatorToken) &&
				 fourthToken.equals(variableToken)) {
			return true;
		}
		else if ((fourthToken.equals(lowerBoundToken) && relational2 instanceof GreaterThanOperatorToken ||
				 fourthToken.equals(lowerBoundToken) && relational2 instanceof GreaterThanEqualToOperatorToken) &&
				 thirdToken.equals(variableToken)) {
			return true;
		}
		return false;
	}
	
	private LiteralToken getUpperBound(Node firstNode, Node thirdNode,
			Node fifthNode, Node seventhNode) {
		LiteralToken upperBound = null, tempNode1 = null, tempNode2 = null;
		
		if (firstNode instanceof LiteralToken) {
			tempNode1 = (LiteralToken) firstNode;
		} else if (thirdNode instanceof LiteralToken) {
			tempNode1 = (LiteralToken) thirdNode;
		}
		
		if (fifthNode instanceof LiteralToken) {
			tempNode2 = (LiteralToken) fifthNode;
		} else if (seventhNode instanceof LiteralToken) {
			tempNode2 = (LiteralToken) seventhNode;
		}
		
		if (tempNode1 instanceof IntegerLiteralToken && tempNode2 instanceof IntegerLiteralToken) {
			int t1Val = ((IntegerLiteralToken) tempNode1).getValueAsInt();
			int t2Val = ((IntegerLiteralToken) tempNode2).getValueAsInt();
			
			if (t1Val > t2Val) upperBound = tempNode1;
			else if (t2Val > t1Val) upperBound = tempNode2;
		} else if (tempNode1 instanceof FloatLiteralToken && tempNode2 instanceof FloatLiteralToken) {
			float t1Val = ((FloatLiteralToken) tempNode1).getValueAsFloat();
			float t2Val = ((FloatLiteralToken) tempNode2).getValueAsFloat();
			
			if (t1Val > t2Val) upperBound = tempNode1;
			else if (t2Val > t1Val) upperBound = tempNode2;
		} else if (tempNode1 instanceof CharacterLiteralToken && tempNode2 instanceof CharacterLiteralToken) {
			char t1Val = ((CharacterLiteralToken) tempNode1).getValueAsChar();
			char t2Val = ((CharacterLiteralToken) tempNode2).getValueAsChar();
			
			if (t1Val > t2Val) upperBound = tempNode1;
			else if (t2Val > t1Val) upperBound = tempNode2;
		}
		
		return upperBound;
	}
	
	private LiteralToken getLowerBound(Node firstNode, Node thirdNode,
			Node fifthNode, Node seventhNode) {
		LiteralToken lowerBound = null, tempNode1 = null, tempNode2 = null;
		
		if (firstNode instanceof LiteralToken) {
			tempNode1 = (LiteralToken) firstNode;
		} else if (thirdNode instanceof LiteralToken) {
			tempNode1 = (LiteralToken) thirdNode;
		}
		
		if (fifthNode instanceof LiteralToken) {
			tempNode2 = (LiteralToken) fifthNode;
		} else if (seventhNode instanceof LiteralToken) {
			tempNode2 = (LiteralToken) seventhNode;
		}
		
		if (tempNode1 instanceof IntegerLiteralToken && tempNode2 instanceof IntegerLiteralToken) {
			int t1Val = ((IntegerLiteralToken) tempNode1).getValueAsInt();
			int t2Val = ((IntegerLiteralToken) tempNode2).getValueAsInt();
			
			if (t1Val < t2Val) lowerBound = tempNode1;
			else if (t2Val < t1Val) lowerBound = tempNode2;
		} else if (tempNode1 instanceof FloatLiteralToken && tempNode2 instanceof FloatLiteralToken) {
			float t1Val = ((FloatLiteralToken) tempNode1).getValueAsFloat();
			float t2Val = ((FloatLiteralToken) tempNode2).getValueAsFloat();
			
			if (t1Val < t2Val) lowerBound = tempNode1;
			else if (t2Val < t1Val) lowerBound = tempNode2;
		} else if (tempNode1 instanceof CharacterLiteralToken && tempNode2 instanceof CharacterLiteralToken) {
			char t1Val = ((CharacterLiteralToken) tempNode1).getValueAsChar();
			char t2Val = ((CharacterLiteralToken) tempNode2).getValueAsChar();
			
			if (t1Val < t2Val) lowerBound = tempNode1;
			else if (t2Val < t1Val) lowerBound = tempNode2;
		}
		
		return lowerBound;
	}

	private VariableIdentifierToken getVariableNode(Node firstNode, Node thirdNode,
			Node fifthNode, Node seventhNode) {
		VariableIdentifierToken variableNode = null;
		if (firstNode instanceof VariableIdentifierToken) {
			if (fifthNode instanceof VariableIdentifierToken) {
				String firstNodeToken = ((VariableIdentifierToken) firstNode).getToken();
				String secondNodeToken = ((VariableIdentifierToken) fifthNode).getToken();
				
				if (firstNodeToken.equals(secondNodeToken)) {
					variableNode = (VariableIdentifierToken) firstNode;
				}
			}
			else if (seventhNode instanceof VariableIdentifierToken) {
				String firstNodeToken = ((VariableIdentifierToken) firstNode).getToken();
				String secondNodeToken = ((VariableIdentifierToken) seventhNode).getToken();
				
				if (firstNodeToken.equals(secondNodeToken)) {
					variableNode = (VariableIdentifierToken) firstNode;
				}
			}
		}
		else if (thirdNode instanceof VariableIdentifierToken) {
			if (fifthNode instanceof VariableIdentifierToken) {
				String firstNodeToken = ((VariableIdentifierToken) thirdNode).getToken();
				String secondNodeToken = ((VariableIdentifierToken) fifthNode).getToken();
				
				if (firstNodeToken.equals(secondNodeToken)) {
					variableNode = (VariableIdentifierToken) thirdNode;
				}
			}
			else if (seventhNode instanceof VariableIdentifierToken) {
				String firstNodeToken = ((VariableIdentifierToken) thirdNode).getToken();
				String secondNodeToken = ((VariableIdentifierToken) seventhNode).getToken();
				
				if (firstNodeToken.equals(secondNodeToken)) {
					variableNode = (VariableIdentifierToken) thirdNode;
				}
			}
		}
		
		return variableNode;
	}
	
	private boolean sameType(Node firstNode, Node thirdNode,
			Node fifthNode, Node seventhNode, VariableIdentifierToken variableNode) {
		Node firstBound = null, secondBound = null;
		
		if (firstNode instanceof VariableIdentifierToken) {
			firstBound = thirdNode;
		} else if (thirdNode instanceof VariableIdentifierToken) {
			firstBound = firstNode;
		}
		
		if (fifthNode instanceof VariableIdentifierToken) {
			secondBound = seventhNode;
		} else if (seventhNode instanceof VariableIdentifierToken) {
			secondBound = fifthNode;
		}
		
		if ((firstBound instanceof IntegerLiteralToken && secondBound instanceof IntegerLiteralToken) ||
				(firstBound instanceof CharacterLiteralToken && secondBound instanceof CharacterLiteralToken) ||
				(firstBound instanceof FloatLiteralToken && secondBound instanceof FloatLiteralToken)) {
			return true;
		}
		
		return false;
	}
	
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> messages = new ArrayList<String>();

		if (ExpressionPatternStatics.isLogicalExpression(node)){
			if (node.getChildNodes().size() == 7) {
				Node firstNode = node.getChildNodes().get(0);
				Node relational1 = node.getChildNodes().get(1);
				Node thirdNode = node.getChildNodes().get(2);
				Node andNode = node.getChildNodes().get(3);
				Node fifthNode = node.getChildNodes().get(4);
				Node relational2 = node.getChildNodes().get(5);
				Node seventhNode = node.getChildNodes().get(6);
				
				VariableIdentifierToken variableNode = getVariableNode(firstNode, thirdNode, fifthNode, seventhNode);
				LiteralToken upperBound;
				LiteralToken lowerBound;
				
				if (variableNode != null) {
					if (sameType(firstNode, thirdNode, fifthNode, seventhNode, variableNode)) {
						
						upperBound = getUpperBound(firstNode, thirdNode, fifthNode, seventhNode);
						lowerBound = getLowerBound(firstNode, thirdNode, fifthNode, seventhNode);
						
						String firstToken = ((Token) firstNode).getToken();
						String secondToken = ((Token) thirdNode).getToken();
						String thirdToken = ((Token) fifthNode).getToken();
						String fourthToken = ((Token) seventhNode).getToken();
						String variableToken = variableNode.getToken();
						String upperBoundToken = upperBound.getToken();
						String lowerBoundToken = lowerBound.getToken();
						
						if (andNode instanceof AndOperatorToken) {
							if (!checkProperUpperBound(relational1, relational2, firstToken, secondToken, thirdToken, fourthToken, upperBoundToken, variableToken)) {
								
							}
							if (!checkProperLowerBound(relational1, relational2, firstToken, secondToken, thirdToken, fourthToken, lowerBoundToken, variableToken)) {
								
							}
						}
					}
				}
			}
			
		}
		
		return messages;
	}
	
}
