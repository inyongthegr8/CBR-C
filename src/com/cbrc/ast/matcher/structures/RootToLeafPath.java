package com.cbrc.ast.matcher.structures;

import java.util.ArrayList;

import com.cbrc.ast.matcher.consts.NodeWeight;
import com.cbrc.base.Node;

public class RootToLeafPath extends ArrayList<Node>{

	private static final long serialVersionUID = 2678002820179523840L;
	
	public RootToLeafPath(Node... nodes) {
		for (Node node : nodes) {
			this.add(node);
		}
	}
	
	public RootToLeafPath(Object[] array) {
		for (Object node:array) {
			this.add((Node) node);
		}
	}
	
	public double getMaximumScore() {
		double maximumScore = 0.0;
		
		for (Node node: this) {
			maximumScore += NodeWeight.getWeight(node);
		}
		
		return maximumScore;
	}
	
	
}

	
