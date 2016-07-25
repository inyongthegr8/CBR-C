package com.cbrc.base.exceptions;

import java.util.ArrayList;

import com.cbrc.base.Node;

public class NotInSiblingListException extends Exception {

	private static final long serialVersionUID = -7159742116475072298L;

	public NotInSiblingListException(String message) {
		super(message + ": Node not found in sibling list");
	}

	public NotInSiblingListException(String message, Node node, ArrayList<Node> siblings) {
		super(message + ": " + node.toString() + " not found in " + siblings.toString());
	}

}
