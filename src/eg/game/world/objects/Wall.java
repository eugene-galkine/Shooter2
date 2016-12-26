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
	
	public Wall (float inX, float inY)
	{
		img = new Image("images/bullets/bullet.png",16,16,true,true);
		x = inX;
		y = inY;
		w = 16;
		h = 16;
		
		r = new Rectangle(x, y, 16, 16);
	}
	
	public Wall (float inX, float inY, float w, float h)
	{
		img = new Image("images/map/selected_texture.png",16,16,true,true);
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

	@Override
	public float getWidth()
	{
		return w;
	}
	
	@Override
	public float getHeight()
	{
		return h;
	}
	
	@Override
	public Bounds getBounds() 
	{
		return r.getBoundsInLocal();
	}
	
	/*
	 * editor functions
	 */
	
	public void setWidth(float w)
	{
		this.w = w;
		r.setWidth(w);
	}
	
	public void setHeight(float h)
	{
		this.h = h;
		r.setHeight(h);
	}
	
	public void select()
	{
		img = new Image("images/map/selected_texture.png",16,16,true,true);
	}

	public void deselect() 
	{
		img = new Image("images/map/empty_texture.png",16,16,true,true);
	}
	
	public void normalize()
	{
		//fix negative size
		if (w < 0)
		{
			w = Math.abs(w);
			x -= w;
			r.setWidth(w);
			r.setX(x);
		}
		
		if (h < 0)
		{
			h = Math.abs(h);
			y -= h;
			r.setHeight(h);
			r.setY(y);
		}
	}
}
