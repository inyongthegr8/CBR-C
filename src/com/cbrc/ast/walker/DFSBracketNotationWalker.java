package com.cbrc.ast.walker;

import java.util.ArrayList;

import com.cbrc.base.Node;

public class DFSBracketNotationWalker extends DFSGenericTreeWalker {

	private StringBuilder bracketNotation;
	
	public DFSBracketNotationWalker(Node headNode) {
		super(headNode);
		bracketNotation = new StringBuilder();
	}
	
	public void nextNode() throws Exception{
		bracketNotation.append("{").append(this.getCurrentNode().getClass().getSimpleName());
		this.traversedNodes.push(this.getCurrentNode());
		ArrayList<Node> children = this.getCurrentNode().getChildNodes();
		Node testNode = null;
		
		if (!children.isEmpty()) {
			this.currentNode = children.get(0);
		} else {
			testNode = this.traversedNodes.pop();
			bracketNotation.append("}");
			if (!this.traversedNodes.empty()) {
				children = this.traversedNodes.peek().getChildNodes();
				while (!this.traversedNodes.empty() && !testNode.hasSiblings(children)) {
					testNode = this.traversedNodes.pop();
					bracketNotation.append("}");
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
	
	public String getBracketNotation() throws Exception {
		while (!this.isFinished()) this.getNextNode();
		return this.bracketNotation.toString();
	}

}
