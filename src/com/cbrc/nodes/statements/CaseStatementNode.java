package com.cbrc.nodes.statements;

import java.util.ArrayList;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.LiteralToken;
import com.cbrc.tokens.keywords.BreakKeywordToken;
import com.cbrc.tokens.keywords.CaseKeywordToken;
import com.cbrc.tokens.operators.ColonToken;

public class CaseStatementNode extends StatementNode {

	public CaseStatementNode() {
		super();
	}
	
	public CaseStatementNode(CaseKeywordToken caseKeyword, LiteralToken condition, ColonToken colon) {
		caseKeyword.setParentNode(this);
		condition.setParentNode(this);
		colon.setParentNode(this);
		
		caseKeyword.setIndex(0);
		condition.setIndex(1);
		colon.setIndex(2);
		
		this.addChild(caseKeyword);
		this.addChild(condition);
		this.addChild(colon);
	}

	public CaseStatementNode(Node parentNode) {
		super(parentNode);
	}

	public CaseStatementNode(ArrayList<Node> childNodes) {
		super(childNodes);
	}

	public CaseStatementNode(Node parentNode, ArrayList<Node> childNodes) {
		super(parentNode, childNodes);
	}
	
	public CaseKeywordToken getCaseKeyword() {
		return (CaseKeywordToken) this.getChildNodes().get(0);
	}
	
	public void setCaseKeyword(CaseKeywordToken caseKeyword) {
		caseKeyword.setParentNode(this);
		this.getChildNodes().set(0, caseKeyword);
	}
	
	public LiteralToken getCondition() {
		return (LiteralToken) this.getChildNodes().get(1);
	}
	
	public void setCondition(LiteralToken condition) {
		condition.setParentNode(this);
		this.getChildNodes().set(1, condition);
	}
	
	public ColonToken getColon() {
		return (ColonToken) this.getChildNodes().get(2);
	}
	
	public void setColon(ColonToken colon) {
		colon.setParentNode(this);
		this.getChildNodes().set(2, colon);
	}

	@Override
	protected String getAdditionalDetails() {
		return "Case statement";
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		CaseStatementNode statementNode = new CaseStatementNode();
		statementNode.setParentNode(parent);
		
		statementNode.addChild((CaseKeywordToken) tokens.consume());
		statementNode.addChild((LiteralToken) tokens.consume());
		statementNode.addChild((ColonToken) tokens.consume());
		
		statementNode.addChild(new CaseStatementBodyNode().generateThisNode(tokens, statementNode));
		
		if (tokens.lookahead() instanceof BreakKeywordToken) {
			//consumes break then the semicolon
			statementNode.addChild(tokens.consume());
			tokens.consume();
		}
		
		return statementNode;
	}

}
