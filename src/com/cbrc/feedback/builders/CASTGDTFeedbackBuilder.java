package com.cbrc.feedback.builders;

import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;

public abstract class CASTGDTFeedbackBuilder {
	
	HeadGDTNode headGDTNode;
	
	public CASTGDTFeedbackBuilder(HeadGDTNode headGDTNode) {
		this.headGDTNode = headGDTNode;
	}
	
	public abstract String getFeedbackText(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception;
	
	public abstract String getFeedbackTextAsHTML(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception;

}
