package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class defines the Board for the Minesweeper package.
 *
 * @author kylesmith, brendanp, matthewgleason, austinhillis6, cdpeterson
 */

@SuppressWarnings("serial")
public class MinesweeperBoard implements Serializable {
	private int[][] mineBoard;
	private String[][] cellStatusBoard;
	private Integer time;
	private ArrayList<Integer> scoreInfo;
	
	/**
	 * Default constructor for the MinesweeperBoard class.
	 * 
	 * @param file the file to load into the local fields
	 */
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
	 * Copy constructor for the MinesweeperBoard class.
	 * 
	 * @param currentMineBoard the current mine board to save locally
	 * @param currentStatusBoard the current status board to save locally
	 * @param seconds the current elapsed seconds to save locally
	 */
	public MinesweeperBoard(int[][] currentMineBoard, String[][] currentStatusBoard, int seconds) {
		mineBoard = currentMineBoard;
		cellStatusBoard = currentStatusBoard;
		time = Integer.valueOf(seconds);
	}
	
	/**
	 * Empty constructor for the MinewsweeperBoard class
	 */
	public MinesweeperBoard() {
		
	}

	/**
	 * This public method saves the board object into save_game.dat
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
	
	/**
	 * This public method saves the given time to the high scores save file.
	 * 
	 * With use of serialization and file/ObjectOutputStream, this object
	 * is able to be encoded into the dat file and saved inside our project.
	 * 
	 * @param time the new time to add to the high scores
	 */
	public void saveHighScores(int time) {
		String Filename = "highScores.dat";
		getHighScores(new File(Filename));
        // Serialization 
        try
        {   
            //Saving of object in a file
            FileOutputStream scoresFile = new FileOutputStream(Filename);
            ObjectOutputStream scoresOut = new ObjectOutputStream(scoresFile);
              
            // Method for serialization of object
            if (scoreInfo == null) {
            	scoreInfo = new ArrayList<Integer>();
            	scoreInfo.add(time);
            }
            else {
            scoreInfo.add(time);
            }
            scoresOut.writeObject(scoreInfo);
              
            scoresOut.close();
            scoresFile.close();
  
        }
          
        catch(IOException ex)
        {
            System.out.println("Scores IOException occured");
        }
	}
	
	/**
	 * This public method deserializes the given scoresFile into an array of integers.
	 * 
	 * @param scoresFile the File object to deserialize
	 * 
	 * @return an array of high scores from the given file
	 */
	public ArrayList<Integer> getHighScores(File scoresFile) {
		FileInputStream scoresInfoFile;
		try {
			scoresInfoFile = new FileInputStream(scoresFile);
			ObjectInputStream in = new ObjectInputStream(scoresInfoFile);
			
			scoreInfo = (ArrayList<Integer>) in.readObject();
			System.out.println("SCORE INFO:"+scoreInfo);
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return scoreInfo;
	}
	
	/**
	 * This public method is a getter for the cellStatusBoard field
	 * 
	 * @return the cellStatusBoard field
	 */
	public String[][] getStatusBoard() {
		return cellStatusBoard;
	}
	
	/**
	 * This public method is a getter for the getMineBoard field
	 * 
	 * @return the getMineBoard field
	 */
	public int[][] getMineBoard() {
		return mineBoard;
	}
	
	/**
	 * This public method is a getter for the time field
	 * 
	 * @return the time field
	 */
	public int getTime() {
		return time.intValue();
	}
	
	/**
	 * This public method is a setter for the time field
	 * 
	 * @param sec the updated time value
	 */
	public void setTime(int sec) {
		time = Integer.valueOf(sec);
	}
}
