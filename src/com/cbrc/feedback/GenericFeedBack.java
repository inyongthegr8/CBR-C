package com.cbrc.feedback;

import com.cbrc.nodes.gdt.PlanGDTNode;

/* UNUSED */
public class GenericFeedBack extends FeedBack {
	
	public GenericFeedBack(PlanGDTNode node)
	{
		System.out.println("Feedback for " + node.getDescriptor());
	}
	

}
