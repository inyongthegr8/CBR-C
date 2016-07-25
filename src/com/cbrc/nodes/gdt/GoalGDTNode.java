package com.cbrc.nodes.gdt;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;

public class GoalGDTNode extends GDTNode {

	private int DBID; 
	
	public GoalGDTNode(String descriptor) {
		super(descriptor);
	}

	protected String getAdditionalDetails()
	{
		return super.getAdditionalDetails() + "GOAL GDT NODE: " + this.getDescriptor() == null?"No additional details":this.getDescriptor();
	}
	
	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getDBID() {
		return DBID;
	}

	public void setDBID(int dBID) {
		DBID = dBID;
	}

}
