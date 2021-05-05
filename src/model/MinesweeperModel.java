package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

/**
 * This class defines the Model for the Minesweeper package.
 *
 * @author kylesmith, brendanp, matthewgleason, austinhillis6, cdpeterson
 */

@SuppressWarnings("deprecation")
public class MinesweeperModel extends Observable {
	private int[][] mines;
	private String[][] cellStates;
	private Set<int[]> mineLocations;
	private int totalRows;
	private int totalCols;
	private int totalBombs;
	private MinesweeperBoard boardObject;
	private int seconds; // Make sure its in the Integer Wrapper Class for Serialization
	
	/**
	 * Default constructor for MinesweeperModel.
	 * 
	 * First, this constructor initializes all of the local fields. Then, it generates a random number of bombs into the state of the board, indicated by bombAmount.
	 * 
	 * @param rowAmount the number of rows to construct
	 * @param colAmount the number of cols to construct
	 * @param bombAmount the number of bombs to initialize
	 */
	public MinesweeperModel(int rowAmount, int colAmount, int bombAmount) {
		seconds = 0;
		mines = new int[rowAmount][colAmount];
		cellStates = new String[rowAmount][colAmount];
		mineLocations = new HashSet<int[]>();
		this.totalRows = rowAmount;
		this.totalCols = colAmount;
		this.totalBombs = bombAmount;
		
		for(int r = 0; r < totalRows; r++) {
			for(int c = 0; c < totalCols; c++) {
				mines[r][c] = 0;
				cellStates[r][c] = "covered";
			}
		}//
		
		Random rand = new Random();
		int randomRow = rand.nextInt(rowAmount);
		int randomCol = rand.nextInt(colAmount);
		for(int i = 0; i < bombAmount; i++) {
			while(isMineLocation(randomRow, randomCol) == true) {
				randomRow = rand.nextInt(rowAmount);
				randomCol = rand.nextInt(colAmount);
			}
			mines[randomRow][randomCol] = -1;
			int[] coord = new int[2];
			coord[0] = randomRow;
			coord[1] = randomCol;
			mineLocations.add(coord);
		}
		boardObject = new MinesweeperBoard(mines, cellStates, seconds);
	}
	
	/**
	 * The saved game constructor for MinesweeperModel
	 * 
	 * This constructor is used to load a saved game into the model class. 
	 * 
	 * @param gameInfo the saved game file to load
	 */
	public MinesweeperModel(File gameInfo) {
		boardObject = new MinesweeperBoard(gameInfo);
		mineLocations = new HashSet<int[]>();
		mines = boardObject.getMineBoard();
		cellStates = boardObject.getStatusBoard();
		seconds = boardObject.getTime();
		totalRows = mines.length;
		totalCols = mines[0].length;
		System.out.println(String.valueOf(totalRows));
		System.out.println(String.valueOf(totalCols));
		printBoards();
		totalBombs = 0;
		for(int i = 0; i < mines.length; i++) {
			for(int j = 0; j < mines.length; j++) {
				if (mines[i][j] == -1) {
					totalBombs ++;
					int[] coord = new int[2];
					coord[0] = i;
					coord[1] = j;
				}
			}
		}
		
	}
	
	/**
	 * The empty constructor for MinesweeperModel
	 */
	public MinesweeperModel() {
		boardObject = new MinesweeperBoard();
	}
	
	/**
	 * This public method is the setter for the seconds field.
	 * 
	 * @param secs the updated seconds value
	 */
	public void setTime(int secs) {
		seconds = secs;
	}
	
	/**
	 * This public method wraps setChanged and notifyObservers into one method call to simplify updating the view.
	 */
	public void sendUpdate() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This public method updates the state of the indicated cell. This value has the options of 'covered', 'uncovered', and 'flagged'.
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * @param newStr the new state of the position
	 */
	public void updateCellState(int row, int col, String newStr) {
		cellStates[row][col] = newStr;
		sendUpdate();
	}
	
	/**
	 * This public method updates the adjacent mine count at the specified position. This method is used by the controller to update the state of the game.
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * @param newNum the new number of adjacent mines
	 */
	public void updateMine(int row, int col, int newNum) {
		int[] coord = new int[2];
		coord[0] = row;
		coord[1] = col;
		if(mineLocations.contains(coord) == true) {
			System.out.println("Can't update bomb");
		} else {
			mines[row][col] = newNum;
		}
		
	}
	
	/**
	 * This public method checks if the given coordinate contains a mine. 
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * 
	 * @return whether the indicated position contains a mine
	 */
	public boolean isMineLocation(int row, int col) {
		return mines[row][col] == -1;
	}
	
	/**
	 * This public method is a getter for the underlying two-dimensional array indicating the number of adjacent mines to each cell. 0 through 8 indicate bombs adjacent to the square; -1 indicates that the square is a bomb.
	 * 
	 * @return a two dimensional array of mines
	 */
	public int[][] returnMinesBoard(){
		return mines;
	}
	
	/**
	 * This public method is a getter for the underlying two-dimensional array indicating the state of each cell. This value has the options of 'covered', 'uncovered', and 'flagged'.
	 * @return a two dimensional array of states
	 */
	public String[][] returnCellStateBoard(){
		return cellStates;
	}
	
	/**
	 * This public method is a getter for the set of mineLocations used to track all of the mines on a board. The Set contains pairs of ints indicating the coordinates of each mine.
	 * 
	 * @return all mine locations 
	 */
	public Set<int[]> returnMineLocations(){
		return mineLocations;
	}
	
	/**
	 * This public method returns the state of the cell at the given coordinate. This value has the options of 'covered', 'uncovered', and 'flagged'.
	 * @param row the row on the board
	 * @param col the col on the board
	 * @return the state of the given coordinate
	 */
	public String cellStateAtCoords(int row, int col) {
		return cellStates[row][col];
	}
	
	/**
	 * This public method returns the number of adjacent mines at a given coordinate. 0 through 8 indicate bombs adjacent to the square; -1 indicates that the square is a bomb.
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * 
	 * @return the number of adjacent mines
	 */
	public int mineAtCoords(int row, int col) {
		return mines[row][col];
	}
	
	/**
	 * This public method is a getter for the totalRows field used to track the total number of rows on the board. 
	 * 
	 * @return the total number of rows
	 */
	public int totalRows() {
		return totalRows;
	}
	
	/**
	 * This public method is a getter for the totalCols field used to track the total number of columns on the board. 
	 * 
	 * @return the total number of columns
	 */
	public int totalCols() {
		return totalCols;
	}
	
	/**
	 * This public method is a getter for the totalBombs field used to track the total number of bombs on the board. 
	 * 
	 * @return the total number of bombs
	 */
	public int totalBombs() {
		return totalBombs;
	}
	
	/**
	 * This public method is a getter for the seconds field used to track the total elapsed seconds since game initiation. 
	 * 
	 * @return the elapsed time
	 */
	public int getTime() {
		return seconds;
	}
	
	/**
	 * This public method prints the underlying boards used to represent the state of the game. This method is used for debugging purposes exclusively. 
	 */
	public void printBoards() {
		System.out.println("Mines and Numbers Board");
		System.out.print("   ");
		for(int c = 0; c < totalCols; c++) {
			System.out.print(c + "  ");
		}
		System.out.println();
		
		for(int r = 0; r < totalRows; r++) {
			if(r < 10) {
				System.out.print(" " + r + " ");
			} else {
				System.out.print(r + " ");
			}
			for(int c = 0; c < totalCols; c++) {
				if(c < 9) {
					if(mines[r][c] == -1) {
						System.out.print(mines[r][c] + " ");
					} else {
						System.out.print(mines[r][c] + "  ");
					}
				} else {
					if(mines[r][c] == -1) {
						System.out.print(mines[r][c] + "  ");
					} else {
						System.out.print(mines[r][c] + "   ");
					}
				}
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Cell States Board");
		System.out.print("   ");
		for(int c = 0; c < totalCols; c++) {
			System.out.print(c + " ");
		}
		System.out.println();
		for(int r = 0; r < totalRows; r++) {
			if(r < 10) {
				System.out.print(" " + r + " ");
			} else {
				System.out.print(r + " ");
			}
			for(int c = 0; c < totalCols; c++) {
				if(c < 9) {
					System.out.print(cellStates[r][c].substring(0, 1) + " ");
				} else {
					System.out.print(cellStates[r][c].substring(0, 1) + "  ");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * This public method calls the saveboard() method on the underlying MinesweeperBoard object.
	 */
	public void saveBoard() {
		boardObject.setTime(seconds);
		boardObject.saveboard();
	}
	

	/**
	 * This public method returns the ArrayList of high scores stored in the given highScoreFile.
	 * 
	 * @param highScoreFile the file object to deserialize
	 * 
	 * @return an array of high scores
	 */
	public ArrayList<Integer> getHighScores(File highScoreFile) {
		return boardObject.getHighScores(highScoreFile);
	}
	
	/**
	 * This public method calls the saveHighScores() method on the underlying MinesweeperBoard object.
	 * 
	 * @param time the current elapsed time 
	 */
	public void saveHighScores(int time) {
		boardObject.saveHighScores(time);
	}
}
