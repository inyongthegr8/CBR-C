package com.cbrc.ast.walker;

import com.cbrc.base.Node;

public abstract class TreeWalker {
	
	protected Node headNode;
	protected Node currentNode;
	protected boolean isFinished;
	
	public TreeWalker() {
		this.isFinished = false;
	}
	
	public Node getHeadNode() {
		return this.headNode;
	}
	
	public void setHeadNode(Node headNode) {
		this.headNode = headNode;
	}
	
	public Node getCurrentNode() {
		return this.currentNode;
	}
	
	public void setCurrentNode(Node node) {
		this.currentNode =  node;
	}
	
	public abstract void nextNode() throws Exception;
	
	public Node getNextNode() throws Exception {
		this.nextNode();
		return this.getCurrentNode();
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	
	public void reset() {
		this.isFinished = false;
		this.headNode = this.getHeadNode();
	}

}
