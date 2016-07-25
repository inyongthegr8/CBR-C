package com.cbrc.ast.matcher;

import com.cbrc.ast.walker.DFSGenericTreeWalker;
import com.cbrc.base.Node;
import com.cbrc.base.consts.FormulaPatternList;
import com.cbrc.nodes.TranslationUnitNode;
import com.cbrc.nodes.patterns.PatternNode;
import com.cbrc.nodes.statements.AssignmentStatementNode;

public class FormulaPatternMatcher {
	
	private TranslationUnitNode codeTree;
	private boolean debug = false;
	private FormulaPatternList formulaPatternList;
	private ClassLoader classLoader;
	
	public FormulaPatternMatcher(TranslationUnitNode codeTree) {
		this.codeTree = codeTree;
		this.formulaPatternList = new FormulaPatternList();
		this.classLoader = this.getClass().getClassLoader();
	}
	
	public void match() throws Exception {
		DFSGenericTreeWalker dfsTreeWalker = new DFSGenericTreeWalker(this.codeTree);
		
		while (!dfsTreeWalker.isFinished()) {
			if (debug == true) System.out.println(dfsTreeWalker.toStringCurrentNode());
			
			if (dfsTreeWalker.getCurrentNode() instanceof AssignmentStatementNode) {
				Node currentNode = dfsTreeWalker.getCurrentNode();
				
				PatternNode pattern = generatePatternNode(currentNode);
				if (pattern != null){
					pattern.setPatternFormOfNodeToThis(currentNode);
					//dfsTreeWalker.setCurrentNode(pattern);
				}
			}
			
			dfsTreeWalker.nextNode();
		}
	}

	private PatternNode generatePatternNode(Node currentNode)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		for (String className:this.formulaPatternList) {
			Class<?> clazz = this.classLoader.loadClass(className);
			PatternNode patternType = (PatternNode) clazz.newInstance();
			
			if (patternType.isPattern(currentNode)) {
				return patternType;
			}
		}
		return null;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
