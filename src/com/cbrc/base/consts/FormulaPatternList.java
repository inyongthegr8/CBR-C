package com.cbrc.base.consts;

import java.util.ArrayList;

public class FormulaPatternList extends ArrayList<String>{

	private static final long serialVersionUID = 4659510601368454269L;
	
	public FormulaPatternList() {
		super();
		initContents();
	}
	
	private void initContents() {
		this.add("com.cbrc.nodes.patterns.formulapattern.DigitShiftPattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.DigitAppendPattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.CounterPatternNode");
		this.add("com.cbrc.nodes.patterns.formulapattern.DigitExtractionPattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.DigitRemovalPattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.AccumulatorPatternNode");
		this.add("com.cbrc.nodes.patterns.formulapattern.InitializeToOnePattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.InitializeToZeroPattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.InitializePattern");
		this.add("com.cbrc.nodes.patterns.formulapattern.FormulaPatternNode");
	}
}
