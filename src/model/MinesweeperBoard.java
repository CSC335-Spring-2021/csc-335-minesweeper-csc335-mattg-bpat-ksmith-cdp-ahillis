package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MinesweeperBoard implements Serializable {
	private int[][] mineBoard;
	private String[][] cellStatusBoard;
	private Integer time;
	
	public MinesweeperBoard(File file) {
		FileInputStream gameInfoFile;
		try {
			gameInfoFile = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(gameInfoFile);
			
			ArrayList<Object> gameInfo = (ArrayList<Object>) in.readObject();
			System.out.println(gameInfo);
			mineBoard = (int[][]) gameInfo.get(0);
			cellStatusBoard = (String[][]) gameInfo.get(1);
			time = (Integer) gameInfo.get(2);
			in.close();
			
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
		String Filename = "save_game.dat";
        
        // Serialization 
        try
        {   
            //Saving of object in a file
            FileOutputStream mineFile = new FileOutputStream(Filename);
            ObjectOutputStream mineOut = new ObjectOutputStream(mineFile);
              
            // Method for serialization of object
            ArrayList<Object> gameInfo = new ArrayList<Object>();
            gameInfo.add(mineBoard);
            gameInfo.add(cellStatusBoard);
            gameInfo.add(time);
            
            mineOut.writeObject(gameInfo);
              
            mineOut.close();
            mineFile.close();
  
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
	
	public int getTime() {
		return time.intValue();
	}
	
	public void setTime(int sec) {
		time = sec;
	}
}
