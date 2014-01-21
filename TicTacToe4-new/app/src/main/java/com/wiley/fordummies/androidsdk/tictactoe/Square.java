package com.wiley.fordummies.androidsdk.tictactoe;

public class Square {
	private int x;
	private int y;
	
	public Square(int x, int y, String value){
		this.x = x;
		this.y = y;
	}
	
	public Square(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
}
