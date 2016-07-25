package com.cbrc.nodes.statements;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.identifiers.FunctionIdentifierToken;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenGroupingToken;
import com.cbrc.tokens.operators.SemicolonToken;
import com.cbrc.tokens.operators.SeparatorToken;

public class FunctionCallStatementNode extends StatementNode {

	public FunctionCallStatementNode() {
		super();
	}
	
	public FunctionIdentifierToken getFunctionCallToken() {
		return (FunctionIdentifierToken) this.getChildNodes().get(0);
	}
	
	public void setFunctionCallToken(FunctionIdentifierToken functionCallToken) {
		functionCallToken.setParentNode(this);
		this.getChildNodes().set(0, functionCallToken);
	}
	
	public OpenGroupingToken getOpenParenthesis() {
		return (OpenGroupingToken) this.getChildNodes().get(1);
	}

	public void setOpenParenthesis(OpenGroupingToken openParenthesis) {
		openParenthesis.setParentNode(this);
		this.getChildNodes().set(1, openParenthesis);
	}
	
	public CloseGroupingToken getCloseParenthesis() {
		return (CloseGroupingToken) this.getChildNodes().get(this.getChildNodes().size() - 1);
	}

	public void setCloseParenthesis(CloseGroupingToken closeParenthesis) {
		closeParenthesis.setParentNode(this);
		this.getChildNodes().set(this.getChildNodes().size() - 1, closeParenthesis);
	}
	
	@Override
	protected String getAdditionalDetails() {
		return this.getFunctionCallToken().getToken() + this.getOpenParenthesis().getToken() + this.getCloseParenthesis().getToken();
	}

	/**
	 * Subclasses of this class must override this method
	 */
	protected FunctionCallStatementNode createSpecificFunctionCallStatementNode() {
		return new FunctionCallStatementNode();
	}
	
	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		FunctionCallStatementNode functionCallStatementNode = createSpecificFunctionCallStatementNode();
		functionCallStatementNode.setParentNode(parent);
		
		//consumes function identifier token
		functionCallStatementNode.addChild(tokens.consume());
		
		//consumes open parenthesis token
		functionCallStatementNode.addChild(tokens.consume());
		
		// change these parameters to expression nodes
		while (!(tokens.lookahead() instanceof CloseGroupingToken)) {
			//functionCallStatementNode.addChild(tokens.consume());
			ExpressionNode expressionParameter = new ExpressionNode();
			functionCallStatementNode.addChild(expressionParameter.generateThisNode(tokens, functionCallStatementNode));
			
			if (tokens.lookahead() instanceof SeparatorToken) tokens.consume();
		}
		
		functionCallStatementNode.addChild(tokens.consume());
		
		//Consumes the final semicolon
		if (tokens.lookahead() instanceof SemicolonToken) tokens.consume();
		
		return functionCallStatementNode;
	}

}
