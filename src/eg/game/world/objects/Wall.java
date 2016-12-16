package eg.game.world.objects;

import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Wall implements IDrawable, ICollidable
{
	private float x, y;
	private Image img;
	private Rectangle r;
	
	public Wall (int inX, int inY)
	{
		img = new Image("images/bullets/bullet.png",16,16,true,true);
		x = inX;
		y = inY;
		
		r = new Rectangle(x, y, 16, 16);
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
	public Bounds getBounds() 
	{
		return r.getBoundsInLocal();
	}
}
