package com.cbrc.tokens.operators;

import com.cbrc.tokens.OperatorToken;
import com.cbrc.tokens.operators.enums.GroupingType;

public abstract class GroupingToken extends OperatorToken {

	protected GroupingType groupingType;

	public GroupingToken(String token) {
		super(token);
	}

	public GroupingType getGroupingType() {
		return groupingType == null ? GroupingType.EXPRESSION_GROUPING : groupingType;
	}

	public void setGroupingType(GroupingType groupingType) {
		this.groupingType = groupingType;
	}

	@Override
	protected String getAdditionalDetails() {
		return this.getGroupingType().toString();
	}

}