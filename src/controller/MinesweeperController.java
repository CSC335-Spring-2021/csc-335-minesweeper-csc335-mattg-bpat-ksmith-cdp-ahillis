package controller; 

import model.MinesweeperModel; 

public class MinesweeperController {
	
	private MinesweeperModel model; 
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
	}
	
	public void setBoardSize(int n, int m) {
		//model.setBoardSize(n, m);
	}
	
	public boolean isMine(int[][] numsBoard, String[][] coversBoard, int row, int col) {
		if (numsBoard[row][col] == -1 && (coversBoard[row][col].equals("covered") || coversBoard[row][col].equals("flagged"))) { // might need to change depending on what values are inside covers array
			return true;
		}
		return false;
	}
	
	public int numMinesAroundCell(int[][] numsBoard, int row, int col) {
		int diagonals = checkDiagonals(numsBoard, row, col);
		int othogs = checkOthog(numsBoard, row, col);
		return diagonals + othogs;
	}

	private int checkOthog(int[][] numsBoard, int row, int col) {
		// TODO Auto-generated method stub
		int retval = 0;
		// check top othog
		if (row-1 > 0 && row-1 < numsBoard.length) {
			if (numsBoard[row-1][col] == -1) {
				retval+=1;
			}
		}
		// check right othog
		if (col+1 < numsBoard[col].length && col+1 > 0) {
			if (numsBoard[row][col+1] == -1) {
				retval+=1;
			}
		}
		// check left othog
		if (col-1 < numsBoard[col].length && col-1 > 0) {
			if (numsBoard[row][col-1] == -1) {
				retval+=1;
			}
		}
		// checks bottom othog
		if (row+1 > 0 && row+1 < numsBoard.length-1) {
			if (numsBoard[row+1][col] == -1) {
				retval+=1;
			}
		}
		return retval;
	}

	private int checkDiagonals(int[][] numsBoard, int row, int col) {
		// TODO Auto-generated method stub
		int retval = 0;
		// check top left diag
		if (row-1 > 0 && row-1 < numsBoard.length && col-1 < numsBoard[col].length && col-1 > 0) {
			if (numsBoard[row-1][col-1] == -1) {
				retval+=1;
			}
		}
		// check top right diag
		if (row-1 > 0 && row-1 < numsBoard.length && col+1 < numsBoard[col].length && col+1 > 0) {
			if (numsBoard[row-1][col+1] == -1) {
				retval+=1;
			}
		}
		// check bottom left diag
		if (row+1 > 0 && row+1 < numsBoard.length && col-1 < numsBoard[col].length && col-1 > 0) {
			if (numsBoard[row+1][col-1] == -1) {
				retval+=1;
			}
		}
		// checks bottom right diag
		if (row+1 > 0 && row+1 < numsBoard.length-1 && col+1 < numsBoard[col].length && col+1 > 0) {
			if (numsBoard[row+1][col+1] == -1) {
				retval+=1;
			}
		}
		return retval;
	}
	
	public boolean isGameOver(int[][] numsBoard, String[][] coversBoard) {
		for (int i = 0; i < numsBoard.length; i++) {
			for (int j = 0; j < numsBoard[i].length; j++) {
				if (numsBoard[i][j] != -1 && coversBoard[i][j].equals("covered")) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean isWon(int[][] numsBoard, String[][] coversBoard) {
		for (int i = 0; i < numsBoard.length; i++) {
			for (int j = 0; j < numsBoard[i].length; j++) {
				if (numsBoard[i][j] != -1) {
					return false;
				}
			}
		}
		return true;
	}
}
