package com.example.app;

public class Grid {
public int [][] array = new int[][]{
		{5,0,0,0,9,0,3,0,0},
		{0,8,9,7,1,3,0,0,0},
		{0,0,0,5,2,0,0,4,7},
		{0,0,1,0,0,9,5,7,0},
		{0,0,5,2,0,1,4,0,0},
		{0,9,4,3,0,0,6,0,0},
		{2,1,0,0,3,5,0,0,0},
		{0,0,0,8,7,2,1,3,0},
		{0,0,7,0,4,0,0,0,9}		
};


public void clearGrid(){
    for(int i = 0; i < 9; i++){
        for(int j = 0; j < 9; j++){
            array[i][j] = 0;
        }
    }
}

public void printGrid(){
	int i = 0;
	
	while(i < 9){
		int k = 0;
		while(k < 9){
			System.out.print(array[i][k] + " ");
			k++;
		}
		System.out.println();
		i++;
	}
}

	
	
}
