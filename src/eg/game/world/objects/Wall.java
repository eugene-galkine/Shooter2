package eg.game.world.objects;

import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Wall implements IDrawable, ICollidable
{	
	private int currentImg;
	private float x, y, w, h;
	private Image img;
	private Rectangle r;
	
	public Wall (float inX, float inY)
	{
		img = new Image("images/map/wall1.png",16,16,true,true);
		x = inX;
		y = inY;
		w = 16;
		h = 16;
		currentImg = 1;
		r = new Rectangle(x, y, 16, 16);
	}
	
	public Wall (float inX, float inY, float w, float h)
	{
		img = new Image("images/map/selected_texture.png",16,16,true,true);
		x = inX;
		y = inY;
		this.w = w;
		this.h = h;
		currentImg = -1;
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
}
