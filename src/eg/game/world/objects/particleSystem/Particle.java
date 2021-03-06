package eg.game.world.objects.particleSystem;

import eg.game.state.mpShooter.GameWorld;
import eg.game.world.objects.interfaces.IDrawable;
import eg.game.world.objects.interfaces.IUpdatable;
import javafx.scene.image.Image;

public class Particle extends IUpdatable implements IDrawable
{
	private float x, y, mx, my, duration, dir;
	private Image img;
	
	public Particle (float x, float y, float dir, float speed, float timer, float size, Image img)
	{
		this.x = x - (size/2);
		this.y = y - (size/2);
		this.mx = (float) (Math.sin(Math.toRadians(dir)) * speed);
		this.my = (float) -(Math.cos(Math.toRadians(dir)) * speed);
		this.duration = timer;
		this.dir = dir;
		
		this.img = img;
	}
	
	@Override
	public Image getImg() 
	{
		return img;
	}

	@Override
	public float getRot()
	{
		return dir;
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
		duration -= delta;
		
		if (duration <= 0)
		{
			GameWorld.getInstance().removeObject(this);
			return;
		}
		
		x += mx * delta;
		y += my * delta;
	}

}
