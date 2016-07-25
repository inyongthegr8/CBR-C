package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.tokens.keywords.BreakKeywordToken;
import com.cbrc.tokens.keywords.CaseKeywordToken;
import com.cbrc.tokens.keywords.DefaultKeywordToken;
import com.cbrc.tokens.operators.CloseBlockToken;

public class CaseStatementBodyNode extends StatementBodyNode {

	// See switchstatement body node for details on how to implement this
	
	public CaseStatementBodyNode() {
		super();
	}

	public CaseStatementBodyNode(Node parentNode) {
		super(parentNode);
	}

	public CaseStatementBodyNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public CaseStatementBodyNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	protected StatementBodyNode prepareStatementBodyNode(){
		return new CaseStatementBodyNode();
	}
	
	protected boolean isDelimiter(Token token) {
		if (token instanceof BreakKeywordToken || token instanceof CaseKeywordToken || token instanceof DefaultKeywordToken || token instanceof CloseBlockToken) {
			return true;
		} else return false;
	}
	
	protected boolean isBlockType(Token token) {
		return true;
	}

}
