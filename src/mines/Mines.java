package mines;

import java.util.Random;

public class Mines {
	private int height;
	private int width;
	protected int numMines;
	private int[][] board;
	private boolean[][] flag; //saves the spot of the flag 
	protected boolean showAll;
	
	/*
	 * board[i][j] = -1 - the spot havent opened and doesnt have a mine
	 * board[i][j] = 1 - the spot have a mine
	 * board[i][j] = 0 - the spot opened
	 */
	
	
	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		this.numMines = numMines;
		showAll = false;
		board = new int[height][width];
		flag = new boolean[height][width];
		for(int i = 0; i<height; i++)
			for(int j=0; j<width; j++) {
				board[i][j] = -1;
				flag[i][j] = false;
			}
		setMines();
	}
	
	public boolean addMine(int i, int j) {
		if(checkSpot(i, j) == true)
			if(board[i][j] != 1) {
				board[i][j] = 1;
				return true;
			}
		return false; //if there is already a mine in the spot
	}
	
	public boolean open(int i, int j) {
		boolean flag = true;
		if(board[i][j] == 1)//mine
			return false;
		board[i][j] = 0; //open spot
		for(int k=i-1; k<=i+1; k++) //check for each spot near the current spot
			for(int t=j-1; t<=j+1; t++)
				if(k != i || t != j) //if it isnt the current spot
					if(checkSpot(k, t) == true) {
						if(board[k][t] == 1)
							flag = false;
					}
		if(flag == true) //open all near spots
			for(int k=i-1; k<=i+1; k++) //check for each spot near the current spot
				for(int t=j-1; t<=j+1; t++)
					if(k != i || t != j) //if it isnt the current spot
						if(checkSpot(k, t) == true) 
							if(board[k][t] == -1)
								open(k,t);
		return true;
		
	}
	
	public void toggleFlag(int x, int y) {
		if(checkSpot(x, y) == true) 
			if(flag[x][y] == true)
				flag[x][y] = false;
			else flag[x][y] = true;
	}
	
	public boolean isDone() {
		for(int i = 0; i<height; i++)
			for(int j=0; j<width; j++)
				if(board[i][j] == -1)
					return false;
		return true;
	}
	
	public String get(int i, int j) {
		if(flag[i][j] == true) return "F";
		if(board[i][j] == 1 && showAll == true) return "X";

		if((board[i][j] == -1 || board[i][j] == 1 )&& showAll == false) return ".";
		if(nearMines( i, j) == 0) return " ";
		return "" + nearMines( i, j);
	}
	
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}


	public String toString() {
		String s = "";
		for(int i = 0; i<height; i++) {
			for(int j=0; j<width; j++)
				s += get(i, j);
			s+= "\n";
		}
		return s;
	}
	
	private boolean checkSpot(int i, int j) { //returns true if the spot is in the board
		if(i>=height || i<0 || j>=width ||j<0) //the spot is not in the board
			return false;
		return true;
	}
	
	private int nearMines(int i, int j) { //return the num of mines near the spot
		int count = 0;
		for(int k=i-1; k<=i+1; k++) //check for each spot near the current spot
			for(int t=j-1; t<=j+1; t++)
				if(k != i || t != j) //if it isnt the current spot
					if(checkSpot(k, t) == true) 
						if(board[k][t] == 1) { //if there is a mine in the spot
							count++;
						}
		return count;
	}
	
	private void setMines() {
		Random rand = new Random();
		int w, h;
		h=rand.nextInt(height);
		w=rand.nextInt(width);
		for(int i=0; i<numMines; i++) { //set a mine in a random empty spot 
			while(board[h][w] == 1) {
				h=rand.nextInt(height);
				w=rand.nextInt(width);
			}
			board[h][w] = 1;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Mines m = new Mines(4, 4, 0);
		m.open(2,2);
		m.open(3,0);
		m.setShowAll(true);
		System.out.println(m);


	}


}
