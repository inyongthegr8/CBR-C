package com.cbrc.gdt.builder;

import com.cbrc.nodes.gdt.GoalGDTNode;
import com.cbrc.nodes.gdt.HeadGDTNode;

public class CASTGDTStatics {
	
	public static GoalGDTNode getSuperGoal(HeadGDTNode headNode) {
		return (GoalGDTNode) headNode.getChildNodes().get(0);
	}

}
