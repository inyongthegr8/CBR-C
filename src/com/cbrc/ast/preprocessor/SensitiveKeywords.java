package com.cbrc.ast.preprocessor;

public enum SensitiveKeywords {

	IF {
		@Override
		public String toString()
		{
			return "IF STATEMENT";
		}
	},
	
	ELSE {
		@Override
		public String toString()
		{
			return "ELSE STATEMENT";
		}
	},
	
	ELSEIF {
		@Override
		public String toString()
		{
			return "ELSEIF STATEMENT";
		}
	},
	
	SWITCH {
		@Override
		public String toString()
		{
			return "SWITCH STATEMENT";
		}
	}, 
	
	WHILE {
		@Override
		public String toString()
		{
			return "WHILE STATEMENT";
		}
	}, 
	
	DOWHILE {
		@Override
		public String toString()
		{
			return "DOWHILE STATEMENT";
		}
	}, 
	
	FOR {
		@Override
		public String toString()
		{
			return "FOR STATEMENT";
		}
	}
	
}
