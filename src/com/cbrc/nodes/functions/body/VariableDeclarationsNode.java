package com.cbrc.nodes.functions.body;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.CharKeywordToken;
import com.cbrc.tokens.keywords.DoubleKeywordToken;
import com.cbrc.tokens.keywords.FloatKeywordToken;
import com.cbrc.tokens.keywords.IntKeywordToken;
import com.cbrc.tokens.keywords.LongKeywordToken;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;

/**
 * Node representing Variable declarations within a function.
 * Children of this node normally include individual 
 * VariableDeclarations for datatypes, depending on the variabes
 * defined in the code. Examples of these datatype related
 * VariableDeclaraionts are IntegerVariableDeclarationNodes,
 * FloatVariableDeclarationNodes etc. These extend from
 * the abstract class VariableDeclarationNode.
 * 
 * Each of these individuated variable declaration nodes contain
 * two parts, the Data type and the variable identifier. Remember
 * that initialiations made during variable declaration are
 * preprocessed and rewritten as code after the declarations
 * are made, in the order they were assigned.
 * 
 * @author Alice
 *
 */
public class VariableDeclarationsNode extends Node {

	public VariableDeclarationsNode() {
		super();
	}

	public VariableDeclarationsNode(Node parentNode) {
		super(parentNode);
	}

	public VariableDeclarationsNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public VariableDeclarationsNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		VariableDeclarationsNode variableDeclarationsNode = new VariableDeclarationsNode();
		variableDeclarationsNode.setParentNode(parent);
		
		while((tokens.lookahead() instanceof TypeSpecKeywordToken)) {
			// TODO: Think of a more object oriented approach to this
			VariableDeclarationNode variableDeclarationNode = null;
			
			if (tokens.lookahead() instanceof CharKeywordToken) {
				variableDeclarationNode = new CharVariableDeclarationNode();
			} else if (tokens.lookahead() instanceof IntKeywordToken) {
				variableDeclarationNode = new IntegerVariableDeclarationNode();
			} else if (tokens.lookahead() instanceof LongKeywordToken) {
				variableDeclarationNode = new LongVariableDeclarationNode();
			} else if (tokens.lookahead() instanceof FloatKeywordToken) {
				variableDeclarationNode = new FloatVariableDeclarationNode();
			} else if (tokens.lookahead() instanceof DoubleKeywordToken) {
				variableDeclarationNode = new DoubleVariableDeclarationNode();
			} else {
				System.out.println(this.getClass().getName() + ": Undefined variable type for " + tokens.lookahead().toString());
			}
			
			variableDeclarationsNode.addChild(variableDeclarationNode.generateThisNode(tokens, variableDeclarationsNode));
		}
		
		return variableDeclarationsNode;
	}

}
