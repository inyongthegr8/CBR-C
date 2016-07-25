package com.cbrc.nodes.functions;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.CharKeywordToken;
import com.cbrc.tokens.keywords.DoubleKeywordToken;
import com.cbrc.tokens.keywords.FloatKeywordToken;
import com.cbrc.tokens.keywords.IntKeywordToken;
import com.cbrc.tokens.keywords.LongKeywordToken;
import com.cbrc.tokens.operators.CloseGroupingToken;

/**
 * Node that represents a function header. Normally, several Nodes
 * conencto to a FunctionHeaderNode, all of which are FormalParamterNodes.
 * These FormalParamterNodes are then comprised of a TypeSpecToken and
 * a VariableIdentifierToken (while not strictly a part of the tree,
 * these tokens in effect become the leaves of the ParameterNode, and
 * thus, the AST.
 * 
 * @author Alice
 *
 */
public class FunctionHeaderNode extends Node {

	public FunctionHeaderNode() {
		super();
	}

	public FunctionHeaderNode(Node parentNode) {
		super(parentNode);
	}

	public FunctionHeaderNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public FunctionHeaderNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		FunctionHeaderNode funcHeaderNode = new FunctionHeaderNode();
		funcHeaderNode.setParentNode(parent);
		
		//consumes open parenthesis
		tokens.consume();
		
		while(!(tokens.lookahead() instanceof CloseGroupingToken)) {
			// TODO: Think of a more object oriented approach to this
			FormalParameterNode formalParameterNode = null;
			
			if (tokens.lookahead() instanceof CharKeywordToken) {
				formalParameterNode = new CharFormalParameterNode();
			} else if (tokens.lookahead() instanceof IntKeywordToken) {
				formalParameterNode = new IntegerFormalParameterNode();
			} else if (tokens.lookahead() instanceof LongKeywordToken) {
				formalParameterNode = new LongFormalParameterNode();
			} else if (tokens.lookahead() instanceof FloatKeywordToken) {
				formalParameterNode = new FloatFormalParameterNode();
			} else if (tokens.lookahead() instanceof DoubleKeywordToken) {
				formalParameterNode = new DoubleFormalParameterNode();
			} else {
				System.out.println(this.getClass().getName() + ": Undefined paramter type for " + tokens.lookahead().toString());
			}
			
			funcHeaderNode.addChild(formalParameterNode.generateThisNode(tokens, funcHeaderNode));
		}
		
		//consumes close parenthesis
		tokens.consume();
		
		return funcHeaderNode;
	}

}
