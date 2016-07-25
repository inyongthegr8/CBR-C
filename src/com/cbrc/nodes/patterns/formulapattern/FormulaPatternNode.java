package com.cbrc.nodes.patterns.formulapattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.patterns.PatternNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;

public class FormulaPatternNode extends PatternNode{

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return null;
	}

	@Override
	public boolean isPattern(Node node) {
		if (node instanceof AssignmentStatementNode) return true;
		return false;
	}
	
	public boolean isPatternWithInit(Node node, Token initVar) {
		if (node instanceof AssignmentStatementNode) {
			VariableIdentifierToken var = ((AssignmentStatementNode) node).getVariable();
			if (var.getToken().equals(initVar.getToken())) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public ArrayList<String> isPatternWithFeedback(Node node) {
		ArrayList<String> message = new ArrayList<String>();
		// TODO: Create a loop that goes through children of node, comparing it with the contents of the formula pattern node and create feedback based from that.
		return message;
	}
	
	public ArrayList<String> isPatternWithFeedbackWithInit(Node node, Token initVar) {
		ArrayList<String> message = new ArrayList<String>();
		// TODO: Create a loop that goes through children of node, comparing it with the contents of the formula pattern node and create feedback based from that.
		return message;
	}
}
