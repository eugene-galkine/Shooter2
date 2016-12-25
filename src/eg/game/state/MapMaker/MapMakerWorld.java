package eg.game.state.MapMaker;

import javafx.scene.canvas.GraphicsContext;
import eg.game.world.World;

public class MapMakerWorld extends World
{
	private static MapMakerWorld instance;
	
	public MapMakerWorld(GraphicsContext newgc) 
	{
		super(newgc);
		instance = this;
	}
	
	public static MapMakerWorld getInstance()
	{
		return instance;
	}
}
