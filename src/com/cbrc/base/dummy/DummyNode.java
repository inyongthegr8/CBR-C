package com.cbrc.base.dummy;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;

public class DummyNode extends Node {

	public DummyNode(Node parentNode) {
		super(parentNode);
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return new DummyNode(parent);
	}

}
