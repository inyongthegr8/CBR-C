package com.cbrc.feedback.builders;

import java.util.ArrayList;

public class PatternList {
	
	private ArrayList<String> patterns;
	private ArrayList<String> syntaxes;
	
	public PatternList() {
		patterns = new ArrayList<String>();
		syntaxes = new ArrayList<String>();
		initContents();
	}

	private void initContents() {
		this.patterns.add("DigitShiftPattern");
		this.patterns.add("DigitAppendPattern");
		this.patterns.add("CounterPatternNode");
		this.patterns.add("DigitExtractionPattern");
		this.patterns.add("DigitRemovalPattern");
		this.patterns.add("AccumulatorPatternNode");
		this.patterns.add("InitializeToOnePattern");
		this.patterns.add("InitializeToZeroPattern");
		this.patterns.add("InitializePattern");
		this.patterns.add("ReverseSentinelValuePattern");
		this.patterns.add("SentinelValuePattern");
		
		this.syntaxes.add("[VARIABLE] = [NUMBER_VAR] * [POWER_OF_TEN_LITERAL]");
		this.syntaxes.add("[NUMBER_VAR] = [NUMBER_VAR] * [POWER_OF_TEN_LITERAL] + [DIGIT]");
		this.syntaxes.add("[COUNTER_VAR] = [COUNTER_VAR] [PLUS] 1");
		this.syntaxes.add("[VARIABLE] = [NUMBER_VAR] % [POWER_OF_TEN_LITERAL]");
		this.syntaxes.add("[VARIABLE] = [NUMBER_VAR] / [POWER_OF_TEN_LITERAL]");
		this.syntaxes.add("[ACCUMULATOR_VAR] = [ACCUMULATOR_VAR] [OPERATOR] [VALUE]");
		this.syntaxes.add("[VARIABLE IDENTIFIER] = 1");
		this.syntaxes.add("[VARIABLE IDENTIFIER] = 0");
		this.syntaxes.add("[VARIABLE IDENTIFIER] = [VALUE]");
		this.syntaxes.add("[SENTINEL_VAR] == [SENTINEL_VAL_LITERAL]");
		this.syntaxes.add("[SENTINEL_VAR] != [SENTINEL_VAL_LITERAL]");
	}
	
	public boolean contains(String s) {
		if (patterns.contains(s)) return true;
		return false;
	}
	
	public String getSyntax(String s) {
		if (patterns.contains(s)) {
			int i = patterns.indexOf(s);
			return syntaxes.get(i);
		}
		return "";
	}
	
	

}
