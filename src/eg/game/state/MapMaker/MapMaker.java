package eg.game.state.mapMaker;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import eg.game.Main;
import eg.game.state.State;

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
		
		scene.setOnScroll(mv);
		
		scene.setOnKeyPressed(mv);
		
		MapMakerWorld.getInstance().addObject(mv);
	}
}
