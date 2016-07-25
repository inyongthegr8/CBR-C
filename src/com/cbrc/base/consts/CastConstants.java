package com.cbrc.base.consts;

public class CastConstants {
	public static final String IDENTIFIER_REGEX = "(_?[a-zA-Z]{1,}(_[a-zA-Z]{1,})*)|(([a-zA-Z]{1,})([0-9]{1,})*)*|(\"{1}.*\"{1})|('.{1}')";
	public static final String FLOAT_LITERAL_REGEX = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
	public static final String INT_LITERAL_REGEX = "^[-+]?[0-9]*([eE][-+]?[0-9]+)?$";
	public static final String NEWLINE = System.getProperty("line.separator");
}
