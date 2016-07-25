package com.cbrc.ast.matcher.factory;

import java.util.ArrayList;
import java.util.Stack;

import com.cbrc.ast.matcher.structures.RootToLeafPath;
import com.cbrc.ast.walker.DFSGenericTreeWalker;
import com.cbrc.base.Node;
import com.cbrc.nodes.TranslationUnitNode;

public class RootToLeafPathFactory {
	
	private Node rootNode;
	private ArrayList<RootToLeafPath> rootToLeafPaths;
	
	public RootToLeafPathFactory() {
		this.rootToLeafPaths = new ArrayList<RootToLeafPath>();
	}
	
	public RootToLeafPathFactory(TranslationUnitNode rootNode) {
		this();
		this.rootNode = rootNode;
	}
	
	public Node getRootNode() {
		return this.rootNode;
	}
	
	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
	
	public ArrayList<RootToLeafPath> generateRootToLeafPaths() throws Exception {
		
		DFSGenericTreeWalker dfsWalker = new DFSGenericTreeWalker(rootNode);
		
		while (!dfsWalker.isFinished()) {
			while (!dfsWalker.getCurrentNode().isLeafNode()) dfsWalker.nextNode();
			
			Stack<Node> traversedNodes = dfsWalker.getTraversedNodes();
			RootToLeafPath path = new RootToLeafPath (traversedNodes.toArray());
			path.add(dfsWalker.getCurrentNode());
			this.rootToLeafPaths.add(path);
			
			if (!dfsWalker.isFinished()) dfsWalker.nextNode();
		}
		
		return this.rootToLeafPaths;
	}

}
