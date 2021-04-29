import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import controller.MinesweeperController;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {
	
	private MinesweeperModel model;
	private MinesweeperController controller;
	private GridPane board = new GridPane();
	private File savedGame;
	private boolean fileExists;
	private ArrayList<ArrayList<StackPane>> grid = new ArrayList<ArrayList<StackPane>>();
	int count = 0;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		model = new MinesweeperModel(20, 20, 40); // 20x20, with 40 bombs (~10% of the board)
		model.addObserver(this);
		controller = new MinesweeperController(model); // add model to () when its more done
		stage.setTitle("Minesweeper");
		BackgroundFill backgroundTan = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY) ;
		Background background = new Background(backgroundTan);
		board.setBackground(background);
		board.setPadding(new Insets(8,8,8,8));
		//make 20 x 20 grid
		for (int i = 0; i < 20; i++) {
			grid.add(new ArrayList<StackPane>());
			for (int j = 0; j < 20; j++) {
				StackPane stack = new StackPane();
				Rectangle square = new Rectangle();
				square.setFill(Color.LIGHTBLUE);
				square.setWidth(25);
				square.setHeight(25);
				//stack.setPadding(new Insets(2,2,2,2));
				stack.getChildren().addAll(square);
				stack.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				board.add(stack,i,j);
				grid.get(i).add(stack);
			}
		}
		
		Menu menu = new Menu("File");
		MenuBar menubar = new MenuBar();
		MenuItem item = new MenuItem("New game");
		menu.getItems().add(item);
		menubar.getMenus().add(menu);
		BorderPane window = new BorderPane();
		
		window.setTop(menubar);
		window.setCenter(board);
		Scene scene = new Scene (window, 556, 584);
		stage.setScene(scene);
		stage.show();
		
		board.setOnMouseClicked((event) -> {
			int x = getIndexFromPosition(event.getX());
			int y = getIndexFromPosition(event.getY());
			System.out.println("(x, y): " + x + ", " + y);
			if (count == 0) {
				model = new MinesweeperModel(20, 20, 40); // 20x20, with 40 bombs (~10% of the board)
				controller = new MinesweeperController(model);// add model to () when its more done
				while (controller.isMine(model.returnMinesBoard(), model.returnCellStateBoard(), y, x)) {
					model = new MinesweeperModel(20, 20, 40); // 20x20, with 40 bombs (~10% of the board)
					controller = new MinesweeperController(model);// add model to () when its more done
				}
				model.addObserver(this);
				controller.updateMineBoard();
				count++;
			}
			
			
			// TODO: Complete turn, including mine checking
			
			if (event.getButton() == MouseButton.SECONDARY) {
				if (! model.cellStateAtCoords(y, x).equals("uncovered")) {
					if (model.cellStateAtCoords(y, x).equals("flagged")) {
						model.updateCellState(y, x, "covered");
					}
					else {
					model.updateCellState(y, x, "flagged");
					}
				}
			}
			else {
				model.updateCellState(y, x, "uncovered");
				controller.clicked(x, y);
			} // Will need to use controller, using model for demonstration purposes
		});
		
		model.sendUpdate();
	}
	
	private int getIndexFromPosition(double position) {
		// minus 8 for inset
		int aligned = (int) position - 8;
		
		// 27 is the width of each square
		return aligned / 27;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		MinesweeperModel model = (MinesweeperModel) o;
		// MinesweeperBoard board = (MinesweeperBoard) arg;
		// update current state of board
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				Rectangle square = (Rectangle) grid.get(j).get(i).getChildren().get(0);
				switch (model.cellStateAtCoords(i, j)) {
				case "covered":
					square.setFill(Color.LIGHTBLUE);
					break;
				case "uncovered":
					square.setFill(Color.TRANSPARENT);
					Text text = new Text();
					if (model.mineAtCoords(i, j) > 0) {
						text.setText(String.valueOf(model.mineAtCoords(i, j)));
						grid.get(j).get(i).getChildren().add(text);
					}
					else if (model.mineAtCoords(i, j) < 0) {
						text.setText("*");
						grid.get(j).get(i).getChildren().add(text);
					}
					break;
				case "flagged":
					square.setFill(Color.RED);
					break;
				default:
				}
			}
		}


		// check if game over
		
	}

}
