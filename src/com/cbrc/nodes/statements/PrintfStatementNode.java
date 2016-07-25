package com.cbrc.nodes.statements;

public class PrintfStatementNode extends FunctionCallStatementNode {

	public PrintfStatementNode() {
		super();
	}
	
	protected FunctionCallStatementNode createSpecificFunctionCallStatementNode() {
		return new PrintfStatementNode();
	}

}
