package controller;

import model.MinesweeperModel;

import model.MinesweeperModel;  

public class MinesweeperController {	
		
	private MinesweeperModel model;
	private int[][] mines;
	private String[][] cellStates;
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		mines = model.returnMinesBoard();
		cellStates = model.returnCellStateBoard();
	}
	
	public void setBoardSize(int n, int m) {
		//model.setBoardSize(n, m);
	}
	
	public boolean isMine(int row, int col) {
		if (mines[row][col] == -1 && (cellStates[row][col].equals("covered") || cellStates[row][col].equals("flagged"))) { // might need to change depending on what values are inside covers array
			return true;
		}
		return false;
	}
	
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
	
	public int numMinesAroundCell(int row, int col) {
		int diagonals = checkDiagonals( row, col);
		int othogs = checkOthog(row, col);
		return diagonals + othogs;
	}

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

	private int checkDiagonals(int row, int col) {
		// TODO Auto-generated method stub
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
	
	public boolean isGameOver() {
		for (int i = 0; i < mines.length; i++) {
			for (int j = 0; j < mines[i].length; j++) {
				if (mines[i][j] != -1 && cellStates[i][j].equals("covered")) {
					return false;
				}
			}
		}
		
		return true;
	}
	
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
	
	public void clicked(int col, int row) {
		model.updateCellState(row, col, "uncovered");
		if (model.mineAtCoords(row, col) == 0) {
			uncoverAroundZero(row, col);
		}
	}
	
	public void uncoverAroundZero(int x, int y){
		model.updateCellState(x, y, "uncovered");
		if (x != 0) {
			if (model.returnCellStateBoard()[x-1][y].equals("covered"))
				if (model.mineAtCoords(x-1 , y) == 0) {
					uncoverAroundZero(x-1, y);
				}
				else {
					model.updateCellState(x-1, y, "uncovered");
				}
		}
		if (x != 0 && y != 19) {
			if (model.returnCellStateBoard()[x-1][y+1].equals("covered")) {
				if (model.mineAtCoords(x-1 , y + 1) == 0) {
					uncoverAroundZero(x-1, y + 1);
				}
				else {
					model.updateCellState(x-1, y+1, "uncovered");
				}
			}
		}
		if (y != 19) {
			if (model.returnCellStateBoard()[x][y+1].equals("covered")) {
				if (model.mineAtCoords(x , y + 1) == 0) {
					uncoverAroundZero(x, y + 1);
				}
				else {
					model.updateCellState(x, y+1, "uncovered");
				}
			}
		}
		if (x != 19 && y != 19) {
			if (model.returnCellStateBoard()[x+1][y+1].equals("covered")) {
				if (model.mineAtCoords(x + 1, y + 1) == 0) {
					uncoverAroundZero(x+1, y + 1);
				}
				else {
					model.updateCellState(x+1, y+1, "uncovered");
				}
			}
		}
		if (x != 19) {
			if (model.returnCellStateBoard()[x+1][y].equals("covered")) {
				if (model.mineAtCoords(x + 1, y) == 0) {
					uncoverAroundZero(x+1, y);
				}
				else {
					model.updateCellState(x+1, y, "uncovered");
				}
			}
		}
		if (x != 19 && y != 0) {
			if (model.returnCellStateBoard()[x+1][y-1].equals("covered")) {
				if (model.mineAtCoords(x + 1, y -1) == 0) {
					uncoverAroundZero(x+1, y-1);
				}
				else {
					model.updateCellState(x+1, y-1, "uncovered");
				}
			}
		}
		if (y != 0) {
			if (model.returnCellStateBoard()[x][y-1].equals("covered")) {
				if (model.mineAtCoords(x, y -1) == 0) {
					uncoverAroundZero(x, y-1);
				}
				else {
					model.updateCellState(x, y-1, "uncovered");
				}
			}
		}
		if (y != 0 && x !=0) {
			if (model.returnCellStateBoard()[x-1][y-1].equals("covered")) {
				if (model.mineAtCoords(x-1, y -1) == 0) {
					uncoverAroundZero(x-1, y-1);
				}
				else {
					model.updateCellState(x-1, y-1, "uncovered");
				}
			}
		}
	}
}
