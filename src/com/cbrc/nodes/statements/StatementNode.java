package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;

public abstract class StatementNode extends Node {
	
	public StatementNode() {
		super();
	}

	public StatementNode(Node parentNode) {
		super(parentNode);
	}

	public StatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public StatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected abstract String getAdditionalDetails();

}
