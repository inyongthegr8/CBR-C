package com.cbrc.nodes.statements;

public class ScanfStatementNode extends FunctionCallStatementNode {

	public ScanfStatementNode() {
		super();
	}
	
	protected FunctionCallStatementNode createSpecificFunctionCallStatementNode() {
		return new ScanfStatementNode();
	}

}
