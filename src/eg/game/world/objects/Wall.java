package eg.game.world.objects;

import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Wall implements IDrawable, ICollidable
{
	private float x, y, w, h;
	private Image img;
	private Rectangle r;
	
	public Wall (int inX, int inY)
	{
		img = new Image("images/bullets/bullet.png",16,16,true,true);
		x = inX;
		y = inY;
		w = 16;
		h = 16;
		
		r = new Rectangle(x, y, 16, 16);
	}
	
	public Wall (int inX, int inY, int w, int h)
	{
		img = new Image("images/bullets/bullet.png",16,16,true,true);
		x = inX;
		y = inY;
		this.w = w;
		this.h = h;
		
		r = new Rectangle(x, y, w, h);
	}
	
	@Override
	public Image getImg() 
	{
		return img;
	}

	@Override
	public float getX() 
	{
		return x;
	}

	@Override
	public float getY() 
	{
		return y;
	}

	public float getWidth()
	{
		return w;
	}
	
	public void setWidth(float w)
	{
		this.w = w;
	}
	
	public float getHeight()
	{
		return h;
	}
	
	public void setHeight(float h)
	{
		this.h = h;
	}
	
	@Override
	public Bounds getBounds() 
	{
		return r.getBoundsInLocal();
	}
}
