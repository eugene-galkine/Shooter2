package eg.game.state.MapMaker;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import eg.game.world.World;

public class MapMakerWorld extends World
{
	private static MapMakerWorld instance;
	
	private ArrayList<Object> list;
	
	public MapMakerWorld(GraphicsContext newgc) 
	{
		super(newgc);
		instance = this;
		list = new ArrayList<Object>();
	}
	
	public static MapMakerWorld getInstance()
	{
		return instance;
	}
	
	@Override
	public void addObject(Object obj)
	{
		super.addObject(obj);
		list.add(obj);
	}
	
	@Override
	public void removeObject(Object obj)
	{
		//don't remove map controller
		if (obj instanceof MapView)
			return;
		
		super.removeObject(obj);
		list.remove(obj);
	}
	
	public void removeLast()
	{
		removeObject(list.get(list.size() - 1));
	}

	public void zoom(float i) 
	{
		//zoom the drawable
		drawer.zoom(i);
	}
}
