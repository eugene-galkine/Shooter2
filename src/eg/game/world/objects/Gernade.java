package eg.game.world.objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;

public class Gernade extends IUpdatable implements IDrawable
{
	private static final float speed = 30;
	private static final float imgSize = 16;
	
	private float x, y, mx, my;
	private Image img;
	private Rectangle rect;
	private int ownerID;
	
	public Gernade (float x, float y, float dir, int id)
	{
		this.x = x - (imgSize/2);
		this.y = y - (imgSize/2);
		this.mx = (float) (Math.sin(Math.toRadians(dir)) * speed);
		this.my = (float) -(Math.cos(Math.toRadians(dir)) * speed);
		this.ownerID = id;
		
		img = new Image("images/bullets/gernade.png", imgSize, imgSize, true, true);
		
		rect = new Rectangle(x, y);
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
	public void update(float delta) 
	{
		float oldX = x;
		float oldY = y;
		
		//move in assigned direction
		x += mx * delta;
		y += my * delta;
		
		//check collision
		Object obj = GameWorld.getInstance().checkCollision(rect.getBoundsInLocal());
		//if we hit something (that has an x and y)
		if (obj != null && obj instanceof IDrawable)
		{
			IDrawable collider = (IDrawable) obj;
			
			//move back to old position
			x = oldX;
			y = oldY;
			
			if (collider.getRot() == 0 || (int)collider.getRot() % 90 == 0)
			{
				//is vertical/horizontal
				//TODO
			} else
			{
				//diagonal
			}
		}
	}

}
