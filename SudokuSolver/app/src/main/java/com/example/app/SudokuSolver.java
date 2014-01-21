package com.example.app;

public class SudokuSolver {

// please start from #1 at the very bottom.
	// #6
	public static boolean solveSudoku(Grid grid){
		
		CurrentState state = new CurrentState(0, 0);
		if(!findUnassignedLocation(grid,state)) return true;
		for(int i = 1; i <= 9; i++){
			
			if(noConflicts(grid,state,i)){
				int row = state.row;
				int col = state.col;
				grid.array[row][col] = i;
				if(solveSudoku(grid)) return true;
				grid.array[row][col] = 0;
			}			
			
		}	
		return false;
		
	}
	
	// #5
	public static boolean findUnassignedLocation(Grid grid,CurrentState state){
		for(int row = 0; row < 9; row++){
			for(int col = 0; col < 9; col++){
				if(grid.array[row][col] == 0) {
					state.row = row;
					state.col = col;
					return true;
				}
			}
		}
		
		return false;
	}
	
	// #4
	public static boolean noConflicts(Grid grid,CurrentState state,int num){
		
		int newRow = state.row - state.row % 3;
		int newCol = state.col - state.col % 3;
		int row = state.row;
		int col = state.col;
		return !usedInRow(grid,row,num) && !usedInCol(grid,col,num) && !usedInBox(grid,newRow,newCol,num);
	}
	
	// #3
	public static boolean usedInRow(Grid grid, int row,int num){
		for(int col = 0; col < 9; col++){
			if(grid.array[row][col] == num) return true;
		}
		return false;
	}
	
	// #2
	public static boolean usedInCol(Grid grid, int col,int num){
		for(int row = 0; row < 9; row++){
			if(grid.array[row][col] == num) return true;
		}
		return false;
	}
	
	// #1
	public static boolean usedInBox(Grid grid, int boxStartRow,int boxStartCol,int num){
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 3; col++){
				if(grid.array[row+boxStartRow][col + boxStartCol] == num) return true;
			}
		}
		return false;
	}
	

	public static void main(String argv[]){
		Grid grid = new Grid();
		solveSudoku(grid);
		grid.printGrid();
		
		// check after the sudoku is solved
		System.out.println(solveSudoku(grid));
		
		
	}
	
	
	
}
