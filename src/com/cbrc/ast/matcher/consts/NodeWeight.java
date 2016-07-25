package com.cbrc.ast.matcher.consts;

import com.cbrc.base.Node;
import com.cbrc.nodes.FunctionNode;
import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.expressions.ExpressionNode;
import com.cbrc.nodes.functions.FunctionBodyNode;
import com.cbrc.nodes.functions.FunctionHeaderNode;
import com.cbrc.nodes.functions.body.BodyProperNode;
import com.cbrc.nodes.functions.body.VariableDeclarationNode;
import com.cbrc.nodes.statements.StatementNode;
import com.cbrc.tokens.keywords.CharKeywordToken;
import com.cbrc.tokens.keywords.DoubleKeywordToken;
import com.cbrc.tokens.keywords.FloatKeywordToken;
import com.cbrc.tokens.keywords.IntKeywordToken;
import com.cbrc.tokens.keywords.LongKeywordToken;
import com.cbrc.tokens.operators.CloseGroupingToken;
import com.cbrc.tokens.operators.OpenGroupingToken;
import com.cbrc.tokens.operators.SemicolonToken;

public class NodeWeight {

	public static double getWeight(Node node) {
		//TODO: create instance checking to determine the weight of this node.
		//		set intermediary (bodyproper, etc) nodes weight to 0.
		
		if (node instanceof TranslationUnitNode) return 0.0;
		else if (node instanceof FunctionNode) return 0.0;
		else if (node instanceof FunctionHeaderNode) return 0.0;
		else if (node instanceof FunctionBodyNode) return 0.0;
		else if (node instanceof IntKeywordToken) return 0.0;
		else if (node instanceof FloatKeywordToken) return 0.0;
		else if (node instanceof DoubleKeywordToken) return 0.0;
		else if (node instanceof LongKeywordToken) return 0.0;
		else if (node instanceof CharKeywordToken) return 0.0;
		else if (node instanceof VariableDeclarationNode) return 0.0;
		else if (node instanceof BodyProperNode) return 0.0;
		else if (node instanceof SemicolonToken) return 0.0;
		else if (node instanceof OpenGroupingToken) return 0.0;
		else if (node instanceof CloseGroupingToken) return 0.0;
		else if (node instanceof StatementNode) return 0.0;
		else if (node instanceof ExpressionNode) return 0.0;
		else return 1.0;
	}
}
