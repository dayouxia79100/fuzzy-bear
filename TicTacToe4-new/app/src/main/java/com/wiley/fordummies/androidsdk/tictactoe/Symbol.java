package com.wiley.fordummies.androidsdk.tictactoe;

public class Symbol {
	private enum MARK { X, O, Blank }
	private MARK value=null;
	private static Symbol SymbolX=null;
	private static Symbol SymbolO=null;
	private static Symbol SymbolBlank=null;
	
	private Symbol(){/* Empty PRIVATE constructor to enforce Singleton */}
	
	public static final Symbol SymbolXCreate() { 
		if (SymbolX == null){
			SymbolX = new Symbol();
			SymbolX.value = MARK.X;
		}
		return SymbolX;
	}
	
	public static final Symbol SymbolOCreate() { 
		if (SymbolO == null){
			SymbolO = new Symbol();
			SymbolO.value = MARK.O;
		}
		return SymbolO;
	}
	public static final Symbol SymbolBlankCreate() { 
		if (SymbolBlank == null){
			SymbolBlank = new Symbol();
			SymbolBlank.value = MARK.Blank;
		}
		return SymbolBlank;
	}
	
	public String toString(){
		if (value == MARK.X) 
			return "X";
		else if (value == MARK.O) 
			return "O";
		return "";
	}
}


