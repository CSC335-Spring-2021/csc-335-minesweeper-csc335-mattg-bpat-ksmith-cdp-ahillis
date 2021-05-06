

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import controller.MinesweeperController;
import model.MinesweeperBoard;
import model.MinesweeperModel;


/**
 * This class tests the model, control, and board
 *
 * @author kylesmith, brendanp, matthewgleason, austinhillis6, cdpeterson
 */
public class MinesweeperTest {
	
	@Test
	void testModel() {
		MinesweeperModel model = new MinesweeperModel(12, 8, 2);
		MinesweeperModel emptyConstructorModel = new MinesweeperModel();
		model.printBoards();
		model.saveBoard();
		model = new MinesweeperModel(12, 12, 10);
		model.printBoards();
		
		assertEquals(0, model.getTime());
		assertEquals(10, model.totalBombs());
		assertEquals(12, model.totalCols());
		assertEquals(12, model.totalRows());
		assertEquals("covered", model.cellStateAtCoords(0, 0));
		assertEquals(0, model.mineAtCoords(0, 0));
		
		Set<int[]> mines = new HashSet();
		model = new MinesweeperModel(12, 12, 0);
		assertEquals(mines, model.returnMineLocations());
		
		model.updateMine(0, 0, 10);
		assertEquals(10, model.mineAtCoords(0, 0));
		
		model.updateMine(0, 0, -1);
		assertEquals(-1, model.mineAtCoords(0, 0));
		
		model.updateCellState(0, 0, "bomb");
		
		model.setTime(10);
		assertEquals(10, model.getTime());
		
		model.saveHighScores(10);
	}
	
	@Test
	void testBoard() {
		String[][] stringBoard = new String[2][2];
		stringBoard[0][0] = "covered";
		stringBoard[0][1] = "covered";
		stringBoard[1][0] = "covered";
		stringBoard[1][1] = "covered";
		int[][] intBoard = new int[2][2];
		intBoard[0][0] = 0;
		intBoard[0][1] = 0;
		intBoard[1][0] = 0;
		intBoard[1][1] = 0;
		
		MinesweeperBoard board = new MinesweeperBoard(intBoard, stringBoard, 0);
		
		assertEquals(stringBoard, board.getStatusBoard());
		assertEquals(intBoard, board.getMineBoard());
		board.setTime(1);
		assertEquals(1, board.getTime());
		assertEquals(stringBoard, board.getStatusBoard());
		
		MinesweeperBoard emptyConstructorBoard = new MinesweeperBoard();
	}
	
	@Test
	void testController() {
		String[][] stringBoard = new String[2][2];
		stringBoard[0][0] = "covered";
		stringBoard[0][1] = "covered";
		stringBoard[1][0] = "covered";
		stringBoard[1][1] = "covered";
		int[][] intBoard = new int[2][2];
		intBoard[0][0] = -1;
		intBoard[0][1] = 0;
		intBoard[1][0] = 0;
		intBoard[1][1] = 0;
		
		MinesweeperBoard board = new MinesweeperBoard(intBoard, stringBoard, 0);
		MinesweeperModel model = new MinesweeperModel(10, 10, 10);
		MinesweeperController controller = new MinesweeperController(model);
		
		controller.updateMineBoard();
		
		
		assertEquals(false, controller.isGameOver());
		assertEquals(false, controller.isWon());
		
		controller.clicked(0, 0, 3);
		controller.uncoverAroundZero(0, 0, 0);
		controller.uncoverAroundZero(2, 2, 1);
		controller.uncoverAroundZero(1, 2, 1);
		controller.uncoverAroundZero(3, 3, 3);
		controller.uncoverAroundZero(3, 3, 0);
		
		assertEquals(false, controller.isMine(0, 0));
		
		controller.setBoardSize(10, 10);
	}	
}