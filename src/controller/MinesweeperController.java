package controller;

import model.MinesweeperModel;

/**
 * This class defines the Model for the Minesweeper package.
 *
 * @author
 */

public class MinesweeperController {	
		
	private MinesweeperModel model;
	private int[][] mines;
	private String[][] cellStates;
	
	/**
	 * Default constructor for MinesweeperModel.
	 * 
	 * The constructor stores a reference to the model locally, as well as references to the mine and cell state boards.
	 * 
	 * @param model a reference to the model object used by the view
	 */
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		mines = model.returnMinesBoard();
		cellStates = model.returnCellStateBoard();
	}
	
	public void setBoardSize(int n, int m) {
		//model.setBoardSize(n, m);
	}
	
	/**
	 * This public method check whether the provided position is a mine and covered or flagged. This is to prevent uncovered bombs from being unnecessarily uncovered.
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * 
	 * @return whether the given position is a mine
	 */
	public boolean isMine(int row, int col) {
		if (mines[row][col] == -1 && (cellStates[row][col].equals("covered") || cellStates[row][col].equals("flagged"))) { // might need to change depending on what values are inside covers array
			return true;
		}
		return false;
	}
	
	/**
	 * This public method updates the number of adjacent mines at each square that isn't a mine itself. This uses the model to update the underlying mines board.
	 */
	public void updateMineBoard() {
		for (int i = 0; i < model.returnMinesBoard().length; i++) {
			for (int j = 0; j < model.returnMinesBoard()[i].length; j++) {
				if (model.returnMinesBoard()[i][j] != -1) {
					 int x = numMinesAroundCell(i, j);
					 model.updateMine(i, j, x);
				}
			}
		}
		model.printBoards();
	}
	
	/**
	 * This public method checks for adjacent mines to a given position. It uses both checkOthog() and checkDiagonals() and sums the return values.
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * 
	 * @return the number of adjacent mines 
	 */
	public int numMinesAroundCell(int row, int col) {
		int diagonals = checkDiagonals( row, col);
		int othogs = checkOthog(row, col);
		return diagonals + othogs;
	}

	/**
	 * This private method checks for mines along the orthogonal squares adjacent to the provided position. This method is used in conjunction with checkDiagonals().
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * 
	 * @return the number of adjacent mines in the orthogonal adjacent squares
	 */
	private int checkOthog(int row, int col) {
		// TODO Auto-generated method stub
		int retval = 0;
		// check top othog
		if (row > 0) {
			if (mines[row-1][col] == -1) {
				retval+=1;
			}
		}
		// check right othog
		if (col+1 < mines[col].length) {
			if (mines[row][col+1] == -1) {
				retval+=1;
			}
		}
		// check left othog
		if (col > 0) {
			if (mines[row][col-1] == -1) {
				retval+=1;
			}
		}
		// checks bottom othog
		if (row+1 < mines.length) {
			if (mines[row+1][col] == -1) {
				retval+=1;
			}
		}
		return retval;
	}

	/**
	 * This private method checks for mines along the diagonal squares adjacent to the provided position. This method is used in conjunction with checkOthog().
	 * 
	 * @param row the row on the board
	 * @param col the col on the board
	 * 
	 * @return the number of adjacent mines in the diagonally adjacent squares
	 */
	private int checkDiagonals(int row, int col) {
		int retval = 0;
		// check top left diag
		if (row > 0 && col > 0) {
			if (mines[row-1][col-1] == -1) {
				retval+=1;
			}
		}
		// check top right diag
		if (row > 0 && col+1 < mines[col].length) {
			if (mines[row-1][col+1] == -1) {
				retval+=1;
			}
		}
		// check bottom left diag
		if (row+1 < mines.length && col > 0) {
			if (mines[row+1][col-1] == -1) {
				retval+=1;
			}
		}
		// checks bottom right diag
		if (row+1 < mines.length && col+1 < mines[col].length) {
			if (mines[row+1][col+1] == -1) {
				retval+=1;
			}
		}
		return retval;
	}
	
	/**
	 * This public method checks if the game is over by searching for uncovered mines and returning false if any are found.
	 * 
	 * @return whether the game is over or not
	 */
	public boolean isGameOver() {
		for (int i = 0; i < mines.length; i++) {
			for (int j = 0; j < mines[i].length; j++) {
				if (mines[i][j] == -1 && cellStates[i][j].equals("uncovered")) {
					return true;
				}
			}
		}
		for (int i = 0; i < mines.length; i++) {
			for (int j = 0; j < mines[i].length; j++) {
				if (mines[i][j] != -1 && cellStates[i][j].equals("covered")) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * This public method checks if the game has been won by iterating through all of the game squares and checking if they are uncovered. If any are still covered, it returns false.
	 * 
	 * @return whether the game has been won or not
	 */
	public boolean isWon() {
		for (int i = 0; i < mines.length; i++) {
			for (int j = 0; j < mines[i].length; j++) {
				if (mines[i][j] != -1 && ! cellStates[i][j].equals("uncovered")) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * This public method uncovers a square and uncovers surrounding empty space if applicable. 
	 * 
	 * @param row the row to recursively uncover
	 * @param col the col to recursively uncover
	 * @param size the size to recursively uncover
	 */
	public void clicked(int col, int row, int size) {
		model.updateCellState(row, col, "uncovered");
		if (model.mineAtCoords(row, col) == 0) {
			uncoverAroundZero(row, col, size);
		}
	}
	
	/**
	 * This public method recursively uncovers empty positions on the board when an empty square is clicked. 
	 * 
	 * @param x the row to recursively uncover
	 * @param y the col to recursively uncover
	 * @param size the size to recursively uncover
	 */
	public void uncoverAroundZero(int x, int y, int size){
		model.updateCellState(x, y, "uncovered");
		if (x != 0) {
			if (model.returnCellStateBoard()[x-1][y].equals("covered"))
				if (model.mineAtCoords(x-1 , y) == 0) {
					uncoverAroundZero(x-1, y, size);
				}
				else {
					model.updateCellState(x-1, y, "uncovered");
				}
		}
		if (x != 0 && y != size - 1) {
			if (model.returnCellStateBoard()[x-1][y+1].equals("covered")) {
				if (model.mineAtCoords(x-1 , y + 1) == 0) {
					uncoverAroundZero(x-1, y + 1, size);
				}
				else {
					model.updateCellState(x-1, y+1, "uncovered");
				}
			}
		}
		if (y != size-1) {
			if (model.returnCellStateBoard()[x][y+1].equals("covered")) {
				if (model.mineAtCoords(x , y + 1) == 0) {
					uncoverAroundZero(x, y + 1, size);
				}
				else {
					model.updateCellState(x, y+1, "uncovered");
				}
			}
		}
		if (x != size-1 && y != size-1) {
			if (model.returnCellStateBoard()[x+1][y+1].equals("covered")) {
				if (model.mineAtCoords(x + 1, y + 1) == 0) {
					uncoverAroundZero(x+1, y + 1, size);
				}
				else {
					model.updateCellState(x+1, y+1, "uncovered");
				}
			}
		}
		if (x != size-1) {
			if (model.returnCellStateBoard()[x+1][y].equals("covered")) {
				if (model.mineAtCoords(x + 1, y) == 0) {
					uncoverAroundZero(x+1, y, size);
				}
				else {
					model.updateCellState(x+1, y, "uncovered");
				}
			}
		}
		if (x != size-1 && y != 0) {
			if (model.returnCellStateBoard()[x+1][y-1].equals("covered")) {
				if (model.mineAtCoords(x + 1, y -1) == 0) {
					uncoverAroundZero(x+1, y-1, size);
				}
				else {
					model.updateCellState(x+1, y-1, "uncovered");
				}
			}
		}
		if (y != 0) {
			if (model.returnCellStateBoard()[x][y-1].equals("covered")) {
				if (model.mineAtCoords(x, y -1) == 0) {
					uncoverAroundZero(x, y-1, size);
				}
				else {
					model.updateCellState(x, y-1, "uncovered");
				}
			}
		}
		if (y != 0 && x !=0) {
			if (model.returnCellStateBoard()[x-1][y-1].equals("covered")) {
				if (model.mineAtCoords(x-1, y -1) == 0) {
					uncoverAroundZero(x-1, y-1, size);
				}
				else {
					model.updateCellState(x-1, y-1, "uncovered");
				}
			}
		}
	}
}
