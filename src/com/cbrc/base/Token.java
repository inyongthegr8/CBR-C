package com.cbrc.base;

import com.cbrc.lexers.structs.TokenizedCode;
import com.cbrc.tokens.interfaces.TokenGeneratable;

public abstract class Token extends Node implements TokenGeneratable{

	private String token;
	
	public Token (String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Return blank string if you dont have anything else to
	 * display.
	 * @return Additional details when displaying the node as string
	 */
	protected abstract String getAdditionalDetails();
	
	public String toString() {
		return this.getClass().getSimpleName() + " : " + this.getToken() + " : " + this.getAdditionalDetails(); 
	}
	
	/**
	 * This is only used by pure Nodes, might consider
	 * turning into an interface later, implemented by
	 * said pure Nodes. For now implemented as an abstract method
	 * in Node (super class of this) to ensure all pure Node
	 * subclasses inherits it.
	 * 
	 * Unless of course, this ends up being used by the
	 * NodeFactory ;)
	 */
	public Node generateThisNode(TokenizedCode tokens, Node parent) {
		return this;
	}

}
