package com.cbrc.base.consts;

import java.util.ArrayList;

public class TokenClassList extends ArrayList<String>{

	private static final long serialVersionUID = 7712597751877803568L;

	public TokenClassList() {
		super();
		initContents();
	}

	private void initContents() {
		this.add("com.cbrc.tokens.keywords.BreakKeywordToken");
		this.add("com.cbrc.tokens.keywords.CaseKeywordToken");
		this.add("com.cbrc.tokens.keywords.CharKeywordToken");
		this.add("com.cbrc.tokens.keywords.DoKeywordToken");
		this.add("com.cbrc.tokens.keywords.DoubleKeywordToken");
		this.add("com.cbrc.tokens.keywords.ElseIfKeywordToken");
		this.add("com.cbrc.tokens.keywords.ElseKeywordToken");
		this.add("com.cbrc.tokens.keywords.FloatKeywordToken");
		this.add("com.cbrc.tokens.keywords.ForKeywordToken");
		this.add("com.cbrc.tokens.keywords.IfKeywordToken");
		this.add("com.cbrc.tokens.keywords.IntKeywordToken");
		this.add("com.cbrc.tokens.keywords.LongKeywordToken");
		this.add("com.cbrc.tokens.keywords.ReturnKeywordToken");
		this.add("com.cbrc.tokens.identifiers.ScanfKeywordToken");
		this.add("com.cbrc.tokens.identifiers.PrintfKeywordToken");
		this.add("com.cbrc.tokens.keywords.SwitchKeywordToken");
		this.add("com.cbrc.tokens.keywords.VoidKeywordToken");
		this.add("com.cbrc.tokens.keywords.WhileKeywordToken");
		this.add("com.cbrc.tokens.keywords.DefaultKeywordToken");
		this.add("com.cbrc.tokens.operators.AddressOperatorToken");
		this.add("com.cbrc.tokens.operators.AndOperatorToken");
		this.add("com.cbrc.tokens.operators.DivisionOperatorToken");
		this.add("com.cbrc.tokens.operators.EqualOperatorToken");
		this.add("com.cbrc.tokens.operators.GreaterThanEqualToOperatorToken");
		this.add("com.cbrc.tokens.operators.GreaterThanOperatorToken");
		this.add("com.cbrc.tokens.operators.LessThanEqualToOperatorToken");
		this.add("com.cbrc.tokens.operators.LessThanOperatorToken");
		this.add("com.cbrc.tokens.operators.MinusOperatorToken");
		this.add("com.cbrc.tokens.operators.ModuloOperatorToken");
		this.add("com.cbrc.tokens.operators.MultiplyOperatorToken");
		this.add("com.cbrc.tokens.operators.NotEqualOperatorToken");
		this.add("com.cbrc.tokens.operators.NotOperatorToken");
		this.add("com.cbrc.tokens.operators.OrOperatorToken");
		this.add("com.cbrc.tokens.operators.PlusOperatorToken");
		this.add("com.cbrc.tokens.operators.AssignmentOperatorToken");
		this.add("com.cbrc.tokens.operators.CloseBlockToken");
		this.add("com.cbrc.tokens.operators.CloseGroupingToken");
		this.add("com.cbrc.tokens.operators.SemicolonToken");
		this.add("com.cbrc.tokens.operators.OpenBlockToken");
		this.add("com.cbrc.tokens.operators.OpenGroupingToken");
		this.add("com.cbrc.tokens.identifiers.CharVariableIdentifierToken");
		this.add("com.cbrc.tokens.identifiers.DoubleVariableIdentifierToken");
		this.add("com.cbrc.tokens.identifiers.FloatVariableIdentifierToken");
		this.add("com.cbrc.tokens.identifiers.FunctionIdentifierToken");
		this.add("com.cbrc.tokens.identifiers.IntegerVariableIdentifierToken");
		this.add("com.cbrc.tokens.identifiers.LongVariableIdentifierToken");
		this.add("com.cbrc.tokens.literals.CharacterLiteralToken");
		this.add("com.cbrc.tokens.literals.FloatLiteralToken");
		this.add("com.cbrc.tokens.literals.IntegerLiteralToken");
		this.add("com.cbrc.tokens.literals.StringLiteralToken");
		this.add("com.cbrc.tokens.operators.ColonToken");
		this.add("com.cbrc.tokens.operators.SeparatorToken");
	}
	
}
