package eg.game.state;

import eg.game.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

public class MainMenu extends State
{
	Button start, exit;

	public MainMenu(GraphicsContext gc, Scene newScene, Main newParent)
	{
		super(gc, newScene, newParent);
	}

	@Override
	public void start()
	{
		start = new Button("Start Game");
		exit = new Button("Quit");
		//start button init
		start.setScaleX(2);
		start.setScaleY(2);
		start.setLayoutX(350);
		start.setLayoutY(300);
		start.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				startClicked();
			}
		});
		
		//exit button init
		exit.setScaleX(2);
		exit.setScaleY(2);
		exit.setLayoutX(370);
		exit.setLayoutY(550);
		exit.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				exitClicked();
			}
		});
		
		//pane.getChildren().addAll(start,exit);
	}

	private void exitClicked()
	{
		System.exit(0);
	}
	
	private void startClicked()
	{
		//parent.changeState(new Game(pane, scene, parent));
	}
}
