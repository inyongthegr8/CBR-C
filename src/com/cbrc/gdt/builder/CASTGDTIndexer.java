package com.cbrc.gdt.builder;

import java.util.ArrayList;
import java.util.HashMap;

import com.cbrc.base.Node;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTIndexer {
	
	private class MappingPair {
		private int indexOfPlanNode;
		private int indexOfPattern;
		
		public MappingPair(int indexOfPlanNode, int indexOfPattern) {
			this.indexOfPattern = indexOfPattern;
			this.indexOfPlanNode = indexOfPlanNode;
		}

		public int getIndexOfPlanNode() {
			return indexOfPlanNode;
		}

		public int getIndexOfPattern() {
			return indexOfPattern;
		}
	
	}

	private Node parentNode;
	/* 
	 * String is the patternName (getClass().getSimpleName())
	 * HashMap<Integer, Integer>
	 *   Integer is the index of the planNode in the parentNode
	 *   Integer is the index of the patternName in the patterns ArrayList of the planNode
	 * 
	 * */
	private HashMap<String, ArrayList<MappingPair>> indexMap;
	
	public CASTGDTIndexer(Node parentNode) {
		this.parentNode = parentNode;
		this.indexMap = new HashMap<String, ArrayList<MappingPair>>();
	}
	
	public void nonIndex(PlanGDTNode newPlan) {
		newPlan.setParentNode(this.parentNode);
		this.parentNode.addChild(newPlan);
	}
	
	public void index(PlanGDTNode newPlan) {
		newPlan.setParentNode(this.parentNode);
		this.parentNode.addChild(newPlan);
		
		ArrayList<String> indexString = newPlan.getPatterns();
		
		for(int i = 0; i < indexString.size(); i++) {
			String s = indexString.get(i);
			if(indexMap.get(s) != null) {
				ArrayList<MappingPair> mapping = indexMap.get(s);
				mapping.add(new MappingPair(newPlan.getIndex(), i));
			} else {
				ArrayList<MappingPair> mapping = new ArrayList<MappingPair>();
				MappingPair mappingPair = new MappingPair(newPlan.getIndex(), i);
				mapping.add(mappingPair);
				indexMap.put(s, mapping);
				
			}
		}
		
		//this.parentNode.addChild(newPlan, int index)
	}
	
	public ArrayList<Node> indexedSearch(PlanGDTNode plan) {
		ArrayList<Node> results = new ArrayList<Node>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ArrayList<Integer> prevIndices = new ArrayList<Integer>();
		for (int i = 0; i < this.parentNode.getChildNodes().size(); i++) {
			indices.add(i);
			prevIndices.add(i);
		}
		
		Boolean isEmpty = false;
		ArrayList<String> indexString = plan.getPatterns();
		
		for(int i = 0; i < indexString.size() && isEmpty == false; i++) {
			
			prevIndices = copyContents(indices);
			ArrayList<MappingPair> mapping = indexMap.get(indexString.get(i));
			
			eliminateFromIndices(indices, i, mapping);
			
			if (indices.isEmpty()) {
				isEmpty = true;
				indices = copyContents(prevIndices);
			}
			// copyContents is not working (values are not saved properly to the array)
		}
		
		for (int i = 0; i < indices.size(); i++) {
			results.add((PlanGDTNode) this.parentNode.getChildNodes().get(indices.get(i)));
		}
		return results;
	}

	private void eliminateFromIndices(ArrayList<Integer> indices, int i, ArrayList<MappingPair> mapping) {
		ArrayList<Integer> tempArray = new ArrayList<Integer>();
		for(int j = 0; j < mapping.size(); j++) {
			if (mapping.get(j).getIndexOfPattern() == i) {
				tempArray.add(mapping.get(j).getIndexOfPlanNode());
			}
		}
		
		for(int j = 0; j < indices.size(); j++) {
			if (!tempArray.contains(indices.get(j))) {
				indices.remove(j);
			}
		}
	}
	
	private ArrayList<Integer> copyContents(ArrayList<Integer> source) {
		ArrayList<Integer> destination = new ArrayList<Integer>();
		for(int i = 0; i < source.size(); i++) {
			destination.add(source.get(i).intValue());
		}
		return destination;
	}
}
