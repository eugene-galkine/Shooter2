package eg.game.world.objects.player;

import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public abstract class Person extends IUpdatable implements IDrawable
{
	public static final int IMG_WIDTH = 64;
	public static final int IMG_HEIGHT = 64;
	public static final int BOUND_SIZE_DIFF = 6;
	
	protected float x, y;
	protected float rot;
	private Image img;
	
	protected Rectangle bounds;
	
	public Person(float x, float y)
	{
		this.x = x;
		this.y = y;
		rot = 0;
		img = getImage();

		//set up collision box
		bounds = new Rectangle(x + BOUND_SIZE_DIFF, y + BOUND_SIZE_DIFF, IMG_WIDTH - 2*BOUND_SIZE_DIFF, IMG_HEIGHT - 2*BOUND_SIZE_DIFF);
		bounds.setVisible(false);
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
	public Image getImg()
	{
		return img;
	}
	
	@Override
	public float getRot()
	{
		return rot;
	}
	
	public void setRot (float newRot)
	{
		rot = newRot;
	}
	
	public Bounds getBounds()
	{
		bounds.setX(x + BOUND_SIZE_DIFF);
		bounds.setY(y + BOUND_SIZE_DIFF);
		return bounds.getBoundsInLocal();
	}
	
	public abstract Image getImage();
}
