package com.cbrc.nodes.functions.body;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.statements.AssignmentStatementNode;
import com.cbrc.nodes.statements.DoWhileStatementNode;
import com.cbrc.nodes.statements.ElseIfStatementNode;
import com.cbrc.nodes.statements.ElseStatementNode;
import com.cbrc.nodes.statements.ForStatementNode;
import com.cbrc.nodes.statements.FunctionCallStatementNode;
import com.cbrc.nodes.statements.IfStatementNode;
import com.cbrc.nodes.statements.PrintfStatementNode;
import com.cbrc.nodes.statements.ReturnStatementNode;
import com.cbrc.nodes.statements.ScanfStatementNode;
import com.cbrc.nodes.statements.StatementNode;
import com.cbrc.nodes.statements.SwitchStatementNode;
import com.cbrc.nodes.statements.WhileStatementNode;
import com.cbrc.tokens.identifiers.FunctionIdentifierToken;
import com.cbrc.tokens.identifiers.PrintfKeywordToken;
import com.cbrc.tokens.identifiers.ScanfKeywordToken;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.keywords.DoKeywordToken;
import com.cbrc.tokens.keywords.ElseIfKeywordToken;
import com.cbrc.tokens.keywords.ElseKeywordToken;
import com.cbrc.tokens.keywords.ForKeywordToken;
import com.cbrc.tokens.keywords.IfKeywordToken;
import com.cbrc.tokens.keywords.ReturnKeywordToken;
import com.cbrc.tokens.keywords.SwitchKeywordToken;
import com.cbrc.tokens.keywords.WhileKeywordToken;
import com.cbrc.tokens.operators.CloseBlockToken;

/**
 * This node models the body of a function, and will contain
 * other nodes depending on the statements found in the code,
 * in the order they were listed (to preserve the code logic).
 * 
 * @author Alice
 *
 */
public class BodyProperNode extends Node {

	public BodyProperNode() {
		super();
	}

	public BodyProperNode(Node parentNode) {
		super(parentNode);
	}

	public BodyProperNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public BodyProperNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	@Override
	protected String getAdditionalDetails() {
		return "";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		BodyProperNode bodyProperNode = new BodyProperNode();
		bodyProperNode.setParentNode(parent);
		
		StatementNode statementNode = null;
		
		
		while (!(tokens.lookahead() instanceof CloseBlockToken)) {
			
			statementNode = prepareStatementNode(tokens, statementNode);
			
			bodyProperNode.addChild(statementNode.generateThisNode(tokens, bodyProperNode));

		}
		/**
		 * For each potential statement, make a call to the corresponding Node
		 * Class' generateThisNode, reacting to the TokenType. Do not forget to
		 * pass bodyProperNode as the parent for the function calls, and to 
		 * properly consume tokens to ensure proper execution.
		 */
		
		return bodyProperNode;
	}

	protected StatementNode prepareStatementNode(TokenizedCode tokens,
			StatementNode statementNode) {
		if (tokens.lookahead() instanceof IfKeywordToken) {
			statementNode = new IfStatementNode();
		} else if (tokens.lookahead() instanceof ElseIfKeywordToken) {
			statementNode = new ElseIfStatementNode();
		} else if (tokens.lookahead() instanceof ElseKeywordToken) {
			statementNode = new ElseStatementNode();
		} else if (tokens.lookahead() instanceof SwitchKeywordToken) {
			statementNode = new SwitchStatementNode();
		} else if (tokens.lookahead() instanceof ForKeywordToken) {
			statementNode = new ForStatementNode();
		} else if (tokens.lookahead() instanceof WhileKeywordToken) {
			statementNode = new WhileStatementNode();
		} else if (tokens.lookahead() instanceof DoKeywordToken) {
			statementNode = new DoWhileStatementNode();
		} else if (tokens.lookahead() instanceof ReturnKeywordToken) {
			statementNode = new ReturnStatementNode();
		} else if (tokens.lookahead() instanceof PrintfKeywordToken) {
			statementNode = new PrintfStatementNode();
		} else if (tokens.lookahead() instanceof ScanfKeywordToken) {
			statementNode = new ScanfStatementNode();
		} else if (tokens.lookahead() instanceof VariableIdentifierToken) {
			statementNode = new AssignmentStatementNode();
		} else if (tokens.lookahead() instanceof FunctionIdentifierToken) {
			statementNode = new FunctionCallStatementNode();
		} else {
			System.out.println(this.getClass().getName() + ": Error Found: No statement class found for statement " + tokens.lookahead());
		}
		return statementNode;
	}

}
