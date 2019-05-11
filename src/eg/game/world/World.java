package eg.game.world;

import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

public abstract class World
{	
	private final LinkedList<ICollidable> solidObjects = new LinkedList<>();
	private final WorldUpdater updater;
	protected final WorldDrawer drawer;
	
	protected World(GraphicsContext newgc) {
		updater = new WorldUpdater();
		drawer = new WorldDrawer(newgc);
		drawer.start();
		updater.start();
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
				solidObjects.add(0, (ICollidable)obj);
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
				solidObjects.remove(obj);
			}
	}
	
	public void updateCamera(float x, float y)
	{
		//move camera over player
		drawer.setOffset(x, y);
	}
	
	public boolean collidesWith(ICollidable obj, Bounds b)
	{
		if (((IDrawable)obj).getRot() == 0 && b.intersects(obj.getBounds()))
			return true;
		else if (((IDrawable)obj).getRot() != 0)
		{
			//special collision case when object is rotated
			IDrawable drawObj = (IDrawable)obj;
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(drawObj.getRot()), drawObj.getX() + drawObj.getWidth() / 2, drawObj.getY() + drawObj.getHeight() / 2);
			Shape s = at.createTransformedShape(new Rectangle((int)drawObj.getX(), (int)drawObj.getY(), (int)drawObj.getWidth(), (int)drawObj.getHeight()));
			return s.intersects(new Rectangle((int) b.getMinX(), (int) b.getMinY(), (int) b.getWidth(), (int) b.getHeight()));
		}
		
		return false;
	}
	
	public ICollidable checkCollision(Bounds b)
	{
		synchronized (solidObjects)
		{
			//check if this object intersects any that are collidable
			for (ICollidable obj : solidObjects)
			{
				if (collidesWith(obj, b))
					return obj;
			}
			
			return null;
		}
	}
}
