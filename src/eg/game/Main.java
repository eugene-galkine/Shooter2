package eg.game;

import eg.game.state.*;
import eg.game.state.MapMaker.MapMaker;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application
{
	private static final int WINDOW_HEIGHT = 800;
	private static final int WINDOW_WIDTH = 800;
	private static final boolean FULLSCREEN = false;
	
	Scene scene;
	//AnchorPane pane;
	State gameState;

	@Override
	public void start(Stage stage) throws Exception
	{
		//JavaFX init
		//pane = new AnchorPane();
		//scene = new Scene(pane, WINDOW_HEIGHT, WINDOW_WIDTH);
		Group root = new Group();
		scene = new Scene(root);
		stage.setResizable(false);
		stage.setTitle("Shooter2");
		stage.setScene(scene);
		stage.setFullScreen(FULLSCREEN);
		
		Canvas canvas = new Canvas( WINDOW_WIDTH, WINDOW_HEIGHT );
		root.getChildren().add( canvas );
		
		stage.show();
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//TODO: changeState(new MainMenu(pane, scene, this));
		changeState(new MapMaker(gc, scene, this));
	}
	
	public void changeState(State state)
	{
		//pane.getChildren().clear();
		gameState = state;
		gameState.start();
	}
	
	public static void main (String[] args)
	{
		launch(args);
		System.exit(0);
	}
}
