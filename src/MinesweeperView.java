import java.io.File;
import java.util.Observer;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import javafx.stage.Stage;
import model.MinesweeperModel;
import controller.MinesweeperController;

public class MinesweeperView extends Application {
	
	private MinesweeperModel model;
	private MinesweeperController controller;
	private GridPane board = new GridPane();
	private File savedGame;
	private boolean fileExists;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		model = new MinesweeperModel();
		controller = new MinesweeperController(); // add model to () when its more done
		stage.setTitle("Minesweeper");
		BackgroundFill backgroundTan = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY) ;
		Background background = new Background(backgroundTan);
		board.setBackground(background);
		board.setPadding(new Insets(8,8,8,8));
		//make 20 x 20 grid
		for (int i = 0; i < 20; i++) {
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
			}
		
		}
	}

}
