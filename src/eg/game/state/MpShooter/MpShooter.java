package eg.game.state.mpShooter;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import eg.game.Main;
import eg.game.map.MapFileLoader;
import eg.game.net.ClientFactory;
import eg.game.state.State;
import eg.game.world.objects.player.Player;

public class MpShooter extends State
{
	public MpShooter(GraphicsContext gc, Scene newScene, Main newParent)
	{
		super(gc, newScene, newParent);
		
		new GameWorld(gc, ClientFactory.connectToServer("localhost", 1426));
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
		
		GameWorld.getInstance().addObject(p);
		
		//load the map
		ArrayList<Object> inList = MapFileLoader.loadMap();
		
		for (Object obj : inList)
			GameWorld.getInstance().addObject(obj);
		
		/*GameWorld.getInstance().addObject(new Wall(10, 10));
		GameWorld.getInstance().addObject(new Wall(26, 10));*/
	}
}
