package com.cbrc.nodes.gdt;

import com.cbrc.base.Node;

public abstract class GDTNode extends Node {
	
	private String descriptor;

	public GDTNode (String descriptor)
	{
		this.descriptor = descriptor;
	}
	
	@Override
	protected String getAdditionalDetails() {
		return "GDT NODE: ";
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	
	

}
