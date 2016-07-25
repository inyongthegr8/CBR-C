package com.cbrc.ast.walker;

import java.util.ArrayList;
import java.util.Stack;

import com.cbrc.base.Node;

public class DFSGenericTreeWalker extends TreeWalker {

	protected Node headNode;
	protected Stack<Node> traversedNodes;
	
	public DFSGenericTreeWalker(Node headNode) {
		super();
		this.headNode = headNode;
		this.currentNode = this.headNode;
		this.traversedNodes = new Stack<Node>();
	}

	@Override
	public void nextNode() throws Exception, IndexOutOfBoundsException{
		this.traversedNodes.push(this.getCurrentNode());
		ArrayList<Node> children = this.getCurrentNode().getChildNodes();
		Node testNode = null;
		
		if (!children.isEmpty()) {
			this.currentNode = children.get(0);
		} else {
			testNode = this.traversedNodes.pop();
			if (!this.traversedNodes.empty()) {
				children = this.traversedNodes.peek().getChildNodes();
				while (!this.traversedNodes.empty() && !testNode.hasSiblings(children)) {
					testNode = this.traversedNodes.pop();
					if (!this.traversedNodes.empty()) {
						children = this.traversedNodes.peek().getChildNodes();
					}
				}
			}
			

			if (!traversedNodes.empty()) {
				//int currentIndex = children.indexOf(testNode);
				int currentIndex = testNode.getIndex();
				this.currentNode = children.get(currentIndex + 1);
			} else {
				this.currentNode = this.headNode;
				this.isFinished = true;
			}
		}
		
	}
	
	public String toStringCurrentNode() {
		StringBuilder nodeToString = new StringBuilder();
		for (int i = 0; i < this.traversedNodes.size(); i++) {
			nodeToString.append("|_");
		}
		nodeToString.append(this.getCurrentNode().toString());
		return nodeToString.toString();
	}
	
	public Stack<Node> getTraversedNodes() {
		return this.traversedNodes;
	}



}
