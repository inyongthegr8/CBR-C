package com.cbrc.nodes.patterns;

import java.util.ArrayList;

import com.cbrc.base.Node;

public abstract class PatternNode extends Node {
	
	private String additionalDetails;

	public void setPatternFormOfNodeToThis(Node node) {
//		this.setIndex(node.getIndex());
//		
//		this.setParentNode(node.getParentNode());
//		node.getParentNode().replaceChildAtIndex(this, node.getIndex());
//		
//		this.setChildNodes(node.getChildNodes());
//		for (Node child:node.getChildNodes()) {
//			child.setParentNode(this);
//		}
//		this.setAdditionalDetails(node.getAdditionalNodeDetails());
//		node.disconnect();
		node.setPatternForm(this);
	}
	
	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}
	
	public String getAdditionalDetails() {
		return this.additionalDetails;
	}
	
	public abstract boolean isPattern(Node node);
	
	public abstract ArrayList<String> isPatternWithFeedback(Node node);

}
