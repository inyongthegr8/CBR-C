package com.cbrc.nodes;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.functions.FunctionBodyNode;
import com.cbrc.nodes.functions.FunctionHeaderNode;
import com.cbrc.tokens.identifiers.FunctionIdentifierToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;

/**
 * A node that identifies a function. Two nodes normally connect
 * to a FunctionNode, a FunctionHeaderNode and a FunctionBodyNode.
 * These nodes defines the parts of a function.
 * 
 * @author Alice
 *
 */
public class FunctionNode extends Node {
	
	public FunctionNode() {
		super();
	}
	
	public FunctionNode(FunctionIdentifierToken functionIdentifierToken, TypeSpecKeywordToken returnType) {
		super();
		
		functionIdentifierToken.setParentNode(this);
		returnType.setParentNode(this);
		
		returnType.setIndex(0);
		functionIdentifierToken.setIndex(1);
		
		this.getChildNodes().add(returnType);
		this.getChildNodes().add(functionIdentifierToken);
	}

	public FunctionNode(Node parentNode) {
		super(parentNode);
	}

	public FunctionNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public FunctionNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	public FunctionIdentifierToken getFunctionIdentifier() {
		return (FunctionIdentifierToken) this.getChildNodes().get(1);
	}

	public void setFunctionIdentifier(FunctionIdentifierToken functionIdentifier) {
		functionIdentifier.setParentNode(this);
		this.getChildNodes().set(1, functionIdentifier);
	}

	public TypeSpecKeywordToken getReturnType() {
		return (TypeSpecKeywordToken) this.getChildNodes().get(0);
	}

	public void setReturnType(TypeSpecKeywordToken returnType) {
		returnType.setParentNode(this);
		
		this.getChildNodes().set(0, returnType);
	}

	@Override
	protected String getAdditionalDetails() {
		return "Function Name: " + this.getFunctionIdentifier().getToken() + ": Return Type: " + this.getReturnType().getToken();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		
		TypeSpecKeywordToken returnTypeToken = (TypeSpecKeywordToken) tokens.consume();
		FunctionIdentifierToken funcIndentifierToken = (FunctionIdentifierToken) tokens.consume();
		
		FunctionNode functionNode = new FunctionNode(funcIndentifierToken, returnTypeToken);
		functionNode.setParentNode(parent);
		
		FunctionHeaderNode functionHeader = new FunctionHeaderNode();
		FunctionBodyNode functionBody = new FunctionBodyNode();
		
		functionNode.addChild(functionHeader.generateThisNode(tokens, functionNode));
		functionNode.addChild(functionBody.generateThisNode(tokens, functionNode));
		
		return functionNode;
	}

}
