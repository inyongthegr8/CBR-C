package com.cbrc.nodes.gdt;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;

public class HeadGDTNode extends GDTNode {
	
	public HeadGDTNode()
	{
		super("Head GDT Node");
	}
	
	public HeadGDTNode(String descriptor)
	{
		super(descriptor);
	}
	
	protected String getAdditionalDetails()
	{
		return super.getAdditionalDetails() + "HEAD GDT NODE: " + this.getDescriptor();
	}
	
	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return null;
	}

}
