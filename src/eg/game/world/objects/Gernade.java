package eg.game.world.objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.interfaces.ICollidable;
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
		
		rect = new Rectangle(x, y, imgSize, imgSize);
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
		
		rect.setX(x);
		rect.setY(y);
		
		//check collision
		ICollidable obj = GameWorld.getInstance().checkCollision(rect.getBoundsInParent());
		//if we hit something (that has an x and y)
		if (obj != null && obj instanceof IDrawable)
		{
			IDrawable collider = (IDrawable) obj;
			
			if (collider.getRot() == 0 || (int)collider.getRot() % 90 == 0)
			{	
				boolean flipX = false, flipY = false;
				rect.setX(oldX);
				
				if (rect.getBoundsInParent().intersects(obj.getBounds()))
					flipY = true;
				
				rect.setY(oldY);
				rect.setX(x);
				
				if (rect.getBoundsInParent().intersects(obj.getBounds()))
					flipX = true;
				
				//change direction
				if (flipX)
				{
					mx *= -1;
					x = oldX;
				}
				if (flipY)
				{
					my *= -1;
					y = oldY;
				}
			} else
			{
				//TODO
				//diagonal collision
				boolean flipX = true, flipY = true;
				
				//rect.setX(oldX);
				
				//if (GameWorld.getInstance().checkCollision(rect.getBoundsInParent()) == obj)
				//	flipY = true;
				//
				//rect.setY(oldY);
				//rect.setX(x);
				
				//if (GameWorld.getInstance().checkCollision(rect.getBoundsInParent()) == obj)
				//	flipX = true;
				
				/*if (flipX && flipY)
				{
					System.out.println("x and y");
					float temp = mx;
					mx = my * -1;
					my = temp * -1;
				}
				else if (flipY)
				{
					System.out.println("y");
					float temp = mx;
					mx = my;
					my = temp * -1;
				} else if (flipX)
				{
					System.out.println("x");
					float temp = mx;
					mx = my * -1;
					my = temp;
				}
				
				y = oldY;
				x = oldX;*/
			}
		}
	}

}
