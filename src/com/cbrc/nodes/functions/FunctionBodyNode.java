package com.cbrc.nodes.functions;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.functions.body.BodyProperNode;
import com.cbrc.nodes.functions.body.VariableDeclarationsNode;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;
import com.cbrc.tokens.operators.OpenBlockToken;

/**
 * A node that represents all that is contained inside the
 * Function body. Two nodes are normally conencted to this node,
 * the VariableDeclarationNode and the BodyProperNode. The
 * VariableDeclarationNode contains all the variables delcared
 * in the code, and the BodyProperNode contains all the other
 * statements within the function body. 
 * 
 * For the sake of easier tracking, assignment statements made
 * when declaring variables should be preprocessed and stored
 * as part of the body of the function (Note that this is possible
 * because these assignment statements would not affect the 
 * logic if the code, if added in the proper sequence).
 * 
 * @author Alice
 *
 */
public class FunctionBodyNode extends Node {

	public FunctionBodyNode() {
		super();
	}

	public FunctionBodyNode(Node parentNode) {
		super(parentNode);
	}

	public FunctionBodyNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public FunctionBodyNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		FunctionBodyNode functionBodyNode = new FunctionBodyNode();
		functionBodyNode.setParentNode(parent);
		
		if (tokens.lookahead() instanceof OpenBlockToken) tokens.consume();
		
		if (tokens.lookahead() instanceof TypeSpecKeywordToken) {
			Node variableDeclarationsNode = new VariableDeclarationsNode();
			functionBodyNode.addChild(variableDeclarationsNode.generateThisNode(tokens, functionBodyNode));
		}
		
		// TODO: Trace execution of bodyProperNode.generateThisNode
		Node bodyProperNode = new BodyProperNode();
		functionBodyNode.addChild(bodyProperNode.generateThisNode(tokens, functionBodyNode));
		
		
		return functionBodyNode;
	}

}
