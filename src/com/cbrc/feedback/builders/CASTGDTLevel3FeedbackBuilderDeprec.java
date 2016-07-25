package com.cbrc.feedback.builders;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Scanner;

import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTLevel3FeedbackBuilderDeprec extends CASTGDTFeedbackBuilder {

	public CASTGDTLevel3FeedbackBuilderDeprec(HeadGDTNode headGDTNode) {
		super(headGDTNode);
	}

	@Override
	public String getFeedbackText(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		StringBuilder strBuilder = new StringBuilder("This buggy code is similar to your code:");
		System.out.println(bestPlan.toString());
		FileInputStream inputStream = new FileInputStream(((TranslationUnitNode) bestPlan.getASTNode()).getSource());
		try {
			DataInputStream dataStream = new DataInputStream(inputStream);
			try {
				Scanner sc = new Scanner(dataStream);
				try {
					while (sc.hasNext()) {
						strBuilder.append(sc.nextLine());
						strBuilder.append("\n");
					}
				} finally {
					sc.close();
				}
			} finally {
				dataStream.close();
			}
		} finally {
			inputStream.close();
		}
		
		strBuilder.append("\nIt might be helpful if you compare your code with this code to discover your bugs.");
		
		return strBuilder.toString();
	}

	@Override
	public String getFeedbackTextAsHTML(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		StringBuilder strBuilder = new StringBuilder("<p>This buggy code is similar to your code:</p>");
		FileInputStream inputStream = new FileInputStream(((TranslationUnitNode) bestPlan.getASTNode()).getSource());
		try {
			DataInputStream dataStream = new DataInputStream(inputStream);
			try {
				Scanner sc = new Scanner(dataStream);
				try {
					strBuilder.append("<p><code>");
					while (sc.hasNext()) {
						strBuilder.append(sc.nextLine());
						strBuilder.append("<br />");
					}
					strBuilder.append("</code></p>");
				} finally {
					sc.close();
				}
			} finally {
				dataStream.close();
			}
		} finally {
			inputStream.close();
		}
		
		strBuilder.append("<p>It might be helpful if you compare your code with this code to discover your bugs.</p>");
		
		return strBuilder.toString();
	}

}
