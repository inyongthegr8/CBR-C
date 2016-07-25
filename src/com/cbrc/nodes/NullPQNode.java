package com.cbrc.nodes;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;

public class NullPQNode extends Node {

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return new NullPQNode();
	}

}
