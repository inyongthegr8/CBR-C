package com.cbrc.base.consts;

public abstract class KeywordList {

	public static boolean isKeyWord(String token) {
		if (token.equals("int")    ||
			token.equals("float")  ||
			token.equals("char")   ||
			token.equals("long")   ||
			token.equals("double") ||
			token.equals("void")   ||
			token.equals("scanf")  ||
			token.equals("printf") ||
			token.equals("if")     ||
			token.equals("else")   ||
			token.equals("switch") ||
			token.equals("case")   ||
			token.equals("break")  ||
			token.equals("for")    ||
			token.equals("while")  ||
			token.equals("do")     ||
			token.equals("return")) return true;
		return false;
	}
	
}
