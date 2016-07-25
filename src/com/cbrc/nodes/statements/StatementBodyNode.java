package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.functions.body.BodyProperNode;
import com.cbrc.tokens.operators.CloseBlockToken;
import com.cbrc.tokens.operators.OpenBlockToken;

/**
 * Models the body of a statement. Normally attached to
 * statement Nodes as their last child. attached to this
 * node are all nodes that can be generated inside a
 * BodyProperNode, but has been separated as a class on its own
 * because of too many specialziation considerations.
 * 
 * Check BodyProperNode for this class' generateThisNode
 * function
 * 
 * @author Alice
 *
 */
public class StatementBodyNode extends BodyProperNode {

	public StatementBodyNode() {
		super();
	}

	public StatementBodyNode(Node parentNode) {
		super(parentNode);
	}

	public StatementBodyNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public StatementBodyNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected String getAdditionalDetails() {
		return "Statement Body";
	}
	
	protected StatementBodyNode prepareStatementBodyNode(){
		return new StatementBodyNode();
	}
	
	protected boolean isDelimiter(Token token) {
		if (token instanceof CloseBlockToken) {
			return true;
		} else return false;
	}
	
	protected boolean isBlockType(Token token) {
		if (token instanceof OpenBlockToken) {
			return true;
		} else return false;
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		StatementBodyNode statementBody = prepareStatementBodyNode();
		statementBody.setParentNode(parent);
		
		StatementNode statementNode = null;
		if (isBlockType(tokens.lookback())) {
			while (!(isDelimiter(tokens.lookahead()))) {
				statementNode = prepareStatementNode(tokens, statementNode);
				statementBody.addChild(statementNode.generateThisNode(tokens, statementBody));
			}
		} else {
			statementNode = prepareStatementNode(tokens, statementNode);
			statementBody.addChild(statementNode.generateThisNode(tokens, statementBody));
		}
		
		return statementBody;
	}

}
