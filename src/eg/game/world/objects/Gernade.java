package eg.game.world.objects;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.interfaces.ICollidable;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import eg.game.world.objects.particleSystem.ParticleEmitterFactory;
import eg.game.world.objects.player.Player;

public class Gernade extends IUpdatable implements IDrawable
{
	private static final float speed = 30;
	private static final float imgSize = 16;
	private static final float SLOWDOWNRATE = 0.04f;
	private static final int MAX_DURATION = 20;
	
	private float x, y, mx, my, dir, timer, startTime;
	private Image img;
	private Rectangle rect;
	private int ownerID;
	
	public Gernade (float x, float y, float dir, int id)
	{
		this.x = x - (imgSize/2);
		this.y = y - (imgSize/2);
		this.dir = dir;
		this.mx = (float) (Math.sin(Math.toRadians(dir)) * speed);
		this.my = (float) -(Math.cos(Math.toRadians(dir)) * speed);
		this.ownerID = id;
		this.timer = MAX_DURATION;
		this.startTime = MAX_DURATION;
		
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
		timer -= delta;
		if (timer <= 0)
		{
			GameWorld.getInstance().removeObject(this);
			ParticleEmitterFactory.Explosion.create(x, y, 0);
			
			//damage with explosion
			Player p = GameWorld.getInstance().getPlayer();
			int distance = (int)Math.sqrt(Math.pow(p.getX() - getX(), 2) + Math.pow(p.getY() - getY(), 2));
			if (distance < 150)
				p.takeDamage(150 - distance);
			//System.out.println();
			return;
		}
		
		float oldX = x;
		float oldY = y;
		
		//move in assigned direction
		x += mx * delta;
		y += my * delta;
		
		mx -= mx * SLOWDOWNRATE * delta;
		my -= my * SLOWDOWNRATE * delta;
		
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
				
				if (GameWorld.getInstance().collidesWith(obj, rect.getBoundsInParent()))
					flipY = true;
				
				rect.setY(oldY);
				rect.setX(x);
				
				if (GameWorld.getInstance().collidesWith(obj, rect.getBoundsInParent()))
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
				//diagonal collision
				boolean bellow = false, above = false, toLeft = false, toRight = false;
				
				//find collision direction
				rect.setX(oldX - 12);
				rect.setY(oldY);
				if (GameWorld.getInstance().collidesWith(obj, rect.getBoundsInParent()))
					toLeft = true;
				
				rect.setX(oldX + 12);
				if (GameWorld.getInstance().collidesWith(obj, rect.getBoundsInParent()))
					toRight = true;
				
				rect.setX(oldX);
				rect.setY(oldY - 12);
				if (GameWorld.getInstance().collidesWith(obj, rect.getBoundsInParent()))
					above = true;
				
				rect.setY(oldY + 12);
				if (GameWorld.getInstance().collidesWith(obj, rect.getBoundsInParent()))
					bellow = true;
				
				y = oldY;
				x = oldX;
				
				if ((toRight && bellow) || (toLeft && above))
				{
					float temp = mx;
					mx = my * -1;
					my = temp * -1;
				} else if ((toRight && above)|| (toLeft && bellow))
				{
					dir = (float) Math.toDegrees(Math.atan2(my, mx)) + 90;
					
					dir -= 45;
					
					float nspeed = speed - ((startTime - timer) * (startTime/2) * SLOWDOWNRATE);
					
					System.out.println("speed="+speed+" nspeed="+nspeed);
					
					this.mx = (float) (Math.sin(Math.toRadians(dir)) * nspeed);
					this.my = (float) (Math.cos(Math.toRadians(dir)) * nspeed);
					dir = (float) Math.toDegrees(Math.atan2(my, mx)) + 90;
					
					dir += 45;
					
					this.mx = (float) (Math.sin(Math.toRadians(dir)) * nspeed);
					this.my = (float) -(Math.cos(Math.toRadians(dir)) * nspeed);
				} else if (toRight || toLeft)
					mx *= -1;
				else if (above || bellow)
					my *= -1;
			}
		}
	}
}
