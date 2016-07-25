package com.cbrc.tokens.operators;

import com.cbrc.tokens.OperatorToken;
import com.cbrc.tokens.operators.enums.BlockType;

public abstract class BlockToken extends OperatorToken {

	protected BlockType blockType;

	public BlockToken(String token) {
		super(token);
	}

	public BlockType getBlockType() {
		return blockType == null ? BlockType.ANONYMOUS_BLOCK : blockType;
	}

	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	@Override
	protected String getAdditionalDetails() {
		return this.getBlockType().toString();
	}

}
