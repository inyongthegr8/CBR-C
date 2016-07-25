package com.cbrc.lexers.factories;

import java.util.Stack;

import com.cbrc.base.Token;
import com.cbrc.base.consts.TokenClassList;
import com.cbrc.lexers.structs.TokenizedCode;

public class CASTTokenFactory {
	
	private Stack<String> currentMarker;
	private TokenClassList tokenList;
	private ClassLoader classLoader;
	
	public CASTTokenFactory() {
		this.currentMarker = new Stack<String>();
		this.tokenList = new TokenClassList();
		this.classLoader = this.getClass().getClassLoader();
	}
	
	public Token createToken(String token, String succeedingToken, TokenizedCode tokens) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Token tokenObject = null;
		//if (token.equals("nX")) System.out.println();
		for (String className:this.tokenList) {
			//if (token.equals("nX")) System.out.print(this.getClass().getName() + ": LOADING CLASS: " + className);
			Class<?> clazz = this.classLoader.loadClass(className);
			Token tokenType = (Token) clazz.newInstance();
			
			tokenObject = tokenType.generateCondition(token, tokens, currentMarker, succeedingToken);
			if (tokenObject != null) {
				//if (token.equals("nX"))System.out.println(": TOKEN GENERATED FOR TOKEN " + token + "!");
				return tokenObject;
			}//else if (token.equals("nX")) System.out.println();
			else {
				break;
			}
		}
		return null;
	}

}
