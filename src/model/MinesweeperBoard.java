package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MinesweeperBoard implements Serializable {
	private int[][] mineBoard;
	private String[][] cellStatusBoard;
	
	public MinesweeperBoard(File savedMineBoard, File savedCellStatusBoard) {
		FileInputStream mineFile;
		try {
			mineFile = new FileInputStream(savedMineBoard);
			ObjectInputStream in = new ObjectInputStream(mineFile);
			mineBoard = (int[][]) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		FileInputStream cellStatusFile;
		try {
			cellStatusFile = new FileInputStream(savedCellStatusBoard);
			ObjectInputStream in = new ObjectInputStream(cellStatusFile);
			cellStatusBoard = (String[][]) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * This method saves the board object into save_game.dat
	 * 
	 * With use of serialization and file/ObjectOutputStream, this object
	 * is able to be encoded into the dat file and saved inside our project.
	 */
	public void saveboard() {
		String mineFilename = "save_mine_game.dat";
		String statusFilename = "save_cell_game.dat";
        
        // Serialization 
        try
        {   
            //Saving of object in a file
            FileOutputStream mineFile = new FileOutputStream(mineFilename);
            ObjectOutputStream mineOut = new ObjectOutputStream(mineFile);
              
            // Method for serialization of object
            mineOut.writeObject(mineBoard);
              
            mineOut.close();
            mineFile.close();
  
        }
          
        catch(IOException ex)
        {
            System.out.println("Mine IOException occured");
        }
        
        // Serialization 
        try
        {   
            //Saving of object in a file
            FileOutputStream statusFile = new FileOutputStream(statusFilename);
            ObjectOutputStream statusOut = new ObjectOutputStream(statusFile);
              
            // Method for serialization of object
            statusOut.writeObject(cellStatusBoard);
              
            statusOut.close();
            statusFile.close();
  
        }
          
        catch(IOException ex)
        {
            System.out.println("Mine IOException occured");
        }
	}
	
	public MinesweeperBoard(int[][] currentMineBoard, String[][] currentStatusBoard) {
		mineBoard = currentMineBoard;
		cellStatusBoard = currentStatusBoard;
		
	}

	public String[][] getStatusBoard() {
		return cellStatusBoard;
	}
	
	public int[][] getMineBoard() {
		return mineBoard;
	}
}
