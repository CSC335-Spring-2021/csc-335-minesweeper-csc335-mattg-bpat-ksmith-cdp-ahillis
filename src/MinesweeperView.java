import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import javafx.scene.layout.VBox;
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
	int size = 0;
	int mineCount = 0;
	
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane startPane = new BorderPane();
		Button newGame = new Button();
		newGame.setText("New Game");
		Button loadGame = new Button();
		loadGame.setText("Load Game");
		Label welcome = new Label("Welcome to Minesweeper");
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.getChildren().add(welcome);
		vbox.getChildren().add(newGame);
		vbox.getChildren().add(loadGame);
		startPane.setCenter(vbox);
		
		newGame.setOnMouseClicked((event) -> {
			selectSize(stage);
		});
		Scene scene = new Scene (startPane, 200, 150);
		stage.setScene(scene);
		stage.show();
	}
	
	private void selectSize(Stage stage) {
		BorderPane sizePane = new BorderPane();
		Button small = new Button();
		small.setText("Small");
		Button medium = new Button();
		medium.setText("Medium");
		Button large = new Button();
		large.setText("Large");
		Label welcome = new Label("Select Board Size");
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.getChildren().add(welcome);
		vbox.getChildren().add(small);
		vbox.getChildren().add(medium);
		vbox.getChildren().add(large);
		sizePane.setCenter(vbox);
		
		small.setOnMouseClicked((event) -> {
			size = 15;
			selectDiff(stage);
			
		});
		medium.setOnMouseClicked((event) -> {
			size = 20;
			selectDiff(stage);
		});
		large.setOnMouseClicked((event) -> {
			size = 25;
			selectDiff(stage);
		});
		Scene scene = new Scene (sizePane, 200, 180);
		stage.setScene(scene);
		stage.show();
	}
	
	private void selectDiff(Stage stage) {
		BorderPane diffPane = new BorderPane();
		Button easy = new Button();
		easy.setText("Easy");
		Button medium = new Button();
		medium.setText("Medium");
		Button hard = new Button();
		hard.setText("Hard");
		Label welcome = new Label("Select Difficulty");
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.getChildren().add(welcome);
		vbox.getChildren().add(easy);
		vbox.getChildren().add(medium);
		vbox.getChildren().add(hard);
		diffPane.setCenter(vbox);
		
		easy.setOnMouseClicked((event) -> {
			mineCount = (size * size) / 15;
			createGame(stage);
			
		});
		medium.setOnMouseClicked((event) -> {
			mineCount = (size * size) / 10;
			createGame(stage);
			
		});
		hard.setOnMouseClicked((event) -> {
			mineCount = (size * size) / 5;
			createGame(stage);
			
		});
		Scene scene = new Scene (diffPane, 200, 180);
		stage.setScene(scene);
		stage.show();
	}
	
	private void createGame(Stage stage) {
		model = new MinesweeperModel(size, size, mineCount); // 20x20, with 40 bombs (~10% of the board)
		model.addObserver(this);
		controller = new MinesweeperController(model); // add model to () when its more done
		stage.setTitle("Minesweeper");
		BackgroundFill backgroundTan = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY) ;
		Background background = new Background(backgroundTan);
		board.setBackground(background);
		board.setPadding(new Insets(8,8,8,8));
		for (int i = 0; i < size; i++) {
			grid.add(new ArrayList<StackPane>());
			for (int j = 0; j < size; j++) {
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
		//Scene scene = new Scene (window, 556, 584);
		Scene scene = new Scene (window, size * 27 + 16, size * 27 + 44);
		stage.setScene(scene);
		stage.show();
		
		board.setOnMouseClicked((event) -> {
			int x = getIndexFromPosition(event.getX());
			int y = getIndexFromPosition(event.getY());
			System.out.println("(x, y): " + x + ", " + y);
			if (count == 0) {
				model = new MinesweeperModel(size, size, mineCount); // 20x20, with 40 bombs (~10% of the board)
				controller = new MinesweeperController(model);// add model to () when its more done
				//while (controller.isMine(model.returnMinesBoard(), model.returnCellStateBoard(), y, x)) {
				while (model.isMineLocation(y, x)) {
					model = new MinesweeperModel(size, size, mineCount); // 20x20, with 40 bombs (~10% of the board)
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
				controller.clicked(x, y, size);
				if (model.isMineLocation(y,x)) {
					lose();
				}
				if (controller.isWon()) {
					win();
				}
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
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
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
		
	}
	
	/**
	 * Displays an alert in the case that the player wins at the end of the game.
	 */
	private void win() {
		Alert b = new Alert(Alert.AlertType.INFORMATION);
		b.setTitle("Message");
		b.setContentText("You uncovered all the correct mines. You Won!");
		b.setHeaderText("Victory");
		b.showAndWait();
	}
	
	/**
	 * Displays an alert in the case that the player loses at the end of the game.
	 */
	private void lose() {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("Message");
		a.setContentText("You uncovered a mine. You lose!");
		a.setHeaderText("Game Over");
		a.showAndWait();
	}

}
