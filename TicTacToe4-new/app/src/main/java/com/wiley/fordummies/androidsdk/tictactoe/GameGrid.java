package com.wiley.fordummies.androidsdk.tictactoe;

import java.util.ArrayList;

public class GameGrid {
	public static final int SIZE=3;
	private Symbol[][] grid=null;
	
	GameGrid(){// Constructor. Initializes the grid to blanks
		grid = new Symbol[SIZE][SIZE];
		for (int i=0; i<SIZE;i++)
			for(int j=0; j<SIZE; j++)
				grid[i][j]=Symbol.SymbolBlankCreate();
	}
	
	public void setValueAtLocation(int x, int y, Symbol value){
		if (((x>=0)&&(x< SIZE)) && ((y>=0)&&(y<SIZE)))
			grid[x][y] = value;
	}
	public Symbol getValueAtLocation (int x, int y){
		Symbol returnValue=null;
		if (((x>=0)&&(x< SIZE)) && ((y>=0)&&(y<SIZE)))returnValue = grid[x][y];
		return returnValue;
	}
	public boolean isRowFilled (int row){//Entire row has the same symbol
		boolean isFilled=false;
		boolean foundMismatch=false;
		for(int col = 0; (col < SIZE)&&(!foundMismatch); col++){
			if(grid[row][0] != grid[row][col]) 
				foundMismatch=true;
		}
		isFilled = (!foundMismatch) && (grid[row][0]!=Symbol.SymbolBlankCreate());
		return isFilled;
	}
	public boolean isColumnFilled (int column){//Entire column has the same symbol
		boolean isFilled=false;
		boolean foundMismatch=false;
		for(int row = 0; (row < SIZE)&&(!foundMismatch); row++){
			if(grid[0][column] != grid[row][column]) foundMismatch=true;
		}
		isFilled = (!foundMismatch) && (grid[0][column]!=Symbol.SymbolBlankCreate());
		return isFilled;
	}
	public boolean isLeftToRightDiagonalFilled(){//Left diagonal has the same symbol
		boolean isFilled=false;
		boolean foundMismatch=false;
		for(int index = 0; (index < SIZE)&&(!foundMismatch); index++){
			if(grid[0][0] != grid[index][index]) foundMismatch=true;
		}
		isFilled = (!foundMismatch) && (grid[0][0]!=Symbol.SymbolBlankCreate());
		return isFilled;
	}
	public boolean isRightToLeftDiagonalFilled(){//Right diagonal has the same symbol
		int foundIndex=-1;
		boolean isFilled=false;
		boolean foundMismatch=false;
		System.out.println("Entering isRightToLeftDiagonalFilled");
		for(int index = SIZE-1; (index >= 0)&&(!foundMismatch); index--){
			System.out.println(">"+grid[0][SIZE-1].toString()+"<   >"+grid[index][index].toString()+"<");
			if(grid[0][SIZE-1] != grid[SIZE-1-index][index]){
				foundMismatch=true;
				foundIndex=index;
			}
		}
		isFilled = (!foundMismatch) && (grid[0][SIZE-1]!=Symbol.SymbolBlankCreate());
		System.out.println("Leaving isRightToLeftDiagonalFilled"+foundMismatch+"index>"+foundIndex+"<>"+grid[0][SIZE-1].toString()+"<");
		return isFilled;
	}
	
	public ArrayList<Square> getEmptySquares(){//Get the unfilled squares
		ArrayList<Square> list = new ArrayList<Square>();
		for(int i = 0; i < GameGrid.SIZE; i++) {
			for(int j = 0; j < GameGrid.SIZE; j++) {
				if(grid[i][j] == Symbol.SymbolBlankCreate()) {
					Square b = new Square(i, j);
					list.add(b);
				}
			}
		}
		return list;
	}
}