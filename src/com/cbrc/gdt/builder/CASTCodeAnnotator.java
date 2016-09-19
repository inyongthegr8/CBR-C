package com.cbrc.gdt.builder;

import java.io.File;
import java.io.IOException;

import com.cbrc.ast.matcher.BlockPatternMatcher;
import com.cbrc.ast.matcher.FormulaPatternMatcher;
import com.cbrc.ast.matcher.LogicalExpressionPatternMatcher;
import com.cbrc.ast.preprocessor.CASTPreprocessor;
import com.cbrc.lexers.CASTLexer;
import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.parsers.CASTParser;

public class CASTCodeAnnotator {

	private File source;
	private TranslationUnitNode headNode;
	private boolean debug;

	public CASTCodeAnnotator () {
		this.source = null;
		this.headNode = null;
	}
	
	public CASTCodeAnnotator (File source) {
		this.source = source;
		this.headNode = null;
	}
	
	public void annotateCode() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, Exception {
		this.headNode = createParentNode(this.source);
		
		FormulaPatternMatcher fpm = new FormulaPatternMatcher(this.headNode);
		fpm.match();
		
		LogicalExpressionPatternMatcher epm = new LogicalExpressionPatternMatcher(this.headNode);
		epm.match();
		
		BlockPatternMatcher bpm = new BlockPatternMatcher(this.headNode);
		bpm.match();
	}
	
	private TranslationUnitNode createParentNode(File file) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
		CASTPreprocessor preprocessor = new CASTPreprocessor(file);
		preprocessor.preprocess();

		CASTLexer lexer = new CASTLexer(preprocessor.getSource());
		if (this.debug == true) lexer.setDebug(true);
		TokenizedCode tokens = lexer.getTokens();
		CASTParser parser = new CASTParser(tokens, file);
		TranslationUnitNode parentNode = parser.getParentNode();
	
		return parentNode;
	}
	
	public TranslationUnitNode getHeadNode() {
		return headNode;
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
