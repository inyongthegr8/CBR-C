package com.cbrc.nodes.patterns.expressionpattern;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.patterns.PatternNode;

public class LogicalExpressionPattern extends PatternNode {

	@Override
	public boolean isPattern(Node node) {
		if (ExpressionPatternStatics.isLogicalExpression(node)) return true;
		return false;
	}


	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return null;
	}


	@Override
	public ArrayList<String> isPatternWithFeedback(Node node) {
		// TODO Create a loop that goes through children of node, comparing it with the contents of the formula pattern node and create feedback based from that.
		return null;
	}

}
