package eg.game.world.objects.mapMaker;

import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class MaperWall implements IDrawable, ICollidable
{
	public static final String[] imgs =
		{
			"images/map/wall0.png",
			"images/map/wall1.png"
		};
	
	private int currentImg;
	private float x, y, w, h;
	private Image img;
	private Rectangle r;
	
	public MaperWall (float inX, float inY, float w, float h)
	{
		img = new Image("images/map/empty_texture.png",16,16,true,true);
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
		if (currentImg == -1)
			img = new Image("images/map/empty_texture.png",16,16,true,true);
		else
			img = new Image(imgs[currentImg],16,16,true,true);
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
	
	public void nextImg()
	{
		//cycle through images
		currentImg++;
		if (currentImg == imgs.length)
			currentImg = 0;
		
		System.out.println("current image set to: " + imgs[currentImg]);
	}

	public int getImgID() 
	{
		return currentImg;
	}
	
	public void setImg(int i)
	{
		if (i < 0 || i > imgs.length - 1)
			return;
		
		currentImg = i;
		img = new Image(imgs[currentImg],16,16,true,true);
	}
}
