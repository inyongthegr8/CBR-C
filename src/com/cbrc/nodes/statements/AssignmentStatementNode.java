package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.tokens.identifiers.VariableIdentifierToken;
import com.cbrc.tokens.operators.AssignmentOperatorToken;
import com.cbrc.tokens.operators.SemicolonToken;

/**
 * Models an assignment statement. Normally, connected to this
 * statement is a VariableIdentifierToken, an AssignementToken
 * and and ExpressionNode.
 * @author Alice
 *
 */
public class AssignmentStatementNode extends StatementNode {

	public AssignmentStatementNode() {
		super();
	}
	
	public AssignmentStatementNode(VariableIdentifierToken variable, AssignmentOperatorToken assignment, ExpressionNode expression, SemicolonToken semicolon) {
		super();
		
		variable.setParentNode(this);
		assignment.setParentNode(this);
		expression.setParentNode(this);
		semicolon.setParentNode(this);
		
		variable.setIndex(0);
		assignment.setIndex(1);
		expression.setIndex(2);
		semicolon.setIndex(3);
		
		this.getChildNodes().add(variable);
		this.getChildNodes().add(assignment);
		this.getChildNodes().add(expression);
		this.getChildNodes().add(semicolon);
	}

	public AssignmentStatementNode(Node parentNode) {
		super(parentNode);
	}

	public AssignmentStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public AssignmentStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}

	public VariableIdentifierToken getVariable() {
		return (VariableIdentifierToken) this.getChildNodes().get(0);
	}
	
	public void setVariable(VariableIdentifierToken variable) {
		variable.setParentNode(this);
		this.getChildNodes().set(0, variable);
	}
	
	public AssignmentOperatorToken getAssignmentOperator() {
		return (AssignmentOperatorToken) this.getChildNodes().get(1);
	}
	
	public void setAssignmentOperator(AssignmentOperatorToken assignment) {
		assignment.setParentNode(this);
		this.getChildNodes().set(1, assignment);
	}
	
	public ExpressionNode getExpression() {
		return (ExpressionNode) this.getChildNodes().get(2);
	}
	
	public void setExpression(ExpressionNode expression) {
		expression.setParentNode(this);
		this.getChildNodes().set(2, expression);
	}
	
	public SemicolonToken getSemicolon() {
		return (SemicolonToken) this.getChildNodes().get(3);
	}
	
	public void setSemicolon(SemicolonToken semicolon) {
		semicolon.setParentNode(this);
		this.getChildNodes().set(3, semicolon);
	}

	@Override
	protected String getAdditionalDetails() {
		return this.getVariable().getToken() + " " + this.getAssignmentOperator().getToken();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		AssignmentStatementNode assignmentStatement = new AssignmentStatementNode();
		assignmentStatement.setParentNode(parent);
		
		assignmentStatement.addChild((VariableIdentifierToken) tokens.consume());
		assignmentStatement.addChild((AssignmentOperatorToken) tokens.consume());
		assignmentStatement.addChild((ExpressionNode) new ExpressionNode().generateThisNode(tokens, assignmentStatement));
		
		//consumes last semicolon if present
		if (tokens.lookahead() instanceof SemicolonToken && !(parent instanceof ForStatementNode)) assignmentStatement.addChild((SemicolonToken) tokens.consume());
		
		return assignmentStatement;
	}
	
	public String toStringAsC() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getVariable());
		sb.append(this.getAssignmentOperator());
		sb.append(this.getExpression().toStringAsC());
		sb.append(this.getSemicolon());
		return sb.toString();
	}

}
