package model;

import java.io.File;
import java.util.HashSet;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("deprecation")
public class MinesweeperModel extends Observable {
	
	private int[][] mines;
	private String[][] cellStates;
	private Set<int[]> mineLocations;
	private int totalRows;
	private int totalCols;
	private int totalBombs;
	private MinesweeperBoard boardObject;
	private int seconds;
	
	
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
		boardObject = new MinesweeperBoard(mines, cellStates);
	}
	
	public MinesweeperModel(File mineFile, File cellStatusFile, int secs) {
		seconds = secs;
		boardObject = new MinesweeperBoard(mineFile, cellStatusFile);
		mineLocations = new HashSet<int[]>();
		mines = boardObject.getMineBoard();
		cellStates = boardObject.getStatusBoard();
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
		
//		for(int r = 0; r < totalRows; r++) {
//			for(int c = 0; c < totalCols; c++) {
//				mines[r][c] = 0;
//				cellStates[r][c] = "covered";
//			}
//		}
		
//		Random rand = new Random();
//		int randomRow = rand.nextInt(rowAmount);
//		int randomCol = rand.nextInt(colAmount);
//		for(int i = 0; i < bombAmount; i++) {
//			while(isMineLocation(randomRow, randomCol) == true) {
//				randomRow = rand.nextInt(rowAmount);
//				randomCol = rand.nextInt(colAmount);
//			}
//			mines[randomRow][randomCol] = -1;
//			int[] coord = new int[2];
//			coord[0] = randomRow;
//			coord[1] = randomCol;
//			mineLocations.add(coord);
//		}
	}
	public void setTime(int secs) {
		seconds = secs;
	}
	
	public void sendUpdate() {
		setChanged();
		notifyObservers();
	}
	
	public void updateCellState(int row, int col, String newStr) {
		cellStates[row][col] = newStr;
		sendUpdate();
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
	
	public int getTime() {
		return seconds;
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
	
	public void saveBoard() {
		boardObject.saveboard();
	}
}
