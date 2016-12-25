package eg.game.world;

import java.util.LinkedList;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;

public abstract class World
{	
	private LinkedList<ICollidable> solidObjects;
	private WorldUpdater updater;
	private WorldDrawer drawer;
	
	public World(GraphicsContext newgc)
	{
		solidObjects = new LinkedList<ICollidable>();
		updater = new WorldUpdater();
		drawer = new WorldDrawer(newgc);
		drawer.start();
		new Thread(updater).start();
	}
	
	public void addObject(Object obj)
	{
		//add object with image to the world
		if (obj instanceof IDrawable)
			drawer.add((IDrawable)obj);
		
		//add updatable object to the world
		if (obj instanceof IUpdatable)
			updater.add((IUpdatable)obj);
		
		//add solid object to the world
		if (obj instanceof ICollidable)
			synchronized (solidObjects)
			{
				solidObjects.add((ICollidable)obj);
			}
	}
	
	public void removeObject(Object obj)
	{
		//remove object with image from the world
		if (obj instanceof IDrawable)
			drawer.remove((IDrawable)obj);
		
		//remove updatable object from the world
		if (obj instanceof IUpdatable)
			updater.remove((IUpdatable)obj);
		
		//add solid object from the world
		if (obj instanceof ICollidable)
			synchronized (solidObjects)
			{
				solidObjects.remove((ICollidable)obj);
			}
	}
	
	public void updateCamera(float x, float y)
	{
		//move camera over player
		drawer.setOffset(x, y);
	}
	
	public boolean checkCollision(Bounds b)
	{
		synchronized (solidObjects)
		{
			//check if this object intersects any that are collidable
			for (ICollidable obj : solidObjects)
				if (b.intersects(obj.getBounds()))
					return true;
			
			return false;
		}
	}
}
