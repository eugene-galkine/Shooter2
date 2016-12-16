package eg.game.state;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import eg.game.Main;
import eg.game.net.ClientFactory;
import eg.game.world.World;
import eg.game.world.objects.Wall;
import eg.game.world.objects.player.Player;

public class Game extends State
{
	public Game(GraphicsContext gc, Scene newScene, Main newParent)
	{
		super(gc, newScene, newParent);
		
		new World(gc, ClientFactory.connectToServer("localhost", 1426));
	}

	@Override
	public void start()
	{
		//add player into the world and give input handling to him
		Player p = new Player(348,348);
		
		scene.setOnMouseMoved(p);
		scene.setOnMouseDragged(p);
		scene.setOnMousePressed(p);
		scene.setOnMouseReleased(p);
		
		scene.setOnKeyPressed(p);
		scene.setOnKeyReleased(p);
		
		World.getInstance().addObject(p);
		
		World.getInstance().addObject(new Wall(10, 10));
		World.getInstance().addObject(new Wall(26, 10));
	}
}
