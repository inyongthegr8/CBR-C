package com.cbrc.base.consts;

import java.util.ArrayList;

public class LogicalExpressionPatternList extends ArrayList<String>{

	private static final long serialVersionUID = 4458794774108969184L;
	
	public LogicalExpressionPatternList() {
		super();
		initContents();
	}
	
	private void initContents() {
		this.add("com.cbrc.nodes.patterns.expressionpattern.ReverseSentinelValuePattern");
		this.add("com.cbrc.nodes.patterns.expressionpattern.SentinelValuePattern");
		this.add("com.cbrc.nodes.patterns.expressionpattern.RangeExpressionPattern");
		this.add("com.cbrc.nodes.patterns.expressionpattern.LogicalExpressionPattern");
	}
}
