package eg.game.state.MapMaker;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import eg.game.Main;
import eg.game.state.State;
import eg.game.world.objects.Wall;

public class MapMaker extends State
{
	public MapMaker(GraphicsContext gc, Scene newScene, Main newParent) 
	{
		super(gc, newScene, newParent);
		
		new MapMakerWorld(gc);
	}

	@Override
	public void start() 
	{
		MapView mv = new MapView();
		scene.setOnMouseMoved(mv);
		scene.setOnMouseDragged(mv);
		scene.setOnMousePressed(mv);
		scene.setOnMouseReleased(mv);
		
		scene.setOnKeyPressed(mv);
		
		MapMakerWorld.getInstance().addObject(mv);
		MapMakerWorld.getInstance().addObject(new Wall(10, 10, 40, 90));
		MapMakerWorld.getInstance().addObject(new Wall(72, 10));
		MapMakerWorld.getInstance().addObject(new Wall(72, 26));
	}
}
