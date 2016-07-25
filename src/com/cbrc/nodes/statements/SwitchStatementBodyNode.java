package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.CaseKeywordToken;
import com.cbrc.tokens.keywords.DefaultKeywordToken;

/**
 * Models the switch statement body. 
 * 
 * Modify the generation of this node (override) becuase
 * this node generates case statement nodes instead of the usual
 * nodes. case statement nodes are also extensions of the statement
 * body node, but have special delimiters (case or break).
 * 
 * modify statement body node so that the delimtiers can be controlled
 * somehow.
 * 
 * @author Alice
 *
 */
public class SwitchStatementBodyNode extends StatementBodyNode {

	public SwitchStatementBodyNode() {
		super();
	}

	public SwitchStatementBodyNode(Node parentNode) {
		super(parentNode);
	}

	public SwitchStatementBodyNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public SwitchStatementBodyNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	protected StatementNode prepareStatementNode(TokenizedCode tokens,
			StatementNode statementNode) {
		if (tokens.lookahead() instanceof CaseKeywordToken) {
			statementNode = new CaseStatementNode();
		} else if (tokens.lookahead() instanceof DefaultKeywordToken) {
			statementNode = new DefaultStatementNode();
		} else {
			System.out.println(this.getClass().getName() + ": Error Found: No statement class found for statement " + tokens.lookahead());
		}
		return statementNode;
	}
	
	protected StatementBodyNode prepareStatementBodyNode(){
		return new SwitchStatementBodyNode();
	}
	
}
