import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.util.Duration;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import controller.MinesweeperController;

/**
 * This class defines the View for the Minesweeper package.
 *
 * @author 
 */

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {
	
	private MinesweeperModel model;
	private MinesweeperController controller;
	private GridPane board = new GridPane();
	private Label time = new Label();
	//private File savedMineGame;
	//private File savedCellGame;
	private File highScoreFile;
	private boolean highScoresExists;
	private File savedGameInfo;
	private boolean fileExists;
	private ArrayList<ArrayList<StackPane>> grid = new ArrayList<ArrayList<StackPane>>();
	
	// Survival fields
	private int[] levels = {15, 15, 15, 20, 20, 20, 25, 25, 25};
	private int[] difficulties = {15, 20, 25, 15, 20, 25, 15, 20, 25};
	private int levelsIterator = 0;
	private int currLevel = 1;
	private boolean isSurvival = false;
	
	int count = 0;
	int size = 0;
	int mineCount = 0;
	int minutes = 0;
	int seconds = 0;
	int totalSeconds = 0;
	Timeline timeline;
	Stage stage;
	boolean stopped = false;

	
	/**
	 * This method controls the GUI View (in MVC) for the Minesweeper program.
	 * 
	 */
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		BorderPane startPane = new BorderPane();
		Button newGame = new Button();
		newGame.setText("New Game");
		Button loadGame = new Button();
		loadGame.setText("Load Game");
		Button highScores = new Button();
		highScores.setText("High Scores");
		Button survival = new Button();
		survival.setText("Survival (Wow Factor)");
		Label welcome = new Label("Welcome to Minesweeper");
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.getChildren().add(welcome);
		vbox.getChildren().add(newGame);
		
		savedGameInfo = new File("save_game.dat");
		fileExists = savedGameInfo.exists();
		highScoreFile = new File("highScores.dat");
		highScoresExists = highScoreFile.exists();
		if (fileExists) {
			vbox.getChildren().add(loadGame);
			
		}
		if (highScoresExists) {
			vbox.getChildren().add(highScores);
			
		}
		vbox.getChildren().add(survival);
		startPane.setCenter(vbox);
		
		newGame.setOnMouseClicked((event) -> {
			selectSize(stage);
		});
		
		loadGame.setOnMouseClicked((event) -> {
			count = 1;
			createGame(stage, false);
		});
		
		highScores.setOnMouseClicked((event) -> {
			showScoreBoard();
		});

		survival.setOnMouseClicked((event) -> {
			createSurvival(stage);
		});
		Scene scene;
		if (fileExists) {
			scene = new Scene (startPane, 200, 200);
		} else {
			scene = new Scene (startPane, 200, 175);
		}
		stage.setScene(scene);
		stage.show();
	}
	
	private void createSurvival(Stage stage) {
		// TODO Auto-generated method stub
		mineCount = difficulties[levelsIterator];
		size = levels[levelsIterator];
		levelsIterator++;
		isSurvival = true;
		createGame(stage, true);
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
			createGame(stage, true);
			
		});
		medium.setOnMouseClicked((event) -> {
			mineCount = (size * size) / 10;
			createGame(stage, true);
			
		});
		hard.setOnMouseClicked((event) -> {
			mineCount = (size * size) / 5;
			createGame(stage, true);
			
		});
		Scene scene = new Scene (diffPane, 200, 180);
		stage.setScene(scene);
		stage.show();
	}
	
	private void createGame(Stage stage, boolean newGameBool) {
		if (!newGameBool) {
			model = new MinesweeperModel(savedGameInfo);
			size = model.totalCols();
			mineCount = model.totalBombs();
			totalSeconds = model.getTime();
			seconds = totalSeconds;
			while (seconds > 59) {
				minutes++;
				seconds = seconds - 60;
			}
		}
		else {
			model = new MinesweeperModel(size, size, mineCount); 
		}
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
				stack.getChildren().addAll(square);
				stack.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				board.add(stack,i,j);
				grid.get(i).add(stack);
			}
		}
		BorderPane window = new BorderPane();
		MenuBar menubar = null;
		GridPane pane = null;
		// --- SURVIVAL ---
		if (isSurvival) {
			pane = new GridPane();
			Label label = new Label();
			label.setText("Level " + currLevel);
			label.setStyle("-fx-font: 24 arial;");
			Button button = new Button();
			button.setVisible(false);
			button.setText("Next Level");
			button.setOnMouseClicked((event) -> {
				if (totalSeconds >= 500) {
					loseFromTime();
				}
				grid = new ArrayList<ArrayList<StackPane>>();
				board.getChildren().clear();
				mineCount = difficulties[levelsIterator];
				size = levels[levelsIterator];
				currLevel++;
				model = new MinesweeperModel(size, size, mineCount);
				controller = new MinesweeperController(model);
				
				board.getChildren().clear();
				for (int i = 0; i < size; i++) {
					grid.add(new ArrayList<StackPane>());
					for (int j = 0; j < size; j++) {
						StackPane stack = new StackPane();
						Rectangle square = new Rectangle();
						square.setFill(Color.LIGHTBLUE);
						square.setWidth(25);
						square.setHeight(25);
						stack.getChildren().addAll(square);
						stack.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
						board.add(stack,i,j);
						grid.get(i).add(stack);
					}
				}
				
				count = 0;
				totalSeconds = 0;
				seconds = 0;
				minutes = 0;
				model.setTime(0);
				model.addObserver(this);
				controller.updateMineBoard();
				model.sendUpdate();
			});
			if (controller.isGameOver()) {
				if (controller.isWon()) {
					button.setVisible(true);
				}
			}
			pane.add(label, 0, 0);
			pane.add(button, 1, 0);
			pane.setPadding(new Insets(8, 20, 8, 8));
			pane.setMaxHeight(10);
		// --- END SURVIVAL ---
		} else {
			Menu menu = new Menu("File");
			menubar = new MenuBar();
			MenuItem item = new MenuItem("New game");
			MenuItem otherItem = new MenuItem("Menu");
			menu.getItems().add(item);
			menu.getItems().add(otherItem);
			menubar.getMenus().add(menu);
		
		
		
		item.setOnAction(e -> { 
			if(stopped) {
				timeline.playFromStart();
				stopped = false;
			}
			grid = new ArrayList<ArrayList<StackPane>>();
			board.getChildren().clear();
			model = new MinesweeperModel(size, size, mineCount);
			controller = new MinesweeperController(model);
			if (savedGameInfo.exists()) {
				savedGameInfo.delete();
				savedGameInfo = new File("save_game.dat");
			};
			
			
			board.getChildren().clear();
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
			
			count = 0;
			totalSeconds = 0;
			seconds = 0;
			minutes = 0;
			model.setTime(0);
			model.addObserver(this);
			controller.updateMineBoard();
			model.sendUpdate();
		});
		
		
		otherItem.setOnAction(ev -> { 
			timeline.stop();
			stopped = true;
			board = new GridPane();
			time = new Label();
			grid = new ArrayList<ArrayList<StackPane>>();
			count = 0;
			size = 0;
			mineCount = 0;
			minutes = 0;
			seconds = 0;
			totalSeconds = 0;
			try {
				if (!controller.isGameOver()) {
					model.saveBoard();
				}
				start(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		}
		if (isSurvival) {
			window.setTop(pane);
		} else {
			window.setTop(menubar);
		}
		window.setCenter(board);
		time.setText(String.valueOf(seconds));
		window.setBottom(time);
		//Scene scene = new Scene (window, 556, 584);
		Scene scene = null;
		if (isSurvival) {
			scene = new Scene (window, size * 27 + 16, levels[levelsIterator] * 27 + 59 + 18);
		} else {
			scene = new Scene (window, size * 27 + 16, size * 27 + 59);
		}
		stage.setScene(scene);
		stage.show();
		if (!isSurvival) {
			stage.setOnCloseRequest(e -> {
				
				if (controller.isGameOver()) {
					savedGameInfo.delete();  
					//savedCellGame.delete();
				}
				else if (!isSurvival) {
					model.setTime(totalSeconds);
					model.saveBoard();
				} });
		}
//		EventHandler<MouseEvent> clickHan = new clickHandler();
//		board.addEventHandler(MouseEvent.MOUSE_CLICKED, clickHan);
		board.setOnMouseClicked((event) -> {
			if (!controller.isGameOver()) {
			int x = getIndexFromPosition(event.getX());
			int y = getIndexFromPosition(event.getY());
			if (count == 0) {
				//seconds = 50;
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
				}//
			}
			else if (model.cellStateAtCoords(y, x).equals("covered")){
				model.updateCellState(y, x, "uncovered");
				controller.clicked(x, y, size);
				if (isSurvival) {
					if (model.isMineLocation(y,x)) {
						lose();
					} else if (controller.isWon()) {
						if (levelsIterator + 1 == levels.length) {
							win();
							return;
						}
						GridPane gpane = (GridPane) window.getChildren().get(0);
						gpane.setPadding(new Insets(8, 20, 8, 8));
						Button button = (Button) gpane.getChildren().get(1);
						currLevel++;
						button.setVisible(true);
						button.setOnMouseClicked((anotherEvent) -> {
							if (totalSeconds >= 300) {
								loseFromTime();
								return;
							}
							Label label = new Label();
							label.setText("Level " + currLevel);
							label.setStyle("-fx-font: 24 arial;");
							gpane.getChildren().set(0, label);
							
							button.setVisible(false);
							grid = new ArrayList<ArrayList<StackPane>>();
							board.getChildren().clear();
//							createGame(stage, true);
							levelsIterator++;
							mineCount = difficulties[levelsIterator];
							size = levels[levelsIterator];
							model = new MinesweeperModel(size, size, mineCount);
							controller = new MinesweeperController(model);
							
							board.getChildren().clear();
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
							stage.setHeight(levels[levelsIterator] * 27 + 59 + 40);
							stage.setWidth(levels[levelsIterator] * 27 + 16);
							
							count = 0;
							totalSeconds = 0;
							seconds = 0;
							minutes = 0;
							model.setTime(0);
							model.addObserver(this);
							controller.updateMineBoard();
							model.sendUpdate();
							
						});
					}
				} else {
					if (controller.isGameOver()) {
						//board.removeEventHandler(MouseEvent.MOUSE_CLICKED, event);
					}
					if (model.isMineLocation(y,x)) {
						lose();
					}
					if (controller.isWon()) {
						win();
					}
				}
			}
			}// Will need to use controller, using model for demonstration purposes
		});
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    // KeyFrame event handler
					@Override
                    public void handle(ActionEvent actionEvent) {
                        seconds++;
                        totalSeconds++;
                        DecimalFormat formatter = new DecimalFormat("00");
                        String secFormatted = formatter.format(seconds);
                        String minFormatted = formatter.format(minutes);
                        if (seconds == 60) {
                        	minutes++;
                        	seconds = 0;
                        	secFormatted = formatter.format(seconds);
                        }
                        time.setText("Timer " + minFormatted + ":" + secFormatted);
                        model.setTime(totalSeconds);
                        model.sendUpdate();
                        }

					}));
                      
		timeline.playFromStart();
		stopped = false;
		model.sendUpdate(); //
	}
	
	/**
	 * 
	 * Stops the game once the player has exceeded the time limit in Survival (Wow Factor).
	 */
	private void loseFromTime() {
		// TODO Auto-generated method stub
		timeline.stop();
		stopped = true;
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("Message");
		a.setContentText("Due to time, running over five minutes, you lose. You were allowed to continue to finish because I find it funny to watch people helplessly struggle. Just as I have on this assignment. Now, you shall know my pain. GG, bud.");
		a.setHeaderText("Game Over");
		a.showAndWait();
	}

	/**
	 * This private method takes a position on screen, and converts it to its associated index in the mine board. This method handles both X and Y coordinates since the board is square.
	 * 
	 * @param position the location in the window of a mouse click
	 * 
	 * @return the associated index
	 */
	private int getIndexFromPosition(double position) {
		// minus 8 for inset
		int aligned = (int) position - 8;
		
		// 27 is the width of each square
		return aligned / 27;
	}
	
	/**
	 * This public method implements the update method of the Observer interface. This method is called by the model after this object has been added as an observer.
	 * 
	 * This method redraws the board based on changes made to the underlying model.
	 */
	@Override
	public void update(Observable o, Object arg) {
		MinesweeperModel model = (MinesweeperModel) o;
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
		totalSeconds = model.getTime();
		DecimalFormat formatter = new DecimalFormat("00");
        String secFormatted = formatter.format(seconds);
        String minFormatted = formatter.format(minutes);
		time.setText("Timer " + minFormatted + ":" + secFormatted);	
	}
	
	/**
	 * This private method displays an alert in the case that the player wins at the end of the game.
	 */
	private void win() {
		if (savedGameInfo.exists() && !isSurvival) {
			savedGameInfo.delete();
		};
		if (highScoreFile.exists()) {
			model.saveHighScores(model.getTime());
		}
		else {
			highScoreFile = new File("highScores.dat");
			model.saveHighScores(model.getTime());
		}
		timeline.stop();
		stopped = true;
		Alert b = new Alert(Alert.AlertType.INFORMATION);
		b.setTitle("Message");
		b.setContentText("You uncovered all the correct mines. You Won!");
		b.setHeaderText("Victory");
		b.showAndWait();
	}
	
	/**
	 * This private method displays an alert in the case that the player loses at the end of the game.
	 */
	private void lose() {
		timeline.stop();
		if (savedGameInfo.exists() && !isSurvival) {
			savedGameInfo.delete();
		};
		stopped = true;
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("Message");
		a.setContentText("You uncovered a mine. You lose!");
		a.setHeaderText("Game Over");
		a.showAndWait();
	}

	private void showScoreBoard() {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle("High Scores");
		String scores = "";
		model = new MinesweeperModel();
		if (model.getHighScores(highScoreFile) != null) {
			 ArrayList<Integer> x = model.getHighScores(highScoreFile);
			 Collections.sort(x);
			 scores ="Current High Scores: \n";
			 if (x.size() < 5) {
				 for (int i = 0; i < x.size(); i++) {
					 int minutes = x.get(i)/60;
					 int seconds = x.get(i)%60;
					 if (seconds < 10) {
						 scores += (i+1) + ". " + minutes + ":0" + seconds + " \n";
					 }
					 else {
					 scores += (i+1) + ". " + minutes + ":" + seconds + " \n";
					 }
				 } 
			 }
			 else {
				 for (int i = 0; i < 5; i++) {
					 int minutes = x.get(i)/60;
					 int seconds = x.get(i)%60;
					 if (seconds < 10) {
						 scores += (i+1) + ". " + minutes + ":0" + seconds + " \n";
					 }
					 else {
					 scores += (i+1) + ". " + minutes + ":" + seconds + " \n";
					 }
				 } 
			 }
		}
		else {
			scores = "No high scores yet, keep playing!";
		}
		a.setContentText(scores);
		a.setHeaderText("Top 5 High Scores");
		a.showAndWait();
	}
}
