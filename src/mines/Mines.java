package mines;

import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Mines {
	private int height;
	private int width;
	protected int numMines;
	protected int openedSpots;
	private int numFlags; //num of flags to set for win
	private int[][] board;
	private boolean[][] flags; //saves the spot of the flag 
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
		numFlags = numMines;
		openedSpots = 0;
		showAll = false;
		board = new int[height][width];
		flags = new boolean[height][width];
		for(int i = 0; i<height; i++)
			for(int j=0; j<width; j++) {
				board[i][j] = -1;
				flags[i][j] = false;
			}
		setMines();
	}
	
	public int getNumFlags() {
		return numFlags;
	}
	
	public void setNumFlags (int cnt) {
		numFlags = cnt;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	/*
	 *  input: i, j are indexes in the board
	 *  output: false if the user lost, otherwise true. 
	 */
	public boolean open(int i, int j, GridPane grid, Label flagCnt) {   		
		if(board[i][j] == 1) return false; //mine - user lost
		if(board[i][j] == 0) return true;  //spot already opened
		
		board[i][j] = 0; //open spot
		openedSpots++;
		Spot spot = (Spot)grid.getChildren().get(i*width + j); //updates button in gui
		if(spot.getGraphic() != null) { //open a spot with a flag
			setNumFlags(getNumFlags() + 1);
			flagCnt.setText(String.valueOf(getNumFlags()));
			spot.setGraphic(null);
		}
		spot.setOpened(true); 
		spot.setText(get(i,  j));
		
		if(nearMines(i, j) > 0) return true; 
		
		for(int k=i-1; k<=i+1; k++) //each spot near the current spot
			for(int t=j-1; t<=j+1; t++)
				if(spotExist(k, t) == true) 
					if(board[k][t] == -1)
						open(k, t, grid, flagCnt);
		return true;
	
	}
	
	public void toggleFlag(int x, int y) {
		if(spotExist(x, y) == true) 
			if(flags[x][y] == true)
				flags[x][y] = false;
			else flags[x][y] = true;
	}
	
	public String get(int i, int j) { //return the current status of spot on board 
		if(flags[i][j] == true) return "F"; //flag
		if(board[i][j] == 1 && showAll == true) return "X"; //mine
		if((board[i][j] == -1 || board[i][j] == 1 ) && showAll == false) return "."; //spot not opened
		
		//number of mines
		if(nearMines(i, j) == 0) return " "; 
		return String.valueOf(nearMines(i, j));
	}
	
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}
	
	protected boolean spotExist(int i, int j) { 
		if(i>=height || i<0 || j>=width || j<0) 
			return false;
		return true;
	}
	
	private int nearMines(int i, int j) { 
		int count = 0;
		for(int k=i-1; k<=i+1; k++) //check for each spot near the current spot
			for(int t=j-1; t<=j+1; t++)
				if(k != i || t != j) 
					if(spotExist(k, t) == true) 
						if(board[k][t] == 1) { //there is a mine in the spot
							count++;
						}
		return count;
	}
	
	private void setMines() { //set mines in a random empty spot 
		Random rand = new Random();
		int w, h;
		h=rand.nextInt(height);
		w=rand.nextInt(width);
		for(int i=0; i<numMines; i++) { 
			while(board[h][w] == 1) {
				h=rand.nextInt(height);
				w=rand.nextInt(width);
			}
			board[h][w] = 1;
		}
	}


}
