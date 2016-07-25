package com.cbrc.parsers;

import java.io.File;

import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.TranslationUnitNode;

public class CASTParser {

	private TokenizedCode tokens;
	private TranslationUnitNode parentNode;
	private File source;
	
	public CASTParser(TokenizedCode tokens, File source) {
		this.tokens = tokens;
		this.source = source;
		parentNode = new TranslationUnitNode(source);
		
		this.parse();
	}

	public TokenizedCode getTokens() {
		return tokens;
	}

	public void setTokens(TokenizedCode tokens) {
		this.tokens = tokens;
	}

	public TranslationUnitNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TranslationUnitNode parentNode) {
		this.parentNode = parentNode;
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}
	
	private void parse() {
		// NOTE: This might not be the best way to do this, but this is sufficient
		this.parentNode = (TranslationUnitNode) this.parentNode.generateThisNode(this.tokens, null);
	}

}
