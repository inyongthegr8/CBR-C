package com.cbrc.nodes;

import java.io.File;
import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.base.Token;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.keywords.TypeSpecKeywordToken;

/**
 * This is the root node of the AST.
 * @author Alice
 *
 */

public class TranslationUnitNode extends Node {
	
	private File source;
	
	public TranslationUnitNode() {
		super();
	}

	public TranslationUnitNode(File source) {
		super();
		this.source = source;
	}

	public TranslationUnitNode(Node parentNode) {
		super(parentNode);
	}

	public TranslationUnitNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public TranslationUnitNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

	@Override  
	protected String getAdditionalDetails() {
		return "Source: " + this.getSource().getName();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		TranslationUnitNode translationUnitNode = new TranslationUnitNode(tokens.getSourceCode());
		translationUnitNode.setParentNode(null);
		FunctionNode functionNode = new FunctionNode();
		
		while (tokens.hasMoreToConsume()) {
			Token currentTokenObject = tokens.lookahead();
			
			if (currentTokenObject instanceof TypeSpecKeywordToken) {
				translationUnitNode.addChild(functionNode.generateThisNode(tokens, translationUnitNode));
			}
			
			// Tentative consumption of close block token.
			
			tokens.consume();
			
		}
		
		/**
		 * In this portion, create code that will recursively
		 * call generateThisNode for each of the children of
		 * this node, depending on the situation (check
		 * tokens for this) and the required children of translation
		 * unit (check comments).
		 * 
		 * Algorithm:
		 * Requirements: A consume() function in tokenizedCode that automatically
		 * gets the next token in the arrayList. Add a reset() function as well
		 * that makes the next consume() go back to the first token of tokenizedCode.
		 * All other convenience methods to TokenizedCode as needed.
		 * 
		 * Note: Use lookahead() to check the next consumable token without
		 * consuming it.
		 * 
		 * 1. Until all tokens in TokenizedCode has been consumed, consume a token
		 * and check its type.
		 * 2. If its a type that this a possible child of this class, create the
		 * appropriate Node for that token, then call its generateThisNode (which
		 * will recursively implement this same algorithm)
		 * 3. Note: In the other generateThisNode functions, make sure to react
		 * to tokens accordingly. For example, in an assignmentNode, you have to take
		 * the VariableIdentifierToken where the assignment was performed against,
		 * attach it to the assignmentNode as a node, get the equal sign, doing the same,
		 * then call the generateThisNode function of an ExpressionNode, passing the
		 * rest of the tokens.
		 * 4. Note: The base case of the recursive function is in nodes that only
		 * require tokens to be attached to it as children, and does not require
		 * other (pure) nodes (tokens are nodes too, but aren't "pure").
		 */
		
		return translationUnitNode;
	}

}
