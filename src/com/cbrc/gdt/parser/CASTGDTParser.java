package com.cbrc.gdt.parser;

import java.io.File;

import com.cbrc.nodes.TranslationUnitNode;

public class CASTGDTParser {  
	
	private File source; // File to parse against GDT
	private TranslationUnitNode headNode; // GDT head node
	
	public CASTGDTParser() {
		this.source = null;
		this.headNode = null;
	}
	
	public CASTGDTParser(File source, TranslationUnitNode headnode) {
		this.source = source;
		this.headNode = headnode;
	}
	
	public CASTGDTParserResults gdtParse() {
		// TODO: CREATE GDT PARSER HERE
		return null;
	}
	
	public CASTGDTParserResults gdtParse(File source, TranslationUnitNode headnode) {
		this.source = source;
		this.headNode = headnode;
		return this.gdtParse();
	}
	
	public CASTGDTParserResults gdtParse(TranslationUnitNode headnode) {
		this.headNode = headnode;
		if (this.source != null) {
			return this.gdtParse();
		}
		return null;
	}
	
	public File getSource() {
		return source;
	}
	
	public void setSource(File source) {
		this.source = source;
	}
	
	public TranslationUnitNode getHeadNode() {
		return headNode;
	}
	
	public void setHeadNode(TranslationUnitNode headNode) {
		this.headNode = headNode;
	}
	
	
	
	
}
