package eg.game.world;

import java.util.LinkedList;

import eg.game.world.objects.interfaces.IDrawable;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

public class WorldDrawer extends AnimationTimer
{
	private GraphicsContext gc;
	private float offX, offY, zoom;
	private LinkedList<IDrawable> objects;
	
	public WorldDrawer (GraphicsContext newGC)
	{
		gc = newGC;
		offX = 0;
		offY = 0;
		objects = new LinkedList<IDrawable>();
		zoom = 1;
	}
	
	public void setOffset(float x, float y) 
	{
		offX = x;
		offY = y;
	}
	
	public synchronized void add(IDrawable obj) 
	{
		synchronized (objects) 
		{
			objects.add(obj);
		}
	}

	public synchronized void remove(IDrawable obj) 
	{
		synchronized (objects) 
		{
			objects.remove(obj);
		}
	}
	
	@Override
	public void handle(long arg0) 
	{
		//clear the screen
		gc.clearRect(0, 0, 800, 800);
		
		synchronized (objects) 
		{
			//draw everything
			for (IDrawable drawable : objects)
			{
				gc.save();
				
				//rotate canvas if needed
				if (drawable.getRot() != 0)
					rotate(drawable.getRot(), 
							drawable.getX() + offX + (drawable.getImg().getWidth()/2), 
							drawable.getY() + offY + (drawable.getImg().getHeight()/2));
		        
				gc.drawImage(drawable.getImg(), drawable.getX() * zoom + offX, drawable.getY() * zoom + offY, drawable.getWidth() * zoom, drawable.getHeight() * zoom);
				gc.restore();
			}
		}
	}
	
	private void rotate(double angle, double px, double py) 
	{
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

	public void zoom(float i) 
	{
		zoom = i;
	}
}
