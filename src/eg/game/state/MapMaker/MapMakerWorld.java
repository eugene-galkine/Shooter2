package eg.game.state.mapMaker;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import eg.game.map.MapFileLoader;
import eg.game.world.World;
import eg.game.world.objects.Wall;

public class MapMakerWorld extends World
{
	private static MapMakerWorld instance;
	
	private final ArrayList<Object> list;
	
	public MapMakerWorld(GraphicsContext newgc) 
	{
		super(newgc);
		instance = this;
		list = new ArrayList<>();
	}
	
	public static MapMakerWorld getInstance()
	{
		return instance;
	}
	
	@Override
	public void addObject(Object obj)
	{
		super.addObject(obj);
		if (obj instanceof Wall)
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
	
	private void removeLast()
	{
		removeObject(list.get(list.size() - 1));
	}

	public void zoom(float i) 
	{
		//change zoom level
		drawer.zoom(i);
	}

	public void saveMap() 
	{
		MapFileLoader.saveMap(list);
	}

	public void loadMap() 
	{
		//remove all the objects
		while (list.size() > 0)
			removeLast();
		
		ArrayList<Object> inList = MapFileLoader.loadMap();
		
		for (Object obj : inList)
			addObject(obj);
	}
}
