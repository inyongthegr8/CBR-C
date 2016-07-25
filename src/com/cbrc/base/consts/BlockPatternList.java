package com.cbrc.base.consts;

import java.util.ArrayList;

public class BlockPatternList extends ArrayList<String>{

	private static final long serialVersionUID = -3803031920036455442L;
	
	public BlockPatternList() {
		super();
		initContents();
	}
	
	private void initContents() {
		this.add("com.cbrc.nodes.patterns.blockpattern.DigitCountPattern");
	}

}
