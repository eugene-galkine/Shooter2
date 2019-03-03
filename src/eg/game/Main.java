package eg.game;

import eg.game.state.*;
import eg.game.state.mpShooter.MpShooter;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class Main extends Application
{
	public static final int WINDOW_HEIGHT = 800;
	public static final int WINDOW_WIDTH = 800;
	public static final int MAGIC_NUM = 348;
	private static final boolean FULLSCREEN = false;
	
	private Scene scene;
	//AnchorPane pane;
    private State gameState;

	@Override
	public void start(Stage stage) {
		//JavaFX init
		Group root = new Group();
		scene = new Scene(root);
		stage.setResizable(false);
		stage.setTitle("Shooter2");
		stage.setScene(scene);
		stage.setWidth(WINDOW_WIDTH);
		stage.setHeight(WINDOW_HEIGHT);
		stage.setFullScreen(FULLSCREEN);
		
		Canvas canvas = new Canvas( WINDOW_WIDTH, WINDOW_HEIGHT );
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		root.getChildren().add( canvas );
		stage.show();
		
		//TODO: changeState(new MainMenu(pane, scene, this));
		changeState(new MpShooter(gc, scene, this));
	}
	
	private void changeState(State state)
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
