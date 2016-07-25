package com.cbrc.nodes.gdt;
import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;


public class GenericGDTNode extends GDTNode {

	public GenericGDTNode(String descriptor) {
		super(descriptor);
	}

	protected String getAdditionalDetails()
	{
		return super.getAdditionalDetails() + "Generic GDT Node: ";
	}
	
	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return null;
	}

}
