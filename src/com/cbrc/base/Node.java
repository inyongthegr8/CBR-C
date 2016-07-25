package com.cbrc.base;

import java.util.ArrayList;

import com.cbrc.base.exceptions.NotInSiblingListException;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.patterns.PatternNode;

public abstract class Node {

	private Node parentNode;
	private PatternNode patternForm;
	private ArrayList<Node> childNodes;
	private int index;
	
	
	
	public Node() {
		this.parentNode = null;
		this.childNodes = new ArrayList<Node>();
		this.index = 0;
	}

	public Node(Node parentNode) {
		super();
		this.parentNode = parentNode;
	}
	
	public Node(ArrayList<Node> childNodes) {
		super();
		this.childNodes = childNodes;
	}
	
	public Node(Node parentNode, ArrayList<Node> childNodes) {
		this.parentNode = parentNode;
		this.childNodes = childNodes;
	}
	
	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public ArrayList<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ArrayList<Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void addChild(Node child) {
		//ensures the parent is properly set
		child.setParentNode(this);
		child.setIndex(this.getChildNodes().size());
		this.getChildNodes().add(child);
	}
	
	public void addChild(Node child, int index) {
		//ensures the parent is properly set
		child.setParentNode(this);
		child.setIndex(index);
		for (int i = index; i < this.getChildNodes().size(); i++) {
			this.getChildNodes().get(i).setIndex(i + 1);
		}
		this.getChildNodes().add(index, child);
	}
	
	public void replaceChildAtIndex(Node child, int index) {
		child.setParentNode(this);
		child.setIndex(index);
		this.getChildNodes().set(index, child);
	}
	
	public boolean hasSiblings (ArrayList<Node> siblings) throws NotInSiblingListException {
		if (siblings.contains(this)) {
			int index = siblings.indexOf(this);
			if (index == siblings.size() - 1) return false;
			else return true;
		} else throw new NotInSiblingListException("", this, siblings);
	}
	
	public boolean hasChildren() {
		return !this.getChildNodes().isEmpty();
	}
	
	public boolean isLeafNode() {
		return this.getChildNodes().isEmpty();
	}

	/**
	 * Return blank string if you dont have anything else to
	 * display.
	 * @return Additional details when displaying the node as string
	 */
	protected abstract String getAdditionalDetails();
	
	public String getAdditionalNodeDetails() {
		return this.getAdditionalDetails();
	}
	
	public String toString() {
		if (this.getPatternForm() != null) {
			return  this.getPatternForm().getClass().getSimpleName() + ": " + this.getAdditionalDetails();
		}
		return  this.getClass().getSimpleName() + ": " + this.getAdditionalDetails();
	}
	
	public abstract Node generateThisNode(TokenizedCode tokens, Node parent);
	
	public void disconnect(){
		this.setParentNode(null);
		this.setChildNodes(null);
	}

	public PatternNode getPatternForm() {
		return patternForm;
	}

	public void setPatternForm(PatternNode patternForm) {
		this.patternForm = patternForm;
	}
	
}
