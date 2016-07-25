package com.cbrc.nodes.gdt;

import java.util.ArrayList;
import java.util.UUID;

import com.cbrc.base.Node;
import com.cbrc.lexers.structs.TokenizedCode;

public class PlanGDTNode extends GDTNode {
	
	private Node astNode;
	private boolean isFaulty;
	private UUID ID;
	private int DBID;
	private ArrayList<String> patterns;
	
	public PlanGDTNode() {
		super("No Descriptor");
		this.isFaulty = false;
		this.ID = UUID.randomUUID();
		this.patterns = new ArrayList<String>();
	}
	
	public PlanGDTNode(String descriptor) {
		super(descriptor);
		this.isFaulty = false;
		this.patterns = new ArrayList<String>();
	}

	protected String getAdditionalDetails()
	{
		return super.getAdditionalDetails() + "PLAN GDT NODE: " + this.getDescriptor() == null?"No additional details":this.getDescriptor();
	}

	@Override
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getASTNode() {
		return astNode;
	}

	/* Mirror node is its actual node in the code */
	public void setASTNode(Node astNode) {
		this.astNode = astNode;
	}

	public boolean isFaulty() {
		return isFaulty;
	}

	public void setFaulty(boolean isFaulty) {
		this.isFaulty = isFaulty;
	}

	public UUID getID() {
		return ID;
	}

	public void setID(UUID iD) {
		ID = iD;
	}

	public int getDBID() {
		return DBID;
	}

	public void setDBID(int dBID) {
		DBID = dBID;
	}
	
	public void addToPatterns(String pattern) {
		this.patterns.add(pattern);
	}
	
	public ArrayList<String> getPatterns() {
		return this.patterns;
	}

}
