package model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MinesweeperModel {
	
	private int[][] mines;
	private String[][] cellStates;
	private Set<int[]> mineLocations;
	private int totalRows;
	private int totalCols;
	private int totalBombs;
	
	public MinesweeperModel(int rowAmount, int colAmount, int bombAmount) {
		mines = new int[rowAmount][colAmount];
		cellStates = new String[rowAmount][colAmount];
		mineLocations = new HashSet<int[]>();
		this.totalRows = rowAmount;
		this.totalCols = colAmount;
		this.totalBombs = bombAmount;
		
		for(int r = 0; r < totalRows; r++) {
			for(int c = 0; c < totalCols; c++) {
				mines[r][c] = 0;
				cellStates[r][c] = "uncovered";
			}
		}
		
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
	}
	
	public void updateCellState(int row, int col, String newStr) {
		cellStates[row][col] = newStr;
	}
	
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
	
	public boolean isMineLocation(int row, int col) {
		return mines[row][col] == -1;
	}
	
	public int[][] returnMinesBoard(){
		return mines;
	}
	
	public String[][] returnCellStateBoard(){
		return cellStates;
	}
	
	public Set<int[]> returnMineLocations(){
		return mineLocations;
	}
	
	public String cellStateAtCoords(int row, int col) {
		return cellStates[row][col];
	}
	
	public int mineAtCoords(int row, int col) {
		return mines[row][col];
	}
	
	public int totalRows() {
		return totalRows;
	}
	
	public int totalCols() {
		return totalCols;
	}
	
	public int totalBombs() {
		return totalBombs;
	}
	
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
}
