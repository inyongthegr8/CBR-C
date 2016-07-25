package com.cbrc.feedback.builders;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Scanner;

import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.gdt.HeadGDTNode;
import com.cbrc.nodes.gdt.PlanGDTNode;

public class CASTGDTLevel4FeedbackBuilder extends CASTGDTFeedbackBuilder {

	public CASTGDTLevel4FeedbackBuilder(HeadGDTNode headGDTNode) {
		super(headGDTNode);
	}

	@Override
	public String getFeedbackText(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		StringBuilder builder = new StringBuilder("\n\nThis is the correct code: \n\n");
		
		FileInputStream inputStream = new FileInputStream(((TranslationUnitNode) bestPlan.getASTNode()).getSource());
		try {
			DataInputStream dataStream = new DataInputStream(inputStream);
			try {
				Scanner sc = new Scanner(dataStream);
				try {
					while (sc.hasNext()) {
						builder.append(sc.nextLine());
						builder.append("\n");
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
		
		builder.append("\n\n");
		
		return builder.toString();
	}

	@Override
	public String getFeedbackTextAsHTML(PlanGDTNode planNode, PlanGDTNode bestPlan) throws Exception {
		StringBuilder builder = new StringBuilder("<p>This is the correct code: </p>");
		
		builder.append("<code>");
		FileInputStream inputStream = new FileInputStream(((TranslationUnitNode) bestPlan.getASTNode()).getSource());
		try {
			DataInputStream dataStream = new DataInputStream(inputStream);
			try {
				Scanner sc = new Scanner(dataStream);
				try {
					while (sc.hasNext()) {
						builder.append(sc.nextLine());
						builder.append("<br />");
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
		
		builder.append("</code>");
		return builder.toString();
	}

}
